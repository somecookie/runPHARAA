package ch.epfl.sweng.runpharaa;

import android.annotation.TargetApi;
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
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.UserProfileChangeRequest;

import ch.epfl.sweng.runpharaa.database.TrackDatabaseManagement;
import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.user.UsersProfileActivity;
import ch.epfl.sweng.runpharaa.utils.Callback;

public class FragmentSearch extends Fragment {
    private View v;
    private ToggleButton searchToggle; // if false searches for tracks, if true searches for users

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.updatable_fragment, container, false);
        searchToggle = new ToggleButton(getContext());
        searchToggle.setTextOn("Currently search for: users");
        searchToggle.setTextOff("Currently search for: tracks");
        searchToggle.setChecked(true);
        ((LinearLayout)v.findViewById(R.id.vertical_layout)).addView(searchToggle, 0);
        setHasOptionsMenu(true);
        searchToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchToggle.setChecked(!searchToggle.isChecked());
            }
        });
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
        SearchView sv = (SearchView)item.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchToggle.isChecked()) {
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
                    UserDatabaseManagement.findUserUIDByName(query, new Callback<String>() {
                        @Override
                        public void onSuccess(String value) {
                            if (value == null) {
                                Toast.makeText(getContext(), String.format(getResources().getString(R.string.no_user_found), query), Toast.LENGTH_LONG).show();
                            } else {
                                Intent i = new Intent(getContext(), UsersProfileActivity.class);
                                i.putExtra("userId", value);
                                startActivity(i);
                            }
                        }
                    });
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
