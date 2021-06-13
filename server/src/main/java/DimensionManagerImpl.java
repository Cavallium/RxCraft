import it.cavallium.rxcraft.api.data.BlockAction;
import it.cavallium.rxcraft.api.data.BlockDefinition;
import it.cavallium.rxcraft.api.data.BlockPosition;
import it.cavallium.rxcraft.api.data.BlockProperty;
import it.cavallium.rxcraft.api.data.BlockSnapshot;
import it.cavallium.rxcraft.api.module.BlockPropertyManager;
import it.cavallium.rxcraft.api.module.BlockTransformer;
import it.cavallium.rxcraft.api.server.BlockManager;
import it.cavallium.rxcraft.api.server.Dimension;
import it.cavallium.rxcraft.api.server.DimensionManager;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DimensionManagerImpl implements DimensionManager, BlockManager {

	private record ScheduledBlockAction(BlockAction blockAction, Instant instant) implements
			Comparable<ScheduledBlockAction> {

		@Override
		public int compareTo(@NotNull DimensionManagerImpl.ScheduledBlockAction o) {
			return this.instant.compareTo(o.instant);
		}
	}

	private record ScheduledBlockPosition(BlockPosition blockPosition, Instant instant) implements
			Comparable<ScheduledBlockPosition> {

		@Override
		public int compareTo(@NotNull DimensionManagerImpl.ScheduledBlockPosition o) {
			return this.instant.compareTo(o.instant);
		}
	}

	private final ConcurrentSkipListSet<ScheduledBlockPosition> nextTransformations = new ConcurrentSkipListSet<>();
	private final ConcurrentSkipListSet<ScheduledBlockAction> nextActions = new ConcurrentSkipListSet<>();
	private final DimensionDatabase database;
	private final List<BlockTransformer> blockTransformers;

	public DimensionManagerImpl(DimensionDatabase database, List<BlockTransformer> blockTransformers) {
		this.database = database;
		this.blockTransformers = blockTransformers;
	}

	public Mono<Void> start() {
		// For each dimension
		return this.dimensions()
				// Get dimension clock
				.flatMap(dimension -> dimension.clock()
						// Execute for 50ms or more
						.flatMap(clock -> Mono
								.when(
										Mono.delay(Duration.ofMillis(50)),

										// Execute tick
										// Get blocks that needs to tick now
										this.getCurrentTransformations(dimension, clock)
												// Get requested blocks
												.transform(database::blocks)
												// Apply all transformations in sequence
												.transform(this::buildBlockSnapshotFlux)
												// Set the changed blocks
												.transform(database::setBlocks)
								)
						)
				)
				// Repeat forever
				.repeat()

				// Return void
				.then();
	}

	private Flux<BlockPosition> getCurrentTransformations(Dimension dimension, Clock clock) {
		return Mono
				.fromCallable(() -> {
					Set<BlockPosition> positionsToTransform
							= new ConcurrentHashMap<BlockPosition, Object>().keySet(true);

					Instant now = clock.instant();
					nextTransformations.removeIf(next -> {
						if (next.blockPosition().dimension() == dimension.id() && !next.instant().isAfter(now)) {
							positionsToTransform.add(next.blockPosition());
							return true;
						} else {
							return false;
						}
					});
					return positionsToTransform;
				})
				.flatMapMany(Flux::fromIterable);
	}

	public Flux<BlockSnapshot> buildBlockSnapshotFlux(Flux<BlockSnapshot> blockSnapshotFlux) {
		for (BlockTransformer blockTransformer : blockTransformers) {
			blockSnapshotFlux = blockTransformer.transformBlock(blockSnapshotFlux);
		}
		return blockSnapshotFlux;
	}

	@Override
	public Flux<Dimension> dimensions() {
		return dimension(0).flux();
	}

	@Override
	public Mono<Clock> clock(int dimensionId) {
		return Mono.fromCallable(() -> new Clock() {
			@Override
			public ZoneId getZone() {
				return null;
			}

			@Override
			public Clock withZone(ZoneId zone) {
				throw new NullPointerException();
			}

			@Override
			public Instant instant() {
				return Instant.ofEpochMilli(System.currentTimeMillis() / 60L);
			}
		});
	}

	@Override
	public Mono<Void> scheduleNextTransformation(BlockPosition position, Instant instant) {
		return Mono.fromRunnable(() -> nextTransformations.add(new ScheduledBlockPosition(position, instant)));
	}

	@Override
	public Mono<Void> scheduleNextAction(BlockAction action, Instant instant) {
		return Mono.fromRunnable(() -> nextActions.add(new ScheduledBlockAction(action, instant)));
	}

	@Override
	public Mono<BlockPropertyManager> getPropertyManager(BlockDefinition definition,
			Class<? extends BlockProperty<?>> propertyType) {
		return null;
	}
}
