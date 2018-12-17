package ch.epfl.sweng.runpharaa.cache;

import android.content.Context;
import android.os.Environment;

import java.io.File;

class FileCache {
    private File cacheDir;

    public FileCache(Context context) {
        if(Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(Environment.getExternalStorageDirectory(), "runpharaaImages");
        } else {
            cacheDir = context.getCacheDir();
        }
        if(!cacheDir.mkdirs())
            cacheDir.mkdirs();
    }

    public File getFile(String url) {
        String fileName = String.valueOf(url.hashCode());
        return new File(cacheDir, fileName);
    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if(files == null) return;
        for(File f : files)
            f.delete();
    }
}
