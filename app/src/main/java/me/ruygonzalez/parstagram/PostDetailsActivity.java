package me.ruygonzalez.parstagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
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
    TextView tvUsername;
    TextView tvDescription;
    TextView tvTimestamp;
    ImageView ivPicture;
    ImageView ivProfilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        // resolve the view objects
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvTimestamp = (TextView) findViewById(R.id.tvTimestamp);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);
        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);

        // unwrap the post passed in via intent, using its simple name as a key
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d("PostDetailsActivity", String.format("Showing details for '%s'", post.getDescription()));
        // set the text for the textviews
        tvDescription.setText(post.getDescription());
        try {
            tvUsername.setText(post.getUser().fetchIfNeeded().getString("username"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // tvTimestamp.setText(getRelativeTimeAgo(post.getCreatedAt()));
        tvTimestamp.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));

        Glide.with(this)
                .load(post.getImage().getUrl())
                .into(ivPicture);
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
        // relative date needs to be shortened to '7h' or '8m' or '9s'
        String shortened = relativeDate.substring(0,relativeDate.indexOf(" ") + 2);
        int i = shortened.indexOf(" ");
        shortened = shortened.substring(0,i) + shortened.substring(i+1);
        return shortened;
    }
}
