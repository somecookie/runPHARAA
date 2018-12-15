package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.gui.CardItem;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.gui.UserCardItem;
import ch.epfl.sweng.runpharaa.user.myProfile.UsersProfileActivity;
import ch.epfl.sweng.runpharaa.user.otherProfile.OtherUsersProfileActivity;
import ch.epfl.sweng.runpharaa.utils.Callback;

public class FragmentFollowing extends UpdatableUserCardItemFragment {

    public FragmentFollowing() {}

    @Override
    protected void setEmptyMessage() {
        emptyMessage.setText(R.string.no_follow);
        emptyMessage.setVisibility(View.VISIBLE);
    }

    /**
     * Create the recyclerView and the list of cardItem used to draw the list of tracks "near me".
     * This function is called when the fragment is created and each time the list is refreshed.
     */
    @Override
    protected void loadData() {
        emptyMessage.setVisibility(View.GONE);
        // Create a fresh recyclerView and listCardItem

        UserDatabaseManagement.mReadDataOnce(UserDatabaseManagement.USERS, new Callback<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot data) {
                RecyclerView recyclerView = v.findViewById(R.id.cardListId);
                List<UserCardItem> listUserCardItem = new ArrayList<>();
                OnItemClickListener listener = new OnItemClickListener() {
                    @Override
                    public void onItemClick(CardItem item) {
                        Intent intent;
                        if (!((UserCardItem)item).getParentUserID().equals(User.instance.getUid())) {
                            intent = new Intent(getContext(), OtherUsersProfileActivity.class);
                        } else {
                            intent = new Intent(getContext(), UsersProfileActivity.class);
                        }
                        intent.putExtra("userId", ((UserCardItem)item).getParentUserID());
                        startActivity(intent);
                    }
                };

                List<User> users = UserDatabaseManagement.initFollowingFragment(data);
                for (User u : users) {
                    if(u == null) continue; // don't load the users that have changed or were deleted
                    u.setUserCardItem(new UserCardItem(u.getName(), u.getUid(), u.getPicture(), u.getCreatedTracks().size()));
                    listUserCardItem.add(u.getUserCardItem());
                }
                Adapter adapter = new Adapter(getActivity(), listUserCardItem, listener);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                if (listUserCardItem.isEmpty())
                    setEmptyMessage();

                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onError(Exception databaseError) {
                Log.d("DB Read: ", "Failed to read data from DB in FragmentFavourites.");
                setEmptyMessage();
                swipeLayout.setRefreshing(false);
            }
        });
    }
}
