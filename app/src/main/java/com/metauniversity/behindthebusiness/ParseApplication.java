package com.metauniversity.behindthebusiness;

import android.app.Application;

import com.metauniversity.behindthebusiness.R;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(com.metauniversity.behindthebusiness.UserPost.class);
        ParseObject.registerSubclass(com.metauniversity.behindthebusiness.BusinessUser.class);
        ParseObject.registerSubclass(com.metauniversity.behindthebusiness.BusinessVideo.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }
}