package org.foomla.androidapp.widgets;

import org.foomla.androidapp.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class PleaseLoginDialog {

    public static interface Listener {
        void login();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PleaseLoginDialog.class);

    private final Context context;
    private final Listener listener;

    public PleaseLoginDialog(final Context context, final Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    public Dialog build() {

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                LOGGER.info("User does not want to login");
            }
        };

        DialogInterface.OnClickListener loginListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                LOGGER.info("User wants to login");
                listener.login();
            }
        };

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(R.string.dialog_forcelogin_title).setMessage(R.string.dialog_forcelogin_message)
                     .setNegativeButton(R.string.dialog_forcelogin_cancel, cancelListener).setPositiveButton(
                         R.string.dialog_forcelogin_ok, loginListener);
        return dialogBuilder.show();
    }
}
