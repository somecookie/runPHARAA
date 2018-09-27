package ch.epfl.sweng.runpharaa;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UserTest {

    @Test
    public void createUserAndGetValues() {
        ArrayList empty_list_of_tracks = new ArrayList<>();

        User guest = new User("test", null, empty_list_of_tracks, empty_list_of_tracks, false, 1);

        assertEquals(guest.getName(), "test");
        //TODO: Tester l'image une fois que l'implementation est faite
        assertEquals(guest.getList_of_created_tracks(), empty_list_of_tracks);
        assertEquals(guest.getList_of_pref(), empty_list_of_tracks);
        //TODO: Tester avec des vraies tracks une fois que @Hugo aura fini l'implementation
        assertEquals(guest.isAdmin(), false);
        assertEquals(guest.getuId(), 1);
    }
}
