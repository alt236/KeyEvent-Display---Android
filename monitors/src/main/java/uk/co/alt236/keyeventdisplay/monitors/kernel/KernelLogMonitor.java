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
package uk.co.alt236.keyeventdisplay.monitors.kernel;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import uk.co.alt236.keyeventdisplay.monitors.Filter;
import uk.co.alt236.keyeventdisplay.monitors.Monitor;
import uk.co.alt236.keyeventdisplay.monitors.MonitorCallback;
import uk.co.alt236.keyeventdisplay.monitors.util.ClosableHelper;
import uk.co.alt236.keyeventdisplay.monitors.util.ProcessWrapper;

public class KernelLogMonitor implements Monitor {
    private static final String SU_CMD = "su";
    private static final String KERNEL_LOG_CMD = "cat /proc/kmsg";
    private final Filter filter;
    private KernelMonitorRunnable runnable;

    public KernelLogMonitor(String[] stringArray) {
        filter = new Filter(stringArray);
    }

    @Override
    public void startMonitor(final MonitorCallback callback) {
        runnable = new KernelMonitorRunnable(callback, filter);
        new Thread(runnable).start();
    }

    @Override
    public void stopMonitor() {
        if (runnable != null) {
            runnable.stop();
            runnable = null;
        }
    }


    private static class KernelMonitorRunnable implements Runnable {
        private final String TAG = this.getClass().getName();
        private final MonitorCallback callback;
        private final ProcessWrapper processWrapper;
        private final Filter filter;
        private boolean stopped;

        private KernelMonitorRunnable(final MonitorCallback callback,
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
            Log.d(TAG, "KernelLogMonitor started...");
            String line;
            BufferedReader reader = null;

            final boolean procStartedOk = processWrapper.execute(SU_CMD);
            if (!procStartedOk) {
                Log.e(TAG, "KernelLogMonitor: Can't open log file. Exiting.");
            } else {
                try {
                    final DataInputStream is = new DataInputStream(processWrapper.getInputStream());
                    final OutputStreamWriter os = new OutputStreamWriter(processWrapper.getOutputStream());

                    os.write(KERNEL_LOG_CMD + "\n");
                    os.flush();
                    os.close();

                    reader = new BufferedReader(new InputStreamReader(is), BUFFER_SIZE);

                    Log.d(TAG, "Pre loop!");

                    while ((line = reader.readLine()) != null && !stopped) {
                        Log.d(TAG, "New line!");
                        if (filter.isValidLine(line)) {
                            callback.onNewline(line);
                        }
                    }

                    Log.d(TAG, "Post loop!");
                } catch (IOException e) {
                    Log.e(TAG, "KernelLogMonitor error: " + e.getMessage());
                    callback.onError("Error reading from process " + KERNEL_LOG_CMD, e);
                } finally {
                    ClosableHelper.close(reader);
                }
                Log.d(TAG, "KernelLogMonitor has finished...");
            }
        }
    }
}
