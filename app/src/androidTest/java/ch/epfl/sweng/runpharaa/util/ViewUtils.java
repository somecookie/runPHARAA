package ch.epfl.sweng.runpharaa.util;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

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

    static DataInteraction onPreferenceRow(Matcher<? extends Object> datamatcher) {

        DataInteraction interaction = onData(datamatcher);

        return interaction
                .inAdapterView(Matchers.allOf(
                        withId(android.R.id.list),
                        not(withParent(withResName("headers")))));
    }

    static Matcher<View> withResName(final String resName) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with res-name: " + resName);
            }

            @Override
            public boolean matchesSafely(View view) {
                int identifier = view.getResources().getIdentifier(resName, "id", "android");
                return !TextUtils.isEmpty(resName) && (view.getId() == identifier);
            }
        };
    }
}
