package ch.epfl.sweng.runpharaa;

import android.graphics.Picture;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.user.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        assertTrue(u1.equals(u2));
        u2.setUid("1");
        assertFalse(u1.equals(u2));
        assertFalse(u1.equals(new Track()));
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
        List<String> favs = Arrays.asList("1234");
        u.setFavoriteTracks(favs);
        List<String> followed = Arrays.asList("0");
        u.setFollowedUsers(followed);
        u.setLikedTracks(favs);
        u.setCreatedTracks(favs);
        u.setName("name");
        u.setPicture("pic");
        assertEquals(u.getUid(), "0");
        assertEquals(u.getName(), "name");
        assertEquals(u.getCreatedTracks(), favs);
        assertEquals(u.getFavoriteTracks(), favs);
        assertEquals(u.getFollowedUsers(), followed);
        assertEquals(u.getLikedTracks(), favs);
        assertEquals(u.getPicture(), "pic");
    }
}
