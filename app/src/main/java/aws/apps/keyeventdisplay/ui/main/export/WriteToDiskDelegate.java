package aws.apps.keyeventdisplay.ui.main.export;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import aws.apps.keyeventdisplay.ui.common.NotifyUser;

public class WriteToDiskDelegate {
    private static final String TAG = WriteToDiskDelegate.class.getSimpleName();

    private final Activity activity;
    private final NotifyUser notifyUser;
    private final Exporter exporter;

    public WriteToDiskDelegate(Activity mainActivity,
                               Exporter exporter,
                               NotifyUser notifyUser) {
        this.activity = mainActivity;
        this.notifyUser = notifyUser;
        this.exporter = exporter;
    }

    public void saveLogToDisk(final String text) {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(final PermissionGrantedResponse response) {
                        exporter.save(text);
                        Log.i(TAG, "onPermissionGranted(" + response.getPermissionName() + ")");
                    }

                    @Override
                    public void onPermissionDenied(final PermissionDeniedResponse response) {
                        notifyUser.notifyLong("Unable to save the log to disk unless permission is granted!");
                        Log.e(TAG, "onPermissionDenied(" + response.getPermissionName() + ")");
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(final PermissionRequest permission, final PermissionToken token) {
                        notifyUser.notifyLong("Unable to save the log to disk unless permission is granted!");
                        Log.w(TAG, "onPermissionRationaleShouldBeShown(" + permission.getName() + ")");
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(error -> Log.e(TAG, "Error when getting permission: " + error))
                .check();
    }
}