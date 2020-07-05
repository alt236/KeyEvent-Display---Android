package aws.apps.keyeventdisplay.ui.main.markup;

import androidx.annotation.ColorInt;

public class LineMarkup {
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
