package ch.epfl.sweng.runpharaa.firebase.authentification;

import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.internal.firebase_auth.zzap;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class FirebaseAuthMock implements FirebaseAuthInterface {

    private static FirebaseUser FakeFireBaseUser = null;

    public static void setFakeFireBaseUser(FirebaseUser user) {
        FakeFireBaseUser = user;
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return FakeFireBaseUser;
    }

    @Override
    public Task<AuthResult> signInWithCredential(AuthCredential c) {
        return null;
    }
}
