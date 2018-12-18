package ch.epfl.sweng.runpharaa.util;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsNot;

import ch.epfl.sweng.runpharaa.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

public interface ViewUtils {

    /**
     * Sets progress on a progress bar
     */
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

    /**
     * Sets a visible component to GONE
     */
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

    /**
     * Used for testing preferences, taken from <a href="https://stackoverflow.com/a/44262604">this stackoverflow post</a>.
     */
    static DataInteraction onPreferenceRow(Matcher<? extends Object> datamatcher) {

        DataInteraction interaction = onData(datamatcher);

        return interaction
                .inAdapterView(Matchers.allOf(
                        withId(android.R.id.list),
                        not(withParent(withResName("headers")))));
    }

    /**
     * Custom matcher to use when searching for predefined android components such as positive / negative buttons, headers, etc.
     */
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

    static ViewInteraction testToastDisplay(ActivityTestRule mActivityRule, String string) {
        return onView(withText(string))
                .inRoot(withDecorView(IsNot.not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    static void swipeToFragmentSearch(){
        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
        onView(withId(R.id.viewPagerId)).perform(swipeLeft());
    }
}
