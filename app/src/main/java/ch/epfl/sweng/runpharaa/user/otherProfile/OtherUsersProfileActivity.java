package ch.epfl.sweng.runpharaa.user.otherProfile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.TrackPropertiesActivity;
import ch.epfl.sweng.runpharaa.cache.ImageLoader;
import ch.epfl.sweng.runpharaa.database.TrackDatabaseManagement;
import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.gui.TrackCardItem;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.user.AdapterTracksToRecyclerViewItem;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Callback;
import ch.epfl.sweng.runpharaa.utils.Util;

public class OtherUsersProfileActivity extends AppCompatActivity {

    private TextView emptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.prepareHomeButton(this);
        final String finalProfileUserId = userProfileUid();

        setContentView(R.layout.activity_other_user);

        UserDatabaseManagement.mReadDataOnce(UserDatabaseManagement.USERS, new Callback<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot data) {
                // Load and search which User we want to see the profile
                User user = UserDatabaseManagement.getUser(data, finalProfileUserId);

                // Load the corresponding activity
                if (user != null)
                    loadActivity(user);

            }

            @Override
            public void onError(Exception databaseError) {
                Log.d("DB Read: ", "Failed to read users from DB in UserProfileActivity.");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Util.goHome(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Get user profile id.
     *
     * @return
     */
    private String userProfileUid() {
        Intent i = getIntent();
        String profileUserId = null;

        if (i != null) {
            profileUserId = getIntent().getStringExtra("userId");
        }

        return profileUserId;
    }

    private void setEmptyMessage() {
        emptyMessage.setText(R.string.no_created_other);
        emptyMessage.setVisibility(View.VISIBLE);
    }

    /**
     * Load activity for OtherUsersProfile.
     *
     * @param user
     */
    private void loadActivity(User user) {
        emptyMessage = findViewById(R.id.emptyMessage);
        emptyMessage.setVisibility(View.GONE);

        Context context = this;

        TextView user_name = findViewById(R.id.user_name);
        user_name.setText(user.getName());

        TextView viewNbTracks = findViewById(R.id.nbTracks);
        int nbTracks = user.getCreatedTracks().size();
        viewNbTracks.setText(Integer.toString(nbTracks));

        TextView viewNbFav = findViewById(R.id.nbFav);
        int nbFav = user.getFavoriteTracks().size();
        viewNbFav.setText(Integer.toString(nbFav));

        User self = User.instance;
        Button followButton = findViewById(R.id.follow_button);
        if (!self.alreadyInFollowed(user)) {
            followButton.setText(getResources().getString(R.string.follow));
            followButton.setBackgroundResource(R.drawable.rounded_corners_color);
            followButton.setTextColor(getResources().getColor(R.color.text_light));
        } else {
            followButton.setText(getResources().getString(R.string.unfollow));
        }
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!self.alreadyInFollowed(user)) {
                    self.addToFollowed(user);
                    UserDatabaseManagement.updateFollowedUsers(self);
                    followButton.setText(getResources().getString(R.string.unfollow));
                    followButton.setBackgroundResource(R.drawable.rounded_corners);
                    followButton.setTextColor(getResources().getColor(R.color.text_dark));
                } else {
                    self.removeFromFollowed(user);
                    UserDatabaseManagement.removeFollowedUser(user);
                    followButton.setText(getResources().getString(R.string.follow));
                    followButton.setBackgroundResource(R.drawable.rounded_corners_color);
                    followButton.setTextColor(getResources().getColor(R.color.text_light));
                }
            }
        });


        ImageLoader.getLoader(this).displayImage(user.getPicture(), findViewById(R.id.profile_picture));

        // Load User's createdTracks
        TrackDatabaseManagement.mReadDataOnce(TrackDatabaseManagement.TRACKS_PATH, new Callback<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot data) {
                RecyclerView recyclerView = findViewById(R.id.createdTracksCardListId);
                List<TrackCardItem> createdTracks = new ArrayList<>();
                AdapterTracksToRecyclerViewItem.OnItemClickListener listener = item -> {
                    Intent intent = new Intent(context, TrackPropertiesActivity.class);
                    intent.putExtra("TrackID", item.getParentTrackID());
                    startActivity(intent);
                };
                List<Track> tracks = TrackDatabaseManagement.initCreatedTracks(data, user);
                for (Track t : tracks) {
                    t.setTrackCardItem(new TrackCardItem(t.getName(), t.getTrackUid(), t.getImageStorageUri()));
                    createdTracks.add(t.getTrackCardItem());
                }
                AdapterTracksToRecyclerViewItem adapter = new AdapterTracksToRecyclerViewItem(context, createdTracks, listener);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

                if (createdTracks.isEmpty())
                    setEmptyMessage();
            }

            @Override
            public void onError(Exception databaseError) {
                Log.d("DB Read: ", "Failed to read data from DB in UserProfileActivity.");
                setEmptyMessage();
            }
        });
    }

}