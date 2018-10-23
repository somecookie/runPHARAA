package ch.epfl.sweng.runpharaa.utils;

public class Required {

    public static void nonNull(Object o, String messageError){
        if(o == null) throw new NullPointerException(messageError);
    }

}
