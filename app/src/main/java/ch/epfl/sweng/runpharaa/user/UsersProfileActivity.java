package ch.epfl.sweng.runpharaa.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.io.InputStream;

import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.login.LoginActivity;

public class UsersProfileActivity extends AppCompatActivity {

    private User actualUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isSelfUser = getIntent().getBooleanExtra("selfUser", false); // TODO should be true
        if (isSelfUser) {
            setContentView(R.layout.activity_user);
            actualUser = User.instance;
        } else {
            setContentView(R.layout.activity_other_user);
            actualUser = User.instance; // TODO Change when other users profile are accessible
        }

        TextView v = findViewById(R.id.user_name);
        v.setText(actualUser.getName());

        TextView v1 = findViewById(R.id.nbTracks);
        int nbTracks = actualUser.getCreatedTracks().size();
        v1.setText(Integer.toString(nbTracks));

        TextView v2 = findViewById(R.id.nbFav);
        int nbFav = actualUser.getFavoriteTracks().size();
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
            if (!self.alreadyInFollowed(actualUser.getID())) {
                followButton.setText("FOLLOW");
            } else {
                followButton.setText("UNFOLLOW");
            }
            followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!self.alreadyInFollowed(actualUser.getID())) {
                        self.addToFollowed(actualUser.getID());
                        UserDatabaseManagement.updateFollowedUsers(self);
                        followButton.setText("UNFOLLOW");
                    } else {
                        self.removeFromFollowed(actualUser.getID());
                        UserDatabaseManagement.removeFollowedUser(actualUser.getID());
                        followButton.setText("FOLLOW");
                    }
                }
            });
        }

        new DownloadImageTask((ImageView) findViewById(R.id.profile_picture))
                .execute(actualUser.getPicture().toString());
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