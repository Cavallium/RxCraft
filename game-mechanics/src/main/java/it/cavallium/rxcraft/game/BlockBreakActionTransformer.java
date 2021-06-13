package it.cavallium.rxcraft.game;

import it.cavallium.rxcraft.api.data.Action;
import it.cavallium.rxcraft.api.module.ActionTransformer;
import it.cavallium.rxcraft.api.server.BlockManager;
import java.time.Instant;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BlockBreakActionTransformer implements ActionTransformer {

	private final BlockManager blockManager;
	private final BlockBreakBlockTransformer blockTransformer;

	public BlockBreakActionTransformer(BlockManager blockManager, BlockBreakBlockTransformer blockTransformer) {
		this.blockManager = blockManager;
		this.blockTransformer = blockTransformer;
	}

	Mono<Void> scheduleBreak(BlockActionBlockBreak actionBlockBreak) {
		return blockManager.scheduleNextAction(actionBlockBreak, Instant.EPOCH);
	}

	@Override
	public Flux<Action> transformAction(Flux<Action> actions) {
		return actions.flatMapSequential(action -> {
			if (action instanceof BlockActionBlockBreak breakAction) {
				return blockManager.scheduleNextTransformation(breakAction.position(), Instant.EPOCH).then(Mono.empty());
			} else {
				return Mono.just(action);
			}
		});
	}
}
