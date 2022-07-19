package com.metauniversity.behindthebusiness;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

@ParseClassName("BusinessVideo")
public class BusinessVideo extends ParseObject {
    // private instance variables
    private static final String KEY_USER = "user";
    private static final String KEY_VIDEO_FILE = "video";
    private static final String KEY_VIDEO_TYPE = "videoType";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseFile getVideo() {
        return getParseFile(KEY_VIDEO_FILE);
    }

    public void setVideo(ParseFile videoFile) {
        put(KEY_VIDEO_FILE, videoFile);
    }

    public String setVideoType() {
        return getString(KEY_VIDEO_TYPE);
    }

    public void setVideoType(String videoType) {
        put(KEY_VIDEO_TYPE, videoType);
    }
}
