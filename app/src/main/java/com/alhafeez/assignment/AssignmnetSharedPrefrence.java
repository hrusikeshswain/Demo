package com.alhafeez.assignment;

import android.content.Context;
import android.content.SharedPreferences;


public class AssignmnetSharedPrefrence {

    private static final String TAG= AssignmnetSharedPrefrence.class.getSimpleName();
    private static SharedPreferences sharedPref;

    private static final String PREF_NAME = "assignment_preference";
    private static final String USER_NAME="username";
    private static final String ISLOGGEDIN = "loggedin";




    public static void init(Context context) {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }



    public static void clearData() {
        sharedPref.edit().clear().apply();
    }




    public static void saveUser(String username,boolean isloggedin){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_NAME, username);
        editor.putBoolean(ISLOGGEDIN, isloggedin);
        editor.apply();
    }

    public static String getUserName(){
        return sharedPref.getString(USER_NAME, null);
    }

    public static boolean getIsloggedin(){
        return sharedPref.getBoolean(ISLOGGEDIN, false);
    }






}
