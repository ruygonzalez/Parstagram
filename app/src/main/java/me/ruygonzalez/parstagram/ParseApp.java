package me.ruygonzalez.parstagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("tom-brady")
                .clientKey("patriots52")
                .server("http://ruygonzalez-fbu-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
