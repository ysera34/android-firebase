package org.inframiner.ysera.chatter.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;

import org.inframiner.ysera.chatter.R;
import org.inframiner.ysera.chatter.model.User;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static org.inframiner.ysera.chatter.common.Common.EXTRA_GOOGLE_RESPONSE_USER;
import static org.inframiner.ysera.chatter.common.Common.SIGN_IN_GOOGLE_REQUEST_CODE;

/**
 * Created by yoon on 2017. 5. 6..
 */

public class SignInFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SignInFragment.class.getSimpleName();

    public static SignInFragment newInstance() {

        Bundle args = new Bundle();

        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private SignInButton mSignInGoogleButton;
    private Button mSignOutGoogleButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mSignInGoogleButton = (SignInButton) view.findViewById(R.id.sign_in_google_button);
        mSignInGoogleButton.setOnClickListener(this);
        mSignOutGoogleButton = (Button) view.findViewById(R.id.sign_out_google_button);
        mSignOutGoogleButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_google_button:
//                ((SignInActivity) getActivity()).signInGoogle();
                Intent intent = UserActivity.newIntent(getActivity(), SIGN_IN_GOOGLE_REQUEST_CODE);
                startActivityForResult(intent, SIGN_IN_GOOGLE_REQUEST_CODE);
                break;
            case R.id.sign_out_google_button:
                ((SignInActivity) getActivity()).signOutGoogle();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SIGN_IN_GOOGLE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    User user = (User) data.getSerializableExtra(EXTRA_GOOGLE_RESPONSE_USER);
                    Log.d(TAG, user.getId());
                    Log.d(TAG, user.getIdToken());
                    Log.d(TAG, user.getEmail());
                    Log.d(TAG, user.getDisplayName());

                } else if (resultCode == RESULT_CANCELED) {
                    Log.d(TAG, "activity result canceled");
                }
        }
    }
}
