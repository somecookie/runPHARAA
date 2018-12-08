package ch.epfl.sweng.runpharaa;

import android.graphics.Bitmap;

import org.junit.Test;

import ch.epfl.sweng.runpharaa.cache.MemoryCache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MemoryCacheTest {
    private MemoryCache cache = new MemoryCache();
    private Bitmap b4 = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    private Bitmap b400 = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);

    @Test
    public void setNegativeLimitFails() {
        long initialLimit = cache.getLimit();
        cache.setLimit(-2);
        assertEquals(initialLimit, cache.getLimit());
    }

    @Test
    public void sizeReturnsCorrectValues() {
        cache.clear();
        assertEquals(0, cache.getSize());

        cache.put("b4", b4);
        assertEquals(4, cache.getSize());

        cache.put("b400", b400);
        assertEquals(400 + 4, cache.getSize());
    }

    @Test
    public void returnsCorrectBitmap() {
        cache.clear();
        cache.put("b4", b4);
        cache.put("b400", b400);
        assertEquals(b4, cache.get("b4"));
        assertEquals(b400, cache.get("b400"));
    }

    @Test
    public void cantAddTooLarge() {
        cache.clear();
        cache.setLimit(0);
        cache.put("b4", b4);
        cache.put("b4", b400);
        assertEquals(0, cache.getSize());
    }

    @Test
    public void removesIfNoSpace() {
        cache.clear();
        cache.setLimit(50);
        cache.put("b4", b4);
        cache.put("b400", b400);
        cache.put("b4_bis", b4);
        assertEquals(4, cache.getSize());
    }

    @Test
    public void returnsNullWhenNotPresent() {
        cache.clear();
        Bitmap b = cache.get("b4");
        assertNull(b);
    }
}
