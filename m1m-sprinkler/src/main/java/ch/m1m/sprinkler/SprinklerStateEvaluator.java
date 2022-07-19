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
import java.time.temporal.ChronoUnit;

@ApplicationScoped
public class SprinklerStateEvaluator {

    private static Logger LOG = LoggerFactory.getLogger(SprinklerStateEvaluator.class);

    public SprinklerState evaluate(SprinklerAppState sprinklerAppState) {
        SprinklerState sprinklerState = new SprinklerState();
        LocalDateTime ldtNow = LocalDateTime.now();
        WaterPipeState wps;

        for (WaterPipe waterPipe : sprinklerAppState.getPipes()) {
            if (sprinklerAppState.isActive()) {
                wps = evaluateStateFromWaterPipe(waterPipe, ldtNow);
            } else {
                wps = new WaterPipeState(waterPipe.getId());
            }

            sprinklerState.getPipes().add(wps);
        }
        return sprinklerState;
    }

    WaterPipeState evaluateStateFromWaterPipe(WaterPipe waterPipe, LocalDateTime ldtNow) {
        WaterPipeState wps = new WaterPipeState(waterPipe.getId());

        if (waterPipe.isActive()) {

            DayOfWeek weekDayNow = ldtNow.getDayOfWeek();
            LocalDateTime ldtPipeFrom = LocalDateTime.of(ldtNow.toLocalDate(), waterPipe.getStartAt());
            LocalDateTime ldtPipeUntil = ldtPipeFrom.plus(waterPipe.getRunFor());
            // correct the day when the period goes over midnight
            if (ldtPipeFrom.getDayOfWeek() != ldtPipeUntil.getDayOfWeek()) {
                // fixup values when period has a day shift
                weekDayNow = weekDayNow.minus(1);
                ldtPipeFrom = ldtPipeFrom.minus(1, ChronoUnit.DAYS);
                ldtPipeUntil = ldtPipeUntil.minus(1, ChronoUnit.DAYS);
            }
            // check day
            if (shouldPipeRunForTodaysWeekDay(waterPipe, weekDayNow)) {
                // check time range
                if (ldtPipeFrom.isBefore(ldtNow) && ldtPipeUntil.isAfter(ldtNow)) {
                    wps.setFlow(true);
                }
            }
        }
        return wps;
    }

    private boolean shouldPipeRunForTodaysWeekDay(WaterPipe waterPipe, DayOfWeek weekDay) {
        switch (weekDay) {
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
