package ch.epfl.sweng.runpharaa;

import android.graphics.Bitmap;
import android.graphics.Color;

import org.junit.Assert;
import org.junit.Test;

import ch.epfl.sweng.runpharaa.utils.Util;

public class CardItemTest {

    private static Bitmap bg = Util.createImage(200, 100, Color.BLACK);

    @Test
    public void constructorAndSetter(){

        CardItem c = new CardItem();

        c.setBackground(bg);
        c.setName("PHARAA");
        c.setParentTrackID("0");

        Assert.assertEquals(bg, c.getBackground());
        Assert.assertEquals("PHARAA", c.getName());
        Assert.assertEquals("0", c.getParentTrackID());
    }

    @Test
    public void constructorAndGetter(){

        CardItem c = new CardItem("PHARAA", "0", "https://firebasestorage.googleapis.com/v0/b/runpharaa.appspot.com/o/TrackImages%2F-LPV-XYcU6_y2KtaC5nC?alt=media&token=ce6a654d-10b9-486f-97c7-1473c5bedd19");

        Assert.assertEquals(bg, c.getBackground());
        Assert.assertEquals("PHARAA", c.getName());
        Assert.assertEquals("0", c.getParentTrackID());
    }
}
