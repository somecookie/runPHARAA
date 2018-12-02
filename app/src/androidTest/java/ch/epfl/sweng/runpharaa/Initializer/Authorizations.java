package ch.epfl.sweng.runpharaa.Initializer;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.BeforeClass;

public abstract class Authorizations {

    private static UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    private final static String ANDROIDBTN = "android.widget.Button";


    @BeforeClass
    public static void clickAllowPermission() throws UiObjectNotFoundException {
        UiObject allowBtn = mDevice.findObject(new UiSelector()
                .text("OK")
                .className(ANDROIDBTN));

        allowBtn.click();
    }
}
