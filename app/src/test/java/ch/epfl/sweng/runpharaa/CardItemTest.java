package ch.epfl.sweng.runpharaa;

import org.junit.Assert;
import org.junit.Test;

public class CardItemTest {

    @Test
    public void contructorAndSetter(){
        CardItem c = new CardItem();

        c.setBackground(3);
        c.setName("PHARAA");

        Assert.assertEquals(3, c.getBackground());
        Assert.assertEquals("PHARAA", c.getName());
    }
}
