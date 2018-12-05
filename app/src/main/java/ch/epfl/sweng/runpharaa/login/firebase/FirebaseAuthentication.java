package ch.epfl.sweng.runpharaa.login.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import ch.epfl.sweng.runpharaa.utils.Config;

public abstract class FirebaseAuthentication {

    private static FirebaseAuthentication instance;

    public static FirebaseAuthentication getInstance() {
        if (instance == null) {
            instance = Config.isTest ? new FirebaseAuthenticationMock() : new FirebaseAuthenticationReal();
        }
        return instance;
    }

    public abstract FirebaseUser getCurrentUser();

    public abstract Task<AuthResult> signInWithCredential(AuthCredential c);
}
