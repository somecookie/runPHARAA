package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;
import java.util.Random;

import ch.epfl.sweng.runpharaa.database.TrackDatabaseManagement;
import ch.epfl.sweng.runpharaa.tracks.FilterProperties;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.myProfile.UsersProfileActivity;
import ch.epfl.sweng.runpharaa.user.otherProfile.OtherUsersProfileActivity;
import ch.epfl.sweng.runpharaa.utils.Callback;
import ch.epfl.sweng.runpharaa.utils.Util;

public class FragmentSearch extends Fragment {
    private View v;
    private ToggleButton searchToggle; // if false searches for tracks, if true searches for users
    private Menu menu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.search_fragment, container, false);
        searchToggle = v.findViewById(R.id.toggle_button);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.actionbar_menu_search, menu);
        initSearch(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.luckyIcon){
            FilterProperties properties = new FilterProperties();
            TrackDatabaseManagement.mReadDataOnce(TrackDatabaseManagement.TRACKS_PATH, new Callback<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot value) {
                    List<Track> nearMe = TrackDatabaseManagement.initTracksNearLocation(value, User.instance.getLocation());
                    if(nearMe.isEmpty()) {
                        Toast.makeText(getContext(),R.string.no_tracks, Toast.LENGTH_LONG).show();
                        return;
                    }
                    List<Track> favorites = TrackDatabaseManagement.initFavouritesTracks(value);
                    if(favorites.isEmpty()){
                        List<Track> liked = TrackDatabaseManagement.initCreatedTracks(value, User.instance);
                        if(liked.isEmpty()) {
                            Toast.makeText(getContext(),R.string.no_favorites_and_likes, Toast.LENGTH_LONG).show();
                            Random r = new Random();
                            startTrackPropertiesWith(nearMe.get(r.nextInt(nearMe.size())).getTrackUid());
                        }
                        else {
                            properties.add(liked);
                            startTrackPropertiesWith(properties.chooseLuckyTrack(nearMe).getTrackUid());
                        }
                    } else {
                        properties.add(favorites);
                        startTrackPropertiesWith(properties.chooseLuckyTrack(nearMe).getTrackUid());
                    }
                }
            });
        }
        return true;
    }

    private void startTrackPropertiesWith(String trackUID){
        Intent intent = new Intent(getContext(), TrackPropertiesActivity.class);
        intent.putExtra("TrackID", trackUID);
        startActivity(intent);
    }

    private void initSearch(Menu menu) {
        MenuItem item = menu.findItem(R.id.searchIcon);
        SearchView sv = (SearchView) item.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchToggle.isChecked()) {
                    TrackDatabaseManagement.findTrackUIDByName(query, new Callback<String>() {
                        @Override
                        public void onSuccess(String value) {
                            if (value == null) {
                                Toast.makeText(getContext(), String.format(getResources().getString(R.string.no_track_found), query), Toast.LENGTH_LONG).show();
                            } else {
                                startTrackPropertiesWith(value);
                            }
                        }
                    });
                } else {
                    // If user searches for himself, directly open his/hers profile
                    if (Util.formatString(query).equals(Util.formatString(User.instance.getName()))) {
                        Intent i = new Intent(getContext(), UsersProfileActivity.class);
                        startActivity(i);
                    } else {
                        UserDatabaseManagement.findUserUIDByName(query, new Callback<String>() {
                            @Override
                            public void onSuccess(String value) {
                                if (value == null) {
                                    Toast.makeText(getContext(), String.format(getResources().getString(R.string.no_user_found), query), Toast.LENGTH_LONG).show();
                                } else {
                                    Intent i = new Intent(getContext(), OtherUsersProfileActivity.class);
                                    i.putExtra("userId", value);
                                    startActivity(i);
                                }
                            }
                        });
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        sv.setIconifiedByDefault(true);
        sv.setFocusable(true);
        sv.setIconified(false);
        sv.requestFocusFromTouch();
    }

    public Menu getMenu() {
        return menu;
    }
}