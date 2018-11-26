package ch.epfl.sweng.runpharaa.user.myProfile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.user.User;

public class DialogFavorite extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_favorite_trophies, null));
        builder.setTitle("Creation Trophies");
        return builder.create();
    }

    @Override
    public void onResume() {
        updateImages();
        super.onResume();
    }

    private void updateImages(){
        final View mView = getLayoutInflater().inflate(R.layout.trophies, null);
        final View vv = getLayoutInflater().inflate(R.layout.dialog_favorite_trophies, null);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView img = mView.findViewById(R.id.trophies_favorite);
                ImageView i = vv.findViewById(R.id.trophy_one_fav);
                if(User.instance.getFavoriteTracks().size() >= 1){
                    img.setImageResource(R.drawable.create_one_track_reward);
                    i.setImageDrawable(getResources().getDrawable(R.drawable.create_one_track_reward));
                } else {
                    img.setImageResource(R.drawable.lock);
                    i.setImageResource(R.drawable.lock);
                }
            }
        });

    }
}
