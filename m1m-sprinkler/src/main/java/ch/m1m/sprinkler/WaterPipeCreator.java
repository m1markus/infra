package ch.m1m.sprinkler;

import ch.m1m.sprinkler.api.WaterPipe;

import java.time.Duration;
import java.time.LocalTime;

public class WaterPipeCreator {

    public static WaterPipe newWaterPipe(int id, LocalTime ltStartAt, int runForMinutes) {
        WaterPipe waterPipe = new WaterPipe();
        waterPipe.setId(id);
        waterPipe.setActive(true);
        waterPipe.setStartAt(ltStartAt);
        waterPipe.setRunFor(Duration.ofMinutes(runForMinutes));
        waterPipe.setRunOnMonday(true);
        waterPipe.setRunOnTuesday(true);
        waterPipe.setRunOnWednesday(true);
        waterPipe.setRunOnThursday(true);
        waterPipe.setRunOnFriday(true);
        waterPipe.setRunOnSaturday(true);
        waterPipe.setRunOnSunday(true);
        return waterPipe;
    }
}
