package me.ruygonzalez.parstagram;

import android.content.Intent;
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
    //private static String imagePath = "/storage/self/primary/DCIM/Camera/20180708_211807.jpg";
    private EditText descriptionInput;
    private Button createButton;
    private Button refreshButton;
    private Button logoutButton;
    private String imagePath;
    private Button takepictureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(getIntent().getExtras() != null){ // if picture has been taken
            imagePath = getIntent().getExtras().getString("image path");
        }
        else{ // just for now use hard coded image
            imagePath = "/storage/self/primary/DCIM/Camera/20180708_211807.jpg";
        }

        // initialize views
        descriptionInput = findViewById(R.id.description_et);
        createButton = findViewById(R.id.create_btn);
        refreshButton = findViewById(R.id.refresh_btn);
        logoutButton = findViewById(R.id.logout_btn);
        takepictureButton = findViewById(R.id.ivTakePicture);

        // create onclicklisteners

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();
                final File file = new File(imagePath); // the path is either from the picture just taken or from the default hard coded picture path
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

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        takepictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // want to go to Sign Up activity when sign up is clicked
                final Intent intent = new Intent(HomeActivity.this, pictureActivity.class);
                startActivity(intent);
            }
        });
    }

    private void logout(){
        ParseUser.logOutInBackground();
        // want to go to Log In (main) Activity with intent after successful log out
        final Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); //so that user can't just press back and log out
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
