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

        ImageView i = v.findViewById(R.id.imageViewCreate);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new DialogCreation();
                dialog.show(getFragmentManager(), "CreationDialog");
            }
        });

        updateImages();

        return v;
    }

    protected void updateImages(){
        if(User.instance.getCreatedTracks().size() >= 1){
            ImageView img= v.findViewById(R.id.trophy_one_track);
            img.setImageResource(R.drawable.create_one_track_reward);

            img= v.findViewById(R.id.imageViewCreate);
            img.setImageResource(R.drawable.create_one_track_reward);

        }
    }

}
