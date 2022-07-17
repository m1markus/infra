package ch.m1m.sprinkler.api;

import java.util.ArrayList;
import java.util.List;

public class SprinklerAppState {

    private boolean isActive;

    List<WaterPipe> pipes = new ArrayList<>();

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<WaterPipe> getPipes() {
        return pipes;
    }

    public void setPipes(List<WaterPipe> pipes) {
        this.pipes = pipes;
    }
}
