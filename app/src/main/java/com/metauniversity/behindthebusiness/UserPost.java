package com.metauniversity.behindthebusiness;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.parceler.Parcel;

@ParseClassName("IndividualPost")
@Parcel(analyze = UserPost.class)
public class UserPost extends ParseObject {
    // private instance variables for different fields of post
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_RATING = "rating";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_LIKES_BY_USERS = "usersLiked";
    public static final String KEY_BUSINESS = "businessFor";

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile imageFile) {
        put(KEY_IMAGE, imageFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public Number getRating() {
        return getNumber(KEY_RATING);
    }

    public void setRating(Number rating) {
        put(KEY_RATING, rating);
    }

    public int getLikes() {
        return getInt(KEY_LIKES);
    }

    public void setLikes(int likes) {
        put(KEY_LIKES, likes);
    }

    public JSONArray getUsersLiked() {
        return getJSONArray(KEY_LIKES_BY_USERS);
    }

    public void setUsersLiked(JSONArray userLiked) {
        put(KEY_LIKES_BY_USERS, userLiked);
    }

    public ParseUser getBusiness() {
        return getParseUser(KEY_BUSINESS);
    }

    public void setBusiness(ParseUser business) {
        put(KEY_BUSINESS, business);
    }
}
