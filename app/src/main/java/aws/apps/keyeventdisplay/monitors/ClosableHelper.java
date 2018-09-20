package aws.apps.keyeventdisplay.monitors;

import java.io.Closeable;
import java.io.IOException;

public final class ClosableHelper {

    public static void close(final Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
