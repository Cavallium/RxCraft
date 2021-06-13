package it.cavallium.rxcraft.api.module.core;

import it.cavallium.rxcraft.api.data.BlockProperty;

public record BlockDurability(long value) implements BlockProperty<Long> {}
