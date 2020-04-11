package com.example.access_control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

public class ChangePassword extends AppCompatActivity {

    private Button btnChangePassword;
    private Button btnGoBack;
    private EditText OldLogin;
    private EditText OldPassword;
    private EditText NewLogin;
    private EditText NewPassword;
    private EditText NewPasswordConfirmation;
    private String sensorUSERLOGIN;
    private String sensorUSERPASSWORD;

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
        btnGoBack = (Button)findViewById(R.id.buttonGoBack);

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
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this, TemporaryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void validate(String oldLogin, String oldPassword, String newLogin, String newPassword, String confirmPassword) throws IOException {
        //method to validate if entered value are correct and if so, then update login information
        //parsing json to model in MyLoginModel

        readJsonFromFile();//TODO zrobić żeby jak nie ma dostępu do pliku (bo np user się rozłączył z czujnikiem ale
        //TODO nie wylogował to wtedy tworzy tymczasowy plik, który nadpisze plik z pasami kiedy użytkownik znowu się połączy)

        if((oldLogin.equals(sensorUSERLOGIN)) && (oldPassword.equals(sensorUSERPASSWORD))) {
            if(!newLogin.equals("") || !newPassword.equals("")){
                if(newPassword.equals(confirmPassword)){
                    ChangeCredentials(newLogin, newPassword);
                    Toast.makeText(this, "Login information were changed",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangePassword.this, TemporaryActivity.class);
                    startActivity(intent);
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

    private void readJsonFromFile(){
        File file = new File(this.getFilesDir(), "login_information.json");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String myJson = inputStreamToString(fileInputStream);//reading json file
        MyLoginModel myModel = new Gson().fromJson(myJson, MyLoginModel.class);//converting json string to model
        sensorUSERLOGIN = myModel.list.get(0).login_;//assigning login value from model
        sensorUSERPASSWORD = myModel.list.get(0).password_;//assigning password value from model
    }

    private void ChangeCredentials(String newLogin, String newPassword) throws IOException {
        //method to update login information file - set new login and password
        String filename = "login_information.json";
        String fileContents = "{\n" +
                "  \"Login_Info\":\n" +
                "  [\n" +
                "    {\n" +
                "      \"Login\": \""+newLogin+"\",\n" +
                "      \"Password\": \""+newPassword+"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
