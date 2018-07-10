package me.ruygonzalez.parstagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

import me.ruygonzalez.parstagram.model.Post;

public class HomeActivity extends AppCompatActivity {
    //private static String imagePath = "/sdcard/DCIM/Camera/IMG_20180619_0110347.jpg";
    //private static String imagePath = "/desktop/facebookpic.png";
    private static String imagePath = "/storage/self/primary/DCIM/Camera/20180708_211807.jpg";
    private EditText descriptionInput;
    private Button createButton;
    private Button refreshButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // initialize views
        descriptionInput = findViewById(R.id.description_et);
        createButton = findViewById(R.id.create_btn);
        refreshButton = findViewById(R.id.refresh_btn);

        // create onclicklisteners

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();
                final File file = new File(imagePath); // would normally not hard code but rather ask user to select file or take photo from camera
                final ParseFile parseFile = new ParseFile(file);

                parseFile.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        // If successful add file to user and signUpInBackground
                        if(null == e)
                            createPost(description, parseFile, user);
                    }
                });

                //createPost(description, parseFile, user); // calls method to create post
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTopPosts(); // when refresh is clicked, load top 20 posts
            }
        });
    }

    // method to create and save a post
    private void createPost(String description, ParseFile imageFile, ParseUser user){
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) { // no errors
                    Log.d("HomeActivity", "Create Post success!");
                }
                else {
                    e.printStackTrace();
                }
            }
        });
    }


    // load the top 20 posts and user information using Post Query
    private void loadTopPosts() {
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
