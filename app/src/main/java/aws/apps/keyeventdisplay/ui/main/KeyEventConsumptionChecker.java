package aws.apps.keyeventdisplay.ui.main;

import android.view.KeyEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class KeyEventConsumptionChecker {
    private static Integer[] ALLOWED_ITEMS = {
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_DPAD_DOWN,
            KeyEvent.KEYCODE_DPAD_LEFT,
            KeyEvent.KEYCODE_DPAD_RIGHT};
    private static final Set<Integer> ALLOWED_ITEMS_SET = new HashSet<>(Arrays.asList(ALLOWED_ITEMS));

    public boolean shouldConsumeEvent(final KeyEvent event) {
        return !ALLOWED_ITEMS_SET.contains(event.getKeyCode());
    }
}
