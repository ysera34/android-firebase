package org.inframiner.ysera.chatter.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.inframiner.ysera.chatter.R;

public class SignInActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_sign_in, SignInFragment.newInstance())
                .commit();
    }


}
