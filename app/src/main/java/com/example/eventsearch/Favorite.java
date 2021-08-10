package com.example.eventsearch;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Favorite {

    SharedPreferences sp;
    private String DB_KEY = "db";

    Favorite(SharedPreferences sp) {
        this.sp = sp;

    }

    public void addFavorite(JSONObject tmEvent) {
        SharedPreferences.Editor editor = sp.edit();
        try {
            editor.putString(tmEvent.getString("id"), tmEvent.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    public void removeFavorite(String id) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(id);
        editor.commit();
    }
    public boolean isFavorite(String id) {
        return !sp.getString(id, "null").equalsIgnoreCase("null");
    }


   public JSONArray getFavorites()  {

       JSONArray fav_list = new JSONArray();

       for (Map.Entry<String, ?> entry : sp.getAll().entrySet()) {
           try {
               fav_list.put(new JSONObject(entry.getValue().toString()));
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
       return fav_list;
    }

}
