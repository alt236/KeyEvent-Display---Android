package aws.apps.keyeventdisplay.monitors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;

public abstract class LogCatMonitor extends AbstractMonitor {
	final String TAG = this.getClass().getName();

	public LogCatMonitor(String[] stringArray) {
		loadWordList(stringArray);
	}
	
	public static final String[] LOGCAT_CLEAR_CMD = new String[] { "logcat", "-c" };
	public static final String[] LOGCAT_CMD = new String[] { "logcat"};

	@Override
	protected abstract void onError(String msg, Throwable e);
	
	@Override
	protected abstract void onNewline(String line);
	
	public void run() {
		Log.d(TAG, "^ LogCatMonitor started...");
		BufferedReader reader = null;
		String line;
		
		execute(LOGCAT_CLEAR_CMD);
		mProcess = execute(LOGCAT_CMD);

		if (mProcess == null) {
			Log.e(TAG, "^ LogCatMonitor: Can't open log file. Exiting.");
			return;
		}

		try {
			reader = new BufferedReader(new InputStreamReader(
					mProcess.getInputStream()), BUFFER_SIZE);

			Log.d(TAG, "^ Pre loop!");
			while ((line = reader.readLine()) != null) {
				if (!line.contains(TAG) && isValidLine(line)) {
					onNewline(line);
				}
			}
			Log.d(TAG, "^ Post loop!");
		} catch (IOException e) {
			Log.e(TAG, "^ LogCatMonitor error: " + e.getMessage());
			onError("Error reading from process " + LOGCAT_CMD[0], e);
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}

			stopCatter();
		}
		Log.w(TAG, "^ LogCatMonitor thread has exited...");
	}	
}
