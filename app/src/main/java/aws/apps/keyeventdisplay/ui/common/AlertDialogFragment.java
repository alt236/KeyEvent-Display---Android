package aws.apps.keyeventdisplay.ui.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import aws.apps.keyeventdisplay.R;

public class AlertDialogFragment extends DialogFragment {
    private static final String EXTRA_TITLE = AlertDialogFragment.class.getName() + ".EXTRA_TITLE";
    private static final String EXTRA_MESSAGE = AlertDialogFragment.class.getName() + ".EXTRA_MESSAGE";
    private static final String EXTRA_MODE = AlertDialogFragment.class.getName() + ".EXTRA_MODE";
    private static final int MODE_RES = 0;
    private static final int MODE_STRING = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int mode = getArguments().getInt(EXTRA_MODE);

        final String title;
        final String message;

        if (mode == 0) {
            title = getString(getArguments().getInt(EXTRA_TITLE));
            message = getString(getArguments().getInt(EXTRA_MESSAGE));
        } else {
            title = getArguments().getString(EXTRA_TITLE);
            message = getArguments().getString(EXTRA_MESSAGE);
        }

        return createDialog(title, message);
    }


    private Dialog createDialog(final String title, final String message) {
        final Context context = getContext();

        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_textview, null);
        final TextView textView = view.findViewById(R.id.text);
        final SpannableString spannableMessage = new SpannableString(message);

        textView.setText(spannableMessage);
        textView.setAutoLinkMask(Activity.RESULT_OK);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        Linkify.addLinks(spannableMessage, Linkify.ALL);

        final DialogInterface.OnClickListener listener = (dialog, id) -> {
        };

        return new AlertDialog
                .Builder(context)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, listener)
                .setView(view)
                .create();
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @NonNull
    protected static DialogFragment newInstance(@StringRes final int title,
                                                @StringRes final int message) {
        final DialogFragment frag = new AlertDialogFragment();
        final Bundle args = new Bundle();

        args.putInt(EXTRA_TITLE, title);
        args.putInt(EXTRA_MESSAGE, message);
        args.putInt(EXTRA_MODE, MODE_RES);

        frag.setArguments(args);

        return frag;
    }

    @NonNull
    protected static DialogFragment newInstance(final String title,
                                                final String message) {
        final DialogFragment frag = new AlertDialogFragment();
        final Bundle args = new Bundle();

        args.putString(EXTRA_TITLE, title);
        args.putString(EXTRA_MESSAGE, message);
        args.putInt(EXTRA_MODE, MODE_STRING);

        frag.setArguments(args);

        return frag;
    }
}