package ch.epfl.sweng.runpharaa.user.myProfile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.cache.ImageLoader;
import ch.epfl.sweng.runpharaa.gui.ViewPagerAdapter;
import ch.epfl.sweng.runpharaa.login.LoginActivity;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.settings.SettingsActivity;
import ch.epfl.sweng.runpharaa.utils.Config;
import ch.epfl.sweng.runpharaa.utils.Util;

public class UsersProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Util.prepareHomeButton(this);

        setContentView(R.layout.activity_user);
        loadActivity(User.instance);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        TabLayout tabLayout = findViewById(R.id.tabLayoutUser);
        ViewPager viewPager = findViewById(R.id.viewPagerUser);

        // Add fragments
        adapter.addFragment(new FragmentMyTracks());
        adapter.addFragment(new FragmentMyTrophies());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);

        // Set icons
        tabLayout.getTabAt(0).setText("My tracks");
        tabLayout.getTabAt(1).setText("My trophies");


        // Remove shadow from action bar
        getSupportActionBar().setElevation(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu_user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        loadActivity(User.instance);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsIcon:
                startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                return true;
            case R.id.signOutIcon:
                Util.signOut(this);
                return true;
            case android.R.id.home:
                Util.goHome(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Load activity for UsersProfile.
     *
     * @param user
     */
    private void loadActivity(User user) {
        TextView streakCount = findViewById(R.id.current_streak);
        streakCount.setText(Integer.toString(User.getStreakManager().getCurrentStreak()));

        TextView v = findViewById(R.id.user_name);
        v.setText(user.getName());

        TextView v1 = findViewById(R.id.nbTracks);
        int nbTracks = user.getCreatedTracks().size();
        v1.setText(Integer.toString(nbTracks));

        TextView v2 = findViewById(R.id.nbFav);
        int nbFav = user.getFavoriteTracks().size();
        v2.setText(Integer.toString(nbFav));

        ImageView picture = findViewById(R.id.profile_picture);
        ImageLoader.getLoader(this).displayImage(User.instance.getPicture(), picture);
    }
}