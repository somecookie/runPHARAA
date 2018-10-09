package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.sweng.runpharaa.User.FAKE_USER;

public class FragmentNearMe extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View v;
    SwipeRefreshLayout swipeLayout;

    public FragmentNearMe(){

    }

    public interface OnItemClickListener {
        void onItemClick(CardItem item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.near_me_fragment, container, false);

        // Setup for refresh on swipe
        swipeLayout = v.findViewById(R.id.refreshNearMe);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.refresh_orange, R.color.refresh_red, R.color.refresh_blue, R.color.refresh_green);

        // Load if the fragment is visible
        if (getUserVisibleHint()) {
            loadData();
        }

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // If the fragment is visible, reload the data
        if (isVisibleToUser && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onRefresh() {
        loadData();

        // Stop refreshing once it is done
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Do nothing if the fragment is not visible
        if (!getUserVisibleHint()) {
            return;
        }
        // Else load the data
        loadData();
    }

    /**
     * Create the recyclerView and the list of cardItem used to draw the list of tracks "near me".
     * This function is called when the fragment is created and each time the list is refreshed.
     */
    public void loadData() {
        // Create a fresh recyclerView and listCardItem
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

        // Add cards to the cardList
        for(Track t : FAKE_USER.tracksNearMe())
            listCardItem.add(t.getCardItem());

        Adapter adapter = new Adapter(getActivity(), listCardItem, listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {

        Context context;
        List<CardItem> listCardItem;
        OnItemClickListener listener;

        public Adapter(Context context, List<CardItem> listCardItem, OnItemClickListener listener) {
            this.context = context;
            this.listCardItem = listCardItem;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View v = layoutInflater.inflate(R.layout.card_item, viewGroup, false);
            return new viewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.viewHolder viewHolder, int position) {
            // Set here the buttons, images and texts created in the viewHolder
            viewHolder.background_img.setImageResource(listCardItem.get(position).getBackground());
            viewHolder.name.setText(listCardItem.get(position).getName());
            viewHolder.bind(listCardItem.get(position), listener);
        }

        @Override
        public int getItemCount() {
            return listCardItem.size();
        }

        public class viewHolder extends RecyclerView.ViewHolder {
            // Buttons, images and texts on the cards will be created here

            ImageView background_img;
            TextView name;
            
            public viewHolder(@NonNull View itemView) {
                super(itemView);
                background_img = itemView.findViewById(R.id.cardBackgroundId);
                name = itemView.findViewById(R.id.nameID);
            }

            public void bind(final CardItem item, final OnItemClickListener listener) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        listener.onItemClick(item);
                    }
                });
            }
        }
    }
}
