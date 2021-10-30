package com.example.cap;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.annotations.SerializedName;

public class Helper {
    //Helper
    //로그인과 회원가입 부분에서  데이터를 전달 저장 함
    private final String INTRO = "intro";
    private final String userID = "userID";
    private final String userPW ="userPW";
    private final String AIRID = "AIRID";


    private SharedPreferences app_prefs; //쉐어드 부분
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
            edit.putString(userID, loginOrOut);
            edit.apply();
        }

        public String getID()
        {
            return app_prefs.getString(userID, "");
        }

        public void putPW(String loginOrOut)
        {
            SharedPreferences.Editor edit = app_prefs.edit();
            edit.putString(userPW, loginOrOut);
            edit.apply();
        }

        public String getPW()
        {
            return app_prefs.getString(userPW, "");
        }

        public void PutIsData(boolean b) {
            SharedPreferences.Editor edit = app_prefs.edit();
            edit.putBoolean(INTRO , b);
            edit.apply();
        }

        public void putAIRID(String airid) {
            {
                SharedPreferences.Editor edit = app_prefs.edit();
                edit.putString(AIRID, airid);
                edit.apply();
            }
        }
}