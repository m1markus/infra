package ch.m1m.sprinkler;

import ch.m1m.sprinkler.api.SprinklerAppState;
import ch.m1m.sprinkler.api.SprinklerState;
import ch.m1m.sprinkler.api.WaterPipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
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
        WaterPipe waterPipe = newWaterPipe(1);

        sprinklerAppState.getPipes().add(waterPipe);
        return sprinklerAppState;
    }

    private WaterPipe newWaterPipe(int id) {
        WaterPipe waterPipe = new WaterPipe();
        waterPipe.setId(1);
        waterPipe.setActive(true);
        waterPipe.setStartAt(LocalTime.of(22, 00));
        waterPipe.setRunFor(Duration.ofMinutes(120));
        waterPipe.setRunOnMonday(true);
        waterPipe.setRunOnTuesday(true);
        waterPipe.setRunOnWednesday(true);
        waterPipe.setRunOnThursday(true);
        waterPipe.setRunOnFriday(true);
        waterPipe.setRunOnSaturday(true);
        waterPipe.setRunOnSunday(true);
        return waterPipe;
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
