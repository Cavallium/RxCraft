package it.cavallium.rxcraft.api.server;

import it.cavallium.rxcraft.api.data.BlockAction;
import it.cavallium.rxcraft.api.data.BlockDefinition;
import it.cavallium.rxcraft.api.data.BlockPosition;
import it.cavallium.rxcraft.api.data.BlockProperty;
import it.cavallium.rxcraft.api.module.BlockPropertyManager;
import java.time.Instant;
import reactor.core.publisher.Mono;

public interface BlockManager {

	Mono<Void> scheduleNextTransformation(BlockPosition position, Instant instant);

	Mono<Void> scheduleNextAction(BlockAction action, Instant instant);

	Mono<BlockPropertyManager> getPropertyManager(BlockDefinition definition, Class<? extends BlockProperty<?>> propertyType);
}
