package me.ruygonzalez.parstagram;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Locale;

import me.ruygonzalez.parstagram.model.Post;

public class PostDetailsActivity extends AppCompatActivity {

    // the post to display
    Post post;

    // the view objects
    public ImageView ivProfileImage;
    public TextView tvHandle;
    public TextView tvDescription;
    public ImageView ivPicture;
    public TextView tvHandle2;
    public TextView timestamp;
    public TextView likes;
    public ImageButton home;
    public ImageView ivHeart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_title);

        // resolve the view objects
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvHandle = (TextView) findViewById(R.id.tvHandle);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);
        timestamp = (TextView) findViewById(R.id.tvTimestamp);
        tvHandle2 = (TextView) findViewById(R.id.tvHandle2);
        likes = (TextView) findViewById(R.id.tvLikes);
        ivHeart = (ImageView) findViewById(R.id.ivHeart);
        home = (ImageButton) findViewById(R.id.btnHome);

        // unwrap the post passed in via intent, using its simple name as a key
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d("PostDetailsActivity", String.format("Showing details for '%s'", post.getDescription()));
        // set the text for the textviews
        // populate the views according to this data
        try {
            tvHandle.setText(post.getUser().fetchIfNeeded().getString("username"));
            tvHandle2.setText("@" + post.getUser().fetchIfNeeded().getString("handle"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvDescription.setText(post.getDescription());
        timestamp.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));

        ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post.addLike();
                post.saveInBackground();
                Log.d("PostAdapter", "Post Like Clicked");
            }
        });
        /*
        // generate a random number of likes to display (this part is faked)
        Random rand = new Random();
        int max = 100;
        int min = 2;
        int value = rand.nextInt((max - min) + 1) + min;
        likes.setText(Integer.toString(value) + " likes");*/
        likes.setText(Integer.toString(post.getLikes()) + " likes");

        // load image
        Glide.with(this)
                .load(post.getImage().getUrl())
                .into(ivPicture);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
