package ch.m1m.sprinkler;

import ch.m1m.sprinkler.api.SprinklerAppState;
import ch.m1m.sprinkler.api.SprinklerState;
import ch.m1m.sprinkler.api.WaterPipe;
import ch.m1m.sprinkler.api.WaterPipeState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@ApplicationScoped
public class SprinklerStateEvaluator {

    private static Logger LOG = LoggerFactory.getLogger(SprinklerStateEvaluator.class);

    public SprinklerState evaluate(SprinklerAppState sprinklerAppState) {
        SprinklerState sprinklerState = new SprinklerState();
        LocalDateTime ldtNow = LocalDateTime.now();

        for (WaterPipe waterPipe : sprinklerAppState.getPipes()) {

            WaterPipeState wps = new WaterPipeState();
            wps.setId(waterPipe.getId());
            boolean isFlow = false;

            if (sprinklerAppState.isActive() && waterPipe.isActive()) {

                // check day
                if (shouldPipeRunForTodaysWeekDay(waterPipe)) {

                    // check time range
                    LocalDateTime ldtPipeFrom = LocalDateTime.of(ldtNow.toLocalDate(), waterPipe.getStartAt());
                    LocalDateTime ldtPipeUntil = ldtPipeFrom.plus(waterPipe.getRunFor());
                    if (ldtPipeFrom.isBefore(ldtNow) && ldtPipeUntil.isAfter(ldtNow)) {
                        isFlow = true;
                    }
                }
            }
            wps.setFlow(isFlow);

            sprinklerState.getPipes().add(wps);
        }
        return sprinklerState;
    }

    private boolean shouldPipeRunForTodaysWeekDay(WaterPipe waterPipe) {
        DayOfWeek weekDayNow = LocalDateTime.now().getDayOfWeek();
        switch(weekDayNow) {
            case MONDAY:
                return waterPipe.isRunOnMonday();
            case TUESDAY:
                return waterPipe.isRunOnTuesday();
            case WEDNESDAY:
                return waterPipe.isRunOnWednesday();
            case THURSDAY:
                return waterPipe.isRunOnThursday();
            case FRIDAY:
                return waterPipe.isRunOnFriday();
            case SATURDAY:
                return waterPipe.isRunOnSaturday();
            case SUNDAY:
                return waterPipe.isRunOnSunday();
        }
        return false;
    }
}
