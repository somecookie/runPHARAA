package ch.epfl.sweng.runpharaa;

import android.view.View;

public class FragmentFollowing extends UpdatableCardItemFragment {

    @Override
    protected void loadListWithData() {

    }

    @Override
    protected void setEmptyMessage() {
        emptyMessage.setText(R.string.no_follow);
        emptyMessage.setVisibility(View.VISIBLE);
    }
}
