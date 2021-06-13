package it.cavallium.rxcraft.api.server;

import java.time.Clock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DimensionManager {

	Flux<Dimension> dimensions();

	default Mono<Dimension> dimension(int dimensionId) {
		return Mono.just(new Dimension(this, dimensionId));
	}

	Mono<Clock> clock(int dimensionId);
}
