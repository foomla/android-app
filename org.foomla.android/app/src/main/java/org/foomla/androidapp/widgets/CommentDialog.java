package org.foomla.androidapp.widgets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.foomla.androidapp.R;
import org.foomla.androidapp.data.Comment;

public class CommentDialog {

    public interface SaveListener {
        void save(Comment comment);
    }

    private final Context context;

    public CommentDialog(Context context) {
        this.context = context;
    }

    public Dialog build(final SaveListener saveListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_comment_exercise, null, true);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_commentexercise_okbutton, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveListener.save(new Comment(getEmail(view), getComment(view)));
            }
        });

        builder.setNegativeButton(R.string.dialog_commentexercise_cancelbutton, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.show();
    }

    private String getEmail(View view) {
        View v = view.findViewById(R.id.email);
        if (v != null && v instanceof EditText) {
            return ((EditText) v).getText().toString();
        }

        return null;
    }

    private String getComment(View view) {
        View v = view.findViewById(R.id.comment);
        if (v != null && v instanceof EditText) {
            return ((EditText) v).getText().toString();
        }

        return null;
    }
}
