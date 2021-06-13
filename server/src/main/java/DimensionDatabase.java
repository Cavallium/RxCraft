import it.cavallium.rxcraft.api.data.BlockPosition;
import it.cavallium.rxcraft.api.data.BlockSnapshot;
import it.cavallium.rxcraft.api.data.BlockState;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DimensionDatabase {

	public DimensionDatabase() {

	}

	public Mono<BlockSnapshot> block(BlockPosition blockPosition) {
		return Mono.empty();
	}

	public Flux<BlockSnapshot> blocks(Flux<BlockPosition> blockPositions) {
		return blockPositions.flatMap(this::block);
	}

	public Mono<Void> setBlock(BlockSnapshot blockSnapshot) {
		return Mono.empty();
	}

	public Mono<Void> setBlocks(Flux<BlockSnapshot> blockSnapshots) {
		return blockSnapshots.flatMap(this::setBlock).then();
	}
}
