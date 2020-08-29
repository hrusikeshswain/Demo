package com.alhafeez.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView txt_name;
    private ConstraintLayout constraint_bookappt;
    private LinearLayout linear_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

    }
    public void initViews(){
        txt_name = findViewById(R.id.textView3);
        constraint_bookappt = findViewById(R.id.constraint_bookappt);
        linear_logout = findViewById(R.id.linear_logout);
        if (AssignmnetSharedPrefrence.getUserName()!=null){
            txt_name.setText(AssignmnetSharedPrefrence.getUserName());
        }
        constraint_bookappt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApptDialogFragment fragment= new ApptDialogFragment();
                fragment.show(getFragmentManager(), "ApptDialogFragment");
            }
        });
        linear_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AssignmnetSharedPrefrence.clearData();
                Intent intent = null;
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
