package ch.epfl.sweng.runpharaa;

import android.util.TypedValue;
import android.view.View;

import ch.epfl.sweng.runpharaa.tracks.Track;

public final class FragmentNearMe extends UpdatableCardItemFragment {

    @Override
    protected void loadListWithData() {
        // Add cards to the cardList
        if (User.instance != null) {
            for (Track t : User.instance.tracksNearMe())
                // Add cards to the cardList
                listCardItem.add(t.getCardItem());

        }
    }

    @Override
    protected void setEmptyMessage() {
        emptyMessage.setText(R.string.no_tracks);
        emptyMessage.setVisibility(View.VISIBLE);
    }
}