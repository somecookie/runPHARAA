package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ch.epfl.sweng.runpharaa.Users.Profile.UsersProfileActivity;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private FloatingActionButton fab;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
