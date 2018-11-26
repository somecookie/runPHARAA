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

        if (nbCreated >= 10) {
            create.setImageResource(R.drawable.create_one_track_reward);
        } else if (nbCreated >= 2) {
            create.setImageResource(R.drawable.create_one_track_reward);
        } else if (nbCreated >= 1) {
            create.setImageResource(R.drawable.create_one_track_reward);
        } else {
            create.setImageResource(R.drawable.lock);
        }

        int nbLike = User.instance.getLikedTracks().size();

        if (nbLike >= 10) {
            like.setImageResource(R.drawable.like_one_track_reward);
        } else if (nbLike >= 2) {
            like.setImageResource(R.drawable.like_one_track_reward);
        } else if (nbLike >= 1) {
            like.setImageResource(R.drawable.like_one_track_reward);
        } else {
            like.setImageResource(R.drawable.lock);
        }

        int nbFav = User.instance.getLikedTracks().size();

        if (nbFav >= 10) {
            fav.setImageResource(R.drawable.favorite_one_track_reward);
        } else if (nbFav >= 2) {
            fav.setImageResource(R.drawable.favorite_one_track_reward);
        } else if (nbFav >= 1) {
            fav.setImageResource(R.drawable.favorite_one_track_reward);
        } else {
            fav.setImageResource(R.drawable.lock);
        }

    }
}



