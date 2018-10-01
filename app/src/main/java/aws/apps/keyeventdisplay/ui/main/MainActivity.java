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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import aws.apps.keyeventdisplay.R;
import aws.apps.keyeventdisplay.monitors.Monitor;
import aws.apps.keyeventdisplay.monitors.MonitorCallback;
import aws.apps.keyeventdisplay.monitors.kernel.KernelLogMonitor;
import aws.apps.keyeventdisplay.monitors.logcat.LogCatMonitor;
import aws.apps.keyeventdisplay.ui.common.ColorProvider;
import aws.apps.keyeventdisplay.ui.common.DialogFactory;
import aws.apps.keyeventdisplay.ui.common.NotifyUser;
import aws.apps.keyeventdisplay.ui.main.export.Exporter;
import aws.apps.keyeventdisplay.ui.main.export.WriteToDiskDelegate;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private static final int LAYOUT_ID = R.layout.activity_main;

    private WriteToDiskDelegate writeToDiskDelegate;
    private Monitor logCatMonitor;
    private Monitor kernelMonitor;
    private NotifyUser notifyUser;
    private Exporter exporter;
    private aws.apps.keyeventdisplay.ui.main.View view;
    private LineMarkupFactory markupFactory;
    private KeyEventConsumptionChecker keyEventConsumptionChecker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Starting: " + this.getClass().getName());

        setContentView(LAYOUT_ID);

        final ColorProvider colorProvider = new ColorProvider(getResources(), getTheme());

        markupFactory = new LineMarkupFactory(colorProvider);
        keyEventConsumptionChecker = new KeyEventConsumptionChecker();

        view = new aws.apps.keyeventdisplay.ui.main.View(colorProvider);
        view.bind(this);

        view.setClearLogButtonListener(v -> view.clearLog());
        view.setAppendBreakButtonListener(v -> view.appendBreakToLog());
        view.setAboutButtonListener(v -> showAboutDialog());
        view.setExitButtonListener(v -> quitApp());
        view.setShareLogButtonListener(v -> shareLog());
        view.setSaveLogButtonListener(v -> saveLogToDisk());

        notifyUser = new NotifyUser(this);
        exporter = new Exporter(this, notifyUser);
        writeToDiskDelegate = new WriteToDiskDelegate(this, exporter, notifyUser);

        final String[] logCatFilter = getResources().getStringArray(R.array.logcat_filter);
        final String[] kernelFilter = getResources().getStringArray(R.array.kmsg_filter);
        logCatMonitor = new LogCatMonitor(logCatFilter);
        kernelMonitor = new KernelLogMonitor(kernelFilter);

        final Object lastState = getLastCustomNonConfigurationInstance();
        if (lastState != null) {
            view.setState((ViewState) lastState);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        final LineMarkupFactory.LineMarkup markup = markupFactory.getForKeyDown();
        addKeyEventLine(markup, event);
        return keyEventConsumptionChecker.shouldConsumeEvent(event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        final LineMarkupFactory.LineMarkup markup = markupFactory.getForKeyLongPress();
        addKeyEventLine(markup, event);
        return keyEventConsumptionChecker.shouldConsumeEvent(event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
        final LineMarkupFactory.LineMarkup markup = markupFactory.getForKeyMultiple();
        addKeyEventLine(markup, event);
        return keyEventConsumptionChecker.shouldConsumeEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        final LineMarkupFactory.LineMarkup markup = markupFactory.getForKeyUp();
        addKeyEventLine(markup, event);
        return keyEventConsumptionChecker.shouldConsumeEvent(event);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
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
            final LineMarkupFactory.LineMarkup markup = markupFactory.getForKernel();
            view.appendLogLine(markup.getTag(), text, markup.getColour());
        }
    }

    private void addLogCatLine(String text) {
        if (view.isLogcatChecked()) {
            final LineMarkupFactory.LineMarkup markup = markupFactory.getForLogcat();
            view.appendLogLine(markup.getTag(), text, markup.getColour());
        }
    }

    private void addKeyEventLine(final LineMarkupFactory.LineMarkup markup,
                                 final KeyEvent event) {
        if (view.isKeyEventsChecked()) {
            view.appendKeyEvent(markup.getTag(), event, markup.getColour());
        }
    }

    private void quitApp() {
        onStop();
        System.exit(0);
    }

    private void showAboutDialog() {
        DialogFactory.createAboutDialog(this).show(getSupportFragmentManager(), "ALERT_DIALOG");
    }

    private void saveLogToDisk() {
        final String text = view.getEventLogText().toString();
        final boolean isSharable = isSharable(text);

        if (isSharable) {
            writeToDiskDelegate.saveLogToDisk(text);
        } else {
            notifyUser.notifyShort(R.string.nothing_to_save);
        }
    }

    private void shareLog() {
        final String text = view.getEventLogText().toString();
        final boolean isSharable = isSharable(text);

        if (isSharable) {
            exporter.share(text);
        } else {
            notifyUser.notifyShort(R.string.nothing_to_share);
        }
    }

    private boolean isSharable(final String content) {
        final String startText = getString(R.string.greeting);

        return content != null
                && !content.isEmpty()
                && !startText.equals(content);
    }
}
