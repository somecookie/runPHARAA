package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.sweng.runpharaa.User.FAKE_USER;

public class FragmentFavourites extends Fragment {
    View v;
    public FragmentFavourites(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.near_me_fragment, container, false);

        return v;
    }



    /**
     * Create the recyclerView and the list of cardItem used to draw the list of tracks "near me".
     * This function is called when the fragment is created and each time the list is refreshed.
     */
    public void loadData() {
        // Create a fresh recyclerView and listCardItem
        RecyclerView recyclerView = v.findViewById(R.id.cardListId);
        List<CardItem> listCardItem = new ArrayList<>();
        FragmentNearMe.OnItemClickListener listener = new FragmentNearMe.OnItemClickListener() {
            @Override
            public void onItemClick(CardItem item) {
                Intent intent = new Intent(getContext(), TrackPropertiesActivity.class);
                intent.putExtra("TrackID", item.getParentTrackID());
                startActivity(intent);
            }
        };

        // Add cards to the cardList
        for(Integer i : FAKE_USER.getFavorites())
            for(Track t : Track.allTracks())
                if(t.getUid() == i)
                    listCardItem.add(t.getCardItem());

        Adapter adapter = new Adapter(getActivity(), listCardItem, listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private class Adapter extends RecyclerView.Adapter<FragmentFavourites.Adapter.viewHolder> {

        Context context;
        List<CardItem> listCardItem;
        FragmentNearMe.OnItemClickListener listener;

        public Adapter(Context context, List<CardItem> listCardItem, FragmentNearMe.OnItemClickListener listener) {
            this.context = context;
            this.listCardItem = listCardItem;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View v = layoutInflater.inflate(R.layout.card_item, viewGroup, false);
            return new Adapter.viewHolder(v);
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

            public void bind(final CardItem item, final FragmentNearMe.OnItemClickListener listener) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        listener.onItemClick(item);
                    }
                });
            }
        }
    }
}
