package aws.apps.keyeventdisplay.ui.common;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class NotifyUser {
    private final Context context;

    public NotifyUser(final Context context) {
        this.context = context.getApplicationContext();
    }

    @Deprecated
    public void notifyLong(final CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public void notifyLong(@StringRes int stringResId) {
        Toast.makeText(context, stringResId, Toast.LENGTH_LONG).show();
    }

    @Deprecated
    public void notifyShort(final CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public void notifyShort(@StringRes int stringResId) {
        Toast.makeText(context, stringResId, Toast.LENGTH_SHORT).show();
    }
}