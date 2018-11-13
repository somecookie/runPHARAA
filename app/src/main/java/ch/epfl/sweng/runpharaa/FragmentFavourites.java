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

import ch.epfl.sweng.runpharaa.database.DatabaseManagement;
import ch.epfl.sweng.runpharaa.tracks.Track;

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

        DatabaseManagement.mReadDataOnce(DatabaseManagement.TRACKS_PATH, new DatabaseManagement.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot data) {
                RecyclerView recyclerView = v.findViewById(R.id.cardListId);
                List<CardItem> listCardItem = new ArrayList<>();
                OnItemClickListener listener = new OnItemClickListener() {
                    @Override
                    public void onItemClick(CardItem item) {
                        Intent intent = new Intent(getContext(), TrackPropertiesActivity.class);
                        intent.putExtra("TrackID", item.getParentTrackID());
                        startActivity(intent);
                    }
                };
                List<Track> tracks = DatabaseManagement.initFavouritesTracks(data);
                for (Track t : tracks) {
                    t.setCardItem(new CardItem(t.getName(), t.getTrackUid(), t.getImageStorageUri()));
                    listCardItem.add(t.getCardItem());
                }
                Adapter adapter = new Adapter(getActivity(), listCardItem, listener);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                if (listCardItem.isEmpty())
                    setEmptyMessage();
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                Log.d("DB Read: ", "Failed to read data from DB in FragmentFavourites.");
                setEmptyMessage();
            }
        });
    }
}