package org.inframiner.ysera.chatter.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;

import org.inframiner.ysera.chatter.R;

/**
 * Created by yoon on 2017. 4. 9..
 */

public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_custom_progress_dialog);
//        setCancelable(false);
    }
}

