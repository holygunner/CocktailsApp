package com.holygunner.cocktailsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.holygunner.cocktailsapp.save.Saver;
import com.holygunner.cocktailsapp.tools.ToastBuilder;

public class AgeVerificationActivity extends AppCompatActivity implements View.OnClickListener {
    private Button yesButton;
    private Button noButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.age_verification_layout);
        yesButton = findViewById(R.id.yes_button);
        noButton = findViewById(R.id.no_button);
        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yes_button:
                //
                Saver.writeVerificationComplete(this, true);
                startActivity(new Intent(this, SelectIngredientsActivity.class));
                break;
            case R.id.no_button:
                ToastBuilder.getAgeDisclaimerToast(this).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
        }
    }
}
