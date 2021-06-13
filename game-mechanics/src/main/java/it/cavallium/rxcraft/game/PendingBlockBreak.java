package it.cavallium.rxcraft.game;

import java.time.Instant;

public record PendingBlockBreak(Instant startInstant, double toolPower) {}
