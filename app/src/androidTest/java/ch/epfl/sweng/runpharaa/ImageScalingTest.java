package ch.epfl.sweng.runpharaa;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;

import ch.epfl.sweng.runpharaa.utils.Util;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ImageScalingTest {

    private Bitmap smallBitmap = Util.createImage(100, 100, Color.BLACK);
    private Bitmap largeBitmap = Util.createImage(1080, 1920, Color.BLACK);

    @Test
    public void rescalesLargeBitmap() {
        Bitmap out = Util.decodeSampledBitmap(bitmapToByteArray(largeBitmap), 480, 100);
        assertEquals(out.getWidth(), 1080 / 2);
        assertEquals(out.getHeight(), 1920 / 2);
    }

    @Test
    public void doesntRescaleSmallBitmap() {
        Bitmap out = Util.decodeSampledBitmap(bitmapToByteArray(smallBitmap), 480, 100);
        assertEquals(out.getWidth(), 100);
        assertEquals(out.getHeight(), 100);
    }

    private byte[] bitmapToByteArray(Bitmap b) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}
