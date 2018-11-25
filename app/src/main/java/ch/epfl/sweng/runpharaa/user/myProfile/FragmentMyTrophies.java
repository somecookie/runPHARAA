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
}



