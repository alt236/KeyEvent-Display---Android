package aws.apps.keyeventdisplay.monitors;

import java.io.Closeable;
import java.io.IOException;

/*package*/ final class Helper {

    public static void close(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
