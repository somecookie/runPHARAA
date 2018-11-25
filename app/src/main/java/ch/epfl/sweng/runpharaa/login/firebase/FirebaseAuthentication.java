package ch.epfl.sweng.runpharaa.login.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public abstract class FirebaseAuthentication {

    public static boolean isTest = false;

    private static FirebaseAuthentication instance;

    public static FirebaseAuthentication getInstance(){
        if(instance == null) {
            if (isTest)
                instance = new FirebaseAuthenticationMock();
            else
                instance = new FirebaseAuthenticationReal();
        }
        return instance;
    }

    public abstract FirebaseUser getCurrentUser();

    public abstract Task<AuthResult> signInWithCredential(AuthCredential c);
}
