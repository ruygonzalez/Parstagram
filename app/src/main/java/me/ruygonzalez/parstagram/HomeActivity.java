package me.ruygonzalez.parstagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import me.ruygonzalez.parstagram.model.Post;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // use Query class inside post
        final Post.Query postQuery = new Post.Query();
        // have Query get top (20) posts with user information
        postQuery.getTop().withUser();

        // will search for and grab all posts in background thread
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null){ // if no errors and Post objects successfully grabbed
                    for(int i = 0; i < objects.size(); i++) { // iterate through Posts in list
                        Log.d("HomeActivity", "Post[" + i + "] = "
                                + objects.get(i).getDescription() // return caption/description of image
                                + "\nusername = " + objects.get(i).getUser().getUsername() // also return the username of user who posted image
                        );
                    }
                } else{
                    e.printStackTrace();
                }
            }
        });
    }

}
