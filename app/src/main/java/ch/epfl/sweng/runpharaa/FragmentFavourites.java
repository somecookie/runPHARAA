package ch.epfl.sweng.runpharaa;

public final class FragmentFavourites extends UpdatableCardItemFragment {

    @Override
    protected void loadListWithData() {
        // Add cards to the cardList
        for (Integer i : User.instance.getFavorites())
            for (Track t : Track.allTracks())
                if (t.getUid() == i)
                    listCardItem.add(t.getCardItem());
    }
}
