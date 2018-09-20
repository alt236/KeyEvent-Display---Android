package aws.apps.keyeventdisplay.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import aws.apps.keyeventdisplay.BuildConfig;
import aws.apps.keyeventdisplay.R;

public final class DialogFactory {

    public static Dialog createAboutDialog(final Context context) {
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_textview, null);
        final TextView textView = view.findViewById(R.id.text);

        final SpannableString text = new SpannableString(getAboutText(context));

        textView.setText(text);
        textView.setAutoLinkMask(Activity.RESULT_OK);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        Linkify.addLinks(text, Linkify.ALL);

        final DialogInterface.OnClickListener listener = (dialog, id) -> {
        };

        final String title = context.getString(R.string.app_name) + " v" + BuildConfig.VERSION_NAME;
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, listener)
                .setView(view)
                .create();
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
