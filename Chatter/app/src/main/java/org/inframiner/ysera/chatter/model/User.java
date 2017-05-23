package org.inframiner.ysera.chatter.model;

import org.inframiner.ysera.chatter.view.OnChangedUserListener;

import java.io.Serializable;

/**
 * Created by yoon on 2017. 5. 23..
 */

public class User implements Serializable {

    private String mId;
    private String mIdToken;
    private String mEmail;
    private String mDisplayName;
    private OnChangedUserListener mOnChangedUserListener;

    public User() {
        mOnChangedUserListener = null;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getIdToken() {
        return mIdToken;
    }

    public void setIdToken(String idToken) {
        mIdToken = idToken;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    public void setOnChangedUserListener(OnChangedUserListener onChangedUserListener) {
        mOnChangedUserListener = onChangedUserListener;
    }

    @Override
    public String toString() {
        return "User{" +
                "mId='" + mId + '\'' +
                ", mIdToken='" + mIdToken + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mDisplayName='" + mDisplayName + '\'' +
                '}';
    }
}
