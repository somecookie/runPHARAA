package ch.epfl.sweng.runpharaa;

import android.graphics.Bitmap;
import android.graphics.Color;

import org.junit.Assert;
import org.junit.Test;

import ch.epfl.sweng.runpharaa.utils.Util;

public class CardItemTest {

    private static Bitmap bg = Util.createImage(200, 100, Color.BLACK);

    @Test
    public void contructorAndSetter(){

        CardItem c = new CardItem();

        c.setBackground(bg);
        c.setName("PHARAA");
        c.setParentTrackID("0");

        Assert.assertEquals(bg, c.getBackground());
        Assert.assertEquals("PHARAA", c.getName());
        Assert.assertEquals("0", c.getParentTrackID());
    }

    @Test
    public void contructorAndGetter(){
<<<<<<< HEAD
        CardItem c = new CardItem(bg, "PHARAA", 0);
=======
        CardItem c = new CardItem(3, "PHARAA", "0");
>>>>>>> f6fbfe7ad2ece40323507e65ea6d0fcc78fe1471

        Assert.assertEquals(bg, c.getBackground());
        Assert.assertEquals("PHARAA", c.getName());
        Assert.assertEquals("0", c.getParentTrackID());
    }
}
