package ch.epfl.sweng.runpharaa.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.TrackCardItem;
import ch.epfl.sweng.runpharaa.TrackPropertiesActivity;
import ch.epfl.sweng.runpharaa.UpdatableCardItemFragment;
import ch.epfl.sweng.runpharaa.cache.ImageLoader;
import ch.epfl.sweng.runpharaa.database.TrackDatabaseManagement;
import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.login.LoginActivity;
import ch.epfl.sweng.runpharaa.tracks.Track;

public class UsersProfileActivity extends AppCompatActivity {

    public interface OnItemClickListener {
        void onItemClick(TrackCardItem item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String finalProfileUserId = userProfileUid();
        boolean isSelfUser = true;

        if (finalProfileUserId != null)
            isSelfUser = finalProfileUserId.equals(User.instance.getUid());

        if (isSelfUser) {
            setContentView(R.layout.activity_user);
            loadActivity(User.instance, true);
        } else {
            setContentView(R.layout.activity_other_user);

            UserDatabaseManagement.mReadDataOnce(UserDatabaseManagement.USERS, new UserDatabaseManagement.OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot data) {
                    // Load and search which User we want to see the profile
                    User user = UserDatabaseManagement.getUser(data, finalProfileUserId);

                    // Load the corresponding activity
                    if (user != null)
                        loadActivity(user, false);

                }

                @Override
                public void onFailed(DatabaseError databaseError) {
                    Log.d("DB Read: ", "Failed to read users from DB in UserProfileActivity.");
                }
            });
        }
    }

    private String userProfileUid () {
        Intent i = getIntent();
        String profileUserId = null;

        if (i != null) {
            profileUserId = getIntent().getStringExtra("userId");
        }

        return profileUserId;
    }

    private void loadActivity(User user, Boolean isSelfUser) {
        Context context = this;

        TextView v = findViewById(R.id.user_name);
        v.setText(user.getName());

        TextView v1 = findViewById(R.id.nbTracks);
        int nbTracks = user.getCreatedTracks().size();
        v1.setText(Integer.toString(nbTracks));

        TextView v2 = findViewById(R.id.nbFav);
        int nbFav = user.getFavoriteTracks().size();
        v2.setText(Integer.toString(nbFav));

        if (isSelfUser) {
            Button signOutButton = findViewById(R.id.sign_out_button);
            signOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signOut();
                }
            });
        } else {
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
        }

        new DownloadImageTask((ImageView) findViewById(R.id.profile_picture))
                .execute(user.getPicture().toString());

        // Load User's createdTracks
        TrackDatabaseManagement.mReadDataOnce(TrackDatabaseManagement.TRACKS_PATH, new TrackDatabaseManagement.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot data) {
                RecyclerView recyclerView = findViewById(R.id.createdTracksCardListId);
                List<TrackCardItem> createdTracks = new ArrayList<>();
                OnItemClickListener listener = new OnItemClickListener() {
                    @Override
                    public void onItemClick(TrackCardItem item) {
                        Intent intent = new Intent(context, TrackPropertiesActivity.class);
                        intent.putExtra("TrackID", item.getParentTrackID());
                        startActivity(intent);
                    }
                };
                List<Track> tracks = TrackDatabaseManagement.initCreatedTracks(data);
                for (Track t : tracks) {
                    t.setTrackCardItem(new TrackCardItem(t.getName(), t.getTrackUid(), t.getImageStorageUri()));
                    createdTracks.add(t.getTrackCardItem());
                }
                Adapter adapter = new Adapter(context, createdTracks, listener);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                Log.d("DB Read: ", "Failed to read data from DB in UserProfileActivity.");
            }
        });
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {
        List<TrackCardItem> createdTracks;
        OnItemClickListener listener;
        Context context;

        Adapter(Context context,List<TrackCardItem> createdTracks, OnItemClickListener listener) {
            this.createdTracks = createdTracks;
            this.listener = listener;
            this.context = context;
        }

        @NonNull
        @Override
        public Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View v = layoutInflater.inflate(R.layout.track_card_item, viewGroup, false);
            return new Adapter.viewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.viewHolder viewHolder, int position) {
            // Set here the buttons, images and texts created in the viewHolder
            viewHolder.name.setText(createdTracks.get(position).getName());

            ImageLoader.getLoader(getBaseContext()/*getContext()*/).displayImage(createdTracks.get(position).getImageURL(), viewHolder.background_img);

            viewHolder.bind(createdTracks.get(position), new OnItemClickListener() {
                @Override
                public void onItemClick(TrackCardItem item) {
                    Intent intent = new Intent(getBaseContext()/*getContext()*/, TrackPropertiesActivity.class);
                    intent.putExtra("TrackID", item.getParentTrackID());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return createdTracks.size();
        }

        protected class viewHolder extends RecyclerView.ViewHolder {
            // Buttons, images and texts on the cards will be created here

            ImageView background_img;
            TextView name;

            public viewHolder(@NonNull View itemView) {
                super(itemView);
                background_img = itemView.findViewById(R.id.cardBackgroundId);
                name = itemView.findViewById(R.id.nameID);
            }

            public void bind(final TrackCardItem item, final OnItemClickListener listener) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(item);
                    }
                });
            }
        }
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

    /**
     * Private class to download Uri images and set the ImageView to the image downloaded
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = BitmapFactory.decodeResource(getResources(), R.drawable.default_photo);
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        /**
         * Set the ImageView to the bitmap result
         *
         * @param result
         */
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}