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
        View v = inflater.inflate(R.layout.dialog_favorite_trophies, null);
        updateImages(v);
        builder.setView(v);
        builder.setTitle("Favorite Trophies");
        return builder.create();
    }

    @Override
    public void onResume() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_favorite_trophies, null);
        updateImages(view);
        super.onResume();
    }

    private void updateImages(View v) {

        ImageView one = v.findViewById(R.id.trophy_one_fav);
        ImageView two = v.findViewById(R.id.trophy_two_fav);
        ImageView ten = v.findViewById(R.id.trophy_ten_fav);

        int nbFav = User.instance.getFavoriteTracks().size();

        if (nbFav >= 10) {
            one.setImageResource(R.drawable.favorite_one_track_reward);
            two.setImageResource(R.drawable.favorite_one_track_reward);
            ten.setImageResource(R.drawable.favorite_one_track_reward);
        } else if (nbFav >= 2) {
            one.setImageResource(R.drawable.favorite_one_track_reward);
            two.setImageResource(R.drawable.favorite_one_track_reward);
            ten.setImageResource(R.drawable.lock);
        } else if (nbFav >= 1) {
            one.setImageResource(R.drawable.favorite_one_track_reward);
            two.setImageResource(R.drawable.lock);
            ten.setImageResource(R.drawable.lock);
        } else {
            one.setImageResource(R.drawable.lock);
            two.setImageResource(R.drawable.lock);
            ten.setImageResource(R.drawable.lock);
        }

    }
}
