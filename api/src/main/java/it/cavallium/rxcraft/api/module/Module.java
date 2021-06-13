package it.cavallium.rxcraft.api.module;

import it.cavallium.rxcraft.api.data.BlockDefinition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Module {

	Mono<Void> load();

	Mono<Void> unload();

	Mono<Void> enable();

	Mono<Void> disable();

	Flux<BlockDefinition> getBlockDefinitions();

	Flux<BlockTransformer> getBlockManagers();
}
