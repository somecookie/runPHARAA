package ch.epfl.sweng.runpharaa.login.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthenticationMock extends FirebaseAuthentication {

    private static FirebaseUser FakeFireBaseUser = null;
    private static Task<AuthResult> task;

    public static void setFakeFireBaseUser(FirebaseUser user) {
        FakeFireBaseUser = user;
    }

    public static void setFakeTask(Task<AuthResult> task) {
        FirebaseAuthenticationMock.task = task;
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return FakeFireBaseUser;
    }

    @Override
    public Task<AuthResult> signInWithCredential(AuthCredential c) {
        return task;
    }
}
