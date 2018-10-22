package ch.epfl.sweng.runpharaa.Firebase.Authentification.Google;

public abstract class GoogleAuth {

    private static boolean isTest = false;

    private static final GoogleAuthInterface instance = initInstance();

    public static GoogleAuthInterface getInstance(){
        return instance;
    }

    private static GoogleAuthInterface initInstance(){
        if(isTest) {
            return new GoogleAuthMock();
        } else {
            return new GoogleAuthReal();
        }
    }
}
