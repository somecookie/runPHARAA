package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.runpharaa.cache.ImageLoader;

import static android.os.SystemClock.sleep;

@RunWith(AndroidJUnit4.class)
public class ImageLoaderTest {
    private ImageView view;
    private Context context;
    private String url1 = "https://picsum.photos/200";
    private String url2 = "https://picsum.photos/200/300";

    @Before
    public void init() {
        context = InstrumentationRegistry.getTargetContext();
        view = new ImageView(context);
        ImageLoader.getLoader(context).clearCache();
    }

    @Test
    public void downloadsImage() {
        ImageLoader.getLoader(context).displayImage(url1, view);
    }

    @Test
    public void reusesImage() {
        ImageLoader.getLoader(context).displayImage(url1, view);
        sleep(5_000);
        ImageLoader.getLoader(context).displayImage(url1, view);
    }

    @Test
    public void downloadSeparateImages() {
        ImageLoader.getLoader(context).displayImage(url1, view);
        ImageLoader.getLoader(context).displayImage(url2, view);
    }
}
