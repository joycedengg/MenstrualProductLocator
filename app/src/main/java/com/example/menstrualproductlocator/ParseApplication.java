package com.example.menstrualproductlocator;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.livequery.ParseLiveQueryClient;

import java.net.URI;
import java.net.URISyntaxException;


public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Supply.class);
        ParseObject.registerSubclass(Request.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());

        ParseLiveQueryClient parseLiveQueryClient = null;
    }
}
