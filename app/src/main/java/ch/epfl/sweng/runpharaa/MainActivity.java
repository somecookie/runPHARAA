package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.gui.ViewPagerAdapter;
import ch.epfl.sweng.runpharaa.map.MapsActivity;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.creation.CreateTrackOnMapActivity;
import ch.epfl.sweng.runpharaa.tracks.properties.TrackType;
import ch.epfl.sweng.runpharaa.user.myProfile.UsersProfileActivity;


public class MainActivity extends AppCompatActivity {

    public static boolean difficultyIsFiltered;
    public static int difficultyFilter;
    public static boolean timeIsFiltered;
    public static int timeFilter;
    public static Set<TrackType> typesFilter;
    public static boolean typesAreFiltered;
    private static String[] listTypesStr;
    private static boolean[] checkedTypes;
    private ViewPager viewPager;


    /**
     * Check if the given {@link Track} pass the filters
     *
     * @param t the given {@link Track}
     * @return true or false
     */
    public static boolean passFilters(Track t) {
        return filterTime(t) && filterDifficulty(t) && filterTypes(t);
    }

    /**
     * Check if the given {@link Track} pass the time filter
     *
     * @param t the given {@link Track}
     * @return true or false
     */
    private static boolean filterTime(Track t) {
        if (timeIsFiltered) {
            return t.getProperties().getAvgDuration() <= timeFilter;
        } else {
            return true;
        }
    }

    /**
     * Check if the given {@link Track} pass the difficulty filter
     *
     * @param t the given {@link Track}
     * @return true or false
     */
    private static boolean filterDifficulty(Track t) {
        if (difficultyIsFiltered) {
            return t.getProperties().getAvgDifficulty() <= difficultyFilter;
        } else {
            return true;
        }
    }

    /**
     * Check if the given {@link Track} pass the type filter
     *
     * @param t the given {@link Track}
     * @return true or false
     */
    private static boolean filterTypes(Track t) {
        if (typesAreFiltered) {
            return t.getProperties().getType().containsAll(typesFilter);
        } else {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        difficultyIsFiltered = false;
        timeIsFiltered = false;
        typesAreFiltered = false;

        timeFilter = 60;
        difficultyFilter = 3;
        typesFilter = new HashSet<>();

        listTypesStr = getResources().getStringArray(R.array.track_types);
        checkedTypes = new boolean[listTypesStr.length];

        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabLayoutId);
        viewPager = findViewById(R.id.viewPagerId);
        FloatingActionButton fab = findViewById(R.id.fab);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add fragments
        adapter.addFragment(new FragmentNearMe());
        adapter.addFragment(new FragmentFollowing());
        adapter.addFragment(new FragmentFavourites());
        adapter.addFragment(new FragmentSearch());

        // Link all
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);

        // Set icons
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_near_me);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_following);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_favorite);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_search);

        removeStrictMode();

        // Remove shadow from action bar
        getSupportActionBar().setElevation(0);

        // Add the floating action button
        fab.setOnClickListener(v -> {
            Intent createTrack = new Intent(getBaseContext(), CreateTrackOnMapActivity.class);
            startActivity(createTrack);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filterIcon:
                filterDialog();
                break;
            case R.id.profileIcon:
                Intent profileIntent = new Intent(getBaseContext(), UsersProfileActivity.class);
                profileIntent.putExtra("selfProfile", true);
                startActivity(profileIntent);
                break;
            case R.id.mapIcon:
                Intent mapIntent = new Intent(getBaseContext(), MapsActivity.class);
                startActivity(mapIntent);
                break;
            case R.id.helpIcon:
                showPopup(viewPager);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Open a {@link AlertDialog} for the user to set up or remove filters
     */
    private void filterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_filters, null, false);

        builder.setTitle(getResources().getString(R.string.filters));

        TextView maxDifficulty = mView.findViewById(R.id.maxDifficultyOf);
        maxDifficulty.setText(getResources().getString(R.string.max_difficulty_of) + " " + difficultyFilter);

        SeekBar difficultyBar = mView.findViewById(R.id.difficulty_bar);
        difficultyBar.setProgress(difficultyFilter);
        difficultyBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                difficultyFilter = progress;
                difficultyIsFiltered = true;
                maxDifficulty.setText(getResources().getString(R.string.max_difficulty_of) + " " + difficultyFilter);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        TextView maxTime = mView.findViewById(R.id.maxTime);
        maxTime.setText(getResources().getString(R.string.max_time_of) + " " + timeFilter + " minutes.");

        SeekBar timeBar = mView.findViewById(R.id.timeBar);
        timeBar.setProgress(timeFilter);
        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= 115) {
                    timeFilter = 120;
                    maxTime.setText(getResources().getString(R.string.no_time_limit));
                    timeIsFiltered = false;
                } else {
                    timeFilter = progress;
                    maxTime.setText(getResources().getString(R.string.max_time_of) + " " + timeFilter + " minutes.");
                    timeIsFiltered = true;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        builder.setMultiChoiceItems(listTypesStr, checkedTypes, (dialog, which, isChecked) -> checkedTypes[which] = isChecked);

        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getText(R.string.OK), (dialog, which) -> {
            typesFilter.clear();
            for (int i = 0; i < checkedTypes.length; i++) {
                if (checkedTypes[i]) typesFilter.add(TrackType.values()[i]);
            }
            typesAreFiltered = !typesFilter.isEmpty();
        });

        builder.setNegativeButton(getResources().getText(R.string.noFilter), (dialog, which) -> {
            timeIsFiltered = false;
            difficultyIsFiltered = false;
            typesAreFiltered = false;
            for (int i = 0; i < checkedTypes.length; i++) {
                checkedTypes[i] = false;
            }
            timeFilter = 60;
            difficultyFilter = 3;
            dialog.dismiss();
        });

        builder.setView(mView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Method to change the thread policy that sometimes block the json send
     */
    private void removeStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    /**
     * Show the help popup
     *
     * @param view a View
     */
    private void showPopup(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_window, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // click outside the popup dismiss it
        final PopupWindow popupWindowGuide = new PopupWindow(popupView, width, height, focusable);
        popupWindowGuide.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindowGuide.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup on click
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindowGuide.dismiss();
                return true;
            }
        });
    }
}
