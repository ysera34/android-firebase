package org.inframiner.ysera.chatter.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.inframiner.ysera.chatter.R;
import org.inframiner.ysera.chatter.model.User;

import static org.inframiner.ysera.chatter.common.Common.EXTRA_GOOGLE_RESPONSE_USER;
import static org.inframiner.ysera.chatter.common.Common.REVOKE_ACCESS_GOOGLE_REQUEST_CODE;
import static org.inframiner.ysera.chatter.common.Common.SIGN_IN_GOOGLE_REQUEST_CODE;
import static org.inframiner.ysera.chatter.common.Common.SIGN_OUT_GOOGLE_REQUEST_CODE;

/**
 * Created by yoon on 2017. 5. 22..
 */

public class UserActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = UserActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;
    private static final String EXTRA_GOOGLE_REQUEST_CODE = "org.inframincer.ysera.google_request_code";

    private FirebaseAuth mFirebaseAuth;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private GoogleApiClient mGoogleApiClient;

    public static Intent newIntent(Context packageContext, int requestCode) {
        Intent intent = new Intent(packageContext, UserActivity.class);
        intent.putExtra(EXTRA_GOOGLE_REQUEST_CODE, requestCode);
        return intent;
    }

    private int mGoogleRequestCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions googleSignInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.google_default_web_client_id))
                        .requestEmail()
                        .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        mGoogleRequestCode = getIntent().getIntExtra(EXTRA_GOOGLE_REQUEST_CODE, -1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (mGoogleRequestCode) {
            case SIGN_IN_GOOGLE_REQUEST_CODE:
                signInGoogle();
                break;
            case SIGN_OUT_GOOGLE_REQUEST_CODE:
                signOutGoogle();
                break;
            case REVOKE_ACCESS_GOOGLE_REQUEST_CODE:
                revokeAccessGoogle();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Intent intent = new Intent();
            if (result.isSuccess()) {
                Log.d(TAG, "Google Sign In was successful, authenticate with Firebase");
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

                intent.putExtra(EXTRA_GOOGLE_RESPONSE_USER, setUser(account));
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Log.d(TAG, "Google Sign In failed.");
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "signInWithCredential:onFailure:" + e.getMessage());
                    }
                });
    }

    public void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOutGoogle() {
        mFirebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Log.d(TAG, "sign out GoogleApiClient.");
                    }
                });
    }

    public void revokeAccessGoogle() {
        mFirebaseAuth.signOut();

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Log.d(TAG, "revoke access GoogleApiClient");
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private User setUser(GoogleSignInAccount account) {

        User user = new User();

        if (account != null) {
            user.setId(account.getId());
            user.setIdToken(account.getIdToken());
            user.setEmail(account.getEmail());
            user.setDisplayName(account.getDisplayName());
        } else {
            user = null;
        }
        return user;
    }
}
