package org.inframiner.ysera.chatter.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.inframiner.ysera.chatter.R;

/**
 * Created by yoon on 2017. 2. 18..
 */

public class SignInFragment extends Fragment implements View.OnClickListener {

    public static SignInFragment newInstance() {

        Bundle args = new Bundle();

        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private EditText mEditText1;
    private EditText mEditText2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mEditText1 = (EditText) view.findViewById(R.id.edit_text_1);
        mEditText2 = (EditText) view.findViewById(R.id.edit_text_2);
        mEditText2.setError("에러입니다.", getResources().getDrawable(android.R.drawable.ic_menu_recent_history));

        (view.findViewById(R.id.button)).setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
//                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//                progressDialog.show();

//                CustomProgressDialogFragment dialogFragment = CustomProgressDialogFragment.newInstance();
//                dialogFragment.show(getFragmentManager(), "customProgressDialog");

//                CustomProgressDialog customProgressDialog = new CustomProgressDialog(getActivity());
//                customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                customProgressDialog.show();

                CustomProgressAlertDialog alertDialog = new CustomProgressAlertDialog(getContext());
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                break;
        }
    }
}
