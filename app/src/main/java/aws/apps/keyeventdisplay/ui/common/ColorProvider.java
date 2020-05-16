package aws.apps.keyeventdisplay.ui.common;

import android.content.res.Resources;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.content.res.ResourcesCompat;

public class ColorProvider {
    private final Resources resources;
    private final Resources.Theme theme;

    public ColorProvider(final Resources resources,
                         final Resources.Theme theme) {
        this.resources = resources;
        this.theme = theme;
    }

    @ColorInt
    public int getColor(@ColorRes final int resId) {
        return ResourcesCompat.getColor(resources, resId, theme);
    }
}
