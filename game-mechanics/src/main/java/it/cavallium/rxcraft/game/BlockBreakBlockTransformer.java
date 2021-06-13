package it.cavallium.rxcraft.game;

import it.cavallium.rxcraft.api.data.BlockPosition;
import it.cavallium.rxcraft.api.data.BlockSnapshot;
import it.cavallium.rxcraft.api.module.core.BlockDurability;
import it.cavallium.rxcraft.api.module.core.BlockStateAir;
import it.cavallium.rxcraft.api.data.Position;
import it.cavallium.rxcraft.api.module.BlockTransformer;
import it.cavallium.rxcraft.api.server.BlockManager;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BlockBreakBlockTransformer implements BlockTransformer {

	private final ConcurrentHashMap<BlockPosition, PendingBlockBreak> pendingBreaks = new ConcurrentHashMap<>();
	private final BlockManager blockManager;

	public BlockBreakBlockTransformer(BlockManager blockManager) {
		this.blockManager = blockManager;
	}

	Mono<Void> addPendingBreak(BlockPosition position, PendingBlockBreak pendingBlockBreak) {
		return Mono.fromRunnable(() -> this.pendingBreaks.put(position, pendingBlockBreak));
	}

	@Override
	public Flux<BlockSnapshot> transformBlock(Flux<BlockSnapshot> blocks) {
		return blocks.flatMapSequential(blockSnapshot -> Mono
				// Get pending break at position, otherwise default
				.fromCallable(() -> pendingBreaks.remove(blockSnapshot.position()))
				// Apply break if possible
				.flatMap(pendingBlockBreak -> blockManager
						// Get property manager of block type + position + property
						.getPropertyManager(blockSnapshot.state().definition(), BlockDurability.class)
						// Get block state durability property
						.flatMap(blockPropertyManager -> blockPropertyManager.property(BlockDurability.class, blockSnapshot))
						// Get long property value
						.map(BlockDurability::value)
						// Can't break block, it's unbreakable
						.filter(durability -> durability != Long.MAX_VALUE)
						// Can break block, it's breakable
						.map(_unused -> new BlockSnapshot(blockSnapshot.position(), new BlockStateAir()))
				)
				// This block can't be broken because it has no
				.defaultIfEmpty(blockSnapshot)
		);
	}
}
