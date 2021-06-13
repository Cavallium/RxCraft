package it.cavallium.rxcraft.game;

import it.cavallium.rxcraft.api.data.BlockAction;
import it.cavallium.rxcraft.api.data.BlockPosition;
import java.time.Instant;

public record BlockActionBlockBreak(BlockPosition position, Instant startInstant, double toolPower) implements
		BlockAction {}
