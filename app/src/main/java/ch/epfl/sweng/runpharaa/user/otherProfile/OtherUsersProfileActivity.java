package ch.epfl.sweng.runpharaa.user.otherProfile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.TrackCardItem;
import ch.epfl.sweng.runpharaa.TrackPropertiesActivity;
import ch.epfl.sweng.runpharaa.cache.ImageLoader;
import ch.epfl.sweng.runpharaa.database.TrackDatabaseManagement;
import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.user.Adapter;
import ch.epfl.sweng.runpharaa.user.User;

public class OtherUsersProfileActivity extends AppCompatActivity {

    TextView emptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String finalProfileUserId = userProfileUid();

        setContentView(R.layout.activity_other_user);

        UserDatabaseManagement.mReadDataOnce(UserDatabaseManagement.USERS, new UserDatabaseManagement.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot data) {
                // Load and search which User we want to see the profile
                User user = UserDatabaseManagement.getUser(data, finalProfileUserId);

                // Load the corresponding activity
                if (user != null)
                    loadActivity(user);

            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                Log.d("DB Read: ", "Failed to read users from DB in UserProfileActivity.");
            }
        });
    }


    private String userProfileUid() {
        Intent i = getIntent();
        String profileUserId = null;

        if (i != null) {
            profileUserId = getIntent().getStringExtra("userId");
        }

        return profileUserId;
    }

    protected void setEmptyMessage() {
        emptyMessage.setText(R.string.no_created_other);
        emptyMessage.setVisibility(View.VISIBLE);
    }

    private void loadActivity(User user) {
        emptyMessage = findViewById(R.id.emptyMessage);
        emptyMessage.setVisibility(View.GONE);
        emptyMessage.setVisibility(View.GONE);

        Context context = this;

        TextView v = findViewById(R.id.user_name);
        v.setText(user.getName());

        TextView v1 = findViewById(R.id.nbTracks);
        int nbTracks = user.getCreatedTracks().size();
        v1.setText(Integer.toString(nbTracks));

        TextView v2 = findViewById(R.id.nbFav);
        int nbFav = user.getFavoriteTracks().size();
        v2.setText(Integer.toString(nbFav));

        User self = User.instance;
        Button followButton = findViewById(R.id.follow_button);
        if (!self.alreadyInFollowed(user)) {
            followButton.setText("FOLLOW");
        } else {
            followButton.setText("UNFOLLOW");
        }
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!self.alreadyInFollowed(user)) {
                    self.addToFollowed(user);
                    UserDatabaseManagement.updateFollowedUsers(self);
                    followButton.setText("UNFOLLOW");
                } else {
                    self.removeFromFollowed(user);
                    UserDatabaseManagement.removeFollowedUser(user);
                    followButton.setText("FOLLOW");
                }
            }
        });


        ImageLoader.getLoader(this).displayImage(user.getPicture(), findViewById(R.id.profile_picture));

        // Load User's createdTracks
        TrackDatabaseManagement.mReadDataOnce(TrackDatabaseManagement.TRACKS_PATH, new TrackDatabaseManagement.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot data) {
                RecyclerView recyclerView = findViewById(R.id.createdTracksCardListId);
                List<TrackCardItem> createdTracks = new ArrayList<>();
                Adapter.OnItemClickListener listener = item -> {
                    Intent intent = new Intent(context, TrackPropertiesActivity.class);
                    intent.putExtra("TrackID", item.getParentTrackID());
                    startActivity(intent);
                };
                List<Track> tracks = TrackDatabaseManagement.initCreatedTracks(data, user);
                for (Track t : tracks) {
                    t.setTrackCardItem(new TrackCardItem(t.getName(), t.getTrackUid(), t.getImageStorageUri()));
                    createdTracks.add(t.getTrackCardItem());
                }
                Adapter adapter = new Adapter(context, createdTracks, (Adapter.OnItemClickListener) listener);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

                if (createdTracks.isEmpty())
                    setEmptyMessage();
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                Log.d("DB Read: ", "Failed to read data from DB in UserProfileActivity.");
                setEmptyMessage();
            }
        });
    }

}