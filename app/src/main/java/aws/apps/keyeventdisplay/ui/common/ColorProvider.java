package aws.apps.keyeventdisplay.ui.common;

import android.content.res.Resources;
import android.support.annotation.ColorRes;
import android.support.v4.content.res.ResourcesCompat;

public class ColorProvider {
    private final Resources resources;
    private final Resources.Theme theme;

    public ColorProvider(final Resources resources,
                         final Resources.Theme theme) {
        this.resources = resources;
        this.theme = theme;
    }

    public int getColor(@ColorRes final int resId) {
        return ResourcesCompat.getColor(resources, resId, theme);
    }
}
