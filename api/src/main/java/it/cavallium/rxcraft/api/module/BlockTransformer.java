package it.cavallium.rxcraft.api.module;

import it.cavallium.rxcraft.api.data.BlockSnapshot;
import reactor.core.publisher.Flux;

public interface BlockTransformer {

	Flux<BlockSnapshot> transformBlock(Flux<BlockSnapshot> blocks);
}
