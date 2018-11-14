package ch.epfl.sweng.runpharaa.utils;

public class Required {

    public static void nonNull(Object o, String messageError){
        if(o == null) throw new NullPointerException(messageError);
    }

    public static void greaterOrEqualZero(int i, String messageError) {
        if(i < 0) throw new IllegalArgumentException(messageError);
    }

    public static void greaterOrEqualZero(double i, String messageError) {
        if(i < 0) throw new IllegalArgumentException(messageError);
    }

}
