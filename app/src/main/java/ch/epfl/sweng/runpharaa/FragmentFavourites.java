/*package ch.epfl.sweng.runpharaa;

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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.tracks.Track;

public final class FragmentFavourites extends UpdatableCardItemFragment {

    @Override
    protected void loadListWithData() {
        // Add cards to the cardList
<<<<<<< HEAD
        for (String i : User.instance.getFavoriteTracksKeys())
=======
        for (Integer i : User.instance.getFavoriteTracks())
>>>>>>> deab85ad6799c8bea37345886644f73bce31806c
            for (Track t : Track.allTracks)
                if (t.getTrackUid() == i)
                    listCardItem.add(t.getCardItem());
    }

    @Override
    protected void setEmptyMessage() {
        emptyMessage.setText(R.string.no_favorite);
        emptyMessage.setVisibility(View.VISIBLE);
    }
}*/

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.cache.ImageLoader;
import ch.epfl.sweng.runpharaa.database.DatabaseManagement;
import ch.epfl.sweng.runpharaa.tracks.Track;

public class FragmentFavourites extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View v;
    SwipeRefreshLayout swipeLayout;
    TextView emptyMessage;
    private ImageLoader imageLoader;

    public FragmentFavourites() {
    }

    public interface OnItemClickListener {
        void onItemClick(CardItem item);
    }

    private void setEmptyMessage() {
        emptyMessage.setText(R.string.no_favorite);
        emptyMessage.setVisibility(View.VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.updatable_fragment, container, false);

        imageLoader = new ImageLoader(getContext());

        emptyMessage = v.findViewById(R.id.emptyMessage);
        emptyMessage.setVisibility(View.GONE);

        // Setup for refresh on swipe
        swipeLayout = v.findViewById(R.id.refreshNearMe);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.refresh_orange, R.color.refresh_red, R.color.refresh_blue, R.color.refresh_green);

        // Load data initially
        loadData();

        return v;
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
    }

    /**
     * Create the recyclerView and the list of cardItem used to draw the list of tracks "near me".
     * This function is called when the fragment is created and each time the list is refreshed.
     */
    public void loadData() {
        emptyMessage.setVisibility(View.GONE);
        // Create a fresh recyclerView and listCardItem

        DatabaseManagement.mReadDataOnce(DatabaseManagement.TRACKS_PATH, new DatabaseManagement.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot data) {
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
                List<Track> tracks = DatabaseManagement.initFavouritesTracks(data);
                for (Track t : tracks) {
                    t.setCardItem(new CardItem(t.getName(), t.getTrackUid(), t.getImageStorageUri()));
                    listCardItem.add(t.getCardItem());
                }
                Adapter adapter = new Adapter(getActivity(), listCardItem, listener);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                if (listCardItem.isEmpty())
                    setEmptyMessage();
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                Log.d("DB Read: ", "Failed to read data from DB in FragmentFavourites.");
                setEmptyMessage();
            }
        });
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
            viewHolder.name.setText(listCardItem.get(position).getName());

            imageLoader.displayImage(listCardItem.get(position).getImageURL(), viewHolder.background_img);
            /*new DownloadImageTask(viewHolder.background_img)
                    .execute(listCardItem.get(position).getImageURL());*/

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
             * * Set the ImageView to the bitmap result
             *
             * @param result
             */
            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }
    }
}