package ch.epfl.sweng.runpharaa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.user.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class UserTest {

    @Test
    public void testDatabaseConstructor(){
        assertNotNull(new User());
    }

    @Test
    public void testEquals(){
        User u1 = new User();
        User u2 = new User();
        u1.setUid("0");
        u2.setUid("0");
        assertEquals(u1, u2);
        u2.setUid("1");
        assertNotEquals(u1, u2);
        assertNotEquals(u1, new Track());
    }

    @Test
    public void testHashCode(){
        User u1 = new User();
        u1.setUid("0");
        assertEquals("0".hashCode(), u1.hashCode());
    }

    @Test
    public void testSettersGetters(){
        User u = new User();
        u.setUid("0");
        List<String> favs = Collections.singletonList("1234");
        u.setFavoriteTracks(favs);
        List<String> followed = Collections.singletonList("0");
        u.setFollowedUsers(followed);
        u.setLikedTracks(favs);
        u.setCreatedTracks(favs);
        u.setName("name");
        u.setPicture("pic");
        u.setNotificationKey("key");
        assertEquals(u.getUid(), "0");
        assertEquals(u.getName(), "name");
        assertEquals(u.getCreatedTracks(), favs);
        assertEquals(u.getFavoriteTracks(), favs);
        assertEquals(u.getFollowedUsers(), followed);
        assertEquals(u.getLikedTracks(), favs);
        assertEquals(u.getPicture(), "pic");
        assertEquals(u.getNotificationKey(), "key");
    }
}
