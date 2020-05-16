package aws.apps.keyeventdisplay.ui.main;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import aws.apps.keyeventdisplay.R;
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

    private enum Tag {
        KERNEL("Kernel", R.color.color_kernel),
        LOGCAT("Logcat", R.color.color_logcat),
        KEY_DOWN("KeyDown", R.color.color_keydown),
        KEY_UP("KeyUp", R.color.color_keyup),
        KEY_LONGPRESS("KeyLongPress", R.color.color_keylongpress),
        KEY_MULTIPLE("KeyMultiple", R.color.color_keymultiple);

        private final String text;
        private final int colorResId;

        Tag(final String text,
            @ColorRes final int colorResId) {
            this.text = text;
            this.colorResId = colorResId;
        }

        public String getText() {
            return text;
        }

        @ColorRes
        public int getColorResId() {
            return colorResId;
        }
    }

    public static class LineMarkup {
        private final String tag;
        private final int colour;

        public LineMarkup(final String tag,
                          @ColorInt final int colour) {
            this.tag = tag;
            this.colour = colour;
        }

        public String getTag() {
            return tag;
        }

        @ColorInt
        public int getColour() {
            return colour;
        }
    }
}
