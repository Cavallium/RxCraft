package it.cavallium.rxcraft.api.module;

import it.cavallium.rxcraft.api.data.Action;
import it.cavallium.rxcraft.api.data.BlockAction;
import it.cavallium.rxcraft.api.data.BlockSnapshot;
import reactor.core.publisher.Flux;

public interface ActionTransformer {

	Flux<Action> transformAction(Flux<Action> actions);
}
