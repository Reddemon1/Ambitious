package com.example.ambitious;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SplashScreen2 extends AppCompatActivity implements View.OnClickListener{
    private Button btnNext;
    private TextView btnskip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);


        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        btnskip = (TextView) findViewById(R.id.btnskip);
        btnskip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNext:
                startActivity(new Intent(this,SplashScreen3.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                break;
            case R.id.btnskip:
                startActivity(new Intent(this,LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                break;
        }
    }
}