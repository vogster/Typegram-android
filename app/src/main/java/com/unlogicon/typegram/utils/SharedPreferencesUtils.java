package com.unlogicon.typegram.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Nikita Korovkin 16.10.2018.
 */
public class SharedPreferencesUtils {

    public static final String APP_PREFERENCES = "settings";

    public static final String KEY_TOKEN = "token";

    public static final String KEY_USERNAME = "username";

    private  SharedPreferences settings = null;

    private  SharedPreferences.Editor editor = null;

    private  Context context = null;

    public SharedPreferencesUtils(Context context){
        this.context = context;
        this.settings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        this.editor = settings.edit();
    }

    public void setToken(String token){
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public String getToken(){
        return settings.getString(KEY_TOKEN, "");
    }

    public boolean isAuth(){
        return !settings.getString(KEY_TOKEN, "").equals("");
    }

    public void singOut(){
        editor.putString(KEY_TOKEN, "");
        editor.commit();
        editor.putString(KEY_USERNAME, "");
        editor.commit();
    }

    public void setUsername(String username){
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public String getUsername(){
        return settings.getString(KEY_USERNAME, "");
    }

}
