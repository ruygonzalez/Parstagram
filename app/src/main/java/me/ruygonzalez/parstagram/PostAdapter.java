package me.ruygonzalez.parstagram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;

import org.parceler.Parcels;

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
        try {
            holder.tvHandle.setText(post.getUser().fetchIfNeeded().getString("username"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            itemView.setOnClickListener(this); //when a post gets clicked on go to onclick
        }

        // when the user clicks on a post, show PostDetailsActivity for the selected post
        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the Post at the position, this won't work if the class is static
                Post post = mPosts.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, PostDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
