package aws.apps.keyeventdisplay.ui.common;

import android.content.Context;
import android.support.v4.app.DialogFragment;

import aws.apps.keyeventdisplay.BuildConfig;
import aws.apps.keyeventdisplay.R;

public final class DialogFactory {

    public static DialogFragment createAboutDialog(final Context context) {
        final String title = context.getString(R.string.app_name) + " v" + BuildConfig.VERSION_NAME;
        final String message = getAboutText(context);
        return AlertDialogFragment.newInstance(title, message);
    }

    private static String getAboutText(final Context context) {
        String text = "";

        text += context.getString(R.string.app_changelog);
        text += "\n\n";
        text += context.getString(R.string.app_notes);
        text += "\n\n";
        text += context.getString(R.string.app_acknowledgements);
        text += "\n\n";
        text += context.getString(R.string.app_copyright);

        return text;
    }
}
