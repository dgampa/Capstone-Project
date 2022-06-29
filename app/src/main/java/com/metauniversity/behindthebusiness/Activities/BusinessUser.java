package com.metauniversity.behindthebusiness.Activities;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

@ParseClassName("BusinessUser")
public class BusinessUser extends ParseObject {
    // private instance variables for different fields of BusinessUser
    private static final String KEY_BUSINESS_USER = "businessUser";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_SocialFb = "socialFacebook";
    private static final String KEY_RATING = "rating";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_ONLINE = "isOnline";
    private static final String KEY_PROFILEPIC = "profilePic";

    public ParseUser getUser() {
        return getParseUser(KEY_BUSINESS_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_BUSINESS_USER, user);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getSocialFB() {
        return getString(KEY_SocialFb);
    }

    public void setSocialFB(String social) {
        put(KEY_SocialFb, social);
    }

    public int getRating() {
        return getInt(KEY_RATING);
    }

    public void setRating(int rating) {
        put(KEY_RATING, rating);
    }

    public String getCategory() {
        return getString(KEY_CATEGORY);
    }

    public void setCategory(String category) {
        put(KEY_CATEGORY, category);
    }

    public String getLocation() {
        return getString(KEY_LOCATION);
    }

    public void setLocation(String location) {
        put(KEY_LOCATION, location);
    }

    public boolean getOnline() {
        return getBoolean(KEY_ONLINE);
    }

    public void setOnline(boolean isOnline) {
        put(KEY_ONLINE, isOnline);
    }

    public ParseFile getProfileImage() {
        return getParseFile(KEY_PROFILEPIC);
    }

    public void setProfileImage(ParseFile profilePic) {
        put(KEY_PROFILEPIC, profilePic);
    }
}
