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
package uk.co.alt236.keyeventdisplay.monitors.logcat;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import uk.co.alt236.keyeventdisplay.monitors.Filter;
import uk.co.alt236.keyeventdisplay.monitors.Monitor;
import uk.co.alt236.keyeventdisplay.monitors.MonitorCallback;
import uk.co.alt236.keyeventdisplay.monitors.util.ClosableHelper;
import uk.co.alt236.keyeventdisplay.monitors.util.ProcessWrapper;

public class LogCatMonitor implements Monitor {
    private static final String[] LOGCAT_CLEAR_CMD = new String[]{"logcat", "-c"};
    private static final String[] LOGCAT_CMD = new String[]{"logcat"};
    private final Filter filter;
    private LogCatMonitorRunnable runnable;

    public LogCatMonitor(String[] stringArray) {
        filter = new Filter(stringArray);
    }

    @Override
    public void startMonitor(final MonitorCallback callback) {
        runnable = new LogCatMonitorRunnable(callback, filter);
        new Thread(runnable).start();
    }

    @Override
    public void stopMonitor() {
        if (runnable != null) {
            runnable.stop();
            runnable = null;
        }
    }

    private static class LogCatMonitorRunnable implements Runnable {
        private final String TAG = this.getClass().getName();
        private final MonitorCallback callback;
        private final ProcessWrapper processWrapper;
        private final Filter filter;
        private boolean stopped;

        private LogCatMonitorRunnable(final MonitorCallback callback,
                                      final Filter filter) {
            this.callback = callback;
            this.filter = filter;
            this.processWrapper = new ProcessWrapper();
        }

        public void stop() {
            stopped = true;
        }

        @Override
        public void run() {
            Log.d(TAG, "LogCatMonitor started...");
            BufferedReader reader = null;
            String line;

            processWrapper.execute(LOGCAT_CLEAR_CMD);
            final boolean procStartedOk = processWrapper.execute(LOGCAT_CMD);

            if (!procStartedOk) {
                Log.e(TAG, "LogCatMonitor: Can't open log file. Exiting.");
            } else {
                try {
                    reader = new BufferedReader(new InputStreamReader(processWrapper.getInputStream()), BUFFER_SIZE);

                    Log.d(TAG, "Pre loop!");
                    while ((line = reader.readLine()) != null && !stopped) {
                        if (!line.contains(TAG) && filter.isValidLine(line)) {
                            callback.onNewline(line);
                        }
                    }
                    Log.d(TAG, "Post loop!");
                } catch (IOException e) {
                    Log.e(TAG, "LogCatMonitor error: " + e.getMessage());
                    callback.onError("Error reading from process " + LOGCAT_CMD[0], e);
                } finally {
                    ClosableHelper.close(reader);
                }

                Log.w(TAG, "LogCatMonitor thread has exited...");
            }
        }
    }
}
