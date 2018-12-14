package ch.epfl.sweng.runpharaa;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.epfl.sweng.runpharaa.user.StreakManager;
import ch.epfl.sweng.runpharaa.utils.Config;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class StreakManagerTest {

    @BeforeClass
    public static void initTest() {
        Config.isTest = true;
    }

    @Test
    public void basicConstructor() {
        Calendar c = new GregorianCalendar(2018, Calendar.JANUARY, 1);
        StreakManager.setFakeCalendar(c);
        StreakManager streakManager = new StreakManager();
        assertEquals(1, streakManager.getCurrentStreak());
        assertEquals(1, streakManager.getLastConnectionDay());
        assertEquals(2018, streakManager.getLastConnectionYear());
    }

    @Test
    public void secondConstructor() {
        Calendar c = new GregorianCalendar(2018, Calendar.DECEMBER, 24);
        StreakManager.setFakeCalendar(c);
        StreakManager streakManager = new StreakManager(3, 50, 2018);
        assertEquals(3, streakManager.getCurrentStreak());
        assertEquals(50, streakManager.getLastConnectionDay());
        assertEquals(2018, streakManager.getLastConnectionYear());
    }

    @Test
    public void updatesOnSuccessiveDays() {
        Calendar c = new GregorianCalendar(2018, Calendar.DECEMBER, 24);
        StreakManager.setFakeCalendar(c);
        StreakManager streakManager = new StreakManager();
        c.set(2018, Calendar.DECEMBER, 25);
        streakManager.update();
        assertEquals(2, streakManager.getCurrentStreak());
    }

    @Test
    public void resetsWhenSameDayButNotYear() {
        Calendar c = new GregorianCalendar(2018, Calendar.DECEMBER, 24);
        StreakManager.setFakeCalendar(c);
        StreakManager streakManager = new StreakManager();
        c.set(2018, Calendar.DECEMBER, 25);
        streakManager.update();
        assertEquals(2, streakManager.getCurrentStreak());
        c.set(2019, Calendar.DECEMBER, 24);
        streakManager.update();
        assertEquals(1, streakManager.getCurrentStreak());
    }

    @Test
    public void keepsOnSameDayAndYear() {
        Calendar c = new GregorianCalendar(2018, Calendar.DECEMBER, 24);
        StreakManager.setFakeCalendar(c);
        StreakManager streakManager = new StreakManager();
        streakManager.update();
        assertEquals(1, streakManager.getCurrentStreak());
    }

    @Test
    public void resetsWhenNonConsecutiveDays() {
        Calendar c = new GregorianCalendar(2018, Calendar.DECEMBER, 24);
        StreakManager.setFakeCalendar(c);
        StreakManager streakManager = new StreakManager();
        c.set(2018, Calendar.DECEMBER, 26);
        streakManager.update();
        assertEquals(1, streakManager.getCurrentStreak());
    }

    @Test
    public void keepsFromDecemberToJanuary() {
        Calendar c = new GregorianCalendar(2018, Calendar.DECEMBER, 31);
        StreakManager.setFakeCalendar(c);
        StreakManager streakManager = new StreakManager();
        c.set(2019, Calendar.JANUARY, 1);
        streakManager.update();
        assertEquals(2, streakManager.getCurrentStreak());
    }

    @Test
    public void resetsWhenNonConsecutiveYears() {
        Calendar c = new GregorianCalendar(2018, Calendar.DECEMBER, 24);
        StreakManager.setFakeCalendar(c);
        StreakManager streakManager = new StreakManager();
        c.set(2019, Calendar.DECEMBER, 25);
        streakManager.update();
        assertEquals(1, streakManager.getCurrentStreak());
    }

    @Test
    public void keepsEvenWithLeapYears() {
        // 2020 is the next leap year, but we check just in case
        Calendar c = new GregorianCalendar(2020, Calendar.DECEMBER, 31);
        assertEquals(366, c.getActualMaximum(Calendar.DAY_OF_YEAR));

        StreakManager.setFakeCalendar(c);
        StreakManager streakManager = new StreakManager();
        c.set(2021, Calendar.JANUARY, 1);
        streakManager.update();
        assertEquals(2, streakManager.getCurrentStreak());
    }

    @AfterClass
    public static void cleanUp() {
        Config.isTest = false;
        StreakManager.setFakeCalendar(null);
    }
}
