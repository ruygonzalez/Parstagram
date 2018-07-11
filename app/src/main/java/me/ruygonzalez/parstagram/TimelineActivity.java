package me.ruygonzalez.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import me.ruygonzalez.parstagram.model.Post;

public class TimelineActivity extends AppCompatActivity {
    // REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE = 20;
    private SwipeRefreshLayout swipeContainer;
    ArrayList<Post> posts;
    PostAdapter postAdapter;
    RecyclerView rvPosts;
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // find the RecyclerView
        rvPosts = (RecyclerView) findViewById(R.id.rvPost);
        // init the arraylist (data source)
        posts = new ArrayList<>();
        // construct the adapter from this datasource
        postAdapter = new PostAdapter(posts);
        // RecyclerView setup (layout manager, use adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        // set the adapter
        rvPosts.setAdapter(postAdapter);

        btnCreate = findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // want to go to to home activity to be able to create posts if button clicked
                final Intent intent = new Intent(TimelineActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        populateTimeline();

        //this stuff is for drag refresh
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
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


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
