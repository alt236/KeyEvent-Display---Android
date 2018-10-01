/*******************************************************************************
 * Copyright 2012 Alexandros Schillings
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package aws.apps.keyeventdisplay.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import aws.apps.keyeventdisplay.R;
import aws.apps.keyeventdisplay.ui.MyAlertBox;

public class UsefulBits {
    final String TAG = this.getClass().getName();
    private Context c;

    public UsefulBits(Context cntx) {
        c = cntx;
    }

    public void autoScroll(TextView tv) {
        int maxLinesInView = tv.getHeight() / tv.getLineHeight();
        int y;
        y = 3;

        if (tv.getLineCount() == 0) {
            return;
        }

        if (maxLinesInView < tv.getLineCount()) {
            y = tv.getLineHeight() * tv.getLineCount()
                    - (maxLinesInView * tv.getLineHeight());
            tv.scrollTo(0, y);
        }
    }

    public Calendar convertMillisToDate(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar;
    }

    public String formatDateTime(String formatString, Date d) {
        Format formatter = new SimpleDateFormat(formatString);
        return formatter.format(d);
    }

    public String getAppVersion() {
        PackageInfo pi;
        try {
            pi = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            return "";
        }
    }

    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm != null) {
                return cm.getActiveNetworkInfo().isConnected();
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    public void saveToFile(String fileName, File directory, String contents) {
        Log.d(TAG, "^ saveToFile: attempting to save at '" + directory.getAbsolutePath() + "/" + fileName + "'");
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            try {

                if (directory.canWrite()) {
                    File gpxfile = new File(directory, fileName);
                    FileWriter gpxwriter = new FileWriter(gpxfile);
                    BufferedWriter out = new BufferedWriter(gpxwriter);
                    out.write(contents);
                    out.close();
                    Log.d(TAG, "^ saveToFile: saved as '" + directory.getAbsolutePath() + "/" + fileName + "'");
                    showToast("Saved to SD as '" + directory.getAbsolutePath() + "/" + fileName + "'",
                            Toast.LENGTH_SHORT, Gravity.TOP, 0, 0);
                } else {
                    throw new Exception();
                }

            } catch (Exception e) {
                Log.e(TAG, "^ Could not write file. Error: " + e.getMessage());
                showToast("Could not write file:\nError" + e.getMessage(), Toast.LENGTH_SHORT, Gravity.TOP, 0, 0);
            }

        } else {
            showToast("No SD card is mounted...", Toast.LENGTH_SHORT, Gravity.TOP, 0, 0);
            Log.e(TAG, "^ No SD card is mounted.");
        }
    }

    public void showAboutDialogue() {
        String text = "";
        String title = "";

        text += c.getString(R.string.app_changelog); //this.getSoftwareInfo(SOFTWARE_INFO.CHANGELOG);
        text += "\n\n";
        text += c.getString(R.string.app_notes); //this.getSoftwareInfo(SOFTWARE_INFO.NOTES);
        text += "\n\n";
        text += c.getString(R.string.app_acknowledgements); //this.getSoftwareInfo(SOFTWARE_INFO.ACKNOWLEDGEMENTS);
        text += "\n\n";
        text += c.getString(R.string.app_copyright);//this.getSoftwareInfo(SOFTWARE_INFO.COPYRIGHT);
        title = c.getString(R.string.app_name) + " v" + getAppVersion();

        if (!(c == null)) {
            MyAlertBox.create(c, text, title, c.getString(R.string.ok)).show();
        } else {
            Log.d(TAG, "^ context is null...");
        }
    }

    public void ShowAlert(String title, String text, String button) {
        if (button.equals("")) {
            button = c.getString(R.string.ok);
        }

        try {
            AlertDialog.Builder ad = new AlertDialog.Builder(c);
            ad.setTitle(title);
            ad.setMessage(text);

            ad.setPositiveButton(button, null);
            ad.show();
        } catch (Exception e) {
            Log.e(TAG, "^ ERROR: ShowAlert()", e);
        }
    }

    public void showToast(String message, int duration, int location, int x_offset, int y_offset) {
        Toast toast = Toast.makeText(c.getApplicationContext(), message, duration);
        toast.setGravity(location, x_offset, y_offset);
        toast.show();
    }

    public void share(String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Intent share = Intent.createChooser(intent, c.getString(R.string.share_result_via));
        c.startActivity(share);
    }
}
