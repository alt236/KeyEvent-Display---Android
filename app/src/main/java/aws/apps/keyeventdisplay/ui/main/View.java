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
import butterknife.BindView;
import butterknife.ButterKnife;

class View {
    private final ColorProvider colorProvider;
    @BindView(R.id.chkKernel)
    CheckBox chkKernel;
    @BindView(R.id.chkKeyEvents)
    CheckBox chkKeyEvents;
    @BindView(R.id.chkLogCat)
    CheckBox chkLogcat;
    @BindView(R.id.btnClear)
    Button btnClearLog;
    @BindView(R.id.btnBreak)
    Button btnAddBreakToLog;
    @BindView(R.id.btnSave)
    ImageButton btnSaveLog;
    @BindView(R.id.btnShare)
    ImageButton btnShareLog;
    @BindView(R.id.btnAbout)
    ImageButton btnAbout;
    @BindView(R.id.btnExit)
    ImageButton btnExit;
    @BindView(R.id.fldEvent)
    TextView eventLog;
    private LogViewWrapper eventLogWrapper;

    public View(final ColorProvider colorProvider) {
        this.colorProvider = colorProvider;
    }

    public void bind(final Activity activity) {
        ButterKnife.bind(this, activity);
        eventLogWrapper = new LogViewWrapper(eventLog, colorProvider);
    }

    public boolean isKernelChecked() {
        return chkKernel.isChecked();
    }

    public boolean isLogcatChecked() {
        return chkLogcat.isChecked();
    }

    public boolean isKeyEventsChecked() {
        return chkKeyEvents.isChecked();
    }

    public ViewState getState() {
        final ViewState state = new ViewState();

        state.setChkKernel(isKernelChecked());
        state.setChkLogcat(isLogcatChecked());
        state.setChkKeyEvents(isKeyEventsChecked());
        state.setLogText(eventLog.getText());

        return state;
    }

    public void setState(final ViewState viewState) {
        eventLog.setText(viewState.getLogText());
        chkKernel.setChecked(viewState.isChkKernel());
        chkKeyEvents.setChecked(viewState.isChkKeyEvents());
        chkLogcat.setChecked(viewState.isChkLogcat());
        eventLogWrapper.autoScroll();
    }

    public CharSequence getEventLogText() {
        return eventLog.getText();
    }

    public void appendLogLine(String title, String event, int color) {
        eventLogWrapper.appendLogLine(title, event, color);
    }

    public void appendKeyEvent(String title, KeyEvent event, int color) {
        eventLogWrapper.appendKeyEvent(title, event, color);
    }

    public void appendBreakToLog() {
        eventLogWrapper.appendBreak();
    }

    public void clearLog() {
        eventLogWrapper.clear();
    }

    public void setClearLogButtonListener(final android.view.View.OnClickListener listener) {
        btnClearLog.setOnClickListener(listener);
    }

    public void setAppendBreakButtonListener(final android.view.View.OnClickListener listener) {
        btnAddBreakToLog.setOnClickListener(listener);
    }

    public void setAboutButtonListener(final android.view.View.OnClickListener listener) {
        btnAbout.setOnClickListener(listener);
    }

    public void setExitButtonListener(final android.view.View.OnClickListener listener) {
        btnExit.setOnClickListener(listener);
    }

    public void setShareLogButtonListener(final android.view.View.OnClickListener listener) {
        btnShareLog.setOnClickListener(listener);
    }

    public void setSaveLogButtonListener(final android.view.View.OnClickListener listener) {
        btnSaveLog.setOnClickListener(listener);
    }
}
