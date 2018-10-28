package ch.epfl.sweng.runpharaa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class FragmentFollowing extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View v;
    SwipeRefreshLayout swipeLayout;
    TextView emptyMessage;
    List<CardItem> listCardItem; // TODO: remove when DB

    public FragmentFollowing(){ }

    public interface OnItemClickListener {
        void onItemClick(CardItem item);
    }

    protected void setEmptyMessage() {
        emptyMessage.setText(R.string.no_follow);
        emptyMessage.setVisibility(View.VISIBLE);
    }

    private void initList() { // TODO: remove when DB
        // Create a fresh recyclerView and listCardItem
        listCardItem = new ArrayList<>();
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

    public void loadData() {
        // Create a fresh recyclerView and listCardItem
        initList();
        if(listCardItem.isEmpty())
            setEmptyMessage();
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {
        Context context;
        List<CardItem> listCardItem;
        FragmentNearMe.OnItemClickListener listener;

        public Adapter(Context context, List<CardItem> listCardItem, OnItemClickListener listener) {
            this.context = context;
            this.listCardItem = listCardItem;
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
            viewHolder.name.setText(listCardItem.get(position).getName());

            new Adapter.DownloadImageTask(viewHolder.background_img)
                    .execute(listCardItem.get(position).getImageURL());

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
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    mIcon11 = BitmapFactory.decodeStream(in, null, options);

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    mIcon11.compress(Bitmap.CompressFormat.PNG, 50, out);

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
