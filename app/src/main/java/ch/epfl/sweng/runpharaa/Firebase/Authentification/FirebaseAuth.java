package ch.epfl.sweng.runpharaa.Firebase.Authentification;

public abstract class FirebaseAuth {

    private static boolean isTest = false;

    private static final FirebaseAuthInterface instance = initInstance();


    public static FirebaseAuthInterface getInstance(){
        return instance;
    }

    private static FirebaseAuthInterface initInstance(){
        if(isTest) {
            return new FirebaseAuthMock();
        } else {
            return new FirebaseAuthReal();
        }
    }
}
