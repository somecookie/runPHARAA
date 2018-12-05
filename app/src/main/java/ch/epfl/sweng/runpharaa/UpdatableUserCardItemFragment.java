package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.runpharaa.cache.ImageLoader;
import ch.epfl.sweng.runpharaa.gui.UserCardItem;

public abstract class UpdatableUserCardItemFragment extends UpdatableCardItemFragment implements SwipeRefreshLayout.OnRefreshListener {

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

        class viewHolder extends RecyclerView.ViewHolder {
            // Buttons, images and texts on the cards will be created here

            ImageView profilePic;
            TextView name;
            TextView nbOfCreatedTracks;

            viewHolder(@NonNull View itemView) {
                super(itemView);
                profilePic = itemView.findViewById(R.id.profilePicId);
                name = itemView.findViewById(R.id.userNameId);
                nbOfCreatedTracks = itemView.findViewById(R.id.nbTracksId);
            }

            void bind(final UserCardItem item, final OnItemClickListener listener) {
                itemView.setOnClickListener(v -> listener.onItemClick(item));
            }
        }
    }
}
