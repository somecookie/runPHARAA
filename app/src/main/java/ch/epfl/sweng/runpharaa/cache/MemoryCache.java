package ch.epfl.sweng.runpharaa.cache;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MemoryCache {
    private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));

    private long size = 0;
    private long limit = 1000000;

    public MemoryCache() {
        Log.i("HI", limit+"");
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    public void setLimit(long newLimit) {
        if (newLimit >= 0)
            limit = newLimit;
    }

    public long getLimit() {
        return limit;
    }

    public long getSize() {
        return size;
    }

    public Bitmap get(String id) {
        if (!cache.containsKey(id))
            return null;
        return cache.get(id);
    }

    public void put(String id, Bitmap b) {
        if (cache.containsKey(id))
            size -= getSizeInBytes(cache.get(id));
        cache.put(id, b);
        size += getSizeInBytes(b);
        checkSize();
    }

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

    public void clear() {
        cache.clear();
        size = 0;
    }

    private long getSizeInBytes(Bitmap b) {
        return b == null ? 0 : b.getRowBytes() * b.getHeight();
    }

}
