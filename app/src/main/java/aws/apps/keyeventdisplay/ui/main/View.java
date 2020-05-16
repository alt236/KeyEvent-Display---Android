package aws.apps.keyeventdisplay.ui.main;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import aws.apps.keyeventdisplay.R;
import aws.apps.keyeventdisplay.ui.common.ColorProvider;
import aws.apps.keyeventdisplay.ui.main.logview.LogViewWrapper;

class View {
    private final ColorProvider colorProvider;
    private LogViewWrapper eventLogWrapper;

    private CheckBox chkKernel;
    private CheckBox chkKeyEvents;
    private CheckBox chkLogcat;
    private Button btnClearLog;
    private Button btnAddBreakToLog;
    private ImageButton btnSaveLog;
    private ImageButton btnShareLog;
    private ImageButton btnAbout;
    private ImageButton btnExit;
    private TextView eventLog;

    View(final ColorProvider colorProvider) {
        this.colorProvider = colorProvider;
    }

    void bind(final Activity activity) {
        chkKernel = activity.findViewById(R.id.chkKernel);
        chkKeyEvents = activity.findViewById(R.id.chkKeyEvents);
        chkLogcat = activity.findViewById(R.id.chkLogCat);
        btnClearLog = activity.findViewById(R.id.btnClear);
        btnAddBreakToLog = activity.findViewById(R.id.btnBreak);
        btnSaveLog = activity.findViewById(R.id.btnSave);
        btnShareLog = activity.findViewById(R.id.btnShare);
        btnAbout = activity.findViewById(R.id.btnAbout);
        btnExit = activity.findViewById(R.id.btnExit);
        eventLog = activity.findViewById(R.id.fldEvent);

        eventLogWrapper = new LogViewWrapper(eventLog, colorProvider);
    }

    boolean isKernelChecked() {
        return chkKernel.isChecked();
    }

    boolean isLogcatChecked() {
        return chkLogcat.isChecked();
    }

    boolean isKeyEventsChecked() {
        return chkKeyEvents.isChecked();
    }

    ViewState getState() {
        final ViewState state = new ViewState();

        state.setChkKernel(isKernelChecked());
        state.setChkLogcat(isLogcatChecked());
        state.setChkKeyEvents(isKeyEventsChecked());
        state.setLogText(eventLog.getText());

        return state;
    }

    void setState(final ViewState viewState) {
        eventLog.setText(viewState.getLogText());
        chkKernel.setChecked(viewState.isChkKernel());
        chkKeyEvents.setChecked(viewState.isChkKeyEvents());
        chkLogcat.setChecked(viewState.isChkLogcat());
        eventLogWrapper.autoScroll();
    }

    CharSequence getEventLogText() {
        return eventLog.getText();
    }

    void appendLogLine(String title, String event, int color) {
        eventLogWrapper.appendLogLine(title, event, color);
    }

    void appendKeyEvent(String title, KeyEvent event, int color) {
        eventLogWrapper.appendKeyEvent(title, event, color);
    }

    void appendBreakToLog() {
        eventLogWrapper.appendBreak();
    }

    void clearLog() {
        eventLogWrapper.clear();
    }

    void setClearLogButtonListener(final android.view.View.OnClickListener listener) {
        btnClearLog.setOnClickListener(listener);
    }

    void setAppendBreakButtonListener(final android.view.View.OnClickListener listener) {
        btnAddBreakToLog.setOnClickListener(listener);
    }

    void setAboutButtonListener(final android.view.View.OnClickListener listener) {
        btnAbout.setOnClickListener(listener);
    }

    void setExitButtonListener(final android.view.View.OnClickListener listener) {
        btnExit.setOnClickListener(listener);
    }

    void setShareLogButtonListener(final android.view.View.OnClickListener listener) {
        btnShareLog.setOnClickListener(listener);
    }

    void setSaveLogButtonListener(final android.view.View.OnClickListener listener) {
        btnSaveLog.setOnClickListener(listener);
    }
}
