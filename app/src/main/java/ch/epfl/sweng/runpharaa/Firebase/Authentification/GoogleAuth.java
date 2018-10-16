package ch.epfl.sweng.runpharaa.Firebase.Authentification;

public abstract class GoogleAuth {

    private static boolean isTest = true;

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
