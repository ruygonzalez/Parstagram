package me.ruygonzalez.parstagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.ruygonzalez.parstagram.model.Post;

public class profileActivity extends AppCompatActivity {
    private TextView tvPosts;
    private TextView tvFollowers;
    private TextView tvFollowing;
    private TextView tvUsername;
    private ImageView ivProfilePic;
    private ParseUser user;
    private final int REQUEST_CODE = 20;
    ArrayList<Post> posts;
    GridAdapter gridAdapter;
    RecyclerView rvGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        tvPosts = (TextView) findViewById(R.id.tvPosts);
        tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);

        Post post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        user = post.getUser();

        // set textviews to display certain text
        //generate random numbers
        Random rand = new Random();
        int postsnum = rand.nextInt(25)+1;
        int followers = rand.nextInt(1000)+1;
        int following = rand.nextInt(500)+1;

        // find the RecyclerView
        rvGrid = (RecyclerView) findViewById(R.id.rvGrid);
        // init the arraylist (data source)
        posts = new ArrayList<>();
        // construct the adapter from this datasource
        gridAdapter = new GridAdapter(posts);
        // RecyclerView setup (layout manager, use adapter)
        rvGrid.setLayoutManager(new GridLayoutManager(this, 3));
        // set the adapter
        rvGrid.setAdapter(gridAdapter);

        populateTimeline();



        // place texts in views
        tvFollowers.setText(Integer.toString(followers));
        tvFollowing.setText(Integer.toString(following));
        try {
            tvUsername.setText(user.fetchIfNeeded().getString("username"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Round the corners of the profile images
        final RoundedCornersTransformation roundedCornersTransformation
                = new RoundedCornersTransformation(600, 15);
        final RequestOptions requestOptions = RequestOptions.bitmapTransform(roundedCornersTransformation);
        // load image
        if((ParseFile)user.get("profilepic") == null){
            /*Glide.with(this)
                    .load("/desktop/profilepicturedef.png")
                    .apply(requestOptions)
                    .into(ivProfilePic);*/
        }
        else {
            Glide.with(this)
                    .load(((ParseFile) user.get("profilepic")).getUrl().toString())
                    .apply(requestOptions)
                    .into(ivProfilePic);
        }
    }

    private void populateTimeline(){
        // Define the class we would like to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("user", user);
        // get the latest 20 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");
        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Post>() {
            public void done(List<Post> itemList, ParseException e) {
                if (e == null) {
                    gridAdapter.clear();
                    // add new items to your adapter
                    gridAdapter.addAll(itemList);
                    tvPosts.setText(Integer.toString(itemList.size()));
                    rvGrid.scrollToPosition(0);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }
}
