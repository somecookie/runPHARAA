package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class UpdatableCardItemFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View v;
    SwipeRefreshLayout swipeLayout;
    RecyclerView recyclerView;
    List<CardItem> listCardItem;
    TextView emptyMessage;
    FragmentNearMe.OnItemClickListener listener; //TODO: Why FragmentNearMe?

    public interface OnItemClickListener {
        void onItemClick(CardItem item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.updatable_fragment, container, false);

        emptyMessage = v.findViewById(R.id.emptyMessage);

        // Setup for refresh on swipe
        swipeLayout = v.findViewById(R.id.refreshNearMe);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.refresh_orange, R.color.refresh_red, R.color.refresh_blue, R.color.refresh_green);

        recyclerView = v.findViewById(R.id.cardListId);

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

    private void initList() {
        // Create a fresh recyclerView and listCardItem
        listCardItem = new ArrayList<>();
        listener = new FragmentNearMe.OnItemClickListener() {
            @Override
            public void onItemClick(CardItem item) {
                Intent intent = new Intent(getContext(), TrackPropertiesActivity.class);
                intent.putExtra("TrackID", item.getParentTrackID());
                Log.i("hiii", ""+ item.getParentTrackID());
                startActivity(intent);
            }
        };
    }

    /**
     * Put all the data in `listCardItem`
     */
    protected abstract void loadListWithData();

    protected abstract void setEmptyMessage();

    private void initAdapterAndLinkAll() {
        CardItemAdapter adapter = new CardItemAdapter(getActivity(), listCardItem, listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    protected void loadData() {
        initList();
        loadListWithData();
        if(listCardItem.isEmpty())
            setEmptyMessage();
        initAdapterAndLinkAll();
    }

    private class CardItemAdapter extends RecyclerView.Adapter<CardItemAdapter.viewHolder> {

        Context context;
        List<CardItem> listCardItem;
        FragmentNearMe.OnItemClickListener listener;

        CardItemAdapter(Context context, List<CardItem> listCardItem, FragmentNearMe.OnItemClickListener listener) {
            this.context = context;
            this.listCardItem = listCardItem;
            this.listener = listener;
        }

        @NonNull
        @Override
        public CardItemAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View v = layoutInflater.inflate(R.layout.card_item, viewGroup, false);
            return new CardItemAdapter.viewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CardItemAdapter.viewHolder viewHolder, int position) {
            // Set here the buttons, images and texts created in the viewHolder
            viewHolder.background_img.setImageBitmap(listCardItem.get(position).getBackground());

            /*new DownloadImageTask((ImageView) viewHolder.background_img)
                    .execute(listCardItem.get(position).getImageURL());*/

            viewHolder.name.setText(listCardItem.get(position).getName());
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

            void bind(final CardItem item, final FragmentNearMe.OnItemClickListener listener) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(item);
                    }
                });
            }
        }

        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;

                Bitmap decoded = null;

                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    mIcon11.compress(Bitmap.CompressFormat.PNG, 20, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return decoded;
            }

            /**
             ** Set the ImageView to the bitmap result
             * @param result
             */
            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }
    }
}
