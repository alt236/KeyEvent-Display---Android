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
package aws.apps.keyeventdisplay.monitors;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class LogCatMonitor extends AbstractMonitor {
    public static final String[] LOGCAT_CLEAR_CMD = new String[]{"logcat", "-c"};
    public static final String[] LOGCAT_CMD = new String[]{"logcat"};
    final String TAG = this.getClass().getName();

    public LogCatMonitor(String[] stringArray) {
        loadWordList(stringArray);
    }

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
