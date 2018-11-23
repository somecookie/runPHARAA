package ch.epfl.sweng.runpharaa.user.myProfile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
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
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new DialogCreation();
                dialog.show(getFragmentManager(), "CreationDialog");
                //((DialogCreation) dialog).updateImages();
            }
        });

        i = v.findViewById(R.id.trophies_like);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new DialogLike();
                dialog.show(getFragmentManager(), "LikeDialog");
                //((DialogCreation) dialog).updateImages();
            }
        });

        i = v.findViewById(R.id.trophies_favorite);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new DialogFavorite();
                dialog.show(getFragmentManager(), "FavoriteDialog");
                //((DialogCreation) dialog).updateImages();
            }
        });

        updateImages();
        return v;
    }

    @Override
    public void onResume() {
        updateImages();
        super.onResume();
    }

    private void updateImages(){
            final View mView = getLayoutInflater().inflate(R.layout.trophies, null);
            ImageView img= mView.findViewById(R.id.trophies_create);
            if(User.instance.getCreatedTracks().size() >= 1){
                img.setImageResource(R.drawable.create_one_track_reward);
            } else {
                img.setImageResource(R.drawable.lock);
            }

            final View vv = getLayoutInflater().inflate(R.layout.dialog_creation_trophies, null);
            ImageView i = vv.findViewById(R.id.trophy_one_track);
            i.setImageResource(R.drawable.create_one_track_reward);
    }
}



