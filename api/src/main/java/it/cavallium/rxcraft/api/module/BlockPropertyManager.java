package it.cavallium.rxcraft.api.module;

import it.cavallium.rxcraft.api.data.BlockProperty;
import it.cavallium.rxcraft.api.data.BlockSnapshot;
import it.cavallium.rxcraft.api.data.BlockState;
import reactor.core.publisher.Mono;

public interface BlockPropertyManager {

	<U, T extends BlockProperty<U>> Mono<T> property(Class<T> propertyType, BlockSnapshot blockSnapshot);
}
