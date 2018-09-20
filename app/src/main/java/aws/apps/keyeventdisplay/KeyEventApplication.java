package aws.apps.keyeventdisplay;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

public class KeyEventApplication extends Application {
    public void onCreate() {
        super.onCreate();

        setUpStrictMode();
    }

    private void setUpStrictMode() {
        Log.i("KeyEventDisplay", "Strict mode: " + BuildConfig.STRICT_MODE);

        if (BuildConfig.STRICT_MODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                        .detectAll()
                        .penaltyLog()
                        .build());

                final StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder()
                        .detectAll()
                        .penaltyLog()
                        .penaltyDeath();

                StrictMode.setVmPolicy(vmPolicyBuilder.build());
            }
        }
    }
}