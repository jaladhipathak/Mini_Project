package com.example.jaladhi.mapapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jaladhi on 25-02-2018.
 */

public class YourPreference {

    private static YourPreference yourPreference;
    private SharedPreferences sharedPreferences;

    public static YourPreference getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new YourPreference(context);
        }
        return yourPreference;
    }

    private YourPreference(Context context) {
        sharedPreferences = context.getSharedPreferences("MAP_APP_PREF",Context.MODE_PRIVATE);
    }

    public void saveData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }
}
