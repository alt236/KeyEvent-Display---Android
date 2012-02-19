package aws.apps.keyeventdisplay.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import aws.apps.keyeventdisplay.R;
import aws.apps.keyeventdisplay.ui.MyAlertBox;

public class UsefulBits {
	final String TAG =  this.getClass().getName();
	private Context c;

	public UsefulBits(Context cntx) {
		c = cntx;
	}

	public void autoScroll(TextView tv) {
		int maxLinesInView = tv.getHeight() / tv.getLineHeight();
		int y;
		y = 3;

		if (tv.getLineCount() == 0) {return;}

		if (maxLinesInView < tv.getLineCount()) {
			y = tv.getLineHeight() * tv.getLineCount()
					- (maxLinesInView * tv.getLineHeight());
			tv.scrollTo(0, y);
		}
	} 

	public Calendar convertMillisToDate(long millis){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar;
	}

	public String formatDateTime(String formatString, Date d){
		Format formatter = new SimpleDateFormat(formatString);
		return formatter.format(d);
	}

	public String getAppVersion(){
		PackageInfo pi;
		try {
			pi = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			return "";
		}
	}

	public boolean isOnline() {
		try{ 
			ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (cm != null) {
				return cm.getActiveNetworkInfo().isConnected();
			} else {
				return false;
			}

		}catch(Exception e){
			return false;
		}
	}

	public void saveToFile(String fileName, File directory, String contents){
		Log.d(TAG, "^ saveToFile: attempting to save at '" + directory.getAbsolutePath() + "/" + fileName + "'");
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
		{
			try {

				if (directory.canWrite()){
					File gpxfile = new File(directory, fileName);
					FileWriter gpxwriter = new FileWriter(gpxfile);
					BufferedWriter out = new BufferedWriter(gpxwriter);
					out.write(contents);
					out.close();
					Log.d(TAG, "^ saveToFile: saved as '" + directory.getAbsolutePath() + "/" + fileName + "'" );
					showToast("Saved to SD as '" + directory.getAbsolutePath() + "/" + fileName + "'", 
							Toast.LENGTH_SHORT, Gravity.TOP,0,0);
				} else {
					throw new Exception();
				}

			} catch (Exception e) {
				Log.e(TAG, "^ Could not write file. Error: " + e.getMessage());
				showToast("Could not write file:\nError" + e.getMessage(), Toast.LENGTH_SHORT, Gravity.TOP,0,0);
			} 

		}else{
			showToast("No SD card is mounted...", Toast.LENGTH_SHORT, Gravity.TOP,0,0);
			Log.e(TAG, "^ No SD card is mounted.");		
		}
	}

	public void showAboutDialogue(){
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

		if (!(c==null)){
			MyAlertBox.create(c, text, title, c.getString(R.string.ok)).show();
		} else {
			Log.d(TAG, "^ context is null...");
		}
	}

	public void ShowAlert(String title, String text, String button){
		if (button.equals("")){button = c.getString(R.string.ok);}

		try{
			AlertDialog.Builder ad = new AlertDialog.Builder(c);
			ad.setTitle( title );
			ad.setMessage(text);

			ad.setPositiveButton( button, null );
			ad.show();
		}catch (Exception e){
			Log.e(TAG, "^ ERROR: ShowAlert()", e);
		}	
	}

	public void showToast(String message, int duration, int location, int x_offset, int y_offset){
		Toast toast = Toast.makeText(c.getApplicationContext(), message, duration);
		toast.setGravity(location,x_offset,y_offset);
		toast.show();
	}
}