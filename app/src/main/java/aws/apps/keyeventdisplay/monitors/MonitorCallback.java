package aws.apps.keyeventdisplay.monitors;

public interface MonitorCallback {
    void onError(String msg, Throwable e);

    void onNewline(String line);
}
