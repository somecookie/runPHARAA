package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.database.TrackDatabaseManagement;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Required;

public class FragmentNearMe extends UpdatableCardItemFragment {


    public FragmentNearMe(){
    }

    protected void setEmptyMessage() {
        emptyMessage.setText(R.string.no_tracks);
        emptyMessage.setVisibility(View.VISIBLE);
    }


    /**
     * Create the recyclerView and the list of cardItem used to draw the list of tracks "near me".
     * This function is called when the fragment is created and each time the list is refreshed.
     */
    protected void loadData() {
        emptyMessage.setVisibility(View.GONE);
        // Create a fresh recyclerView and listCardItem
        String s = TrackDatabaseManagement.TRACKS_PATH;

        TrackDatabaseManagement.OnGetDataListener d = new TrackDatabaseManagement.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot data) {
                RecyclerView recyclerView = v.findViewById(R.id.cardListId);
                List<CardItem> listCardItem = new ArrayList<>();

                List<Track> tracks = TrackDatabaseManagement.initTracksNearLocation(data, User.instance.getLocation());

                for (Track t : tracks) {
                    if(MainActivity.passFilters(t)){
                        t.setCardItem(new CardItem(t.getName(), t.getTrackUid(), t.getImageStorageUri()));
                        listCardItem.add(t.getCardItem());
                    }
                }
                OnItemClickListener listener = item -> {
                    Intent intent = new Intent(getContext(), TrackPropertiesActivity.class);
                    intent.putExtra("TrackID", item.getParentTrackID());
                    startActivity(intent);
                };

                Adapter adapter = new Adapter(getActivity(), listCardItem, listener);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                if (listCardItem.isEmpty()) {
                    setEmptyMessage();
                }

                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                Log.d("DB Read: ", "Failed to read data from DB in FragmentNearMe.");
                setEmptyMessage();
                swipeLayout.setRefreshing(false);
            }
        };

        TrackDatabaseManagement.mReadDataOnce(s, d);
    }
}