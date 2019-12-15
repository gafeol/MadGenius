package com.example.madgenius;

import android.content.Context;
import android.content.SharedPreferences;

public class SavedInfo {
    public static void saveUsername(Context context, String newUsername){
        SharedPreferences settings = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", newUsername.trim());
        editor.apply();
    }
    public static String getUsername(Context context) {
        SharedPreferences settings = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return settings.getString("username", "");
    }
}
