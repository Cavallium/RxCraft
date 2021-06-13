package it.cavallium.rxcraft.api.module.core;

import it.cavallium.rxcraft.api.data.BlockProperty;
import it.cavallium.rxcraft.api.data.BlockSnapshot;
import it.cavallium.rxcraft.api.data.BlockState;
import it.cavallium.rxcraft.api.module.BlockPropertyManager;
import reactor.core.publisher.Mono;

public class CorePropertyManager implements BlockPropertyManager {

	@SuppressWarnings("unchecked")
	@Override
	public <U, T extends BlockProperty<U>> Mono<T> property(Class<T> propertyType, BlockSnapshot blockSnapshot) {
		if (propertyType == BlockDurability.class) {
			return (Mono<T>) getBlockDurability(blockSnapshot);
		} else {
			return Mono.error(new UnsupportedOperationException("Unsupported property type " + propertyType.getName()));
		}
	}

	private Mono<BlockDurability> getBlockDurability(BlockSnapshot blockSnapshot) {
		long durability;
		if (blockSnapshot.state() instanceof BlockStateAir) {
			durability = Long.MAX_VALUE;
		} else {
			durability = 1L;
		}

		return Mono.just(new BlockDurability(durability));
	}
}
