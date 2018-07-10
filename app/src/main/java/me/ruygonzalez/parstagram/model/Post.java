package me.ruygonzalez.parstagram.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


@ParseClassName("Post")
public class Post extends ParseObject{
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    // ParceFile is a class in the sdk that allows us to access files or images that we store with parce
    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public static class Query extends ParseQuery<Post> { // to query our post model (since it's in Post class)

        public Query() {
            super(Post.class);
        }

        // gets top posts
        public Query getTop() {
            setLimit(20); //only gets top 20 posts
            return this; // called the Builder pattern
        }

        public Query withUser() { // so that queries include user (have a user attached)
            include("user"); // basically the same thing we did in our API Console in Parse dashboard to get user information when quering the Post class
            return this;
        }

    }

}
