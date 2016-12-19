package aws.apps.keyeventdisplay.ui.main.logview;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

/*package*/ final class SpannableTextFormatHelper {
    private SpannableTextFormatHelper() {
        // INSTANTIATION BAD
    }

    /**
     * Returns a CharSequence that concatenates the specified array of CharSequence
     * objects and then applies a list of zero or more tags to the entire range.
     *
     * @param content an array of character sequences to apply a style to
     * @param tags    the styled span objects to apply to the content
     *                such as android.text.style.StyleSpan
     */
    private static CharSequence apply(final CharSequence[] content, final Object... tags) {
        final SpannableStringBuilder text = new SpannableStringBuilder();
        openTags(text, tags);
        for (final CharSequence item : content) {
            text.append(item);
        }
        closeTags(text, tags);
        return text;
    }

    /**
     * Returns a CharSequence that applies boldface to the concatenation
     * of the specified CharSequence objects.
     */
    @SuppressWarnings("unused")
    public static CharSequence bold(final CharSequence... content) {
        return apply(content, new StyleSpan(Typeface.BOLD));
    }

    /**
     * Returns a CharSequence that applies bold and italics to the concatenation
     * of the specified CharSequence objects.
     */
    @SuppressWarnings("unused")
    public static CharSequence boldItalic(final CharSequence... content) {
        return apply(content, new StyleSpan(Typeface.BOLD_ITALIC));
    }

    /**
     * "Closes" the specified tags on a Spannable by updating the spans to be
     * endpoint-exclusive so that future text appended to the end will not take
     * on the same styling. Do not call this method directly.
     */
    private static void closeTags(final Spannable text, final Object[] tags) {
        final int len = text.length();
        for (final Object tag : tags) {
            if (len > 0) {
                text.setSpan(tag, 0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                text.removeSpan(tag);
            }
        }
    }

    /**
     * Returns a CharSequence that applies a foreground color to the
     * concatenation of the specified CharSequence objects.
     */
    @SuppressWarnings("unused")
    public static CharSequence color(final int color, final CharSequence... content) {
        return apply(content, new ForegroundColorSpan(color));
    }

    /**
     * Returns a CharSequence that applies italics to the concatenation
     * of the specified CharSequence objects.
     */
    @SuppressWarnings("unused")
    public static CharSequence italic(final CharSequence... content) {
        return apply(content, new StyleSpan(Typeface.ITALIC));
    }

    /**
     * Returns a CharSequence that applies normal to the concatenation
     * of the specified CharSequence objects.
     */
    @SuppressWarnings("unused")
    public static CharSequence normal(final CharSequence... content) {
        return apply(content, new StyleSpan(Typeface.NORMAL));
    }

    /**
     * Iterates over an array of tags and applies them to the beginning of the specified
     * Spannable object so that future text appended to the text will have the styling
     * applied to it. Do not call this method directly.
     */
    private static void openTags(final Spannable text, final Object[] tags) {
        for (final Object tag : tags) {
            text.setSpan(tag, 0, 0, Spannable.SPAN_MARK_MARK);
        }
    }
}