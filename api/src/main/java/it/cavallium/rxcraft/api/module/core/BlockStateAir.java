package it.cavallium.rxcraft.api.module.core;

import it.cavallium.rxcraft.api.data.BlockDefinition;
import it.cavallium.rxcraft.api.data.BlockState;

public record BlockStateAir() implements BlockState {

	@Override
	public BlockDefinition definition() {
		return CoreDefinition.INSTANCE;
	}
}
