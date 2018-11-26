package ch.epfl.sweng.runpharaa.user.myProfile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import ch.epfl.sweng.runpharaa.ViewPagerAdapter;
import ch.epfl.sweng.runpharaa.cache.ImageLoader;
import ch.epfl.sweng.runpharaa.login.LoginActivity;
import ch.epfl.sweng.runpharaa.user.User;

public class UsersProfileActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    TextView emptyMessage;
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
    public void onResume(){
        loadActivity(User.instance);
        super.onResume();
    }

    protected void setEmptyMessage() {
        emptyMessage.setText(R.string.no_created_self);
        emptyMessage.setVisibility(View.VISIBLE);
    }

    private void loadActivity(User user) {
        emptyMessage = findViewById(R.id.emptyMessage);
        emptyMessage.setVisibility(View.GONE);

        TextView v = findViewById(R.id.user_name);
        v.setText(user.getName());

        TextView v1 = findViewById(R.id.nbTracks);
        int nbTracks = user.getCreatedTracks().size();
        v1.setText(Integer.toString(nbTracks));

        TextView v2 = findViewById(R.id.nbFav);
        int nbFav = user.getFavoriteTracks().size();
        v2.setText(Integer.toString(nbFav));

        Button signOutButton = findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(v32 -> signOut());

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
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.loggedOut), Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(login);
                        finish();
                    }
                });
    }


    private void updateImages(){
        final View mView = getLayoutInflater().inflate(R.layout.trophies, null);
        ImageView img= mView.findViewById(R.id.trophies_create);
        if(User.instance.getCreatedTracks().size() >= 1){
            img.setImageResource(R.drawable.create_one_track_reward);
        } else {
            img.setImageResource(R.drawable.lock);
        }

        final View vv = getLayoutInflater().inflate(R.layout.dialog_creation_trophies, null);
        ImageView i = vv.findViewById(R.id.trophy_one_track);
        i.setImageResource(R.drawable.create_one_track_reward);


    }


}