package com.alhafeez.assignment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * Defining layout items.
     **/
    private EditText edt_email,edt_passowrd;
    private CheckBox chkbox_remember;
    private TextView txt_signin;
    private boolean isUersexist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log);

        edt_email = findViewById(R.id.edt_email);
        edt_passowrd =  findViewById(R.id.edt_passowrd);
        txt_signin = findViewById(R.id.txt_signin);
        chkbox_remember = findViewById(R.id.chkbox_remember);
        txt_signin.setOnClickListener(this);


    }








    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onClick(View v) {
        if (v==txt_signin){
            if (edt_email.getText().toString().trim().isEmpty()){
                showToast("Username cannot be empty");
            }else if (edt_passowrd.getText().toString().trim().isEmpty()){
                showToast("Password cannot be empty");
            }else {
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                ArrayList<HashMap<String, String>> allUsers = db.getAllUsers();
                for (int k=0;k<allUsers.size();k++){
                    if (allUsers.get(k).get("username").equals(edt_email.getText().toString())){
                        isUersexist = true;
                        if (allUsers.get(k).get("password").equals(edt_passowrd.getText().toString())){
                            showToast("Login successful");
                            if (chkbox_remember.isChecked()){
                                AssignmnetSharedPrefrence.saveUser(edt_email.getText().toString(),true);
                            }else {
                                AssignmnetSharedPrefrence.saveUser(edt_email.getText().toString(),false);
                            }
                            Intent moveto = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(moveto);
                            finish();
                        }else {
                            showToast("Incorrect password");
                        }
                    }else {
                        isUersexist = false;
                    }
                }
                if (!isUersexist){
                    showToast("User not exist");
                }

            }
        }

    }

    private void showToast(String msg){
        Toast.makeText(this, ""+msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


}