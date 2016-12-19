package aws.apps.keyeventdisplay.monitors;

import android.os.AsyncTask;
import android.os.Build;

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

    public static void startTask(final AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }
}
