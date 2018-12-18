package ch.epfl.sweng.runpharaa.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Callback;
import ch.epfl.sweng.runpharaa.utils.Config;

import static ch.epfl.sweng.runpharaa.utils.Util.formatString;

public class UserDatabaseManagement extends TrackDatabaseManagement {
    public final static String USERS = "users";
    private final static String NAME = "name";
    private final static String FAVORITE = "favoriteTracks";
    private final static String LIKES = "likedTracks";
    private final static String CREATE = "createdTracks";
    private final static String FOLLOWING = "followedUsers";
    private final static String PICTURE = "picture";
    private final static String ID = "uid";
    private static final String FEEDBACK = "feedback";

    public static void writeNewUser(final User user, final Callback<User> callback) {
        DatabaseReference usersRef = mDataBaseRef.child(USERS).child(user.getUid());
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User storedUser = dataSnapshot.getValue(User.class);
                    assert storedUser != null;

                    if (!dataSnapshot.child(LIKES).exists())
                        storedUser.setLikedTracks(new ArrayList<>());
                    if (!dataSnapshot.child(CREATE).exists())
                        storedUser.setCreatedTracks(new ArrayList<>());
                    if (!dataSnapshot.child(FAVORITE).exists())
                        storedUser.setFavoriteTracks(new ArrayList<>());
                    if (!dataSnapshot.child(FOLLOWING).exists())
                        storedUser.setFollowedUsers(new ArrayList<>());
                    if(!dataSnapshot.child(FEEDBACK).exists())
                        storedUser.setFeedbackTracks(new ArrayList<>());

                    callback.onSuccess(storedUser);

                } else {
                    DatabaseReference userRef = mDataBaseRef.child(USERS).child(user.getUid());
                    userRef.setValue(user).addOnSuccessListener(aVoid -> callback.onSuccess(user)).addOnFailureListener(callback::onError);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO:Don't forget to change back -> try for Travis
                //Log.e("DatabaseError", databaseError.getDetails());
                Log.e("DatabaseError", "error");
            }
        });
    }

    public static void updateFavoriteTracks(final User user) {
        DatabaseReference favRef = mDataBaseRef.child(USERS);
        DatabaseReference favRef2 = favRef.child(user.getUid());
        DatabaseReference favRef3 = favRef2.child(FAVORITE);
        mDataBaseRef.child(TRACKS_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    user.setFavoriteTracks(TrackDatabaseManagement.deleteDeletedTrackFromList(dataSnapshot, user.getFavoriteTracks()));
                    Task<Void> t = favRef3.setValue(user.getFavoriteTracks());
                    t.addOnFailureListener(Throwable::printStackTrace);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getDetails());
            }
        });

    }

    public static void removeFavoriteTrack(final String trackID) {
        DatabaseReference favRef = mDataBaseRef.child(USERS).child(User.instance.getUid()).child(FAVORITE).child(trackID);
        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    dataSnapshot.getRef().removeValue().addOnFailureListener(Throwable::printStackTrace);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getDetails());
            }
        });
    }

    public static void updateLikedTracks(final User user) {
        DatabaseReference likedRef = mDataBaseRef.child(USERS).child(user.getUid()).child(LIKES);
        mDataBaseRef.child(TRACKS_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    user.setLikedTracks(TrackDatabaseManagement.deleteDeletedTrackFromList(dataSnapshot, user.getLikedTracks()));
                    likedRef.setValue(user.getLikedTracks()).addOnFailureListener(Throwable::printStackTrace);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getDetails());
            }
        });
    }

    public static void removeLikedTrack(final String trackID) {
        DatabaseReference likeRef = mDataBaseRef.child(USERS).child(User.instance.getUid()).child(FAVORITE).child(trackID);
        likeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    dataSnapshot.getRef().removeValue().addOnFailureListener(Throwable::printStackTrace);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getDetails());
            }
        });
    }

    public static void updateCreatedTracks(final User user) {
        DatabaseReference createRef = mDataBaseRef.child(USERS).child(user.getUid()).child(CREATE);
        mDataBaseRef.child(TRACKS_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    user.setCreatedTracks(TrackDatabaseManagement.deleteDeletedTrackFromList(dataSnapshot, user.getCreatedTracks()));
                    createRef.setValue(user.getCreatedTracks()).addOnFailureListener(Throwable::printStackTrace);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getDetails());
            }
        });

    }

    public static void updateFollowedUsers(final User user) {
        DatabaseReference followedRef = mDataBaseRef.child(USERS).child(user.getUid()).child(FOLLOWING);
        followedRef.setValue(user.getFollowedUsers()).addOnFailureListener(Throwable::printStackTrace);
    }

    public static void removeFollowedUser(final User user) {
        DatabaseReference followedRef = mDataBaseRef.child(USERS).child(user.getUid()).child(FOLLOWING);

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

    public static List<User> initFollowingFragment(DataSnapshot data) {
        List<User> users = new ArrayList<>();
        DataSnapshot followed = data.child(User.instance.getUid()).child(FOLLOWING);
        for (DataSnapshot f : followed.getChildren()) {
            if (f.getValue() != null) {
                User user = User.deserialize(f.getValue().toString());
                if(!data.child(user.getUid()).exists()){
                    User.instance.removeFromFollowed(user);
                }
                else{
                    users.add(User.deserialize(f.getValue().toString()));
                }
            }
        }
        UserDatabaseManagement.updateFollowedUsers(User.instance);
        return users;
    }

    public static User getUser(DataSnapshot data, String uId) {
        User user = null;
        DataSnapshot u = data.child(uId);
        if (u != null && u.exists()){
            user = new User();
            user.setUid(uId);
            if (u.child(NAME).exists()) {
                user.setName(u.child(NAME).getValue().toString());
            }
            List<String> createdTracks = new ArrayList<>();
            if(!Config.isTest){
                for (DataSnapshot c : u.child(CREATE).getChildren()) {
                    createdTracks.add(c.getValue().toString());
                }
            }
            user.setCreatedTracks(createdTracks);
            List<String> favoriteTracks = new ArrayList<>();
            if(!Config.isTest){
                for (DataSnapshot c : u.child(FAVORITE).getChildren()) {
                    favoriteTracks.add(c.getValue().toString());
                }
            }
            user.setFavoriteTracks(favoriteTracks);
            if(!Config.isTest){
                if (u.child(PICTURE).exists()) {
                    user.setPicture(u.child(PICTURE).getValue().toString());
                }
            }
        }
        return user;
    }

    public static void getUserNameFromID(String UID, Callback<String> callback) {
        DatabaseReference nameRef = mDataBaseRef.child(USERS).child(UID).child(NAME);
        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.getValue(String.class);
                    callback.onSuccess(name);
                    return;
                }

                callback.onSuccess(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getDetails());
            }
        });
    }

    public static void getUserPictureFromID(String UID, Callback<String> callback) {
        DatabaseReference pictureRef = mDataBaseRef.child(USERS).child(UID).child(PICTURE);
        pictureRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String picture = dataSnapshot.getValue(String.class);
                    callback.onSuccess(picture);
                    return;
                }

                callback.onSuccess(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getDetails());
            }
        });
    }


    public static void findUserUIDByName(final String name, Callback<String> callback) {
        DatabaseReference ref = mDataBaseRef.child(USERS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String dataName = formatString(data.child(NAME).getValue(String.class));
                    String formattedName = formatString(name);

                    if (dataName.equals(formattedName)) {
                        String id = data.child(ID).getValue(String.class);

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


    public static void deleteUser(User user) {
        //Delete all created tracks
        for (String trackUID : user.getCreatedTracks()) {
            TrackDatabaseManagement.deleteTrack(trackUID);
        }
        //Set track to deleted
        mDataBaseRef.child(USERS).child(user.getUid()).removeValue();
    }
    public static void updateFeedBackTracks(User user) {
        DatabaseReference createRef = mDataBaseRef.child(USERS).child(user.getUid()).child(FEEDBACK);
        createRef.setValue(user.getCreatedTracks()).addOnFailureListener(Throwable::printStackTrace);
    }

    public static void writeNotificationKey(String key){
        DatabaseReference keyRef = mDataBaseRef.child(USERS).child(User.instance.getUid()).child("NotificationKey");
        keyRef.setValue(key).addOnFailureListener(Throwable::printStackTrace);

    }

    public static void getNotificationKeyFromUID(String uid, Callback<String> callback){
        DatabaseReference nameRef = mDataBaseRef.child(USERS).child(uid).child("NotificationKey");
        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.getValue(String.class);
                    callback.onSuccess(name);
                    return;
                }

                callback.onSuccess(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getDetails());
            }
        });
    }
}
