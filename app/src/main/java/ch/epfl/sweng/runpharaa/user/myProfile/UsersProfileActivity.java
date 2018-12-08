package ch.epfl.sweng.runpharaa.user.myProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.gui.ViewPagerAdapter;
import ch.epfl.sweng.runpharaa.cache.ImageLoader;
import ch.epfl.sweng.runpharaa.login.LoginActivity;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.settings.SettingsActivity;

public class UsersProfileActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);
        loadActivity(User.instance);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        tabLayout = findViewById(R.id.tabLayoutUser);
        viewPager = findViewById(R.id.viewPagerUser);

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
    public void onResume(){
        loadActivity(User.instance);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settingsIcon :
                startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                return true;
            case R.id.signOutIcon :
                signOut();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadActivity(User user) {

        TextView v = findViewById(R.id.user_name);
        v.setText(user.getName());

        TextView v1 = findViewById(R.id.nbTracks);
        int nbTracks = user.getCreatedTracks().size();
        v1.setText(Integer.toString(nbTracks));

        TextView v2 = findViewById(R.id.nbFav);
        int nbFav = user.getFavoriteTracks().size();
        v2.setText(Integer.toString(nbFav));

        picture = findViewById(R.id.profile_picture);
        ImageLoader.getLoader(this).displayImage(User.instance.getPicture(), picture);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.loggedOut), Toast.LENGTH_SHORT).show();
                    Intent login = new Intent(getBaseContext(), LoginActivity.class);
                    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(login);
                    finish();
                });
    }


}