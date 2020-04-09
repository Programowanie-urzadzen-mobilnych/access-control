package com.example.access_control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class LoginMain extends AppCompatActivity {

    private String sensonUSERLOGIN="123", sensorUSERPASSWORD="456";//dopóki się z niczym nie łączymy - trzeba dodać
    //jeszcze szyfrowanie i nie porównywać bezpośrednio plaintext, ale to już inna funkcjonalność w gancie :P

    private EditText Login;
    private EditText Password;
    private TextView AttemptsLeft;
    private Button Log_In;
    private int counterConstant = 5,counter = counterConstant;//how many attempts are left to block login activity
    private ArrayList<HashMap<String, String>> formList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        Login = (EditText) findViewById(R.id.editLogin);
        Password = (EditText)findViewById(R.id.editPassword);
        AttemptsLeft = (TextView)findViewById(R.id.textViewAttempts);
        Log_In = (Button)findViewById(R.id.button_LogIn);

        AttemptsLeft.setText("Number of attempts left: "+String.valueOf(counter));

        //readJSON();

        Log_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Login.getText().toString(), Password.getText().toString());
            }
        });
        //Password.setTransformationMethod(PasswordTransformationMethod.getInstance());//ponoć dobra praktyka użyć tego
    }

    private void validate(String userLogin, String userPassword){

        //sensonUSERLOGIN = formList.get(0).get("Login");
        //sensorUSERPASSWORD = formList.get(0).get("Password");

        if((userLogin.equals(sensonUSERLOGIN)) && (userPassword.equals(sensorUSERPASSWORD)))
        {//check if credentials passed by user equals to what is stored in sensor, if so then go to another activity
            Intent intent = new Intent(LoginMain.this, TemporaryActivity.class);
            startActivity(intent);
        }
        else
        {
            counter--;
            AttemptsLeft.setText("Number of attempts left: "+String.valueOf(counter));
            if(counter==0)
            {//if user used all tries then block login button for X amount of time (currently 5 seconds)
                //zrobić żeby nawet po zamknięciu aplikacji i jej ponownym uruchomieniu nadal odliczało czas
                Log_In.setEnabled(false);
                Toast.makeText(this, "Login functionality is disabled for 5 seconds", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        Log_In.setEnabled(true);
                        counter = counterConstant;
                        AttemptsLeft.setText("Number of attempts left: "+String.valueOf(counter));
                    }
                },5000);// set time as per your requirement
            }
        }
    }

    private void readJSON()
    {//próba odczytania i sparsowania jsona (dodanie do arraylisty hashu z danymi logowania)
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(LoginMain.this));
            JSONArray m_jArry = obj.getJSONArray("Login_Info");
            formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                //Log.d("Details-->", jo_inside.getString("Login"));
                String login_value = jo_inside.getString("Login");
                String password_value = jo_inside.getString("Password");

                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                m_li.put("Login", login_value);
                m_li.put("Password", password_value);

                formList.add(m_li);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset(Context context) {
        //as the name suggest, this method load JSON from asset directory in access-control
        String json = null;
        try {
            InputStream is = context.getAssets().open("login_information.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
