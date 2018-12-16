package ch.epfl.sweng.runpharaa.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.epfl.sweng.runpharaa.utils.Config;

public class StreakManager {

    private static Calendar fakeCalendar = null;

    private Calendar calendar = Config.isTest ? fakeCalendar : Calendar.getInstance();
    private int currentStreak;
    private int lastConnectionDay;
    private int lastConnectionYear;

    /**
     * Create a fresh streak manager
     */
    public StreakManager() {
        this.currentStreak = 1;
        this.lastConnectionDay = currentDay();
        this.lastConnectionYear = currentYear();
    }

    /**
     * Create a streak manager with loaded data
     */
    public StreakManager(int currentStreak, int lastConnectionDay, int lastConnectionYear) {
        this.currentStreak = currentStreak;
        this.lastConnectionDay = lastConnectionDay;
        this.lastConnectionYear = lastConnectionYear;
    }

    /**
     * Set a fake calendar (for testing purposes only)
     */
    public static void setFakeCalendar(Calendar c) {
        fakeCalendar = c;
    }

    /**
     * Load data into a manager if there is any, otherwise create a fresh one
     */
    public static StreakManager loadStreakManager(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int currStreak = sp.getInt("streakCurrStreak", 0);
        int lastDay = sp.getInt("streakLastDay", 0);
        int lastYear = sp.getInt("streakLastYear", 0);
        if (currStreak == 0 || lastDay == 0 || lastYear == 0)
            return new StreakManager();
        else
            return new StreakManager(currStreak, lastDay, lastYear);
    }

    /**
     * Updates the streak manager values
     */
    public void update() {
        if (isSuccessiveDay())
            currentStreak++;
        else if (!isSameDay())
            currentStreak = 1;
        lastConnectionDay = currentDay();
        lastConnectionYear = currentYear();
    }

    /**
     * Saves the information contained in a manager to the user's device
     */
    public void saveStreakManager(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt("streakCurrStreak", currentStreak)
                .putInt("streakLastDay", lastConnectionDay)
                .putInt("streakLastYear", lastConnectionYear).apply();
    }

    // --- Getters ----

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getLastConnectionDay() {
        return lastConnectionDay;
    }

    public int getLastConnectionYear() {
        return lastConnectionYear;
    }

    // --- Utils ---

    /**
     * Checks if we're on the same day as the last connection to not increment or reset the streak
     */
    private boolean isSameDay() {
        return currentDay() == lastConnectionDay && currentYear() == lastConnectionYear;
    }

    /**
     * Checks if today is the day following the previous one. Takes into account transitions from
     * the 31st of December to 1st of January as well as leap years.
     */
    private boolean isSuccessiveDay() {
        // Correct the day if it went from 31.12 to 01.01
        int correctedLastConnection = lastConnectionDay + 1;
        int temp = correctedLastConnection;
        if (isLeap(lastConnectionYear)) {
            correctedLastConnection %= 366;
        } else {
            correctedLastConnection %= 365;
        }
        // Correct the year accordingly
        int correctedLastYear = lastConnectionYear;
        if (temp != correctedLastConnection)
            correctedLastYear++;

        return currentDay() == correctedLastConnection && currentYear() == correctedLastYear;
    }

    /**
     * Get's the current year
     */
    private int currentYear() {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Get's the current day of the year (from 1 to 365 or 366 if leap)
     */
    private int currentDay() {
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Checks if a year is a leap year
     */
    private boolean isLeap(int year) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        return c.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }
}