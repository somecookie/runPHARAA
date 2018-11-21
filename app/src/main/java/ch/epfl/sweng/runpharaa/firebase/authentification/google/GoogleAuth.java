package ch.epfl.sweng.runpharaa.firebase.authentification.google;

public abstract class GoogleAuth {

    public static boolean isTest = false;

    private static GoogleAuthInterface instance;

    public static GoogleAuthInterface getInstance(){
        if (instance == null) {
            if (isTest)
                instance = new GoogleAuthMock();
            else
                instance = new GoogleAuthReal();
        }
        return instance;
    }
}
