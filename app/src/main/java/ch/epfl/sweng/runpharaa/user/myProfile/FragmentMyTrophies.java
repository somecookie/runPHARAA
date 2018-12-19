package ch.epfl.sweng.runpharaa.user.myProfile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.user.User;

public class FragmentMyTrophies extends Fragment {
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.trophies, container, false);

        ImageView i = v.findViewById(R.id.trophies_create);
        i.setOnClickListener(v -> {
            DialogFragment dialog = new DialogCreation();
            dialog.show(getFragmentManager(), "CreationDialog");
        });

        i = v.findViewById(R.id.trophies_like);
        i.setOnClickListener(v -> {
            DialogFragment dialog = new DialogLike();
            dialog.show(getFragmentManager(), "LikeDialog");
        });

        i = v.findViewById(R.id.trophies_favorite);
        i.setOnClickListener(v -> {
            DialogFragment dialog = new DialogFavorite();
            dialog.show(getFragmentManager(), "FavoriteDialog");
        });

        return v;
    }

    @Override
    public void onResume() {
        updateImages(v);
        super.onResume();
    }

    /**
     * Update images in view.
     *
     * @param v
     */
    private void updateImages(View v) {

        ImageView create = v.findViewById(R.id.trophies_create);
        ImageView like = v.findViewById(R.id.trophies_like);
        ImageView fav = v.findViewById(R.id.trophies_favorite);

        int nbCreated = User.instance.getCreatedTracks().size();

        setImages(nbCreated,R.drawable.create_one_track_reward, R.drawable.lock, create);

        UserDatabaseManagement.updateLikedTracks(User.instance);
        int nbLike = User.instance.getLikedTracks().size();

        setImages(nbLike,R.drawable.like_one_track_reward, R.drawable.lock, like);

        UserDatabaseManagement.updateFavoriteTracks(User.instance);
        int nbFav = User.instance.getFavoriteTracks().size();

        setImages(nbFav,R.drawable.favorite_one_track_reward, R.drawable.lock, fav);

    }

    /**
     * Set the images depending on the trophies the User gets
     *
     * @param param the number tested to see if the User has the trophy or not
     * @param image the trophy image
     * @param imageDefault the default lock image
     * @param v an ImageView
     */
    private void setImages(int param, int image, int imageDefault, ImageView v){
        if (param >= 10) {
            v.setImageResource(image);
        } else if (param >= 2) {
            v.setImageResource(image);
        } else if (param >= 1) {
            v.setImageResource(image);
        } else {
            v.setImageResource(imageDefault);
        }
    }
}



