package com.example.menstrualproductlocator;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String USER_ID_KEY = "userId";
    public static final String USER_KEY = "user";
    public static final String BODY_KEY = "body";
    public static final String USERNAME_KEY = "username";

    public String getUserId() {
        return getString(USER_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setUserId(String userId) {
        put(USER_ID_KEY, userId);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public String getUsername() { return getString(USERNAME_KEY); }

    public void setUsername(String username) { put(USERNAME_KEY, username); }

    public ParseUser getUser() {
        return getParseUser(USER_KEY);
    }
}