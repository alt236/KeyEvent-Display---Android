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
package aws.apps.keyeventdisplay.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.util.Log;
import android.view.KeyEvent;

import aws.apps.keyeventdisplay.R;
import aws.apps.keyeventdisplay.monitors.Monitor;
import aws.apps.keyeventdisplay.monitors.MonitorCallback;
import aws.apps.keyeventdisplay.monitors.kernel.KernelLogMonitor;
import aws.apps.keyeventdisplay.monitors.logcat.LogCatMonitor;
import aws.apps.keyeventdisplay.ui.common.ColorProvider;
import aws.apps.keyeventdisplay.ui.common.NotifyUser;
import aws.apps.keyeventdisplay.ui.dialogs.DialogFactory;
import aws.apps.keyeventdisplay.ui.main.export.Exporter;

public class MainActivity extends Activity {
    public static final String LOG_LINE_KERNEL = "Kernel:       ";
    public static final String LOG_LINE_LOGCAT = "Logcat:       ";
    public static final String LOG_LINE_KEY_DOWN = "KeyDown:      ";
    public static final String LOG_LINE_KEY_LONG_PRESS = "KeyLongPress: ";
    public static final String LOG_LINE_KEY_MULTIPLE = "KeyMultiple: ";
    public static final String LOG_LINE_KEY_UP = "KeyUp:        ";
    private final static String TAG = MainActivity.class.getSimpleName();

    private static final int LAYOUT_ID = R.layout.activity_main;

    private Monitor logCatMonitor;
    private Monitor kernelMonitor;
    private NotifyUser notifyUser;
    private Exporter exporter;
    private ColorProvider colorProvider;
    private aws.apps.keyeventdisplay.ui.main.View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_ID);
        colorProvider = new ColorProvider(getResources(), getTheme());

        view = new aws.apps.keyeventdisplay.ui.main.View(colorProvider);
        view.bind(this);

        view.setClearLogButtonListener(v -> view.clearLog());
        view.setAppendBreakButtonListener(v -> view.appendBreakToLog());
        view.setAboutButtonListener(v -> DialogFactory.createAboutDialog(this).show());
        view.setExitButtonListener(v -> quitApp());
        view.setShareLogButtonListener(v -> shareLog());
        view.setSaveLogButtonListener(v -> saveLogToDisk());

        notifyUser = new NotifyUser(this);
        exporter = new Exporter(this, notifyUser);

        final String[] logCatFilter = getResources().getStringArray(R.array.logcat_filter);
        final String[] kernelFilter = getResources().getStringArray(R.array.kmsg_filter);
        logCatMonitor = new LogCatMonitor(logCatFilter);
        kernelMonitor = new KernelLogMonitor(kernelFilter);

        final Object lastState = getLastNonConfigurationInstance();
        if (lastState != null) {
            view.setState((ViewState) lastState);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        addKeyEventLine(LOG_LINE_KEY_DOWN, event, R.color.color_keydown);
        return true;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        addKeyEventLine(LOG_LINE_KEY_LONG_PRESS, event, R.color.color_keylongpress);
        return true;
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
        addKeyEventLine(LOG_LINE_KEY_MULTIPLE, event, R.color.color_keymultiple);
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        addKeyEventLine(LOG_LINE_KEY_UP, event, R.color.color_keyup);
        return true;
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return view.getState();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "^ onStart called");

        logCatMonitor.startMonitor(new MonitorCallback() {
            @Override
            public void onError(final String msg, final Throwable e) {
                runOnUiThread(() -> notifyUser.notifyLong("LogCatMonitor: " + msg + ": " + e));
            }

            @Override
            public void onNewline(String line) {
                addLogCatLine(line);
            }
        });

        kernelMonitor.startMonitor(new MonitorCallback() {
            @Override
            public void onError(final String msg, final Throwable e) {
                runOnUiThread(() -> notifyUser.notifyLong("KernelLogMonitor: " + msg + ": " + e));
            }

            @Override
            public void onNewline(String line) {
                addKernelLine(line);
            }

        });
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "^ onStop called");
        logCatMonitor.stopMonitor();
        kernelMonitor.stopMonitor();
        super.onStop();
    }

    private void addKernelLine(String text) {
        if (view.isKernelChecked()) {
            view.appendLogLine(LOG_LINE_KERNEL, text, colorProvider.getColor(R.color.color_kernel));
        }
    }

    private void addLogCatLine(String text) {
        if (view.isLogcatChecked()) {
            view.appendLogLine(LOG_LINE_LOGCAT, text, colorProvider.getColor(R.color.color_logcat));
        }
    }

    private void addKeyEventLine(final String key,
                                 final KeyEvent event,
                                 @ColorRes final int colorId) {
        if (view.isKeyEventsChecked()) {
            view.appendKeyEvent(key, event, colorProvider.getColor(colorId));
        }
    }

    private void quitApp() {
        onStop();
        System.exit(0);
    }

    private void saveLogToDisk() {
        if (hasSharableContent()) {
            exporter.save(view.getEventLogText());
        } else {
            notifyUser.notifyShort(R.string.nothing_to_save);
        }
    }

    private void shareLog() {
        if (hasSharableContent()) {
            exporter.share(view.getEventLogText());
        } else {
            notifyUser.notifyShort(R.string.nothing_to_share);
        }
    }

    private boolean hasSharableContent() {
        final String startText = getString(R.string.greeting);
        final String content = view.getEventLogText().toString();

        return !startText.equals(content);
    }
}
