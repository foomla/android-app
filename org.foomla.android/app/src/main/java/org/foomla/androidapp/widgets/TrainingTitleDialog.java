package org.foomla.androidapp.widgets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import org.foomla.androidapp.R;
import org.foomla.androidapp.utils.UiUtils;
import org.foomla.api.entities.twizard.Training;

public class TrainingTitleDialog {

    public interface SaveListener {
        void save(Training training, String title, String comment);
    }

    private final Training training;
    private final Context context;

    public TrainingTitleDialog(final Context context, final Training training) {
        this.context = context;
        this.training = training;
    }

    public Dialog build(final SaveListener saveListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_trainingtitle_input, null, true);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Training speichern");
        builder.setNegativeButton(R.string.dialog_trainingname_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.dialog_trainingname_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                String name = UiUtils.getSafeTextOrNull(view, R.id.title);
                String comment = UiUtils.getSafeTextOrNull(view, R.id.comment);

                saveListener.save(training, name, comment);
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.foogreen));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.darkgrey));
            }
        });
        dialog.show();
        return dialog;
    }
}
