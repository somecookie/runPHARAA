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
import com.google.firebase.database.DatabaseReference;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.runpharaa.cache.ImageLoader;
import ch.epfl.sweng.runpharaa.database.DatabaseManagement;
import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Callback;

public class FragmentFollowing extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View v;
    SwipeRefreshLayout swipeLayout;
    TextView emptyMessage;
    private ImageLoader imageLoader;

    public FragmentFollowing(){}

    public interface OnItemClickListener {
        void onItemClick(CardItem item);
    }

    protected void setEmptyMessage() {
        emptyMessage.setText(R.string.no_follow);
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

        // Load data the first time
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
    public void onResume() { super.onResume(); }

    public void loadData() {
        emptyMessage.setVisibility(View.GONE);
        // Create a fresh recyclerView and listCardItem

        UserDatabaseManagement.mReadDataOnce("users", new DatabaseManagement.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot data) {

                List<User> followedUsers = User.instance.getFollowedUsers();

            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                Log.d("DB Read: ", "Failed to read data from DB in FragmentFollowing.");
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

            public void bind(final CardItem item, final FragmentFollowing.OnItemClickListener listener) {
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
