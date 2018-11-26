package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ToggleButton;

import ch.epfl.sweng.runpharaa.database.TrackDatabaseManagement;
import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.user.myProfile.UsersProfileActivity;
import ch.epfl.sweng.runpharaa.user.otherProfile.OtherUsersProfileActivity;
import ch.epfl.sweng.runpharaa.utils.Callback;
import ch.epfl.sweng.runpharaa.utils.Util;

public class FragmentSearch extends Fragment {
    private View v;
    private ToggleButton searchToggle; // if false searches for tracks, if true searches for users

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
        inflater.inflate(R.menu.actionbar_menu_search, menu);
        initSearch(menu);
        super.onCreateOptionsMenu(menu, inflater);
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
                                Intent i = new Intent(getContext(), TrackPropertiesActivity.class);
                                i.putExtra("TrackID", value);
                                startActivity(i);
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
}
