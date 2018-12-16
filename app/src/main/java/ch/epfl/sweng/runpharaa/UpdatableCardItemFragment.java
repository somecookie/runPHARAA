package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.runpharaa.cache.ImageLoader;
import ch.epfl.sweng.runpharaa.gui.CardItem;
import ch.epfl.sweng.runpharaa.gui.TrackCardItem;
import ch.epfl.sweng.runpharaa.utils.Config;

public abstract class UpdatableCardItemFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    protected View v;
    protected SwipeRefreshLayout swipeLayout;
    protected TextView emptyMessage;

    public interface OnItemClickListener {
        void onItemClick(CardItem item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.updatable_fragment, container, false);
        setHasOptionsMenu(true);
        emptyMessage = v.findViewById(R.id.emptyMessage);
        emptyMessage.setVisibility(View.GONE);

        // Setup for refresh on swipe
        swipeLayout = v.findViewById(R.id.refreshNearMe);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.refresh_orange, R.color.refresh_red, R.color.refresh_blue, R.color.refresh_green);
        if(!Config.isTest) loadData();
        return v;
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onResume() {
        if(!Config.isTest) loadData();
        super.onResume();
    }

    protected abstract void setEmptyMessage();

    protected abstract void loadData();

    protected class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {
        Context context;
        List<CardItem> listCardItem;
        OnItemClickListener listener;

        Adapter(Context context, List<CardItem> listTrackCardItem, OnItemClickListener listener) {
            this.context = context;
            this.listCardItem = listTrackCardItem;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View v = layoutInflater.inflate(R.layout.track_card_item, viewGroup, false);
            return new Adapter.viewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.viewHolder viewHolder, int position) {
            // Set here the buttons, images and texts created in the viewHolder
            viewHolder.name.setText(listCardItem.get(position).getName());

            ImageLoader.getLoader(getContext()).displayImage(listCardItem.get(position).getImageURL(), viewHolder.background_img);

            viewHolder.bind(listCardItem.get(position), listener);
        }

        @Override
        public int getItemCount() {
            return listCardItem.size();
        }

        class viewHolder extends RecyclerView.ViewHolder {
            // Buttons, images and texts on the cards will be created here

            ImageView background_img;
            TextView name;

            viewHolder(@NonNull View itemView) {
                super(itemView);
                background_img = itemView.findViewById(R.id.cardBackgroundId);
                name = itemView.findViewById(R.id.nameID);
            }

            void bind(final CardItem item, final OnItemClickListener listener) {
                itemView.setOnClickListener(v -> listener.onItemClick(item));
            }
        }
    }
}
