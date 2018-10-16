package ch.epfl.sweng.runpharaa.utils;

public class Required {

    public static void nonNull(Object o, String messageError){
        if(o == null) throw new NullPointerException(messageError);
    }

    public static void nonNull(Object o){
        if(o == null) throw new NullPointerException();
    }

    public static void equal(Object o1, Object o2){
        if(o1 != o2) throw new IllegalArgumentException();
    }
}
