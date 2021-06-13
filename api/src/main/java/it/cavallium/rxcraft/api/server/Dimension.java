package it.cavallium.rxcraft.api.server;

import java.time.Clock;
import reactor.core.publisher.Mono;

public class Dimension {

	private final DimensionManager dimensionManager;
	private final int dimension;

	public Dimension(DimensionManager dimensionManager, int dimension) {
		this.dimensionManager = dimensionManager;
		this.dimension = dimension;
	}

	public Mono<Clock> clock() {
		return dimensionManager.clock(dimension);
	}

	public int id() {
		return dimension;
	}
}
