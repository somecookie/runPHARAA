package ch.epfl.sweng.runpharaa.utils;

public class Required {

    /**
     * Test is a given object is null
     * @param o object to test
     * @param messageError the message to given to the error
     */
    public static void nonNull(Object o, String messageError){
        if(o == null) throw new NullPointerException(messageError);
    }

    /**
     * Test if the given int is greater or equal than zero
     * @param i the int to test
     * @param messageError the message to given to the error
     */
    public static void greaterOrEqualZero(int i, String messageError) {
        if(i < 0) throw new IllegalArgumentException(messageError);
    }

    /**
     * Test if the given double is greater or equal than zero
     * @param i the double to test
     * @param messageError the message to given to the error
     */
    public static void greaterOrEqualZero(double i, String messageError) {
        if(i < 0) throw new IllegalArgumentException(messageError);
    }

}
