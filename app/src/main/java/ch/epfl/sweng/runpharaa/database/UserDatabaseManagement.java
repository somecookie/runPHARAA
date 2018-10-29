package ch.epfl.sweng.runpharaa.database;

import com.google.firebase.database.DatabaseReference;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Callback;

public class UserDatabaseManagement extends DatabaseManagement {
    private final static String USERS = "users";
    private final static String FAVORITE = "favoriteTracks";
    private final static String LIKES = "likedTracks";
    private final static String CREATE = "createdTracks";

    public static void writeNewUser(final User user, final Callback<User> callback) {
        DatabaseReference userRef = mDataBaseRef.child(USERS).child(user.getID());
        userRef.setValue(user.getFirebaseAdapter()).addOnSuccessListener(aVoid -> callback.onSuccess(user)).addOnFailureListener(callback::onError);
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
}
