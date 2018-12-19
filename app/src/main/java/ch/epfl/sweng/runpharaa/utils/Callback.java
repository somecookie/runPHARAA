package ch.epfl.sweng.runpharaa.utils;

abstract public class Callback<T> {

    /**
     * Callback method used in case of success
     *
     * @param value returned value from the method using the callback
     */
    abstract public void onSuccess(T value);

    /**
     * Callback method used in case of failure (error)
     *
     * @param e the error that must be handled
     */
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
