package aws.apps.keyeventdisplay.ui.main.logview;

/*package*/ class Line {
    private final StringBuilder sb;
    private final int color;

    Line(final int color) {
        this.sb = new StringBuilder();
        this.color = color;
    }

    public void append(final String string) {
        sb.append(string);
    }

    public void append(final String val1,
                       final String val2) {
        append(val1);
        append(val2);
    }

    public void append(final String val1,
                       final int val2) {
        append(val1);
        append(String.valueOf(val2));
    }

    public void append(final String val1,
                       final String val2,
                       final String val3) {
        append(val1);
        append(val2);
        append(val3);
    }

    public void append(final String val1,
                       final int val2,
                       final String val3) {
        append(val1);
        append(String.valueOf(val2));
        append(val3);
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    public CharSequence getCharSequence() {
        return SpannableTextFormatHelper.color(color, sb);
    }
}
