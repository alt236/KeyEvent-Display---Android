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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import aws.apps.keyeventdisplay.R;
import aws.apps.keyeventdisplay.monitors.KernelLogMonitor;
import aws.apps.keyeventdisplay.monitors.LogCatMonitor;
import aws.apps.keyeventdisplay.monitors.MonitorCallback;
import aws.apps.keyeventdisplay.ui.common.NotifyUser;
import aws.apps.keyeventdisplay.ui.dialogs.DialogFactory;

public class MainActivity extends Activity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private CheckBox chkKernel;
    private CheckBox chkKeyEvents;
    private CheckBox chkLogcat;
    private LogCatMonitor logCatM;
    private KernelLogMonitor kernelM;
    private NotifyUser notifyUser;
    private Exporter exporter;
    private LogViewWrapper logViewWrapper;
    private TextView fldLog;

    private String getExportText() {
        final StringBuilder sb = new StringBuilder();
        DeviceInfo.collectDeviceInfo(sb);
        sb.append("\n\n-----------------\n\n");
        sb.append(logViewWrapper.getTextAsString());
        return sb.toString();
    }

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

        final String startText = getString(R.string.greeting);
        if (!startText.equals(logViewWrapper.getTextAsString())) {
            exporter.save(getExportText());
        } else {
            notifyUser.notifyShort(R.string.nothing_to_save);
        }
    }

    public void onBtnShareClick(View target) {
        final String startText = getString(R.string.greeting);
        if (!startText.equals(logViewWrapper.getTextAsString())) {
            exporter.share(getExportText());
        } else {
            notifyUser.notifyShort(R.string.nothing_to_share);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        notifyUser = new NotifyUser(this);
        exporter = new Exporter(this, notifyUser);

        chkKeyEvents = (CheckBox) findViewById(R.id.chkKeyEvents);
        chkKernel = (CheckBox) findViewById(R.id.chkKernel);
        chkLogcat = (CheckBox) findViewById(R.id.chkLogCat);

        if (fldLog == null) {
            fldLog = (TextView) findViewById(R.id.fldEvent);
        }

        logViewWrapper = new LogViewWrapper(fldLog);

        final String[] logCatFilter = getResources().getStringArray(R.array.logcat_filter);
        final String[] kernelFilter = getResources().getStringArray(R.array.kmsg_filter);
        logCatM = new LogCatMonitor(logCatFilter);
        kernelM = new KernelLogMonitor(kernelFilter);

        final Object lastState = getLastNonConfigurationInstance();
        if (lastState != null) {
            fldLog.setText(((MainActivityState) lastState).getLogText());
            chkKernel.setChecked(((MainActivityState) lastState).isChkKenel());
            chkKeyEvents.setChecked(((MainActivityState) lastState).isChkKeyEvents());
            chkLogcat.setChecked(((MainActivityState) lastState).isChkLogcat());
            logViewWrapper.autoScroll();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        addKeyEventLine("^ KeyDown:      ", event, R.color.color_keydown);
        return true;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        addKeyEventLine("^ KeyLongPress: ", event, R.color.color_keylongpress);
        return true;
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
        addKeyEventLine("^ KeyMultiple: ", event, R.color.color_keymultiple);
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        addKeyEventLine("^ KeyUp:        ", event, R.color.color_keyup);
        return true;
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        final MainActivityState state = new MainActivityState();
        state.setChkKenel(chkKernel.isChecked());
        state.setChkLogcat(chkLogcat.isChecked());
        state.setChkKeyEvents(chkKeyEvents.isChecked());
        state.setLogText(logViewWrapper.getTextAsString());
        return (state);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "^ onStart called");

        logCatM.startMonitor(new MonitorCallback() {
            @Override
            public void onError(final String msg, final Throwable e) {
                //super.onError(msg, e);
                runOnUiThread(new Runnable() {
                    public void run() {
                        notifyUser.notifyLong("LogCatMonitor: " + msg + ": " + e);
                    }
                });
            }

            @Override
            public void onNewline(String line) {
                addLogCatLine(line);
            }
        });

        kernelM.startMonitor(new MonitorCallback() {
            @Override
            public void onError(final String msg, final Throwable e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        notifyUser.notifyLong("KernelLogMonitor: " + msg + ": " + e);
                    }
                });
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
            logViewWrapper.appendLogLine("^ Kernel:       ", text, getResources().getColor(R.color.color_kernel));
        }
    }

    public void addLogCatLine(String text) {
        if (chkLogcat.isChecked()) {
            logViewWrapper.appendLogLine("^ Logcat:       ", text, getResources().getColor(R.color.color_logcat));
        }
    }

    public void addKeyEventLine(final String key, final KeyEvent event, final int colorId) {
        if (chkKeyEvents.isChecked()) {
            logViewWrapper.appendKeyEvent(key, event, getResources().getColor(colorId));
        }
    }
}
