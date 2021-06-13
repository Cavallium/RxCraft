package it.cavallium.rxcraft.game;

import it.cavallium.rxcraft.api.data.BlockAction;
import it.cavallium.rxcraft.api.data.BlockPosition;
import it.cavallium.rxcraft.api.data.BlockSnapshot;
import it.cavallium.rxcraft.api.module.ActionTransformer;
import it.cavallium.rxcraft.api.module.BlockTransformer;
import it.cavallium.rxcraft.api.server.BlockManager;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BlockBreakManager {

	private final BlockManager blockManager;
	private final BlockBreakActionTransformer actionTransformer;
	private final BlockBreakBlockTransformer blockTransformer;

	public BlockBreakManager(BlockManager blockManager) {
		this.blockManager = blockManager;
		this.blockTransformer = new BlockBreakBlockTransformer(blockManager);
		this.actionTransformer = new BlockBreakActionTransformer(blockManager, blockTransformer);
	}

	public Mono<Void> breakBlock(BlockActionBlockBreak actionBlockBreak) {
		return actionTransformer.scheduleBreak(actionBlockBreak);
	}
}
