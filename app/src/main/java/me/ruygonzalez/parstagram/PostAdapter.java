package me.ruygonzalez.parstagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.ruygonzalez.parstagram.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private List<Post> mPosts;
    Context context;

    // pass in the Posts array in the constructor
    public PostAdapter(List<Post> posts) {
        mPosts = posts;
    }

    // for each row, inflate the layout and cache references into ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_post, parent, false);

        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the data according to position
        Post post = mPosts.get(position);
        // populate the views according to this data
        holder.tvHandle.setText(post.getUser().getUsername());
        holder.tvDescription.setText(post.getDescription());

        // load image
        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPicture);
    }

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }

    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvHandle;
        public TextView tvDescription;
        public ImageView ivPicture;

        public ViewHolder(View itemView){
            super(itemView);

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            ivPicture = (ImageView) itemView.findViewById(R.id.ivPicture);
        }
    }
}
