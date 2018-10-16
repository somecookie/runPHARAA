package ch.epfl.sweng.runpharaa;

public final class FragmentNearMe extends UpdatableCardItemFragment {

    @Override
    protected void loadListWithData() {
        // Add cards to the cardList
        if (User.instance != null) {
            for (Track t : User.instance.tracksNearMe())
                listCardItem.add(t.getCardItem());
        }
    }
}