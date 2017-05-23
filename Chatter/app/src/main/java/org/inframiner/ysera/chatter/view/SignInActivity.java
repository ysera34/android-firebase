package org.inframiner.ysera.chatter.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.inframiner.ysera.chatter.R;

/**
 * Created by yoon on 2017. 5. 4..
 */

public class SignInActivity extends UserActivity {

    private static final String TAG = SignInActivity.class.getSimpleName();

    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.container_sign_in);
        if (mFragment == null) {
            mFragmentManager.beginTransaction()
                    .add(R.id.container_sign_in, SignInFragment.newInstance())
                    .commit();
        }
    }
}
