package ch.epfl.sweng.runpharaa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.database.TrackDatabaseManagement;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.user.SettingsActivity;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.UsersProfileActivity;
import ch.epfl.sweng.runpharaa.utils.Callback;

public class MainActivity extends AppCompatActivity {

    public static boolean difficultyIsFiltered;
    public static int difficultyFilter;
    public static boolean timeIsFiltered;
    public static int timeFilter;
    public static Set<TrackType> typesFilter;
    public static boolean typesAreFiltered;
    public static User main;
    private static String[] listTypesStr;
    private static boolean[] checkedTypes;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private FloatingActionButton fab;
    private FirebaseUser user;

    public static boolean passFilters(Track t) {
        if (timeIsFiltered) {
            if (difficultyIsFiltered) {
                if (typesAreFiltered) {
                    Log.d("Filters", "time , diff, types");
                    return filterTime(t) && filterDifficulty(t) && filterTypes(t);
                }
                Log.d("Filters", "time , diff");
                return filterTime(t) && filterDifficulty(t);
            }
            if (typesAreFiltered) {
                Log.d("Filters", "time , types");
                return filterTime(t) && filterTypes(t);
            }
            Log.d("Filters", "time");
            return filterTime(t);
        }
        if (difficultyIsFiltered) {
            if (typesAreFiltered) {
                Log.d("Filters", "diff , types");
                return filterDifficulty(t) && filterTypes(t);
            }
            Log.d("Filters", "diff");
            return filterDifficulty(t);
        }
        if (typesAreFiltered) {
            Log.d("Filters", "types");
            return filterTypes(t);
        }
        Log.d("Filters", "no filters");
        return true;
    }

    private static boolean filterTime(Track t) {
        return t.getProperties().getAvgDuration() <= timeFilter;
    }

    private static boolean filterDifficulty(Track t) {
        return t.getProperties().getAvgDifficulty() <= difficultyFilter;
    }

    private static boolean filterTypes(Track t) {
        return t.getProperties().getType().containsAll(typesFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Initiate the Firebase Database with these tracks (delete them manually if already there).
        /*
        Log.d("Put Fake Track", "Test");
        CustLatLng coord0 = new CustLatLng(46.518577, 6.563165); //inm
        CustLatLng coord1 = new CustLatLng(46.522735, 6.579772); //Banane
        CustLatLng coord2 = new CustLatLng(46.519380, 6.580669); //centre sportif
        CustLatLng coord3 = new CustLatLng(46.518475, 6.561960); //BC
        CustLatLng coord4 = new CustLatLng(46.517563, 6.562350); //Innovation parc
        CustLatLng coord5 = new CustLatLng(46.518447, 6.568238); //Rolex
        CustLatLng coord6 = new CustLatLng(46.523206, 6.564945); //SwissTech
        CustLatLng coord7 = new CustLatLng(46.520566, 6.567820); //Sat
        CustLatLng coord8 = new CustLatLng(46.506279, 6.626111); //Ouchy
        CustLatLng coord9 = new CustLatLng(46.517210, 6.630105); //Gare
        CustLatLng coord10 = new CustLatLng(46.519531, 6.633149);// Saint-Francois
        CustLatLng coord11 = new CustLatLng(46.522638, 6.634971); //Cathédrale
        CustLatLng coord12 = new CustLatLng(46.521412, 6.627383); //Flon

        TrackProperties p1 = new TrackProperties(1200, 10, 1, 1, null);
        TrackProperties p2 = new TrackProperties(500, 3, 1, 1, null);
        TrackProperties p3 = new TrackProperties(1050, 7, 1, 1, null);
        TrackProperties p4 = new TrackProperties(900, 9, 1, 1, null);
        TrackProperties p5 = new TrackProperties(1500, 23, 1, 1, null);
        TrackProperties p6 = new TrackProperties(2000, 20, 1, 1, null);
        DatabaseManagement.writeNewTrack(new Track("Banane -> Centre Sportif", BitmapFactory.decodeResource(getResources(), R.drawable.centre_sportif) , Arrays.asList(new CustLatLng(46.522735, 6.579772), new CustLatLng(46.519380, 6.580669)), p1));
        DatabaseManagement.writeNewTrack(new Track("Innovation Parc -> BC",BitmapFactory.decodeResource(getResources(), R.drawable.innovation_park),Arrays.asList(new CustLatLng(46.517563, 6.562350), new CustLatLng(46.518475, 6.561960)), p2));
        DatabaseManagement.writeNewTrack(new Track("Rolex -> Swisstech",BitmapFactory.decodeResource(getResources(),R.drawable.rolex), Arrays.asList(new CustLatLng(46.518447, 6.568238), new CustLatLng(46.523206, 6.564945)), p3));
        DatabaseManagement.writeNewTrack(new Track("Sat -> INM",BitmapFactory.decodeResource(getResources(),R.drawable.rolex), Arrays.asList(new CustLatLng(46.520566, 6.567820), new CustLatLng(46.518577, 6.563165)), p4));
        DatabaseManagement.writeNewTrack(new Track("Ouchy -> Gare",BitmapFactory.decodeResource(getResources(),R.drawable.ouchy), Arrays.asList(new CustLatLng(46.506279, 6.626111), new CustLatLng(46.517210, 6.630105)), p5));
        DatabaseManagement.writeNewTrack(new Track("SF -> Cath -> Flon",BitmapFactory.decodeResource(getResources(),R.drawable.saint_francois), Arrays.asList(new CustLatLng(46.519531, 6.633149), new CustLatLng(46.522638, 6.634971), new CustLatLng(46.521412, 6.627383)), p6));
        Log.d("Put Fake Track", "Test2");
        */


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

        // Link all
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        // Set icons
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_near_me);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_following);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_favorite);

        // Remove shadow from action bar
        getSupportActionBar().setElevation(0);

        // Add the floating action button
        fab.setOnClickListener(v -> {
            Intent createTrack = new Intent(getBaseContext(), CreateTrackActivity.class);
            startActivity(createTrack);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        initSearch(menu);
        return true;
    }

    private void initSearch(Menu menu) {
        MenuItem item = menu.findItem(R.id.searchIcon);
        SearchView sv = (SearchView)item.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TrackDatabaseManagement.findTrackUIDByName(query,  new Callback<String>() {
                    @Override
                    public void onSuccess(String value) {
                        if(value == null){
                            Toast.makeText(getBaseContext(),String.format(getResources().getString(R.string.no_track_found), query), Toast.LENGTH_LONG).show();
                        }else{
                            Intent i = new Intent(getBaseContext(),TrackPropertiesActivity.class);
                            i.putExtra("TrackID", value);
                            startActivity(i);
                        }
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filterIcon:
                filterDialog();
                return true;
            case R.id.settingsIcon:
                Intent settingsIntent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(settingsIntent);
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

        builder.setMultiChoiceItems(listTypesStr, checkedTypes, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedTypes[which] = isChecked;
            }
        });

        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getText(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                typesFilter.clear();
                for (int i = 0; i < checkedTypes.length; i++) {
                    if (checkedTypes[i]) typesFilter.add(TrackType.values()[i]);
                }
                typesAreFiltered = !typesFilter.isEmpty();
            }
        });

        builder.setNegativeButton(getResources().getText(R.string.noFilter), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timeIsFiltered = false;
                difficultyIsFiltered = false;
                typesAreFiltered = false;
                for (int i = 0; i < checkedTypes.length; i++) {
                    checkedTypes[i] = false;
                }
                timeFilter = 60;
                difficultyFilter = 3;
                dialog.dismiss();
            }
        });

        builder.setView(mView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
