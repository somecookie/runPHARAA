package ch.epfl.sweng.runpharaa.util;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.SeekBar;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static org.hamcrest.CoreMatchers.is;

public interface ViewUtils {

    static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }

            @Override
            public String getDescription() {
                return "Set a progress on a SeekBar";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }

    static ViewAction setGone() {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return is(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE));
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.setVisibility(View.GONE);
            }

            @Override
            public String getDescription() {
                return "Hide View";
            }
        };
    }
}
