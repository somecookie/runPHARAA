package ch.epfl.sweng.runpharaa.user;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.TrackCardItem;
import ch.epfl.sweng.runpharaa.TrackPropertiesActivity;
import ch.epfl.sweng.runpharaa.cache.ImageLoader;
import ch.epfl.sweng.runpharaa.user.myProfile.UsersProfileActivity;

public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {
    List<TrackCardItem> createdTracks;
    Adapter.OnItemClickListener listener;
    Context context;

    public Adapter(Context context, List<TrackCardItem> createdTracks, Adapter.OnItemClickListener listener) {
        this.createdTracks = createdTracks;
        this.listener = listener;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(TrackCardItem item);
    }

    @NonNull
    @Override
    public Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View v = layoutInflater.inflate(R.layout.track_card_item, viewGroup, false);
        return new Adapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.viewHolder viewHolder, int position) {
        // Set here the buttons, images and texts created in the viewHolder
        viewHolder.name.setText(createdTracks.get(position).getName());

        ImageLoader.getLoader(context).displayImage(createdTracks.get(position).getImageURL(), viewHolder.background_img);

        viewHolder.bind(createdTracks.get(position), new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(TrackCardItem item) {
                Intent intent = new Intent(context, TrackPropertiesActivity.class);
                intent.putExtra("TrackID", item.getParentTrackID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return createdTracks.size();
    }

    protected class viewHolder extends RecyclerView.ViewHolder {
        // Buttons, images and texts on the cards will be created here

        ImageView background_img;
        TextView name;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            background_img = itemView.findViewById(R.id.cardBackgroundId);
            name = itemView.findViewById(R.id.nameID);
        }

        public void bind(final TrackCardItem item, final Adapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}


