package aws.apps.keyeventdisplay.ui;

import android.content.Context;
import android.widget.Toast;

public class NotifyUser {
    private final Context context;

    public NotifyUser(final Context context) {
        this.context = context.getApplicationContext();
    }

    public void notifyLong(final CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public void notifyLong(int stringResId) {
        Toast.makeText(context, stringResId, Toast.LENGTH_LONG).show();
    }

    public void notifyShort(final CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public void notifyShort(int stringResId) {
        Toast.makeText(context, stringResId, Toast.LENGTH_SHORT).show();
    }
}