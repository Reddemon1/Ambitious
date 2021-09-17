package com.example.ambitious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnforgot , btnback;
    private EditText TextEmail;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        mAuth = FirebaseAuth.getInstance();

        btnforgot = (Button) findViewById(R.id.submitforgot);
        btnforgot.setOnClickListener(this);

        btnback = (Button) findViewById(R.id.forgetBack);
        btnback.setOnClickListener(this);

        TextEmail = (EditText) findViewById(R.id.textEmail);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.submitforgot:
                submitforgot();
                break;
            case R.id.forgetBack:
                startActivity(new Intent(ForgetActivity.this , LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                break;
        }
    }

    private void submitforgot() {
        String email = TextEmail.getText().toString().trim();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgetActivity.this,"Check Your Email to Change Our Password",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ForgetActivity.this, LoginActivity.class));
                }else{
                    Toast.makeText(ForgetActivity.this,"We Cannot Reset Your Password",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ForgetActivity.this, LoginActivity.class));
                }
            }
        });
    }
}