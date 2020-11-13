package aws.apps.keyeventdisplay.ui.main.logview;

import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.widget.TextView;

import aws.apps.keyeventdisplay.R;
import aws.apps.keyeventdisplay.ui.common.ColorProvider;

public class LogViewWrapper {
    private static final String NEW_LINE = "\n";
    private static final String BREAK = "-------------------------" + NEW_LINE;
    private static final int MAX_TEXT_LINES = 1024;
    private final TextView textView;
    private final CharSequence breakLine;

    public LogViewWrapper(final TextView textView,
                          final ColorProvider colorProvider) {
        this.textView = textView;
        this.textView.setMovementMethod(new ScrollingMovementMethod());

        final int breakColor = colorProvider.getColor(R.color.lime);
        this.breakLine = SpannableTextFormatHelper.color(breakColor, BREAK);
    }

    public void appendKeyEvent(String title, KeyEvent event, int color) {
        final Line line = new Line(color);

        line.append(title);
        line.append("action=", event.getAction());
        line.append(" code=", event.getKeyCode());
        line.append(" repeat=", event.getRepeatCount());
        line.append(" meta=", event.getMetaState());
        line.append(" scanCode=", event.getScanCode());
        line.append(" flags=", event.getFlags());
        line.append(" label='", event.getDisplayLabel(), "'");
        line.append(" chars='", event.getCharacters(), "'");
        line.append(" number='", event.getNumber(), "'");
        line.append(" deviceId='", event.getDeviceId(), "'");
        line.append(" source='", event.getSource(), "'");
        line.append("\n");

        append(line.getCharSequence());
    }

    public void appendLogLine(String title, String event, int color) {
        final Line line = new Line(color);

        line.append(title);
        line.append(event);
        line.append("\n");

        append(line.getCharSequence());
    }

    private void append(final CharSequence text) {
        sanityCheck();
        textView.append(text);
        autoScroll();
    }

    public void clear() {
        textView.setText(R.string.greeting);
        textView.scrollTo(0, 0);
    }

    public void appendBreak() {
        append(breakLine);
    }

    public CharSequence getText() {
        return textView.getText();
    }

    public void autoScroll() {
        if (textView.getLineCount() == 0) {
            return;
        }

        final int maxLinesInView = textView.getHeight() / textView.getLineHeight();

        if (maxLinesInView < textView.getLineCount()) {
            final int y = textView.getLineHeight() * textView.getLineCount()
                    - (maxLinesInView * textView.getLineHeight());
            textView.scrollTo(0, y);
        }
    }

    private void sanityCheck() {
        if (textView.getLineCount() > MAX_TEXT_LINES) {
            textView.setText("");
            textView.scrollTo(0, 0);
        }
    }
}
