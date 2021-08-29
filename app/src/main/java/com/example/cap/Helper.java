package com.example.cap;

import android.content.Context;
import android.content.SharedPreferences;

public class Helper {
    private final String INTRO = "intro";
    private final String ID = "ID";
    private final String NAME ="name";

    private SharedPreferences app_prefs;
        private Context context;

        public Helper(Context context)
        {
            app_prefs = context.getSharedPreferences("shared", 0);
            this.context = context;
        }

        public void putIsLogin(boolean loginOrOut)
        {
            SharedPreferences.Editor edit = app_prefs.edit();
            edit.putBoolean(INTRO , loginOrOut);
            edit.apply();
        }

        public void putID(String loginOrOut)
        {
            SharedPreferences.Editor edit = app_prefs.edit();
            edit.putString(ID, loginOrOut);
            edit.apply();
        }

        public String getID()
        {
            return app_prefs.getString(ID, "");
        }

        public void putName(String loginOrOut)
        {
            SharedPreferences.Editor edit = app_prefs.edit();
            edit.putString(NAME, loginOrOut);
            edit.apply();
        }

        public String getName()
        {
            return app_prefs.getString(NAME, "");
        }
    }