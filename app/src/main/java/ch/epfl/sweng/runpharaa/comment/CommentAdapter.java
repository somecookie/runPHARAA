package ch.epfl.sweng.runpharaa.comment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.user.UsersProfileActivity;
import ch.epfl.sweng.runpharaa.utils.Callback;
import ch.epfl.sweng.runpharaa.utils.DownloadImageTask;

import static android.support.v4.content.ContextCompat.startActivity;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{

    private Context mContext;
    private List<Comment> comments;

    public CommentAdapter(Context mContext, List<Comment> comments) {
        this.mContext = mContext;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.comment_card, null);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int i) {
        Comment comment = comments.get(i);
        UserDatabaseManagement.getUserNameFromID(comment.getCreatorID(), new Callback<String>() {
            @Override
            public void onSuccess(String name) {
                UserDatabaseManagement.getUserPictureFromID(comment.getCreatorID(), new Callback<String>() {
                    @Override
                    public void onSuccess(String picture) {
                        new DownloadImageTask(holder.imageView, mContext.getResources()).execute(picture);
                        holder.name.setText(name);

                        holder.name.setOnClickListener(v -> {
                            Intent userProfile ;
                            userProfile = new Intent(mContext, UsersProfileActivity.class);
                            userProfile.putExtra("userId", comment.getCreatorID());
                            startActivity(mContext, userProfile, null);
                        });

                        holder.date.setText(comment.getDate());
                        holder.comment.setText(comment.getComment());

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name;
        TextView comment;
        TextView date;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.comment_image);
            name = itemView.findViewById(R.id.comment_name);
            comment = itemView.findViewById(R.id.comment_content);
            date = itemView.findViewById(R.id.comment_date);
        }
    }
}
