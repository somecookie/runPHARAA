package ch.epfl.sweng.runpharaa;

import android.graphics.Bitmap;
import android.graphics.Color;

import org.junit.Assert;
import org.junit.Test;

import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.UserCardItem;
import ch.epfl.sweng.runpharaa.utils.Util;

public class UserCardItemTest {

    private static Bitmap bg = Util.createImage(200, 100, Color.BLACK);

    @Test
    public void constructorAndSetter(){

        UserCardItem c = new UserCardItem();

        c.setName("PHARAA");
        c.setParentUserID("0");

        Assert.assertEquals("PHARAA", c.getName());
        Assert.assertEquals("0", c.getParentUserID());
    }

    @Test
    public void constructorAndGetter(){

        String imageURL = "https://firebasestorage.googleapis.com/v0/b/runpharaa.appspot.com/o/TrackImages%2F-LPV-XYcU6_y2KtaC5nC?alt=media&token=ce6a654d-10b9-486f-97c7-1473c5bedd19";
        UserCardItem c = new UserCardItem("PHARAA", "0", imageURL, 1);

        Assert.assertEquals("PHARAA", c.getName());
        Assert.assertEquals("0", c.getParentUserID());
        Assert.assertEquals(1, c.getNbCreatedTracks());
        Assert.assertEquals(imageURL, c.getImageURL());
        Assert.assertNull(c.getBackground());
    }
}
