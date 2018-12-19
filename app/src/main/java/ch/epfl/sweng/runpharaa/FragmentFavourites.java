package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.database.firebase.TrackDatabaseManagement;
import ch.epfl.sweng.runpharaa.gui.CardItem;
import ch.epfl.sweng.runpharaa.gui.TrackCardItem;
import ch.epfl.sweng.runpharaa.gui.UpdatableCardItemFragment;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.properties.TrackPropertiesActivity;
import ch.epfl.sweng.runpharaa.utils.Callback;

public class FragmentFavourites extends UpdatableCardItemFragment {

    public FragmentFavourites() {
    }

    @Override
    protected void setEmptyMessage() {
        emptyMessage.setText(R.string.no_favorite);
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

        TrackDatabaseManagement.mReadDataOnce(TrackDatabaseManagement.TRACKS_PATH, new Callback<DataSnapshot>() {

            @Override
            public void onSuccess(DataSnapshot data) {
                RecyclerView recyclerView = v.findViewById(R.id.cardListId);
                List<CardItem> listTrackCardItem = new ArrayList<>();
                OnItemClickListener listener = new OnItemClickListener() {
                    @Override
                    public void onItemClick(CardItem item) {
                        Intent intent = new Intent(getContext(), TrackPropertiesActivity.class);
                        intent.putExtra("TrackID", ((TrackCardItem) item).getParentTrackID());
                        startActivity(intent);
                    }
                };
                List<Track> tracks = TrackDatabaseManagement.initFavouritesTracks(data);
                for (Track t : tracks) {
                    t.setTrackCardItem(new TrackCardItem(t.getName(), t.getTrackUid(), t.getImageStorageUri()));
                    listTrackCardItem.add(t.getTrackCardItem());
                }
                Adapter adapter = new Adapter(getActivity(), listTrackCardItem, listener);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                if (listTrackCardItem.isEmpty())
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