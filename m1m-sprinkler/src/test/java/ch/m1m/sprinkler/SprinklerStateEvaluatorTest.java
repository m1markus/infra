package ch.m1m.sprinkler;

import ch.m1m.sprinkler.api.WaterPipe;
import ch.m1m.sprinkler.api.WaterPipeState;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class SprinklerStateEvaluatorTest {

    private SprinklerStateEvaluator testee = new SprinklerStateEvaluator();

    @Test
    public void givenAWaterPipe_whenEvaluatingBeforPipeStarted_thenWeGetNoActiveFlow() {
        // GIVEN
        WaterPipe wp = WaterPipeCreator.newWaterPipe(1, LocalTime.parse("13:00"), 10);
        LocalDateTime ldtNow = LocalDateTime.parse("2022-07-19T12:00:00");
        // WHEN
        WaterPipeState wps = testee.evaluateStateFromWaterPipe(wp, ldtNow);
        // THEN
        assertFalse(wps.isFlow());
    }

    @Test
    public void givenAWaterPipe_whenEvaluatingAfterPipeStarted_thenWeGetAnActiveFlow() {
        // GIVEN
        WaterPipe wp = WaterPipeCreator.newWaterPipe(1, LocalTime.parse("13:00"), 10);
        LocalDateTime ldtNow = LocalDateTime.parse("2022-07-19T13:05:00");
        // WHEN
        WaterPipeState wps = testee.evaluateStateFromWaterPipe(wp, ldtNow);
        // THEN
        assertTrue(wps.isFlow());
    }

    @Test
    public void givenAWaterPipe_whenEvaluatingAfterPipeStopped_thenWeGetNoActiveFlow() {
        // GIVEN
        WaterPipe wp = WaterPipeCreator.newWaterPipe(1, LocalTime.parse("13:00"), 10);
        LocalDateTime ldtNow = LocalDateTime.parse("2022-07-19T13:15:00");
        // WHEN
        WaterPipeState wps = testee.evaluateStateFromWaterPipe(wp, ldtNow);
        // THEN
        assertFalse(wps.isFlow());
    }

    @Test
    public void givenAWaterPipeOverDateShift_whenEvaluatingAfterPipeStarted_thenWeGetAnActiveFlow() {
        // GIVEN
        WaterPipe wp = WaterPipeCreator.newWaterPipe(1, LocalTime.parse("23:00"), 120);
        LocalDateTime ldtNow = LocalDateTime.parse("2022-07-20T00:30:00");
        // WHEN
        WaterPipeState wps = testee.evaluateStateFromWaterPipe(wp, ldtNow);
        // THEN
        assertTrue(wps.isFlow());
    }

    @Test
    public void givenAWaterPipeOverDateShift_whenEvaluatingAfterPipeStopped_thenWeGetNoActiveFlow() {
        // GIVEN
        WaterPipe wp = WaterPipeCreator.newWaterPipe(1, LocalTime.parse("23:00"), 120);
        LocalDateTime ldtNow = LocalDateTime.parse("2022-07-19T02:00:00");
        // WHEN
        WaterPipeState wps = testee.evaluateStateFromWaterPipe(wp, ldtNow);
        // THEN
        assertFalse(wps.isFlow());
    }
}
