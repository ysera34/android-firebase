package org.inframiner.ysera.chatter.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.inframiner.ysera.chatter.R;

/**
 * Created by yoon on 2017. 4. 9..
 */

public class CustomProgressDialogFragment extends DialogFragment {

    public static CustomProgressDialogFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CustomProgressDialogFragment fragment = new CustomProgressDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    private AlertDialog.Builder mBuilder;
    
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_custom_progress_dialog, null);
        mBuilder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return mBuilder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
