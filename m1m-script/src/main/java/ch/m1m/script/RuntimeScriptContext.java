package ch.m1m.script;

public class RuntimeScriptContext {

    private int exitStatus = 1;
    private Throwable throwable = null;

    public int getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(int value) {
        exitStatus = value;
    }

    public Throwable getException() {
        return throwable;
    }

    public void setException(Throwable error) {
        throwable = error;
    }
}
