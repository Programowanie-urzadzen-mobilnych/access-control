package com.example.access_control;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

//TODO - RZECZY DO POPRAWIENIA/DOROBIENIA/PÓŹNIEJSZEGO UŻYCIA
public class ChangePassword extends AppCompatActivity {

    private Button btnChangePassword,btnGoBack;
    private EditText OldLogin,OldPassword,NewLogin,NewPassword,NewPasswordConfirmation;
    private String sensorUSERLOGIN,sensorUSERPASSWORD, userADMIN_LOG,userADMIN_PASSWD, userUSER_LOG,userUSER_PASSWD;
    private TextView txtView;
    private String USER_ID;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Configure action bar
        Toolbar actionbar = findViewById(R.id.change_password_action_bar);
        actionbar.setTitle(getResources().getString(R.string.CHANGE_PASSWD));
        setSupportActionBar(actionbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        OldLogin = (EditText)findViewById(R.id.oldLogin);
        OldPassword = (EditText)findViewById(R.id.oldPassword);
        NewLogin = (EditText)findViewById(R.id.newLogin);
        NewPassword = (EditText)findViewById(R.id.newPassword);
        NewPasswordConfirmation = (EditText)findViewById(R.id.newPassword2);
        btnChangePassword = (Button)findViewById(R.id.buttonChangeLoginInformation);
        //btnGoBack = (Button)findViewById(R.id.buttonGoBack);

        USER_ID = getIntent().getStringExtra("USER_ID");
        txtView = (TextView)findViewById(R.id.txtUserID);
        if(USER_ID.equals("1"))
            txtView.setText("ADMIN");
        else
            txtView.setText("NORMAL USER");

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
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
        /*
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ChangePassword.this, TemporaryActivity.class);
                //intent.putExtra("USER_ID",USER_ID);
                //startActivity(intent);
                Intent data = new Intent();
                data.putExtra("role",USER_ID);
                setResult(RESULT_OK, data);
                finish();
            }
        });
        */
    }

    private void validate(String oldLogin, String oldPassword, String newLogin, String newPassword, String confirmPassword) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        //method to validate if entered value are correct and if so, then update login information
        //parsing json to model in MyLoginModel

        int id;
        if(USER_ID.equals("1"))
            id=0;
        else
            id=1;

        readJsonFromFile(id);//TODO zrobić żeby jak nie ma dostępu do pliku (bo np user się rozłączył z czujnikiem ale
        //TODO nie wylogował to wtedy tworzy tymczasowy plik, który nadpisze plik z pasami kiedy użytkownik znowu się połączy)
        oldLogin = Hash(oldLogin,oldLogin);
        newLogin = Hash(newLogin,newLogin);
        oldPassword = Hash(oldPassword, oldLogin);
        newPassword = Hash(newPassword, newLogin);
        confirmPassword = Hash(confirmPassword, newLogin);

        if((oldLogin.equals(sensorUSERLOGIN)) && (oldPassword.equals(sensorUSERPASSWORD))) {
            if(!newLogin.equals("") || !newPassword.equals("")){
                if((id == 0 && !newLogin.equals(userUSER_LOG))||(id == 1 && !newLogin.equals(userADMIN_LOG)))//
                {
                    if(newPassword.equals(confirmPassword)){

                        ChangeCredentials(id,newLogin, newPassword);
                        Toast.makeText(this, "Login information were changed",
                                Toast.LENGTH_SHORT).show();

                    /*
                    Intent intent = new Intent(ChangePassword.this, TemporaryActivity.class);
                    intent.putExtra("USER_ID",USER_ID);
                    startActivity(intent);
                    */

                        Intent data = new Intent();
                        data.putExtra("role",USER_ID);
                        setResult(RESULT_OK, data);
                        finish();
                    }
                    else {
                        //Toast.makeText(this, "New password and password confirmation are not the same",
                        //        Toast.LENGTH_SHORT).show();
                        EPasswordChange.passwordsAreNotTheSame(this);
                    }
                }
                else{
                    Toast.makeText(this, "Username taken",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Fields 'new login' and 'new password' cannot be empty",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if(!oldLogin.equals(sensorUSERLOGIN)){
            //Toast.makeText(this, "Old login was not provided or entered value was incorrect",
            //        Toast.LENGTH_SHORT).show();
            EPasswordChange.oldLoginNotProvidedWrong(this);
        }
        else if(!oldPassword.equals(sensorUSERPASSWORD)){
            //Toast.makeText(this, "Old password was not provided or entered value was incorrect",
            //        Toast.LENGTH_SHORT).show();
            EPasswordChange.oldPasswordNotProvidedWrong(this);
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

    private void readJsonFromFile(int id){
        File file = new File(this.getFilesDir(), "login_information.json");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String myJson = inputStreamToString(fileInputStream);//reading json file
        MyLoginModel myModel = new Gson().fromJson(myJson, MyLoginModel.class);//converting json string to model
        sensorUSERLOGIN = myModel.list.get(id).login_;//assigning login value from model
        sensorUSERPASSWORD = myModel.list.get(id).password_;//assigning password value from model
        //TODO poszukać czy da się to rozwiązać inaczej - żeby nie było trzech zestawów haseł w trzech różnych zmiennych
        userADMIN_LOG = myModel.list.get(0).login_;
        userUSER_LOG = myModel.list.get(1).login_;
        userADMIN_PASSWD= myModel.list.get(0).password_;
        userUSER_PASSWD = myModel.list.get(1).password_;
    }
/////////////////////////////////////////
    public String Hash(String stringToHash, String userName) throws NoSuchAlgorithmException, InvalidKeySpecException
    {//hashing method - more secure way of storing passwords/logins than encryption because its one way function
        int iterations = 1000;
        int keyLength = 64*8;
        char[] chars = stringToHash.toCharArray();
        String salt = GetSalt(userName);
        salt = salt.substring(0,28);
        byte[] saltBytes = salt.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, saltBytes, iterations, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return GetSalt(Integer.toString(((iterations-keyLength)*(iterations+keyLength))-saltBytes[0])).substring(0, 10)+
                GetSalt(toHex(saltBytes)).substring(0,20)+toHex(hash);
    }

    private String GetSalt(String salt){
        //MD5 hashing user login and treating it as salt
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
            digest.update(salt.getBytes(Charset.forName("US-ASCII")),0,salt.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
            return hash;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "BŁĄD KONWERTOWANIA LOGINU";
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
/////////////////////////////////////////
    private void ChangeCredentials(int id, String newLogin, String newPassword) throws IOException {
        //method to update login information file - set new login and password

        String filename = "login_information.json";
        String fileContents;
        if(id==0){
            fileContents = "{\n" +
                    "  \"Login_Info\":\n" +
                    "  [\n" +
                    "    {\n" +
                    "      \"ID\": \"1\",\n" +
                    "      \"Login\": \""+newLogin+"\",\n" +
                    "      \"Password\": \""+newPassword+"\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"ID\": \"2\",\n" +
                    "      \"Login\": \""+userUSER_LOG+"\",\n" +
                    "      \"Password\": \""+userUSER_PASSWD+"\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
        }
        else {
            fileContents = "{\n" +
                    "  \"Login_Info\":\n" +
                    "  [\n" +
                    "    {\n" +
                    "      \"ID\": \"1\",\n" +
                    "      \"Login\": \""+userADMIN_LOG+"\",\n" +
                    "      \"Password\": \""+userADMIN_PASSWD+"\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"ID\": \"2\",\n" +
                    "      \"Login\": \""+newLogin+"\",\n" +
                    "      \"Password\": \""+newPassword+"\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
        }

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
