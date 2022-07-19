package ch.m1m.sprinkler;

import ch.m1m.sprinkler.api.SprinklerAppState;
import ch.m1m.sprinkler.api.SprinklerState;
import ch.m1m.sprinkler.api.WaterPipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalTime;

@ApplicationScoped
public class StateManager {

    private static Logger LOG = LoggerFactory.getLogger(StateManager.class);

    // input from static- and web config
    private SprinklerAppState sprinklerAppState;

    // evaluated output if water is actually flow or not
    private SprinklerState sprinklerState = new SprinklerState();

    @Inject
    SprinklerStateEvaluator sprinklerStateEvaluator;

    @PostConstruct
    public void onInit() {
        LOG.info("initialize global state...");

        sprinklerAppState = initFromStaticConfig();
    }

    private SprinklerAppState initFromStaticConfig() {
        SprinklerAppState sprinklerAppState = new SprinklerAppState();
        sprinklerAppState.setActive(true);
        WaterPipe waterPipe = WaterPipeCreator.newWaterPipe(1, LocalTime.of(18, 00), 240);
        sprinklerAppState.getPipes().add(waterPipe);
        waterPipe = WaterPipeCreator.newWaterPipe(2, LocalTime.of(00, 01), 60);
        sprinklerAppState.getPipes().add(waterPipe);
        return sprinklerAppState;
    }

    public SprinklerAppState getSprinklerAppState() {
        return sprinklerAppState;
    }

    public SprinklerState getSprinklerState() {
        return sprinklerState;
    }

    public void updatePipeState() {

        SprinklerState newSprinklerState = sprinklerStateEvaluator.evaluate(sprinklerAppState);

        // replace the old value
        sprinklerState = newSprinklerState;
    }
}
