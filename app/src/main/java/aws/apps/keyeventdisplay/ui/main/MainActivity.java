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
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import aws.apps.keyeventdisplay.R;
import aws.apps.keyeventdisplay.monitors.MonitorCallback;
import aws.apps.keyeventdisplay.monitors.kernel.KernelLogMonitor;
import aws.apps.keyeventdisplay.monitors.logcat.LogCatMonitor;
import aws.apps.keyeventdisplay.ui.common.ColorProvider;
import aws.apps.keyeventdisplay.ui.common.NotifyUser;
import aws.apps.keyeventdisplay.ui.dialogs.DialogFactory;
import aws.apps.keyeventdisplay.ui.main.export.Exporter;
import aws.apps.keyeventdisplay.ui.main.logview.LogViewWrapper;

public class MainActivity extends Activity {
    public static final String LOG_LINE_KERNEL = "Kernel:       ";
    public static final String LOG_LINE_LOGCAT = "Logcat:       ";
    public static final String LOG_LINE_KEY_DOWN = "KeyDown:      ";
    public static final String LOG_LINE_KEY_LONG_PRESS = "KeyLongPress: ";
    public static final String LOG_LINE_KEY_MULTIPLE = "KeyMultiple: ";
    public static final String LOG_LINE_KEY_UP = "KeyUp:        ";
    private final static String TAG = MainActivity.class.getSimpleName();

    private static final int LAYOUT_ID = R.layout.activity_main;

    private CheckBox chkKernel;
    private CheckBox chkKeyEvents;
    private CheckBox chkLogcat;
    private LogCatMonitor logCatM;
    private KernelLogMonitor kernelM;
    private NotifyUser notifyUser;
    private Exporter exporter;
    private LogViewWrapper logViewWrapper;
    private TextView fldLog;
    private ColorProvider colorProvider;

    public void onBtnAboutClick(View target) {
        DialogFactory.createAboutDialog(this).show();
    }

    public void onBtnBreakClick(View target) {
        logViewWrapper.appendBreak();
    }

    public void onBtnClearClick(View target) {
        logViewWrapper.clear();
    }

    public void onBtnExitClick(View target) {
        onStop();
        System.exit(0);
    }

    public void onBtnSaveClick(View target) {
        if (hasSharableContent()) {
            exporter.save(logViewWrapper.getText());
        } else {
            notifyUser.notifyShort(R.string.nothing_to_save);
        }
    }

    public void onBtnShareClick(View target) {
        if (hasSharableContent()) {
            exporter.share(logViewWrapper.getText());
        } else {
            notifyUser.notifyShort(R.string.nothing_to_share);
        }
    }


    private boolean hasSharableContent() {
        final String startText = getString(R.string.greeting);
        final String content = logViewWrapper.getText().toString();

        return !startText.equals(content);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_ID);
        colorProvider = new ColorProvider(getResources(), getTheme());
        notifyUser = new NotifyUser(this);
        exporter = new Exporter(this, notifyUser);

        chkKeyEvents = findViewById(R.id.chkKeyEvents);
        chkKernel = findViewById(R.id.chkKernel);
        chkLogcat = findViewById(R.id.chkLogCat);

        if (fldLog == null) {
            fldLog = findViewById(R.id.fldEvent);
        }

        logViewWrapper = new LogViewWrapper(fldLog, colorProvider);

        final String[] logCatFilter = getResources().getStringArray(R.array.logcat_filter);
        final String[] kernelFilter = getResources().getStringArray(R.array.kmsg_filter);
        logCatM = new LogCatMonitor(logCatFilter);
        kernelM = new KernelLogMonitor(kernelFilter);

        final Object lastState = getLastNonConfigurationInstance();
        if (lastState != null) {
            final ActivityState activityState = (ActivityState) lastState;

            fldLog.setText(activityState.getLogText());
            chkKernel.setChecked(activityState.isChkKernel());
            chkKeyEvents.setChecked(activityState.isChkKeyEvents());
            chkLogcat.setChecked(activityState.isChkLogcat());
            logViewWrapper.autoScroll();
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
        final ActivityState state = new ActivityState();
        state.setChkKernel(chkKernel.isChecked());
        state.setChkLogcat(chkLogcat.isChecked());
        state.setChkKeyEvents(chkKeyEvents.isChecked());
        state.setLogText(logViewWrapper.getText());
        return (state);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "^ onStart called");

        logCatM.startMonitor(new MonitorCallback() {
            @Override
            public void onError(final String msg, final Throwable e) {
                runOnUiThread(() -> notifyUser.notifyLong("LogCatMonitor: " + msg + ": " + e));
            }

            @Override
            public void onNewline(String line) {
                addLogCatLine(line);
            }
        });

        kernelM.startMonitor(new MonitorCallback() {
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
        logCatM.stopMonitor();
        kernelM.stopMonitor();
        super.onStop();
    }

    public void addKernelLine(String text) {
        if (chkKernel.isChecked()) {
            logViewWrapper.appendLogLine(LOG_LINE_KERNEL, text, colorProvider.getColor(R.color.color_kernel));
        }
    }

    public void addLogCatLine(String text) {
        if (chkLogcat.isChecked()) {
            logViewWrapper.appendLogLine(LOG_LINE_LOGCAT, text, colorProvider.getColor(R.color.color_logcat));
        }
    }

    public void addKeyEventLine(final String key,
                                final KeyEvent event,
                                @ColorRes final int colorId) {
        if (chkKeyEvents.isChecked()) {
            logViewWrapper.appendKeyEvent(key, event, colorProvider.getColor(colorId));
        }
    }
}
