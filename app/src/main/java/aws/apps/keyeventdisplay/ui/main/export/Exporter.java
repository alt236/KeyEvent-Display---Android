package aws.apps.keyeventdisplay.ui.main.export;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import aws.apps.keyeventdisplay.R;
import aws.apps.keyeventdisplay.ui.common.NotifyUser;

public class Exporter {
    private static final String TAG = Exporter.class.getSimpleName();
    private static final Format TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HHmmssZ", Locale.US);
    private static final String FILENAME_FORMATTER = "keyevent_%s.txt";
    private static final String SUBJECT_FORMATTER = "keyevent_%s";

    private final Activity activity;
    private final NotifyUser notifyUser;
    private final DeviceInfo deviceInfo;

    public Exporter(final Activity activity, NotifyUser notifyUser) {
        this.activity = activity;
        this.notifyUser = notifyUser;
        this.deviceInfo = new DeviceInfo(activity.getApplicationContext());
    }

    public void save(CharSequence text) {
        final String exportableText = getExportText(text);
        if (isValidText(exportableText)) {
            final String time = getNow();
            final String fileName = String.format(Locale.US, FILENAME_FORMATTER, time);
            final File directory = Environment.getExternalStorageDirectory();

            saveToFile(fileName, directory, exportableText);
        } else {
            notifyUser.notifyShort(R.string.nothing_to_save);
        }
    }

    public void share(CharSequence text) {
        final String exportableText = getExportText(text);
        if (isValidText(exportableText)) {
            final String time = getNow();
            final String subject = String.format(Locale.US, SUBJECT_FORMATTER, time);
            final Intent intent = createShareIntent(subject, exportableText);
            activity.startActivity(intent);
        } else {
            notifyUser.notifyShort(R.string.nothing_to_share);
        }
    }

    private String getExportText(final CharSequence text) {
        final StringBuilder sb = new StringBuilder();
        deviceInfo.collectDeviceInfo(sb);
        sb.append("\n\n-----------------\n\n");
        sb.append(text);
        return sb.toString();
    }

    private void saveToFile(String fileName, File directory, String contents) {
        Log.d(TAG, "^ saveToFile: attempting to save at '" + directory.getAbsolutePath() + "/" + fileName + "'");
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {

            try {
                if (directory.canWrite()) {
                    final File gpxfile = new File(directory, fileName);
                    final FileWriter gpxwriter = new FileWriter(gpxfile);
                    final BufferedWriter out = new BufferedWriter(gpxwriter);
                    out.write(contents);
                    out.flush();
                    out.close();

                    final String pathAsString = gpxfile.getAbsolutePath();
                    Log.d(TAG, "^ saveToFile: saved as '" + pathAsString + "'");
                    notifyUser.notifyShort("Saved to SD as '" + pathAsString + "'");
                } else {
                    throw new IllegalStateException("Cannot write to directory: " + directory);
                }

            } catch (Exception e) {
                Log.e(TAG, "^ Could not write file. Error: " + e.getMessage(), e);
                notifyUser.notifyShort("Could not write file:\nError: " + e.getMessage());
            }

        } else {
            notifyUser.notifyShort("No SD card is mounted...");
            Log.e(TAG, "^ No SD card is mounted.");
        }
    }

    private Intent createShareIntent(String subject, String text) {
        final Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        return Intent.createChooser(intent, activity.getString(R.string.share_result_via));
    }

    private static String getNow() {
        return TIME_FORMAT.format(new Date());
    }

    private static boolean isValidText(final String text) {
        return !TextUtils.isEmpty(text);
    }
}
