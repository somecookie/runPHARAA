package ch.epfl.sweng.runpharaa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import ch.epfl.sweng.runpharaa.cache.ImageLoader;
import ch.epfl.sweng.runpharaa.comment.Comment;
import ch.epfl.sweng.runpharaa.comment.CommentAdapter;
import ch.epfl.sweng.runpharaa.database.TrackDatabaseManagement;
import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.notification.FireMessage;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.myProfile.UsersProfileActivity;
import ch.epfl.sweng.runpharaa.user.otherProfile.OtherUsersProfileActivity;
import ch.epfl.sweng.runpharaa.utils.Callback;
import ch.epfl.sweng.runpharaa.utils.Config;
import ch.epfl.sweng.runpharaa.utils.PropertiesOnClickListener;
import ch.epfl.sweng.runpharaa.utils.Util;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;
import static java.security.AccessController.getContext;

public class TrackPropertiesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ShareDialog shareDialog;
    private TweetComposer.Builder tweetBuilder;
    private GoogleMap map;
    private LatLng[] points;
    private TextView testText;

    private Intent startIntent;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startIntent = getIntent();

        Util.prepareHomeButton(this);

        setContentView(R.layout.activity_track_properties);

        Twitter.initialize(this);

        OnMapReadyCallback onMapReadyCallback = this;

        shareDialog = new ShareDialog(this);
        tweetBuilder = new TweetComposer.Builder(this);

        final Intent intent = getIntent();
        testText = findViewById(R.id.maps_test_text2);

        TrackDatabaseManagement.mReadDataOnce(TrackDatabaseManagement.TRACKS_PATH, new Callback<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot data) {

                final String trackID = intent.getStringExtra("TrackID");
                final Track track = TrackDatabaseManagement.initTrack(data, trackID);
                if(track == null){
                    Toast t = Toast.makeText(getApplicationContext(), R.string.deleted_track, Toast.LENGTH_SHORT);
                    t.show();
                    finish();
                    return;
                }
                points = CustLatLng.CustLatLngToLatLng(track.getPath()).toArray(new LatLng[track.getPath().size()]);

                setDeleteButton(track);


                TrackProperties tp = track.getProperties();
                ImageView trackBackground = findViewById(R.id.trackBackgroundID);
                ImageLoader.getLoader(getBaseContext()).displayImage(track.getImageStorageUri(), trackBackground, false); // caching

                setTextOfProperties(track, tp);
                setButtonsOfProperties(trackID, track);

                // Get fakeMap
                SupportMapFragment mapFragment = (CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.create_map_view2);
                mapFragment.getMapAsync(onMapReadyCallback);
                if (Config.isTest) {
                    onMapReady(Config.getFakeMap());
                }
            }

            @Override
            public void onError(Exception databaseError) {
                Log.d("DB Read: ", "Failed to read data from DB in TrackPropertiesActivity.");
            }
        });
    }


    /**
     * Set the delete button to visible/invisible depending if the {@link Track} can be deleted
     * @param track
     */
    private void setDeleteButton(Track track){
        ImageButton deleteButton = findViewById(R.id.deleteButton);
        if(track.getCreatorUid().equals(User.instance.getUid()))
        {
            deleteButton.setVisibility(Button.VISIBLE);
            deleteButton.setClickable(true);
            deleteButton.setOnClickListener(view -> {
                AlertDialog diaBox = deleteTrackConfirmation(track);
                diaBox.show();
            });
        }
        else {
            deleteButton.setVisibility(Button.INVISIBLE);
            deleteButton.setClickable(false);
        }
    }

    /**
     * Delete the track and finish the activity
     * @param track
     */
    private void deleteTrack(Track track){
        TrackDatabaseManagement.deleteTrack(track.getTrackUid());
        Toast t = Toast.makeText(this, R.string.track_deleted, Toast.LENGTH_LONG);
        t.show();
        finish();
    }

    /**
     * Open an {@link AlertDialog} to confirm if the user really want to delete the track
     * @param track
     * @return
     */
    private AlertDialog deleteTrackConfirmation(Track track)
    {
        AlertDialog deleteTrackDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle(R.string.delete_track)
                .setMessage(R.string.want_to_delete_this_track)
                .setIcon(R.drawable.ic_delete)

                .setPositiveButton(R.string.delete, (dialog, whichButton) -> {
                    deleteTrack(track);
                    dialog.dismiss();
                })

                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .create();
        return deleteTrackDialogBox;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Util.goHome(this);
        return true;
    }


    private void setButtonsOfProperties(String trackID, Track track) {
        ToggleButton toggleLike = findViewById(R.id.buttonLikeID);
        ToggleButton toggleFavorite = findViewById(R.id.buttonFavoriteID);

        // Check if the user already liked this track and toggle the button accordingly
        toggleLike.setChecked(User.instance.alreadyLiked(trackID));

        toggleLike.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                updateLikes(track, trackID);
            } else {
                updateLikes(track, trackID);
            }
        });

        // Check if the track already in favorites and toggle the button accordingly
        toggleFavorite.setChecked(User.instance.alreadyInFavorites(trackID));

        toggleFavorite.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                updateNbFavorites(track, trackID);
            } else {
                updateNbFavorites(track, trackID);
            }
        });


        initSocialMediaButtons(track);
        initCommentButton(track);
        initFeedbackButton(trackID, track);

    }

    private void initFeedbackButton(String trackID, Track track) {
        Button feedbackButton = findViewById(R.id.feedbackButton);
        feedbackButton.setOnClickListener(new PropertiesOnClickListener(this, new Callback<PropertiesOnClickListener>() {
            @Override
            public void onSuccess(PropertiesOnClickListener value) {

                if (!value.isPropertiesSet()) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.prop_miss), Toast.LENGTH_LONG).show();
                } else if (User.instance.getFeedbackTracks().contains(trackID)) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.feedback_exists), Toast.LENGTH_LONG).show();
                } else {
                    TrackProperties tp = track.getProperties();
                    tp.addNewDuration(value.getTime());
                    tp.addNewDifficulty(value.getDifficulty());
                    TrackDatabaseManagement.updateTrack(track);
                    UserDatabaseManagement.updateFeedBackTracks(User.instance);
                    relaunchActivity();
                }
            }
        }));
    }

    private void relaunchActivity() {
        finish();
        startActivity(startIntent);
    }

    private void initCommentButton(Track track) {
        Button commentsButton = findViewById(R.id.commentsID);
        TextView nbrComments = findViewById(R.id.trackCommentsID);
        nbrComments.setText(String.format("%d", track.getComments().size()));
        commentsButton.setOnClickListener(v -> {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(TrackPropertiesActivity.this);


            final View mView = getLayoutInflater().inflate(R.layout.dialog_comments, null);
            EditText tv = mView.findViewById(R.id.comments_editText);
            tv.setHint(String.format(getResources().getString(R.string.comment_hint), Comment.MAX_LENGTH));
            Button sendButton = mView.findViewById(R.id.post_button);

            sendButton.setOnClickListener(v1 -> {
                String comment = tv.getText().toString();

                if (Comment.checkSizeComment(comment)) {
                    Date date = new Date();
                    Comment com = new Comment(User.instance.getUid(), comment, date);
                    track.addComment(com);
                    nbrComments.setText(String.format("%d", track.getComments().size()));
                    TrackDatabaseManagement.updateComments(track);
                    tv.setText("");
                    hideKeyboardFrom(getBaseContext(), mView);
                    relaunchActivity();
                } else if(comment.length() > 0) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.comment_too_long), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.comment_too_short), Toast.LENGTH_LONG).show();
                }

            });

            RecyclerView commentRv = mView.findViewById(R.id.comment_rv);
            commentRv.setHasFixedSize(true);
            commentRv.setLayoutManager(new LinearLayoutManager(this));

            CommentAdapter commentAdapter;
            commentAdapter = new CommentAdapter(this, track.getComments());

            commentRv.setAdapter(commentAdapter);


            mBuilder.setView(mView);
            AlertDialog dialog = mBuilder.create();
            dialog.show();
        });
    }

    private void initSocialMediaButtons(Track track) {
        // Share on Facebook
        ImageButton fb = findViewById(R.id.fb_share_button);
        fb.setOnClickListener(v -> {
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://github.com/somecookie/runPHARAA/"))
                        .setShareHashtag(new ShareHashtag.Builder()
                                .setHashtag(String.format(getString(R.string.social_media_post_message), track.getName())).build())
                        .build();
                shareDialog.show(content);
            }
        });


        // Share on Twitter
        ImageButton twitter = findViewById(R.id.twitter_share_button);
        twitter.setOnClickListener(v -> {
            //startActivity(Util.getTwitterIntent(getApplicationContext(), "Text that will be tweeted"));
            try {
                tweetBuilder
                        .text(String.format(getString(R.string.social_media_post_message), track.getName()))
                        .url(new URL("https://github.com/somecookie/runPHARAA"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            tweetBuilder.show();
        });
    }

    private void setTextOfProperties(Track track, TrackProperties tp) {
        TextView trackTitle = findViewById(R.id.trackTitleID);
        trackTitle.setText(track.getName());

        TextView trackCreator = findViewById(R.id.trackCreatorID);
        trackCreator.setText(String.format(getResources().getString(R.string.by), track.getCreatorName()));
        trackCreator.setOnClickListener(v -> {
            Intent userProfile;
            if (track.getCreatorUid().equals(User.instance.getUid())) {
                userProfile = new Intent(getBaseContext(), UsersProfileActivity.class);
            } else {
                userProfile = new Intent(getBaseContext(), OtherUsersProfileActivity.class);
            }

            userProfile.putExtra("userId", track.getCreatorUid());
            startActivity(userProfile);
        });

        TextView trackDuration = findViewById(R.id.trackDurationID);
        trackDuration.setText(String.format(getResources().getString(R.string.duration), tp.getAvgDuration()));

        TextView trackLength = findViewById(R.id.trackLengthID);
        trackLength.setText(String.format(getResources().getString(R.string.distance), tp.getLength()));

        TextView trackDifficulty = findViewById(R.id.track_difficulty);
        trackDifficulty.setText(String.format(getResources().getString(R.string.difficulty), tp.getAvgDifficulty()));

        TextView trackLikes = findViewById(R.id.trackLikesID);
        trackLikes.setText(String.format("%d", tp.getLikes()));

        TextView trackFavourites = findViewById(R.id.trackFavouritesID);
        trackFavourites.setText(String.format("%d", tp.getFavorites()));

        TextView trackTags = findViewById(R.id.trackTagsID);
        trackTags.setText(createTagString(track));

    }

    private String createTagString(Track track) {
        Set<TrackType> typeSet = track.getProperties().getType();
        int nbrTypes = typeSet.size();
        String[] trackType = getResources().getStringArray(R.array.track_types);

        String start = (nbrTypes > 1) ? "Tags: " : "Tag: ";

        StringBuilder sb = new StringBuilder();
        sb.append(start);

        int i = 0;

        for (TrackType tt : typeSet) {

            sb.append(trackType[TrackType.valueOf(tt.name()).ordinal()]);
            if (i < nbrTypes - 2) sb.append(", ");
            else if (i == nbrTypes - 2)
                sb.append(" ").append(getResources().getString(R.string.and)).append(" ");

            i++;
        }
        return sb.toString();
    }

    private void updateLikes(Track track1, String trackID) {


        final Track track = track1;
        if (User.instance.alreadyLiked(trackID)) {
            track.getProperties().removeLike();
            User.instance.unlike(trackID);
            UserDatabaseManagement.removeLikedTrack(trackID);
        } else {
            UserDatabaseManagement.getNotificationKeyFromUID(track.getCreatorUid(), new Callback<String>() {
                @Override
                public void onSuccess(String value) {
                    sentToNotification(value, "LIKE ALERT", "The user " + User.instance.getName() + " liked your track " + track.getName());
                }
            });

            track.getProperties().addLike();
            User.instance.like(trackID);
            UserDatabaseManagement.updateLikedTracks(User.instance);
        }
        TrackDatabaseManagement.updateTrack(track);
        runOnUiThread(() -> {
            TextView trackLikesUpdated = findViewById(R.id.trackLikesID);
            trackLikesUpdated.setText("" + track.getProperties().getLikes());
        });
    }

    private void updateNbFavorites(Track track1, String trackID) {
        final Track track = track1;
        if (User.instance.alreadyInFavorites(trackID)) {
            track.getProperties().removeFavorite();
            User.instance.removeFromFavorites(trackID);
            UserDatabaseManagement.removeFavoriteTrack(trackID);
        } else {
            UserDatabaseManagement.getNotificationKeyFromUID(track.getCreatorUid(), new Callback<String>() {
                @Override
                public void onSuccess(String value) {
                    sentToNotification(value, "FAV ALERT", "The user " + User.instance.getName() + " added one track ( " + track.getName() + " ) to his favorite");
                }
            });
            track.getProperties().addFavorite();
            User.instance.addToFavorites(trackID);
        }

        TrackDatabaseManagement.updateTrack(track);
        UserDatabaseManagement.updateFavoriteTracks(User.instance);
        runOnUiThread(() -> {
            TextView trackFavoritesUpdated = findViewById(R.id.trackFavouritesID);
            trackFavoritesUpdated.setText("" + track.getProperties().getFavorites());
        });

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.zoomTo(18));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnMarkerClickListener(marker -> true);
        map.setOnMapClickListener(latLng -> {
            Intent fullMapIntent = new Intent(getBaseContext(), FullMapActivity.class);
            fullMapIntent.putExtra("points", points);
            startActivity(fullMapIntent);
        });

        testText.setText("ready");

        ScrollView mScrollView = findViewById(R.id.scrollID); //parent scrollview in xml, give your scrollview id value
        ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.create_map_view2))
                .setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));

        drawTrackOnMap();
    }

    /**
     * Draws the full track and markers on the fakeMap
     */
    private void drawTrackOnMap() {
        if (map != null && points != null) {
            Log.i("Create Map : ", "Drawing on fakeMap in TrackPropertiesActivity.");
            // Get correct zoom
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for (LatLng point : points)
                boundsBuilder.include(point);
            LatLngBounds bounds = boundsBuilder.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = 250;
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 0));
            // Add lines
            map.addPolyline(new PolylineOptions().addAll(Arrays.asList(points)));
            // Add markers (start = green, finish = red)
            map.addMarker(new MarkerOptions().position(points[0]).icon(defaultMarker(150)).alpha(0.8f));
            map.addMarker(new MarkerOptions().position(points[points.length - 1]).icon(defaultMarker(20)).alpha(0.8f));
        }
    }

    private void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void sentToNotification(String key, String title, String message) {

        FireMessage f = null;
        try {
            f = new FireMessage(title, message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            f.sendToToken(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}