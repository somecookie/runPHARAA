package ch.epfl.sweng.runpharaa.cache;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.epfl.sweng.runpharaa.utils.Util;

public class ImageLoader {
    private static final Bitmap defaultBitmap = Util.createImage(100, 100, 0); //TODO: put id of default image here
    private static ImageLoader INSTANCE;
    private final ExecutorService executor;
    private MemoryCache memoryCache = new MemoryCache();
    private FileCache fileCache;
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    private ImageLoader(Context context) {
        fileCache = new FileCache(context);
        executor = Executors.newFixedThreadPool(5);
    }

    /**
     * Get the singleton ImageLoader, create it if it does not yet exist
     *
     * @param context a Context
     * @return the instance of ImageLoader
     */
    public static ImageLoader getLoader(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ImageLoader(context);
        }
        return INSTANCE;
    }

    /**
     * Display the image from the given URL in the given ImageView
     *
     * @param url  the image's URL
     * @param view an ImageView to display the image
     */
    public void displayImage(String url, ImageView view) {
        displayImage(url, view, true);
    }

    /**
     * Display the image from the given URL in the given ImageView. Will display a default image
     * if defaultWhileLoad is true and the image at the given URL is null
     *
     * @param url              the image's URL
     * @param view             an ImageView to display the image
     * @param defaultWhileLoad if true, will display a default image if needed
     */
    public void displayImage(String url, ImageView view, boolean defaultWhileLoad) {
        imageViews.put(view, url);
        Bitmap b = memoryCache.get(url);
        if (b != null)
            view.setImageBitmap(b);
        else {
            queuePhoto(url, view);
            if (defaultWhileLoad)
                view.setImageBitmap(defaultBitmap);
        }
    }

    /**
     * Enqueue an image to load
     *
     * @param url   the image's URL
     * @param image an ImageView to display the image
     */
    private void queuePhoto(String url, ImageView image) {
        PhotoToLoad p = new PhotoToLoad(url, image);
        executor.submit(new PhotoLoader(p));
    }

    /**
     * Get the Bitmap located at the given URL
     *
     * @param url the Bitmap URL
     * @return a Bitmap
     */
    private Bitmap getBitmap(String url) {
        File f = fileCache.getFile(url);
        Bitmap b = decodeFile(f);
        if (b != null)
            return b;
        try {
            URL imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setInstanceFollowRedirects(true);
            InputStream is = connection.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Util.copyStream(is, os);
            os.close();
            b = decodeFile(f);
            return b;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }

    }

    /**
     * Decode a file to transform it into a Bitmap
     *
     * @param f a File
     * @return the decoded Bitmap
     */
    private Bitmap decodeFile(File f) {
        try {
            return BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Test if an ImageView of an image is null or its tag not equal to the image's URL
     *
     * @param p a PhotoToLoad
     * @return true if the ImageView is Reused
     */
    private boolean imageViewReused(PhotoToLoad p) {
        String tag = imageViews.get(p.imageView);
        return (tag == null || !tag.equals(p.url));
    }

    /**
     * Clear the cache
     */
    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

    /**
     * Wrapper class representing a photo to be loaded into an image view
     */
    private class PhotoToLoad {
        String url;
        ImageView imageView;

        PhotoToLoad(String url, ImageView view) {
            this.url = url;
            imageView = view;
        }
    }

    class PhotoLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotoLoader(PhotoToLoad p) {
            photoToLoad = p;
        }

        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            Bitmap b = getBitmap(photoToLoad.url);
            memoryCache.put(photoToLoad.url, b);

            if (imageViewReused(photoToLoad))
                return;

            BitmapDisplayer bd = new BitmapDisplayer(b, photoToLoad);
            Activity a = (Activity) photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    class BitmapDisplayer implements Runnable {
        Bitmap b;
        PhotoToLoad p;

        BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            this.b = b;
            this.p = p;
        }

        @Override
        public void run() {
            if (imageViewReused(p))
                return;
            if (b != null)
                p.imageView.setImageBitmap(b);
            else
                p.imageView.setImageBitmap(defaultBitmap);
        }
    }
}
