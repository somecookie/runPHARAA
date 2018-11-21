package ch.epfl.sweng.runpharaa.firebase.authentification;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public interface FirebaseAuthInterface {

    FirebaseUser getCurrentUser();

    Task<AuthResult> signInWithCredential(AuthCredential c);
}
