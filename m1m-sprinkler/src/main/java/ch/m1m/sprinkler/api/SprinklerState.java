package ch.m1m.sprinkler.api;

import java.util.ArrayList;
import java.util.List;

public class SprinklerState {

    List<WaterPipeState> pipes = new ArrayList<>();

    public List<WaterPipeState> getPipes() {
        return pipes;
    }

    public void setPipes(List<WaterPipeState> pipes) {
        this.pipes = pipes;
    }
}
