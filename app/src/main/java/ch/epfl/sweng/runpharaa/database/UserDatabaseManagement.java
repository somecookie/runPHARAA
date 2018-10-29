package ch.epfl.sweng.runpharaa.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import ch.epfl.sweng.runpharaa.user.User;

public class UserDatabaseManagement extends DatabaseManagement {
    private final static String USERS = "users";
    private final static String FAVORITE = "favoriteTracks";
    private final static String LIKES = "likedTracks";
    private final static String CREATE = "createdTracks";

    public static void writeNewUser(final User user){
        mDataBaseRef.child(USERS).child(user.getID()).setValue(user.getFirebaseAdapter()).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Storage", "Failed to upload new user: "+e.getMessage());
                    }
                }
        );
    }

    public static void updateFavoriteTracks(final User user){
        mDataBaseRef.child(USERS).child(user.getID()).child(FAVORITE).setValue(user.getFirebaseAdapter().getFavoriteTracks()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Database", "Failed to upload the favorite tracks: "+ e.getMessage());
            }
        });
    }

    public static void removeFavoriteTrack(final String trackID){
        mDataBaseRef.child(USERS).child(User.instance.getID()).child(FAVORITE).setValue(User.instance.getFirebaseAdapter().getFavoriteTracks()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Database", "Failed to remove a track from the favorite tracks "+e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }

    public static void updateLikedTracks(final User user){
        mDataBaseRef.child(USERS).child(user.getID()).child(LIKES).setValue(user.getFirebaseAdapter().getLikedTracks()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Database", "Failed to upload the favorite tracks: "+ e.getMessage());
            }
        });
    }

    public static void updateCreatedTracks(final User user){
        mDataBaseRef.child(USERS).child(user.getID()).child(CREATE).setValue(user.getFirebaseAdapter().getCreatedTracks()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Database", "Failed to upload the created tracks: "+ e.getMessage());
            }
        });
    }
}
