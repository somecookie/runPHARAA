package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import ch.epfl.sweng.runpharaa.gui.ViewPagerAdapter;
import ch.epfl.sweng.runpharaa.notification.FirebaseNotification;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
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
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private FloatingActionButton fab;
    private FirebaseUser user;

    public static boolean passFilters(Track t) {
        return filterTime(t) && filterDifficulty(t) && filterTypes(t);
    }

    private static boolean filterTime(Track t) {
        if(timeIsFiltered){
            return t.getProperties().getAvgDuration() <= timeFilter;
        } else {
            return true;
        }
    }

    private static boolean filterDifficulty(Track t) {
        if(difficultyIsFiltered){
            return t.getProperties().getAvgDifficulty() <= difficultyFilter;
        } else {
            return true;
        }
    }

    private static boolean filterTypes(Track t) {
        if(typesAreFiltered){
            return t.getProperties().getType().containsAll(typesFilter);
        } else {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        difficultyIsFiltered = false;
        timeIsFiltered = false;
        typesAreFiltered = false;

        timeFilter = 60;
        difficultyFilter = 3;
        typesFilter = new HashSet<>();

        listTypesStr = getResources().getStringArray(R.array.track_types);
        checkedTypes = new boolean[listTypesStr.length];

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayoutId);
        viewPager = findViewById(R.id.viewPagerId);
        fab = findViewById(R.id.fab);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

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
            Intent createTrack = new Intent(getBaseContext(), CreateTrackActivity.class);
            startActivity(createTrack);
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filterIcon:
                filterDialog();
                return true;
            case R.id.profileIcon:
                Intent profileIntent = new Intent(getBaseContext(), UsersProfileActivity.class);
                profileIntent.putExtra("selfProfile", true);
                startActivity(profileIntent);
                return true;
            case R.id.mapIcon:
                Intent mapIntent = new Intent(getBaseContext(), MapsActivity.class);
                startActivity(mapIntent);
                return true;
            case R.id.helpIcon:
                showPopup(viewPager);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

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
    private void removeStrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void showPopup(View view) {
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
