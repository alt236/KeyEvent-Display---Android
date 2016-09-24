package aws.apps.keyeventdisplay.ui.main;

import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.widget.TextView;

import java.util.Locale;

import aws.apps.keyeventdisplay.R;

/*package*/
@SuppressWarnings("WeakerAccess")
class LogViewWrapper {
    private static final int MAX_TEXT_LINES = 1024;
    private static final String BREAK_FORMATTER = "<font color=%d>%s</font><br/>";
    private final TextView textView;
    private final CharSequence breakLine;

    public LogViewWrapper(final TextView textView) {
        this.textView = textView;
        this.textView.setMovementMethod(new ScrollingMovementMethod());

        final int breakColor = getColor(R.color.lime);
        final String breakText = getString(R.string.break_string);
        this.breakLine = Html.fromHtml(String.format(Locale.US, BREAK_FORMATTER, breakColor, breakText));
    }

    public void appendKeyEvent(String title, KeyEvent event, int color) {
        final StringBuilder sb = new StringBuilder();

        sb.append("<font color=" + color + ">" + title.replace(" ", "&nbsp;"));
        sb.append("action=" + event.getAction());
        sb.append(" code=" + event.getKeyCode());
        sb.append(" repeat=" + event.getRepeatCount());
        sb.append(" meta=" + event.getMetaState());
        sb.append(" scancode=" + event.getScanCode());
        sb.append(" mFlags=" + event.getFlags());
        sb.append(" label='" + event.getDisplayLabel() + "'");
        sb.append(" chars='" + event.getCharacters() + "'");
        sb.append(" number='" + event.getNumber() + "'");
        sb.append("</font> <br/>");

        append(Html.fromHtml(sb.toString()));
    }

    public void appendLogLine(String title, String event, int color) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<font color=" + color + ">" + title.replace(" ", "&nbsp;"));
        sb.append(event);
        sb.append("</font> <br/>");
        append(Html.fromHtml(sb.toString()));
    }

    public void append(final CharSequence text) {
        sanityCheck();
        textView.append(text);
        autoScroll();
    }

    public void clear() {
        textView.setText(R.string.greeting);
        textView.scrollTo(0, 0);
    }

    public void appendBreak() {
        textView.append(breakLine);

    }

    public String getTextAsString() {
        return textView.getText().toString();
    }

    private void sanityCheck() {
        if (textView.getLineCount() > MAX_TEXT_LINES) {
            textView.setText("");
            textView.scrollTo(0, 0);
        }
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

    private int getColor(final int resId) {
        return textView.getContext().getResources().getColor(resId);
    }

    private String getString(final int resId) {
        return textView.getContext().getString(resId);
    }
}
