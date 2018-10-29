package ch.epfl.sweng.runpharaa.utils;

public interface Callback<T> {

    /**
     * Callback method used in case of success
     * @param value returned value from the method using the callback
     */
    void onSuccess(T value);

    /**
     * Callback method used in case of failure (error)
     * @param e the error that must be handled
     */
    void onError(Exception e);

}
