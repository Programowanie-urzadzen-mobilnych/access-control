package com.example.access_control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginMain extends AppCompatActivity {

    private String sensonUSERLOGIN="Admin", sensorUSERPASSWORD="AdminPassword";//dopóki się z niczym nie łączymy - trzeba dodać
    //jeszcze szyfrowanie i nie porównywać bezpośrednio plaintext, ale to już inna funkcjonalność w gancie :P

    private EditText Login;
    private EditText Password;
    private TextView AttemptsLeft;
    private Button Log_In;
    private int counter=5;//how many attempts are left to block login activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        Login = (EditText) findViewById(R.id.editLogin);
        Password = (EditText)findViewById(R.id.editPassword);
        AttemptsLeft = (TextView)findViewById(R.id.textViewAttempts);
        Log_In = (Button)findViewById(R.id.button_LogIn);

        AttemptsLeft.setText("Number of attempts left: "+String.valueOf(counter));

        Log_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Login.getText().toString(), Password.getText().toString());
            }
        });

        //Password.setTransformationMethod(PasswordTransformationMethod.getInstance());//ponoć dobra praktyka użyć tego
    }

    private void validate(String userLogin, String userPassword){

        if((userLogin.equals(sensonUSERLOGIN)) && (userPassword.equals(sensorUSERPASSWORD)))
        {
            Intent intent = new Intent(LoginMain.this, TemporaryActivity.class);
            startActivity(intent);
        }
        else
        {
            counter--;

            AttemptsLeft.setText("Number of attempts left: "+String.valueOf(counter));

            if(counter==0)
            {
                Log_In.setEnabled(false);
            }
        }
    }
}
