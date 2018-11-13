package ch.epfl.sweng.runpharaa;

import android.view.View;

public class FragmentFollowing extends UpdatableCardItemFragment {

    public FragmentFollowing() {
    }

    @Override
    protected void setEmptyMessage() {
        emptyMessage.setText(R.string.no_follow);
        emptyMessage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void loadData() {
        setEmptyMessage();
    }
}
