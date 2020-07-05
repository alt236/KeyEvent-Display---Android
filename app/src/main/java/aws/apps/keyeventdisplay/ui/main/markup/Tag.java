package aws.apps.keyeventdisplay.ui.main.markup;

import androidx.annotation.ColorRes;
import aws.apps.keyeventdisplay.R;

enum Tag {
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
