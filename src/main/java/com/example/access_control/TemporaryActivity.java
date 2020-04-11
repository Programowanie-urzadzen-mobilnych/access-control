package com.example.access_control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TemporaryActivity extends AppCompatActivity {
    //TODO tymczasowa klasa żeby wracać do ekranu logowania/przechodzić do zmiany hasła -
    // TODO dopóki nie bedzie zrobionego symulatora
    private Button button;
    private Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary);

        button = (Button)findViewById(R.id.buttonBack);
        btnChangePassword = (Button)findViewById(R.id.buttonChangeLoginInformationForm);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TemporaryActivity.this, LoginMain.class);
                startActivity(intent);
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TemporaryActivity.this, ChangePassword.class);
                startActivity(intent);
            }
        });
    }
}
