package ch.epfl.sweng.runpharaa.cache;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MemoryCache {
    private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));

    private long size = 0;
    private long limit = 1000000;

    public MemoryCache() {
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    /**
     * Set the MemoryCache to the given limit
     *
     * @param newLimit the new limit
     */
    public void setLimit(long newLimit) {
        if (newLimit >= 0)
            limit = newLimit;
    }

    /**
     * Get the current cache limit
     *
     * @return the current cache limit
     */
    public long getLimit() {
        return limit;
    }

    /**
     * Get the cache size
     *
     * @return the cache size
     */
    public long getSize() {
        return size;
    }

    /**
     * Get the cached bitmap corresponding to the given ID
     *
     * @param id the Bitmap ID
     * @return
     */
    public Bitmap get(String id) {
        if (!cache.containsKey(id))
            return null;
        return cache.get(id);
    }

    /**
     * Put a Bitmap in the cache at the given key (ID)
     *
     * @param id a key
     * @param b  a Bitmap
     */
    public void put(String id, Bitmap b) {
        if (cache.containsKey(id))
            size -= getSizeInBytes(cache.get(id));
        cache.put(id, b);
        size += getSizeInBytes(b);
        checkSize();
    }

    /**
     * Check the cache size
     */
    private void checkSize() {
        if (size > limit) {
            Iterator<Map.Entry<String, Bitmap>> iter = cache.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Bitmap> entry = iter.next();
                size -= getSizeInBytes(entry.getValue());
                iter.remove();
                if (size <= limit)
                    break;
            }
        }
    }

    /**
     * Clear the cache
     */
    public void clear() {
        cache.clear();
        size = 0;
    }

    /**
     * Get the size of the given Bitmap in bytes
     *
     * @param b a Bitmap
     * @return the Bitmap's size
     */
    private long getSizeInBytes(Bitmap b) {
        return b == null ? 0 : b.getRowBytes() * b.getHeight();
    }

}
