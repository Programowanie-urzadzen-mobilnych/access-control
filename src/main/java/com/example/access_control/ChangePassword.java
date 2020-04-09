package com.example.access_control;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChangePassword extends AppCompatActivity {

    private Button btnChangePassword;
    private EditText OldLogin;
    private EditText OldPassword;
    private EditText NewLogin;
    private EditText NewPassword;
    private EditText NewPasswordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        OldLogin = (EditText)findViewById(R.id.oldLogin);
        OldPassword = (EditText)findViewById(R.id.oldPassword);
        NewLogin = (EditText)findViewById(R.id.newLogin);
        NewPassword = (EditText)findViewById(R.id.newPassword);
        NewPasswordConfirmation = (EditText)findViewById(R.id.newPassword2);
        btnChangePassword = (Button)findViewById(R.id.buttonChangeLoginInformation);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate();

                //Intent intent = new Intent(ChangePassword.this, TemporaryActivity.class);
                //startActivity(intent);
            }
        });
    }

    private void validate(String oldLogin, String oldPassword, String newLogin, String newPassword, String confirmPassword)
    {

    }
}
