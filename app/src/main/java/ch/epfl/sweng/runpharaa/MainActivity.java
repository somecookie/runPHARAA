package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.Users.Profile.UsersProfileActivity;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.utils.Util;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private FloatingActionButton fab;

    private FirebaseUser user;

    public static User main;

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

        TrackProperties p = new TrackProperties(100, 10, 1, 1, null);
        DatabaseManagement.writeNewTrack(new Track("Banane -> Centre Sportif", BitmapFactory.decodeResource(getResources(), R.drawable.centre_sportif) , Arrays.asList(new CustLatLng(46.522735, 6.579772), new CustLatLng(46.519380, 6.580669)), p));
        DatabaseManagement.writeNewTrack(new Track("Innovation Parc -> BC",BitmapFactory.decodeResource(getResources(), R.drawable.innovation_park),Arrays.asList(new CustLatLng(46.517563, 6.562350), new CustLatLng(46.518475, 6.561960)), p));
        DatabaseManagement.writeNewTrack(new Track("Rolex -> Swisstech",BitmapFactory.decodeResource(getResources(),R.drawable.rolex), Arrays.asList(new CustLatLng(46.518447, 6.568238), new CustLatLng(46.523206, 6.564945)), p));
        DatabaseManagement.writeNewTrack(new Track("Sat -> INM",BitmapFactory.decodeResource(getResources(),R.drawable.rolex), Arrays.asList(new CustLatLng(46.520566, 6.567820), new CustLatLng(46.518577, 6.563165)), p));
        DatabaseManagement.writeNewTrack(new Track("Ouchy -> Gare",BitmapFactory.decodeResource(getResources(),R.drawable.ouchy), Arrays.asList(new CustLatLng(46.506279, 6.626111), new CustLatLng(46.517210, 6.630105)), p));
        DatabaseManagement.writeNewTrack(new Track("SF -> Cath -> Flon",BitmapFactory.decodeResource(getResources(),R.drawable.saint_francois), Arrays.asList(new CustLatLng(46.519531, 6.633149), new CustLatLng(46.522638, 6.634971), new CustLatLng(46.521412, 6.627383)), p));
        Log.d("Put Fake Track", "Test2");
        */

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
        tabLayout.setupWithViewPager(viewPager);

        // Set icons
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_near_me);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_following);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_favorite);

        // Remove shadow from action bar
        getSupportActionBar().setElevation(0);

        // Add the floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createTrack = new Intent(getBaseContext(), CreateTrackActivity.class);
                startActivity(createTrack);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsIcon:
                Intent settingsIntent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.profileIcon:
                Intent profileIntent = new Intent(getBaseContext(), UsersProfileActivity.class);
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
}
