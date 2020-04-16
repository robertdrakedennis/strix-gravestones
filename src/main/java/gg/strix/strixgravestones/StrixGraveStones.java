package gg.strix.strixgravestones;

import com.google.inject.Inject;
import gg.strix.strixgravestones.events.PlayerDeathEventHandling;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "strixgravestones",
        name = "StrixGraveStones",
        description = "Gravestones for when you die.",
        authors = {
                "NoSharp"
        }
)
public class StrixGraveStones {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        Sponge.getEventManager().registerListeners(this, new PlayerDeathEventHandling());
    }
}
