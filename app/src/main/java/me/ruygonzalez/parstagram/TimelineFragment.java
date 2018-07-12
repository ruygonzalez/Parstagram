package me.ruygonzalez.parstagram;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import me.ruygonzalez.parstagram.model.Post;

public class TimelineFragment extends Fragment {
    // REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE = 20;
    private SwipeRefreshLayout swipeContainer;
    ArrayList<Post> posts;
    PostAdapter postAdapter;
    RecyclerView rvPosts;
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_timeline, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        // find the RecyclerView
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPost);
        // init the arraylist (data source)
        posts = new ArrayList<>();
        // construct the adapter from this datasource
        postAdapter = new PostAdapter(posts);
        // RecyclerView setup (layout manager, use adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        // set the adapter
        rvPosts.setAdapter(postAdapter);

        populateTimeline();

        //this stuff is for drag refresh
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void fetchTimelineAsync(int page) {
        postAdapter.clear();
        posts.clear();
        populateTimeline();
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
    }

    private void populateTimeline(){
        // Define the class we would like to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Configure limit and sort order
        query.setLimit(20);
        // get the latest 20 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");
        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Post>() {
            public void done(List<Post> itemList, ParseException e) {
                if (e == null) {
                    postAdapter.clear();
                    // add new items to your adapter
                    postAdapter.addAll(itemList);
                    rvPosts.scrollToPosition(0);
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

}