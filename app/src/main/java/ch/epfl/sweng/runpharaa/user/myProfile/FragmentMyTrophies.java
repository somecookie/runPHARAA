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
import ch.epfl.sweng.runpharaa.user.User;

public class FragmentMyTrophies extends Fragment {
    protected View v;

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

    private void updateImages(View v) {

        ImageView create = v.findViewById(R.id.trophies_create);
        ImageView like = v.findViewById(R.id.trophies_like);
        ImageView fav = v.findViewById(R.id.trophies_favorite);

        int nbCreated = User.instance.getCreatedTracks().size();

        setImages(nbCreated,R.drawable.create_one_track_reward,create);

        int nbLike = User.instance.getLikedTracks().size();

        setImages(nbLike,R.drawable.like_one_track_reward,like);

        int nbFav = User.instance.getLikedTracks().size();

        setImages(nbFav,R.drawable.favorite_one_track_reward,fav);

    }


    private void setImages(int param, int image, ImageView v){
        if (param >= 10) {
            v.setImageResource(image);
        } else if (param >= 2) {
            v.setImageResource(image);
        } else if (param >= 1) {
            v.setImageResource(image);
        } else {
            v.setImageResource(image);
        }
    }
}



