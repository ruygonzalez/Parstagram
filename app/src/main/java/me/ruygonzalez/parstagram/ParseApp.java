package me.ruygonzalez.parstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.ruygonzalez.parstagram.model.Post;

public class ParseApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        // when creating and initializing Parse we need to register a subclass,
        // this tells parse that this model is a custom Parse model that we created
        // to encapsulate our data
        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("tom-brady")
                .clientKey("patriots52")
                .server("http://ruygonzalez-fbu-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
