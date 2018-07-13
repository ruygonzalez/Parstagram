package me.ruygonzalez.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.Random;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileFragment extends Fragment {

    private Button btnLogout;
    private TextView tvPosts;
    private TextView tvFollowers;
    private TextView tvFollowing;
    private TextView tvUsername;
    private ImageView ivProfilePic;

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
        int posts = rand.nextInt(25)+1;
        int followers = rand.nextInt(1000)+1;
        int following = rand.nextInt(500)+1;
        // place texts in views
        tvFollowers.setText(Integer.toString(followers));
        tvFollowing.setText(Integer.toString(following));
        tvPosts.setText(Integer.toString(posts));
        try {
            tvUsername.setText(user.fetchIfNeeded().getString("username"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Round the corners of the profile images
        final RoundedCornersTransformation roundedCornersTransformation
                = new RoundedCornersTransformation(300, 15);
        final RequestOptions requestOptions = RequestOptions.bitmapTransform(roundedCornersTransformation);
        // load image
        Glide.with(this)
                .load(((ParseFile)user.get("profilepic")).getUrl().toString())
                .apply(requestOptions)
                .into(ivProfilePic);

    }

    private void logout(){
        ParseUser.logOutInBackground();
        // want to go to Log In (main) Activity with intent after successful log out
        final Intent intent = new Intent(this.getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}