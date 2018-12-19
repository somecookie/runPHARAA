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

public class DialogCreation extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_creation_trophies, null);
        updateImages(v);
        builder.setView(v);
        builder.setTitle("Creation Trophies");
        return builder.create();
    }

    @Override
    public void onResume() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_creation_trophies, null);
        updateImages(view);
        super.onResume();
    }

    /**
     * Update images in view.
     *
     * @param v
     */
    private void updateImages(View v) {

        ImageView one = v.findViewById(R.id.trophy_one_track);
        ImageView two = v.findViewById(R.id.trophy_two_tracks);
        ImageView ten = v.findViewById(R.id.trophy_ten_tracks);

        int nbCreated = User.instance.getCreatedTracks().size();

        if (nbCreated >= 10) {
            one.setImageResource(R.drawable.create_one_track_reward);
            two.setImageResource(R.drawable.create_one_track_reward);
            ten.setImageResource(R.drawable.create_one_track_reward);
        } else if (nbCreated >= 2) {
            one.setImageResource(R.drawable.create_one_track_reward);
            two.setImageResource(R.drawable.create_one_track_reward);
            ten.setImageResource(R.drawable.lock);
        } else if (nbCreated >= 1) {
            one.setImageResource(R.drawable.create_one_track_reward);
            two.setImageResource(R.drawable.lock);
            ten.setImageResource(R.drawable.lock);
        } else {
            one.setImageResource(R.drawable.lock);
            two.setImageResource(R.drawable.lock);
            ten.setImageResource(R.drawable.lock);
        }

    }
}

