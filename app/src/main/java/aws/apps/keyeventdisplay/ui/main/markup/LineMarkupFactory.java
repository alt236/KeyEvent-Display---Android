package aws.apps.keyeventdisplay.ui.main.markup;

import java.util.HashMap;
import java.util.Map;

import aws.apps.keyeventdisplay.ui.common.ColorProvider;

public class LineMarkupFactory {
    private static final String TAG_END = ": ";

    private final Map<Tag, LineMarkup> lineMarkupMap;

    public LineMarkupFactory(final ColorProvider colorProvider) {
        this.lineMarkupMap = new HashMap<>();

        final int size = findLargestTagSize();

        for (Tag tag : Tag.values()) {
            final String text = padRight(tag.getText(), size) + TAG_END;
            final int color = colorProvider.getColor(tag.getColorResId());
            lineMarkupMap.put(
                    tag,
                    new LineMarkup(text, color));
        }
    }

    public LineMarkup getForKeyDown() {
        return lineMarkupMap.get(Tag.KEY_DOWN);
    }

    public LineMarkup getForKeyUp() {
        return lineMarkupMap.get(Tag.KEY_UP);
    }

    public LineMarkup getForKeyLongPress() {
        return lineMarkupMap.get(Tag.KEY_LONGPRESS);
    }

    public LineMarkup getForKeyMultiple() {
        return lineMarkupMap.get(Tag.KEY_MULTIPLE);
    }

    public LineMarkup getForLogcat() {
        return lineMarkupMap.get(Tag.LOGCAT);
    }

    public LineMarkup getForKernel() {
        return lineMarkupMap.get(Tag.KERNEL);
    }

    private int findLargestTagSize() {
        int size = 0;
        for (Tag tag : Tag.values()) {
            if (tag.getText().length() > size) {
                size = tag.getText().length();
            }
        }
        return size;
    }

    private String padRight(String str, int num) {
        return String.format("%1$-" + num + "s", str);
    }

}
