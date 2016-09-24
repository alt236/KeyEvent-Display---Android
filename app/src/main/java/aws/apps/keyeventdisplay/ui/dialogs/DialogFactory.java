package aws.apps.keyeventdisplay.ui.dialogs;

import android.app.Dialog;
import android.content.Context;

import aws.apps.keyeventdisplay.BuildConfig;
import aws.apps.keyeventdisplay.R;

public final class DialogFactory {


    public static Dialog createAboutDialog(final Context context) {
        String text = "";
        String title = "";

        text += context.getString(R.string.app_changelog);
        text += "\n\n";
        text += context.getString(R.string.app_notes);
        text += "\n\n";
        text += context.getString(R.string.app_acknowledgements);
        text += "\n\n";
        text += context.getString(R.string.app_copyright);
        title = context.getString(R.string.app_name) + " v" + BuildConfig.VERSION_NAME;

        return OkAlertBox.create(context, text, title, context.getString(R.string.ok));
    }

    public static Dialog createOkDialog(Context context, String text, String title, String button) {
        return OkAlertBox.create(context, text, title, button);
    }
}
