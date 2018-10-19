package ch.epfl.sweng.runpharaa.Firebase.Authentification;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthMock implements FirebaseAuthInterface {
    @Override
    public FirebaseUser getCurrentUser() {
        return null;
    }

    @Override
    public Task<AuthResult> signInWithCredential(AuthCredential c) {
        return null;
    }
}
