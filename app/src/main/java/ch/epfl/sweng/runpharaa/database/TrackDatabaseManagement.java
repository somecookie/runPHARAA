package ch.epfl.sweng.runpharaa.database;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.utilities.Pair;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.runpharaa.CustLatLng;
import ch.epfl.sweng.runpharaa.firebase.Database;
import ch.epfl.sweng.runpharaa.firebase.Storage;
import ch.epfl.sweng.runpharaa.tracks.FirebaseTrackAdapter;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Callback;
import ch.epfl.sweng.runpharaa.utils.Config;

import static ch.epfl.sweng.runpharaa.utils.Util.formatString;

public class TrackDatabaseManagement {

    public final static String TRACKS_PATH = "tracks";
    private final static String TRACK_IMAGE_PATH = "TrackImages";
    private final static String NAME_PATH = "name";
    private final static String ID_PATH = "trackUid";
    private final static String COMMENTS = "comments";
    public final static String IS_DELETED = "isDeleted";


    private static FirebaseDatabase mFirebaseDatabase = Database.getInstance();
    static DatabaseReference mDataBaseRef = mFirebaseDatabase.getReference();
    private static FirebaseStorage mFirebaseStorage = Storage.getInstance();
    private static StorageReference mStorageRef = mFirebaseStorage.getReference();

    TrackDatabaseManagement() {
    }

    /**
     * Give a UID to a given {@link Track} and add it to the Firebase database
     *
     * @param track a Track
     */
    public static void writeNewTrack(final FirebaseTrackAdapter track) {
        //Generate a new key in the database
        final String key = mDataBaseRef.child(TRACKS_PATH).push().getKey();

        //Upload image
        Bitmap bitmap = track.getImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mStorageRef.child(TRACK_IMAGE_PATH).child(key).putBytes(data);
        uploadTask.addOnFailureListener(e -> Log.e("Storage", "Failed to upload image to storage :" + e.getMessage()));
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mStorageRef.child(TRACK_IMAGE_PATH).child(key).getDownloadUrl().addOnFailureListener(e -> Log.e("Storage", "Failed to download image url :" + e.getMessage())).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        track.setImageStorageUri(task1.getResult().toString());
                        track.setTrackUid(key);
                        mDataBaseRef.child(TRACKS_PATH).child(key).setValue(track).addOnFailureListener(e -> Log.e("Database", "Failed to upload new track :" + e.getMessage())).addOnSuccessListener(aVoid -> {
                            User.instance.addToCreatedTracks(key);
                            User.instance.addNewFeedBack(key);
                            UserDatabaseManagement.updateFeedBackTracks(User.instance);
                            UserDatabaseManagement.updateCreatedTracks(User.instance);
                        });
                    }
                });
            }
        });
    }

    /**
     * Retrieve a Track's unique ID given its name
     *
     * @param name the track's name
     * @param callback a Callback
     */
    public static void findTrackUIDByName(final String name, Callback<String> callback) {
        DatabaseReference ref = mDataBaseRef.child(TRACKS_PATH);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String dataName = formatString(data.child(NAME_PATH).getValue(String.class));
                    String formattedName = formatString(name);

                    if (dataName.equals(formattedName)) {
                        String id = data.child(ID_PATH).getValue(String.class);
                        callback.onSuccess(id);
                        return;
                    }
                }
                callback.onSuccess(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getDetails());
            }
        });
    }

    /**
     * Given a track, updates the corresponding entry in the Firebase Database.
     *
     * @param track a track
     */
    public static void updateTrack(Track track) {
        mDataBaseRef.child(TRACKS_PATH).child(track.getTrackUid()).child(IS_DELETED).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Boolean isDeleted = dataSnapshot.getValue(Boolean.class);
                    FirebaseTrackAdapter adapter = new FirebaseTrackAdapter(track, isDeleted);
                    mDataBaseRef.child(TRACKS_PATH).child(adapter.getTrackUid()).setValue(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Given a track, update its comments in the Firebase Database
     *
     * @param track a track
     */
    public static void updateComments(Track track) {
        DatabaseReference commentsRef = mDataBaseRef.child(TRACKS_PATH).child(track.getTrackUid()).child(COMMENTS);
        commentsRef.setValue(track.getComments()).addOnFailureListener(Throwable::printStackTrace);
    }

    /**
     * Given a DataSnapshot at the Tracks level from the Firebase Database and a track key, return the corresponding track.
     *
     * @param dataSnapshot a dataSnapshot from the database
     * @param key a track key
     * @return the initiated Track
     */
    public static Track initTrack(DataSnapshot dataSnapshot, String key) {
        boolean isDeleted = dataSnapshot.child(key).child(IS_DELETED).getValue(Boolean.class);
        if(isDeleted){
            return null;
        }
        return new Track(dataSnapshot.child(key).getValue(FirebaseTrackAdapter.class));
    }

    /**
     * Given a DataSnapshot from the Firebase Database, returns the list of tracks near location.
     *
     * @param dataSnapshot a dataSnapshot from the database
     * @param location the location we want the tracks nearby
     * @return a List<Track> that are near the given location
     */
    public static List<Track> initTracksNearLocation(DataSnapshot dataSnapshot, LatLng location) {
        List<Track> tracksNearMe = new ArrayList<>();
        for (DataSnapshot c : dataSnapshot.getChildren()) {
            CustLatLng requestedLocation = new CustLatLng(location.latitude, location.longitude);
            int userPreferredRadius = User.instance.getPreferredRadius();

            if (c.child("path").child("0").getValue(CustLatLng.class) != null) {
                if (c.child("path").child("0").getValue(CustLatLng.class).distance(requestedLocation) <= userPreferredRadius &&
                        !c.child(IS_DELETED).getValue(Boolean.class)) {
                    tracksNearMe.add(new Track(c.getValue(FirebaseTrackAdapter.class)));
                }
            }
        }

        Collections.sort(tracksNearMe);
        return tracksNearMe;
    }

    /**
     * Retrieve the tracks and the deleted tracks from the databases and combine them in a Pair
     *
     * @param dataSnapshot a dataSnapshot from the database
     * @param tracksUID a Track's unique ID
     * @return a Pair<List<Track>, List<String>> containing the tracks and the deleted tracks
     */
    private static Pair<List<Track>, List<String>> initTracksList(DataSnapshot dataSnapshot, List<String> tracksUID){
        List<Track> tracks = new ArrayList<>();
        List<String> deletedTracks = new ArrayList<>();
        for(DataSnapshot c : dataSnapshot.getChildren()){
            if(tracksUID != null){
                if(tracksUID.contains(c.getKey())){
                    if(c.child(IS_DELETED).getValue(Boolean.class)){
                        deletedTracks.add(c.getKey());
                    }
                    else{
                        tracks.add(new Track(c.getValue(FirebaseTrackAdapter.class)));
                    }
                }
            }
        }
        Collections.sort(tracks);
        return new Pair<>(tracks, deletedTracks);
    }

    /**
     * Given a DataSnapshot from the Firebase Database, returns the list of created tracks.
     *
     * @param dataSnapshot a dataSnapshot from the database
     * @param user the target user
     * @return a List<Track> of a user's created tracks
     */
    public static List<Track> initCreatedTracks(DataSnapshot dataSnapshot, User user){
        Pair<List<Track>, List<String>> p = initTracksList(dataSnapshot, user.getCreatedTracks());
        List<String> createdTracks = user.getCreatedTracks();
        createdTracks.removeAll(p.getSecond());
        user.setCreatedTracks(createdTracks);
        UserDatabaseManagement.updateCreatedTracks(user);
        return p.getFirst();
    }

    /**
     * Given a DataSnapshot from the Firebase Database, return a list containing the ID of the
     * tracks to delete
     *
     * @param dataSnapshot a dataSnapshot from the database
     * @param list a List<String> containing all the deleted Tracks
     * @return a List<String> without the deleted Tracks
     */
    static List<String> deleteDeletedTrackFromList(DataSnapshot dataSnapshot, List<String> list){
        if(list != null){
            for(DataSnapshot c : dataSnapshot.getChildren()){
                if(list.contains(c.getKey())){
                    if(c.child(IS_DELETED).getValue(Boolean.class)){
                        list.remove(c.getKey());
                    }
                }
            }
        }
        return list;
    }

    /**
     * Given a DataSnapshot from the Firebase Database, returns the list of favourite tracks.
     *
     * @param dataSnapshot a dataSnapshot from the database
     * @return a List<Track> of the favourite tracks of the user instance
     */
    public static List<Track> initFavouritesTracks(DataSnapshot dataSnapshot) {
        Pair<List<Track>, List<String>> p = initTracksList(dataSnapshot, User.instance.getFavoriteTracks());
        List<String> favoritesTracks = User.instance.getFavoriteTracks();
        favoritesTracks.removeAll(p.getSecond());
        User.instance.setFavoriteTracks(favoritesTracks);
        UserDatabaseManagement.updateFavoriteTracks(User.instance);
        return p.getFirst();
    }

    /**
     * Delete a Track by adding a flag to it in the Database
     *
     * @param trackUID a Track's unique ID
     */
    public static void deleteTrack(String trackUID){
        List<String> createdTracks = User.instance.getCreatedTracks();
        createdTracks.remove(trackUID);
        User.instance.setCreatedTracks(createdTracks);
        UserDatabaseManagement.updateCreatedTracks(User.instance);
        mDataBaseRef.child(TRACKS_PATH).child(trackUID).child(IS_DELETED).setValue(true);
    }

    /**
     * Read the data from the Firebase Database. Two methods to override.
     *
     * @param child a String corresponding to a child in the Database
     * @param listener a Callback<DataSnapshot>
     */
    public static void mReadDataOnce(String child, final Callback<DataSnapshot> listener) {
        DatabaseReference ref = mDataBaseRef.child(child);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }
}
