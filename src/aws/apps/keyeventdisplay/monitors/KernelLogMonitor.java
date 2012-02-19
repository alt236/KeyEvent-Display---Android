package aws.apps.keyeventdisplay.monitors;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.util.Log;

public abstract class KernelLogMonitor extends AbstractMonitor {
	private final String TAG = this.getClass().getName();
	public KernelLogMonitor(String[] stringArray) {
		loadWordList(stringArray);
	}

	public static final String KERNEL_LOG_CMD = "cat /proc/kmsg";

	@Override
	protected abstract void onError(String msg, Throwable e);

	@Override
	protected abstract void onNewline(String line);

	public void run() {
		Log.d(TAG, "^ KernelLogMonitor started...");
		String line;
		BufferedReader reader = null;

		mProcess = execute("su");
		if (mProcess == null) {
			Log.e(TAG, "^ KernelLogMonitor: Can't open log file. Exiting.");
			return;
		}

		try {
			DataInputStream is = new DataInputStream(mProcess.getInputStream());
			OutputStreamWriter os = new OutputStreamWriter(	mProcess.getOutputStream());

			os.write(KERNEL_LOG_CMD + "\n");
			os.flush();
			os.close();

			reader = new BufferedReader(new InputStreamReader(is), BUFFER_SIZE);

			Log.d(TAG, "^ Pre loop!");

			while ((line = reader.readLine()) != null) {
				Log.d(TAG, "^ New line!");
				if (isValidLine(line)) {
					onNewline(line);
				}
			}
			
			Log.d(TAG, "^ Post loop!");
		} catch (IOException e) {
			Log.e(TAG, "^ KernelLogMonitor error: " + e.getMessage());
			onError("Error reading from process " + KERNEL_LOG_CMD, e);
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}

			stopCatter();
		}
		Log.d(TAG, "^ KernelLogMonitor has finished...");
	}	
}
