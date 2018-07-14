package me.ruygonzalez.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.ruygonzalez.parstagram.model.Post;

public class ProfileFragment extends Fragment {

    private Button btnLogout;
    private TextView tvPosts;
    private TextView tvFollowers;
    private TextView tvFollowing;
    private TextView tvUsername;
    private ImageView ivProfilePic;
    private final int REQUEST_CODE = 20;
    ArrayList<Post> posts;
    GridAdapter gridAdapter;
    RecyclerView rvGrid;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_profile, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        tvPosts = (TextView) view.findViewById(R.id.tvPosts);
        tvFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        ivProfilePic = (ImageView) view.findViewById(R.id.ivProfilePic);

        final ParseUser user = ParseUser.getCurrentUser();

        // set textviews to display certain text
        //generate random numbers
        Random rand = new Random();
        int postsnum = rand.nextInt(25)+1;
        int followers = rand.nextInt(1000)+1;
        int following = rand.nextInt(500)+1;

        // find the RecyclerView
        rvGrid = (RecyclerView) view.findViewById(R.id.rvGrid);
        // init the arraylist (data source)
        posts = new ArrayList<>();
        // construct the adapter from this datasource
        gridAdapter = new GridAdapter(posts);
        // RecyclerView setup (layout manager, use adapter)
        rvGrid.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));
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


    private void logout(){
        ParseUser.logOutInBackground();
        // want to go to Log In (main) Activity with intent after successful log out
        final Intent intent = new Intent(this.getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    private void populateTimeline(){
        // Define the class we would like to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("user",ParseUser.getCurrentUser());
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