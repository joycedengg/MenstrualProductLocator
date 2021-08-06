package com.example.menstrualproductlocator;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;

import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Supply.class);
        ParseObject.registerSubclass(Request.class);
        ParseObject.registerSubclass(Message.class);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);



            Parse.initialize(new Parse.Configuration.Builder(this)
                    .applicationId(getString(R.string.back4app_app_id))
                    .clientKey(getString(R.string.back4app_client_key))
                    .clientBuilder(builder)
                    .server("https://estro.b4a.io")
                    .build());
    }
}
