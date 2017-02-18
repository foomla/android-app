package org.foomla.androidapp.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.widget.TextView;

import org.foomla.androidapp.R;

public class ExplainShakeDialog extends Dialog {

    private static ExplainShakeDialog instance;

    public static void display(final Context context, final int messageId) {
        if (instance == null) {
            instance = new ExplainShakeDialog(context);
        }

        instance.show();

        TextView tv = (TextView) instance.findViewById(R.id.message);
        tv.setText(messageId);
    }

    private ExplainShakeDialog(final Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            instance.dismiss();
            instance = null;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void show() {
        setContentView(R.layout.dialog_explain_shake);
        super.show();
    }
}
