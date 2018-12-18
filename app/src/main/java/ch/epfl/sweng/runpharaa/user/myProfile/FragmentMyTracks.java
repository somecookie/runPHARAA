package ch.epfl.sweng.runpharaa.user.myProfile;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.TrackPropertiesActivity;
import ch.epfl.sweng.runpharaa.UpdatableCardItemFragment;
import ch.epfl.sweng.runpharaa.database.TrackDatabaseManagement;
import ch.epfl.sweng.runpharaa.gui.TrackCardItem;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.user.AdapterTracksToRecyclerViewItem;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Callback;

public final class FragmentMyTracks extends UpdatableCardItemFragment {

    @Override
    protected void setEmptyMessage() {
        emptyMessage.setText(R.string.no_created_self);
        emptyMessage.setTextColor(getResources().getColor(R.color.text_dark));
        emptyMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    protected void loadData() {
        // Load User's createdTracks
        TrackDatabaseManagement.mReadDataOnce(TrackDatabaseManagement.TRACKS_PATH, new Callback<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot value) {
                RecyclerView recyclerView = v.findViewById(R.id.cardListId);
                List<TrackCardItem> createdTracks = new ArrayList<>();
                AdapterTracksToRecyclerViewItem.OnItemClickListener listener = item -> {
                    Intent intent = new Intent(getContext(), TrackPropertiesActivity.class);
                    intent.putExtra("TrackID", item.getParentTrackID());
                    startActivity(intent);
                };
                List<Track> tracks = TrackDatabaseManagement.initCreatedTracks(value, User.instance);
                for (Track t : tracks) {
                    t.setTrackCardItem(new TrackCardItem(t.getName(), t.getTrackUid(), t.getImageStorageUri()));
                    createdTracks.add(t.getTrackCardItem());
                }
                AdapterTracksToRecyclerViewItem adapter = new AdapterTracksToRecyclerViewItem(getContext(), createdTracks, listener);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                if (createdTracks.isEmpty()) setEmptyMessage();

                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onError(Exception e) {
                Log.d("DB Read: ", "Failed to read data from DB in UserProfileActivity.");
                setEmptyMessage();
                swipeLayout.setRefreshing(false);
            }
        });
    }
}
