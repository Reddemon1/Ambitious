package com.example.ambitious;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SplashScreen3 extends AppCompatActivity implements View.OnClickListener {
    private Button btnNext2 , btnback;
    private TextView btnskip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen3);

        btnNext2 = (Button) findViewById(R.id.btnNext2);
        btnNext2.setOnClickListener(this);

        btnback = (Button) findViewById(R.id.btnback);
        btnback.setOnClickListener(this);

        btnskip = (TextView) findViewById(R.id.btnskip2);
        btnskip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNext2:
                startActivity(new Intent(this,SplashScreen4.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                break;
            case R.id.btnback:
                startActivity(new Intent(this,SplashScreen2.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
                break;
            case R.id.btnskip2:
                startActivity(new Intent(this,LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                break;

        }
    }
}