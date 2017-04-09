package org.inframiner.ysera.chatter.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import org.inframiner.ysera.chatter.R;

/**
 * Created by yoon on 2017. 4. 9..
 */

public class CustomProgressAlertDialog extends AlertDialog {

    public CustomProgressAlertDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.layout_custom_progress_dialog);
//        setCancelable(false);
    }
}
