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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.runpharaa.cache.ImageLoader;
import ch.epfl.sweng.runpharaa.user.UserCardItem;

public abstract class UpdatableUserCardItemFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View v;
    SwipeRefreshLayout swipeLayout;
    TextView emptyMessage;

    public interface OnItemClickListener {
        void onItemClick(UserCardItem item);
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

        emptyMessage = v.findViewById(R.id.emptyMessage);
        emptyMessage.setVisibility(View.GONE);
        setHasOptionsMenu(true);

        // Setup for refresh on swipe
        swipeLayout = v.findViewById(R.id.refreshNearMe);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.refresh_orange, R.color.refresh_red, R.color.refresh_blue, R.color.refresh_green);

        // Load initial data
        loadData();

        return v;
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected abstract void setEmptyMessage();

    protected abstract void loadData();

    protected class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {
        Context context;
        List<UserCardItem> listUserCardItem;
        OnItemClickListener listener;

        Adapter(Context context, List<UserCardItem> listUserCardItem, OnItemClickListener listener) {
            this.context = context;
            this.listUserCardItem = listUserCardItem;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View v = layoutInflater.inflate(R.layout.user_card_item, viewGroup, false);
            return new Adapter.viewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.viewHolder viewHolder, int position) {
            // Set here the buttons, images and texts created in the viewHolder
            viewHolder.name.setText(listUserCardItem.get(position).getName());
            viewHolder.nbOfCreatedTracks.setText(String.valueOf(listUserCardItem.get(position).getNbCreatedTracks()));

            ImageLoader.getLoader(getContext()).displayImage(listUserCardItem.get(position).getImageURL(), viewHolder.profilePic);

            viewHolder.bind(listUserCardItem.get(position), listener);
        }

        @Override
        public int getItemCount() {
            return listUserCardItem.size();
        }

        public class viewHolder extends RecyclerView.ViewHolder {
            // Buttons, images and texts on the cards will be created here

            ImageView profilePic;
            TextView name;
            TextView nbOfCreatedTracks;

            public viewHolder(@NonNull View itemView) {
                super(itemView);
                profilePic = itemView.findViewById(R.id.profilePicId);
                name = itemView.findViewById(R.id.userNameId);
                nbOfCreatedTracks = itemView.findViewById(R.id.nbTracksId);
            }

            public void bind(final UserCardItem item, final OnItemClickListener listener) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(item);
                    }
                });
            }
        }
    }
}
