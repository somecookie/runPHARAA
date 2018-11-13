package ch.epfl.sweng.runpharaa.database;

import android.support.annotation.NonNull;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.UsersProfileActivity;
import ch.epfl.sweng.runpharaa.utils.Callback;

public class UserDatabaseManagement extends DatabaseManagement {
    private final static String USERS = "users";
    private final static String FAVORITE = "favoriteTracks";
    private final static String LIKES = "likedTracks";
    private final static String CREATE = "createdTracks";
    private final static String FOLLOWING = "followedUsers";

    public static void writeNewUser(final User user, final Callback<User> callback){
        Log.i("KWAY", user.getUid()+" "+user.getName());
        DatabaseReference usersRef = mDataBaseRef.child(USERS).child(user.getUid());
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    User storedUser = dataSnapshot.getValue(User.class);
                    callback.onSuccess(storedUser);

                }else{
                    DatabaseReference userRef = mDataBaseRef.child(USERS).child(user.getUid());
                    userRef.setValue(user).addOnSuccessListener(aVoid -> callback.onSuccess(user)).addOnFailureListener(callback::onError);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getDetails());
            }
        });
    }

    public static void updateFavoriteTracks(final User user){
        DatabaseReference favRef = mDataBaseRef.child(USERS).child(user.getUid()).child(FAVORITE);
        favRef.setValue(user.getFavoriteTracks()).addOnFailureListener(Throwable::printStackTrace);
    }

    public static void removeFavoriteTrack(final String trackID) {
        DatabaseReference favRef = mDataBaseRef.child(USERS).child(User.instance.getUid()).child(FAVORITE).child(trackID);
        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) dataSnapshot.getRef().removeValue().addOnFailureListener(Throwable::printStackTrace);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getDetails());
            }
        });
    }

    public static void updateLikedTracks(final User user) {
        DatabaseReference likedRef = mDataBaseRef.child(USERS).child(user.getUid()).child(LIKES);
        likedRef.setValue(user.getLikedTracks()).addOnFailureListener(Throwable::printStackTrace);
    }

    public static void removeLikedTrack(final String trackID) {
        DatabaseReference likeRef = mDataBaseRef.child(USERS).child(User.instance.getUid()).child(FAVORITE).child(trackID);
        likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) dataSnapshot.getRef().removeValue().addOnFailureListener(Throwable::printStackTrace);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getDetails());
            }
        });
    }

    public static void updateCreatedTracks(final String trackID) {
        DatabaseReference createRef = mDataBaseRef.child(USERS).child(User.instance.getUid()).child(CREATE).child(trackID);
        createRef.setValue(trackID).addOnFailureListener(Throwable::printStackTrace);
    }

    public static void updateFollowedUsers(final User user) {
        DatabaseReference followedRef = mDataBaseRef.child(USERS).child(user.getUid()).child(FOLLOWING);
        followedRef.setValue(user.getFollowedUsers()).addOnFailureListener(Throwable::printStackTrace);
    }

    public static void removeFollowedUser(final User user) {
        DatabaseReference followedRef = mDataBaseRef.child(USERS).child(User.instance.getUid()).child(FOLLOWING);

        followedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        User userFromDatabase = null;
                        if (child.getValue() != null) {
                            userFromDatabase = User.deserialize(child.getValue().toString());
                        }
                        if (userFromDatabase != null && userFromDatabase.getUid().equals(user.getUid())) {
                            child.getRef().removeValue().addOnFailureListener(Throwable::printStackTrace);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getDetails());
            }
        });
    }

}
