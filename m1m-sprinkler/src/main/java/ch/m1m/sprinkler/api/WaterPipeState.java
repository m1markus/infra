package ch.m1m.sprinkler.api;

public class WaterPipeState {

    private int id;
    private boolean flow;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFlow() {
        return flow;
    }

    public void setFlow(boolean flow) {
        this.flow = flow;
    }
}
