package com.unlogicon.typegram.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Nikita Korovkin 18.10.2018.
 */
public class EditorPreferencesUtils {

    public static final String APP_PREFERENCES = "editor_temp";

    public static final String KEY_TITLE = "title";

    public static final String KEY_OGIMGAE = "ogimage";

    public static final String KEY_BODY = "body";

    public static final String KEY_TAG = "tag";

    private SharedPreferences settings = null;

    private  SharedPreferences.Editor editor = null;

    private Context context = null;

    public EditorPreferencesUtils(Context context){
        this.context = context;
        this.settings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        this.editor = settings.edit();
    }

    public String getTitle() {
        return settings.getString(KEY_TITLE, "");
    }

    public void setTitle(String title){
        editor.putString(KEY_TITLE, title);
        editor.commit();
    }

    public String getOgimgae() {
        return settings.getString(KEY_OGIMGAE, "");
    }

    public void setOgimage(String ogimage){
        editor.putString(KEY_OGIMGAE, ogimage);
        editor.commit();
    }

    public String getBody() {
        return settings.getString(KEY_BODY, "");
    }

    public void setBody(String body){
        editor.putString(KEY_BODY, body);
        editor.commit();
    }

    public String getTag() {
        return settings.getString(KEY_TAG, "");
    }

    public void setTag(String tag){
        editor.putString(KEY_TAG, tag);
        editor.commit();
    }

    public void clearAllFields(){
        editor.putString(KEY_TITLE, "");
        editor.commit();
        editor.putString(KEY_OGIMGAE, "");
        editor.commit();
        editor.putString(KEY_BODY, "");
        editor.commit();
        editor.putString(KEY_TAG, "");
        editor.commit();
    }
}
