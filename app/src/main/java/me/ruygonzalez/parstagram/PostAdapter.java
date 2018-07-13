package me.ruygonzalez.parstagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseException;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
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
            holder.tvHandle2.setText("@" + post.getUser().fetchIfNeeded().getString("handle"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvDescription.setText(post.getDescription());
        holder.timestamp.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));

        // generate a random number of likes to display (this part is faked)
        /*Random rand = new Random();
        int max = 100;
        int min = 2;
        int value = rand.nextInt((max - min) + 1) + min;
        holder.likes.setText(Integer.toString(value) + " likes");*/
        holder.likes.setText(Integer.toString(post.getLikes()) + " likes");

        // load image
        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPicture);

        // Round the corners of the profile images
        final RoundedCornersTransformation roundedCornersTransformation
                = new RoundedCornersTransformation(600, 15);
        final RequestOptions requestOptions = RequestOptions.bitmapTransform(roundedCornersTransformation);
        // load image
        Glide.with(context)
                .load(((ParseFile)post.getUser().get("profilepic")).getUrl().toString())
                .apply(requestOptions)
                .into(holder.ivProfileImage);
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
        public TextView tvHandle2;
        public TextView timestamp;
        public TextView likes;
        public ImageView ivHeart;

        public ViewHolder(View itemView){
            super(itemView);

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            ivPicture = (ImageView) itemView.findViewById(R.id.ivPicture);
            timestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
            tvHandle2 = (TextView) itemView.findViewById(R.id.tvHandle2);
            likes = (TextView) itemView.findViewById(R.id.tvLikes);
            ivHeart = (ImageView) itemView.findViewById(R.id.ivHeart);
            ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // gets item position
                    int position = getAdapterPosition();
                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the Post at the position, this won't work if the class is static
                        Post post = mPosts.get(position);
                        // like post
                        post.addLike();
                        post.saveInBackground();
                        Log.d("PostAdapter", "Post Like Clicked");
                        ivHeart.setImageResource(R.drawable.ufi_heart_active);
                        ivHeart.setColorFilter(Color.parseColor("#ff69b4"));
                    }
                }
            });

            ivPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
            });
            //itemView.setOnClickListener(this); //when a post gets clicked on go to onclick
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

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return relativeDate;
    }
}
