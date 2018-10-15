package ch.epfl.sweng.runpharaa;

import org.junit.Assert;
import org.junit.Test;

public class CardItemTest {

    @Test
    public void contructorAndSetter(){
        CardItem c = new CardItem();

        c.setBackground(3);
        c.setName("PHARAA");
        c.setParentTrackID("0");

        Assert.assertEquals(3, c.getBackground());
        Assert.assertEquals("PHARAA", c.getName());
        Assert.assertEquals("0", c.getParentTrackID());
    }

    @Test
    public void contructorAndGetter(){
        CardItem c = new CardItem(3, "PHARAA", "0");

        Assert.assertEquals(3, c.getBackground());
        Assert.assertEquals("PHARAA", c.getName());
        Assert.assertEquals("0", c.getParentTrackID());
    }
}
