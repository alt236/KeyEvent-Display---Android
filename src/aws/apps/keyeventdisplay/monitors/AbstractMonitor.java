package aws.apps.keyeventdisplay.monitors;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import android.util.Log;

//
//Code adapted from:
//http://android-random.googlecode.com/svn-history/r200/trunk/Logcat/src/org/devtcg/tools/logcat/LogcatProcessor.java
//

public abstract class AbstractMonitor extends Thread {
	final String TAG = this.getClass().getName();

	public static final int MSG_ERROR = 0;
	public static final int MSG_NEWLINE = 1;

	protected static final int BUFFER_SIZE = 1024;

	protected Process mProcess = null;

	protected Set<String> mFilteredWords;

	protected Process execute(String command[]) {
		try {
			return Runtime.getRuntime().exec(command);
		} catch (Exception e) {
			Log.e(TAG, "^ Can't start: " + command + e.getMessage());
			onError("Can't start " + command[0], e);
			return null;
		}
	}

	protected Process execute(String command) {
		try {
			return Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			Log.e(TAG, "^ Can't start: " + command + e.getMessage());
			onError("Can't start " + command, e);
			return null;
		}
	}

	public String getFilterWords() {
		StringBuffer sb = new StringBuffer();
		for (String s : mFilteredWords) {
			sb.append(s + "\n");
		}

		return sb.toString();
	}

	public boolean isValidLine(String line) {
		for (String s : mFilteredWords) {
			if (line.toLowerCase().contains(s)) {
				return true;
			}
		}
		return false;
	}

	public void loadWordList(String[] wordArray) {
		Log.i(TAG, "^ Loading wordlist. Length: " + wordArray.length);

		mFilteredWords = new HashSet<String>();

		for (String s : wordArray) {
			mFilteredWords.add(s.toLowerCase());
		}

		Log.i(TAG, "^ Set Length: " + mFilteredWords.size());
	}

	protected abstract void onError(String msg, Throwable e);

	protected abstract void onNewline(String line);

	public void stopCatter() {
		if (mProcess == null) {
			return;
		}

		mProcess.destroy();
		mProcess = null;
	}
}
