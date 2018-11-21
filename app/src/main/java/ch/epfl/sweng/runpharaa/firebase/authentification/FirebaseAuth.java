package ch.epfl.sweng.runpharaa.firebase.authentification;

public abstract class FirebaseAuth {

    public static boolean isTest = false;

    private static FirebaseAuthInterface instance;

    public static FirebaseAuthInterface getInstance(){
        if(instance == null) {
            if (isTest)
                instance = new FirebaseAuthMock();
            else
                instance = new FirebaseAuthReal();
        }
        return instance;
    }
}
