package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentNearMe extends Fragment {
    View v;
    public FragmentNearMe(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.near_me_fragment, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.cardListId);
        List<CardItem> listCardItem = new ArrayList<>();

        // Add cards to the cardList
        listCardItem.add(new CardItem());
        listCardItem.add(new CardItem());
        listCardItem.add(new CardItem());
        listCardItem.add(new CardItem());
        listCardItem.add(new CardItem());

        Adapter adapter = new Adapter(getActivity() /*container.getContext()*/, listCardItem);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity() /*container.getContext()*/));

        return v;
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {

        Context context;
        List<CardItem> listCardItem;

        public Adapter(Context context, List<CardItem> listCardItem) {
            this.context = context;
            this.listCardItem = listCardItem;
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
        }

        @Override
        public int getItemCount() {
            return listCardItem.size();
        }

        public class viewHolder extends RecyclerView.ViewHolder {
            // Buttons, images and texts on the cards will be created here

            public viewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}
