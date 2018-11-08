package ch.epfl.sweng.runpharaa.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Callback;

public class UserDatabaseManagement extends DatabaseManagement {
    private final static String USERS = "users";
    private final static String FAVORITE = "favoriteTracks";
    private final static String LIKES = "likedTracks";
    private final static String CREATE = "createdTracks";

    public static void writeNewUser(final User user, final Callback<User> callback) {
        DatabaseReference usersRef = mDataBaseRef.child(USERS);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user.getID()).exists()){
                    downloadUser(user.getID(), callback);
                }else{
                    DatabaseReference userRef = mDataBaseRef.child(USERS).child(user.getID());
                    userRef.setValue(user.getFirebaseAdapter()).addOnSuccessListener(aVoid -> callback.onSuccess(user)).addOnFailureListener(callback::onError);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void updateFavoriteTracks(final User user) {
        DatabaseReference favRef = mDataBaseRef.child(USERS).child(user.getID()).child(FAVORITE);

        for (String fav : user.getFavoriteTracks()) {
            favRef.child(fav).setValue(fav);
        }
    }

    public static void removeFavoriteTrack(final String trackID) {
        DatabaseReference favRef = mDataBaseRef.child(USERS).child(User.instance.getID()).child(FAVORITE).child(trackID);
        favRef.removeValue().addOnFailureListener(Throwable::printStackTrace);
    }

    public static void updateLikedTracks(final User user) {
        DatabaseReference likedRef = mDataBaseRef.child(USERS).child(user.getID()).child(LIKES);

        for (String like : user.getLikedTracks()) {
            likedRef.child(like).setValue(like);
        }
    }

    public static void removeLikedTrack(final String trackID) {
        DatabaseReference likedRef = mDataBaseRef.child(USERS).child(User.instance.getID()).child(LIKES).child(trackID);
        likedRef.removeValue().addOnFailureListener(Throwable::printStackTrace);
    }


    public static void updateCreatedTracks(final String trackID) {
        DatabaseReference createRef = mDataBaseRef.child(USERS).child(User.instance.getID()).child(CREATE).child(trackID);
        createRef.setValue(trackID).addOnFailureListener(Throwable::printStackTrace);
    }

    public static void downloadUser(final String UID, final Callback<User> whenFinishedCallback){
        downloadUserTracks(UID, CREATE, new Callback<Set<String>>() {
            @Override
            public void onSuccess(Set<String> createdTracks) {
                downloadUserTracks(UID, FAVORITE, new Callback<Set<String>>() {
                    @Override
                    public void onSuccess(Set<String> favoriteTracks) {
                        downloadUserTracks(UID, LIKES, new Callback<Set<String>>() {
                            @Override
                            public void onSuccess(Set<String> likedTracks) {
                                User.instance.setCreatedTracks(createdTracks);
                                User.instance.setFavoriteTracks(favoriteTracks);
                                User.instance.setLikedTracks(likedTracks);
                                whenFinishedCallback.onSuccess(User.instance);
                            }
                        });
                    }
                });
            }
        });

    }


    private static void downloadUserTracks(String UID, String type, Callback<Set<String>> callback) {

        DatabaseReference createRef = mDataBaseRef.child(USERS).child(UID).child(type);
        createRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> createdTracks = new HashSet<>();
                for(DataSnapshot createdSnapshot: dataSnapshot.getChildren()){
                    Log.i("AfricaToto", type+" adding "+ createdSnapshot.getKey());
                    createdTracks.add(createdSnapshot.getKey());
                }

                callback.onSuccess(createdTracks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(new RuntimeException("The database could not download the created tracks"));
            }
        });
    }
}
