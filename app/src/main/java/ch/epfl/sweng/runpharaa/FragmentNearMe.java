package ch.epfl.sweng.runpharaa;

import android.util.TypedValue;
import android.view.View;

import ch.epfl.sweng.runpharaa.tracks.Track;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class FragmentNearMe extends UpdatableCardItemFragment {

    @Override
    protected void loadListWithData() {
        // Add cards to the cardList
        if (User.instance != null) {
            for (Track t : User.instance.tracksNearMe())  //TODO: Change to interact with DB, here or in UpdatableCardItemFragment.
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
    /**
     * Create the recyclerView and the list of cardItem used to draw the list of tracks "near me".
     * This function is called when the fragment is created and each time the list is refreshed.
     */

    /*
    public void loadData() {
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
                List<Track> tracks = DatabaseManagement.initTracksNearMe(data);
                for (Track t : tracks) {
                    t.setCardItem(new CardItem(t.getLocation(), t.getTrackUid(), t.getImageStorageUri()));
                    listCardItem.add(t.getCardItem());
                }
                Adapter adapter = new Adapter(getActivity(), listCardItem, listener);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });
    }
    */