package com.alhafeez.assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Hrusikesh swain on 8/27/2020.
 * Be U Salons
 * hrusikeshswain@beusalons.com
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final String LAST_APP_VERSION = "last_app_version";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AssignmnetSharedPrefrence.init(this);
        checkAppstart();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                if (AssignmnetSharedPrefrence.getIsloggedin()){
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                }else {
                    intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        };
      new Handler().postDelayed(runnable,1000);

    }

    public void checkAppstart(){
        switch (checkAppStart()) {
            case NORMAL:
                // We don't want to get on the user's nerves
                Log.e(LAST_APP_VERSION,"normal");
                break;
            case FIRST_TIME:
                // TODO show a tutorial
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.addUser("user1","123456");
                db.addUser("user2","abcdef");
                db.addUser("user3","abc123");
                break;
            default:
                break;
        }
    }


    public enum AppStart {
        FIRST_TIME, FIRST_TIME_VERSION, NORMAL;
    }

    public AppStart checkAppStart() {
        PackageInfo pInfo;
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        AppStart appStart = AppStart.NORMAL;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int lastVersionCode = sharedPreferences
                    .getInt(LAST_APP_VERSION, -1);
            int currentVersionCode = pInfo.versionCode;
            appStart = checkAppStart(currentVersionCode, lastVersionCode);
            // Update version in preferences
            sharedPreferences.edit().putInt(LAST_APP_VERSION, currentVersionCode).commit();
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(LAST_APP_VERSION, "Unable to determine current app version");
        }
        return appStart;
    }

    public AppStart checkAppStart(int currentVersionCode, int lastVersionCode) {
        if (lastVersionCode == -1) {
            return AppStart.FIRST_TIME;
        } else if (lastVersionCode < currentVersionCode) {
            return AppStart.FIRST_TIME_VERSION;
        } else if (lastVersionCode > currentVersionCode) {
            Log.w(LAST_APP_VERSION, "Current version code (" + currentVersionCode
                    + ") is less then the one recognized on last startup ("
                    + lastVersionCode
                    + "). Defenisvely assuming normal app start.");
            return AppStart.NORMAL;
        } else {
            return AppStart.NORMAL;
        }
    }
}
