package ch.m1m.sprinkler.api;

import java.time.Duration;
import java.time.LocalTime;

public class WaterPipe {

    private int id;
    private boolean isActive;

    private LocalTime startAt;
    private Duration runFor;

    private boolean runOnMonday;
    private boolean runOnTuesday;
    private boolean runOnWednesday;
    private boolean runOnThursday;
    private boolean runOnFriday;
    private boolean runOnSaturday;
    private boolean runOnSunday;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalTime startAt) {
        this.startAt = startAt;
    }

    public Duration getRunFor() {
        return runFor;
    }

    public void setRunFor(Duration runFor) {
        this.runFor = runFor;
    }

    public boolean isRunOnMonday() {
        return runOnMonday;
    }

    public void setRunOnMonday(boolean runOnMonday) {
        this.runOnMonday = runOnMonday;
    }

    public boolean isRunOnTuesday() {
        return runOnTuesday;
    }

    public void setRunOnTuesday(boolean runOnTuesday) {
        this.runOnTuesday = runOnTuesday;
    }

    public boolean isRunOnWednesday() {
        return runOnWednesday;
    }

    public void setRunOnWednesday(boolean runOnWednesday) {
        this.runOnWednesday = runOnWednesday;
    }

    public boolean isRunOnThursday() {
        return runOnThursday;
    }

    public void setRunOnThursday(boolean runOnThursday) {
        this.runOnThursday = runOnThursday;
    }

    public boolean isRunOnFriday() {
        return runOnFriday;
    }

    public void setRunOnFriday(boolean runOnFriday) {
        this.runOnFriday = runOnFriday;
    }

    public boolean isRunOnSaturday() {
        return runOnSaturday;
    }

    public void setRunOnSaturday(boolean runOnSaturday) {
        this.runOnSaturday = runOnSaturday;
    }

    public boolean isRunOnSunday() {
        return runOnSunday;
    }

    public void setRunOnSunday(boolean runOnSunday) {
        this.runOnSunday = runOnSunday;
    }
}
