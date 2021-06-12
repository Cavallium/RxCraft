package it.cavallium.rxcraft.api;

import reactor.core.publisher.Mono;

public interface BlockManager {

	Mono<BlockState> transformBlock(BlockState blockState);
}
