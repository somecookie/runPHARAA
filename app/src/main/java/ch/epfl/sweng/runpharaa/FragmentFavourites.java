package ch.epfl.sweng.runpharaa;

import android.util.TypedValue;
import android.view.View;

import ch.epfl.sweng.runpharaa.tracks.Track;

public final class FragmentFavourites extends UpdatableCardItemFragment {

    @Override
    protected void loadListWithData() {
        // Add cards to the cardList
        for (String i : User.instance.getFavoritesTracksKeys())
            for (Track t : Track.allTracks)
                if (t.getTrackUid() == i)
                    listCardItem.add(t.getCardItem());
    }

    @Override
    protected void setEmptyMessage() {
        emptyMessage.setText(R.string.no_favorite);
        emptyMessage.setVisibility(View.VISIBLE);
    }
}
