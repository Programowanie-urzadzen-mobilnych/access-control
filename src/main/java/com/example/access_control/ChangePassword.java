package com.example.access_control;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

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
                try {
                    validate(
                            OldLogin.getText().toString(),
                            OldPassword.getText().toString(),
                            NewLogin.getText().toString(),
                            NewPassword.getText().toString(),
                            NewPasswordConfirmation.getText().toString()
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Intent intent = new Intent(ChangePassword.this, TemporaryActivity.class);
                //startActivity(intent);
            }
        });
    }

    private void validate(String oldLogin, String oldPassword, String newLogin, String newPassword, String confirmPassword) throws IOException {

        //parsing json to model in MyLoginModel
        String myJson=inputStreamToString(this.getResources().openRawResource(R.raw.login_information));//reading json file
        MyLoginModel myModel = new Gson().fromJson(myJson, MyLoginModel.class);//converting json string to model
        String sensorUSERLOGIN = myModel.list.get(0).login_;//assigning login value from model
        String sensorUSERPASSWORD = myModel.list.get(0).password_;//assigning password value from model

        if((oldLogin.equals(sensorUSERLOGIN)) && (oldPassword.equals(sensorUSERPASSWORD))) {
            if(!newLogin.equals("") || !newPassword.equals("")){
                if(newPassword.equals(confirmPassword)){
                    ChangeCredentials(newLogin, newPassword);
                    Toast.makeText(this, "Login information were changed",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "New password and password confirmation are not the same",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Fields 'new login' and 'new password' cannot be empty",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if(!oldLogin.equals(sensorUSERLOGIN)){
            Toast.makeText(this, "Old login was not provided or entered value was incorrect",
                    Toast.LENGTH_SHORT).show();
        }
        else if(!oldPassword.equals(sensorUSERPASSWORD)){
            Toast.makeText(this, "Old password was not provided or entered value was incorrect",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public String inputStreamToString(InputStream inputStream) {
        //method to read json file and later save to a model
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }

    private void ChangeCredentials(String newLogin, String newPassword) throws IOException {
        //nie działa :(
        JSONObject object = new JSONObject();
        try {
            object.put("Login", newLogin);
            object.put("Password", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Writer output = null;
        File file = new File("C:\\TEST_ANDROID\\login_information.json");
        output = new BufferedWriter(new FileWriter(file));
        output.write(object.toString());
        output.close();
    }
}
