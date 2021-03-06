package com.example.access_control;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
//import com.representation.ThisApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

//TODO - RZECZY DO POPRAWIENIA/DOROBIENIA/PÓŹNIEJSZEGO UŻYCIA
public class LoginMain extends AppCompatActivity {

    private CountDownTimer mCountDownTimer;
    private long LoginLockTime = 0;
    private long SavedTime = 0;
    private long LoginLockTimeSaved = 0;
    private boolean mTimerRunning = false;
    private boolean LoginLock = false;

    private String sensorUSERID_ADMIN="1", sensorUSERID="2";
    private String sensorUSERLOGIN_ADMIN="Admin", sensorUSERLOGIN="User",
            sensorUSERPASSWORD_ADMIN="Admin",sensorUSERPASSWORD="User";

    private EditText Login;
    private EditText Password;
    private TextView AttemptsLeft;
    private TextView LockTime;
    private Button Log_In;
    private int counterConstant = 3,counter = counterConstant;//how many attempts are left to block login activity
    private ArrayList<HashMap<String, String>> formList;//used in readJSON() method
    HashMap<String, String> parsedData;

    @Override
    protected void onRestart() {
        super.onRestart();
        EndSession();
        Login = (EditText) findViewById(R.id.editLogin);
        Password = (EditText) findViewById(R.id.editPassword);
        AttemptsLeft = (TextView) findViewById(R.id.textViewAttempts);
        LockTime = (TextView) findViewById(R.id.DisplayLockTime);
        Log_In = (Button) findViewById(R.id.button_LogIn);

        Log_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validate(Login.getText().toString(), Password.getText().toString());
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                //isExternalStorageReadable();
                //isExternalStorageWritable();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EndSession();
        Login = (EditText) findViewById(R.id.editLogin);
        Password = (EditText) findViewById(R.id.editPassword);
        AttemptsLeft = (TextView) findViewById(R.id.textViewAttempts);
        LockTime = (TextView) findViewById(R.id.DisplayLockTime);
        Log_In = (Button) findViewById(R.id.button_LogIn);

        Log_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validate(Login.getText().toString(), Password.getText().toString());
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                //isExternalStorageReadable();
                //isExternalStorageWritable();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        EndSession();
        // Configure action bar
        Toolbar actionbar = findViewById(R.id.login_main_action_bar);
        actionbar.setTitle(getResources().getString(R.string.LOGIN_MAIN));
        setSupportActionBar(actionbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);


        Login = (EditText) findViewById(R.id.editLogin);
        Password = (EditText) findViewById(R.id.editPassword);
        AttemptsLeft = (TextView) findViewById(R.id.textViewAttempts);
        LockTime = (TextView) findViewById(R.id.DisplayLockTime);
        Log_In = (Button) findViewById(R.id.button_LogIn);

        try {
            sensorUSERLOGIN_ADMIN = Hash(sensorUSERLOGIN_ADMIN, sensorUSERLOGIN_ADMIN);
            sensorUSERPASSWORD_ADMIN = Hash(sensorUSERPASSWORD_ADMIN, sensorUSERLOGIN_ADMIN);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        try {
            sensorUSERLOGIN = Hash(sensorUSERLOGIN, sensorUSERLOGIN);
            sensorUSERPASSWORD = Hash(sensorUSERPASSWORD, sensorUSERLOGIN);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        temporaryCreateJson(sensorUSERLOGIN_ADMIN,sensorUSERPASSWORD_ADMIN,sensorUSERLOGIN,sensorUSERPASSWORD);
        ////////////////////////////////parsing json to model in MyLoginModel
        //readJSON();
        /*String myJson=inputStreamToString(this.getResources().openRawResource(R.raw.login_information));//reading json file
        MyLoginModel myModel = new Gson().fromJson(myJson, MyLoginModel.class);//converting json string to model
        sensorUSERLOGIN = myModel.list.get(0).login_;//assigning login value from model
        sensorUSERPASSWORD = myModel.list.get(0).password_;//assigning password value from model
        *////////////////////////////////

        Log_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validate(Login.getText().toString(), Password.getText().toString());
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                //isExternalStorageReadable();
                //isExternalStorageWritable();
            }
        });
        //Password.setTransformationMethod(PasswordTransformationMethod.getInstance());//TODO ponoć dobra praktyka użyć tego do hasła
    }

    private void validate(String userLogin, String userPassword) throws InvalidKeySpecException, NoSuchAlgorithmException {
        //check if credentials passed by user equals to what is stored in sensor, if so then go to another activity

        readJsonFromFile();
        userLogin = Hash(userLogin,userLogin);
        userPassword = Hash(userPassword, userLogin);//thanks to this, we are comparing two hashes if they are equal

        if((userLogin.equals(sensorUSERLOGIN_ADMIN)) && (userPassword.equals(sensorUSERPASSWORD_ADMIN))){
            CreateSessionJson("1");

            /*
            Intent intent = new Intent(LoginMain.this, TemporaryActivity.class);
            intent.putExtra("USER_ID",sensorUSERID_ADMIN);
            startActivity(intent);
            */


            Intent data = new Intent();
            //String text = "1";//ADMIN
            data.putExtra("role",1);//ADMIN
            //---set the data to pass back---
            //data.setData(Uri.parse(text));
            setResult(RESULT_OK, data);
            //---close the activity---
            finish();
        }
        else if((userLogin.equals(sensorUSERLOGIN))&&(userPassword.equals(sensorUSERPASSWORD))){
            CreateSessionJson("2");
            /*
            Intent intent = new Intent(LoginMain.this, TemporaryActivity.class);
            intent.putExtra("USER_ID",sensorUSERID);
            startActivity(intent);
            */

            Intent data = new Intent();
            //String text = "2";//NORMAL_USER
            data.putExtra("role",2);//NORMAL_USER
            //---set the data to pass back---
            //data.setData(Uri.parse(text));
            setResult(RESULT_OK, data);
            //---close the activity---
            finish();
        }
        else{
            counter--;
            AttemptsLeft.setText("Number of attempts left: "+String.valueOf(counter));
            //Toast.makeText(this, "Incorrect login or password", Toast.LENGTH_SHORT).show();
            ELogin.loginError(this);
            if(counter==0)
            {//if user used all tries then block login button for X amount of time (currently 5 seconds)
                /*Log_In.setEnabled(false);
                Toast.makeText(this, "Login functionality is disabled for 5 seconds", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        Log_In.setEnabled(true);
                        counter = counterConstant;
                        AttemptsLeft.setText("Number of attempts left: "+String.valueOf(counter));
                    }
                },5000);// set time as per your requirement*/
                StartTimer();
            }
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", LoginLockTimeSaved);
        editor.putBoolean("timerRunning", mTimerRunning);
        //editor.putLong("endTime", LoginLockTime);
        editor.putLong("endTime",SavedTime-1000);
        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimerRunning = prefs.getBoolean("timerRunning", false);
        //UpdateTimer();
        if (mTimerRunning)
        {
            LoginLockTimeSaved = prefs.getLong("millisLeft", 6000);
            LoginLockTime = prefs.getLong("endTime", 6000);
            LoginLockTimeSaved = LoginLockTime - System.currentTimeMillis();
            //LoginLock =  prefs.getBoolean("LoginLock", true);
            LoginLockTime=(LoginLockTime-5000)/2;
            StartTimer();
        }
    }
    private void UpdateTimer()
    {
        int minutes = (int) (LoginLockTimeSaved / 1000) / 60;
        int seconds = (int) (LoginLockTimeSaved / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        LockTime.setText(timeLeftFormatted);
    }

    private void StartTimer()
    {
        LoginLockTime=(LoginLockTime*2)+5000;
        LoginLockTimeSaved = LoginLockTime;
        Log_In.setEnabled(false);
        mTimerRunning = true;
        Toast.makeText(this, "Login functionality is disabled for "+ LoginLockTime/1000 +" seconds", Toast.LENGTH_SHORT).show();
        mCountDownTimer = new CountDownTimer(LoginLockTimeSaved+1000, 1000)
        { //Set Timer
            public void onTick(long millisUntilFinished)
            {
                LoginLockTimeSaved = millisUntilFinished;
                SavedTime = millisUntilFinished;
                UpdateTimer();
            }
            @Override
            public void onFinish() {
                Log_In.setEnabled(true);
                counter = counterConstant;
                // AttemptsLeft.setText("Number of attempts left: "+String.valueOf(counter));
                LockTime.setText("");
                AttemptsLeft.setText("");
                mTimerRunning = false;
            }
        }.start();
    }

    /* Checks if external storage is available for read and write */
    public void isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(this, "MOŻNA PISAC", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "NIE MOŻNA PISAC", Toast.LENGTH_LONG).show();
        }
    }
    /* Checks if external storage is available to at least read */
    public void isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Toast.makeText(this, "MOŻNA CZYTAC", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "NIE MOŻNA CZYTAC", Toast.LENGTH_LONG).show();
        }
    }
///////////////////////////////////////////////////////////////////
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
        return GetSalt(Integer.toString(((iterations-keyLength)*(iterations+keyLength))-saltBytes[0])).substring(0, 10)
                +GetSalt(toHex(saltBytes)).substring(0,20)+toHex(hash);
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
///////////////////////////////////////////////////////////////////
    public void CreateSessionJson(String UserID)
    {
        String certificate = "";
        if(UserID == "1")
            certificate = "Privileged_User";
        else if(UserID == "2")
            certificate = "Normal_User";
        File file = new File(this.getFilesDir(), "session_information.json");
        String filename = "session_information.json";
        String fileContents = "{\n" +
                "  \"Session_Info:\":\n" +
                "  [\n" +
                "    {\n" +
                "      \"ID\": \""+UserID+"\",\n" +
                "      \"Certificate\": \""+certificate+"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void EndSession(){
        String certificate = "";
        File file = new File(this.getFilesDir(), "session_information.json");
        String filename = "session_information.json";
        String fileContents = "{\n" +
                "  \"Session_Info:\":\n" +
                "  [\n" +
                "    {\n" +
                "      \"ID\": \""+0+"\",\n" +
                "      \"Certificate\": \"No_User\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void temporaryCreateJson(String AdminLogin, String AdminPasswd, String UserLogin, String UserPasswd){
    //TODO TYMCZASOWA METODA DO ROBIENIA PLIKU JSON (PRZY PIERWSZYM URUCHOMIENIU PROGRAMU (TAKIM PIERWSZYM PIERWSZYM - POTEM
        // TODO KIEDY URUCHAMIASZ ZNOWU TO PLIK BEDZIE ISTNIAL I SIE NIE WYKONA) DOPÓKI NIE BEDZIE ZROBIONY SYMULATOR

        File file = new File(this.getFilesDir(), "login_information.json");
        if(!file.exists()) {
            // TODO - towrzenie pliku na emulatorze telefonu - używamy tej metody całej dopóty nie będziemy mieć dostępu do czujnika
            String filename = "login_information.json";
            String fileContents = "{\n" +
                    "  \"Login_Info\":\n" +
                    "  [\n" +
                    "    {\n" +
                    "      \"ID\": \"1\",\n" +
                    "      \"Login\": \""+AdminLogin+"\",\n" +
                    "      \"Password\": \""+AdminPasswd+"\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"ID\": \"2\",\n" +
                    "      \"Login\": \""+UserLogin+"\",\n" +
                    "      \"Password\": \""+UserPasswd+"\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(fileContents.getBytes());
                outputStream.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
///////////////////////////////////////////////////////////////////
    public void readJsonFromFile(){
        //method to put json file into InputStream of json parser (public string inputStreamToString())
        File file = new File(this.getFilesDir(), "login_information.json");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String myJson = inputStreamToString(fileInputStream);//parsing json file using method inputStreamToString
        MyLoginModel myModel = new Gson().fromJson(myJson, MyLoginModel.class);//converting json string to model
        sensorUSERID_ADMIN = myModel.list.get(0).id_;//assigning first id value from model to variable
        sensorUSERLOGIN_ADMIN = myModel.list.get(0).login_;//assigning first login value from model to variable
        sensorUSERPASSWORD_ADMIN = myModel.list.get(0).password_;//assigning first password value from model to variable
        ///////TODO poszukać czy da się to rozwiązać inaczej - żeby nie było dwóch zestawów haseł w dwóch różnych zmiennych
        sensorUSERID = myModel.list.get(1).id_;//assigning second id value from model to variable
        sensorUSERLOGIN = myModel.list.get(1).login_;//assigning second login value from model to variable
        sensorUSERPASSWORD = myModel.list.get(1).password_;//assigning second password value from model to variable
    }
///////////////////////////////////////////////////////////////////
    public String inputStreamToString(InputStream inputStream) {
    //method to read and parse json file and later save it to a model
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        }
        catch (IOException e) {
            return null;
        }
    }
////////////////////////////////////////////////////////////////////currently not used
    public void ReadFile (){
    try {
        File yourFile = new File(Environment.getExternalStorageDirectory(), "/sdcard/login_information.txt");
        FileInputStream stream = new FileInputStream(yourFile);
        String jsonStr = null;
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            jsonStr = Charset.defaultCharset().decode(bb).toString();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            stream.close();
        }
        JSONObject jsonObj = new JSONObject(jsonStr);

        // Getting data JSON Array nodes
        JSONArray data  = jsonObj.getJSONArray("Login_Info");

        // looping through All nodes
        for (int i = 0; i < data.length(); i++) {
            JSONObject c = data.getJSONObject(i);

            String _login = c.getString("Login");
            String _password = c.getString("Password");

            // tmp hashmap for single node
            parsedData = new HashMap<String, String>();

            // adding each child node to HashMap key => value
            parsedData.put("Login", _login);
            parsedData.put("Password", _password);
            //Toast.makeText(this, _login, Toast.LENGTH_LONG).show();
            //Toast.makeText(this, _password, Toast.LENGTH_LONG).show();

            // do what do you want on your interface
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
////////////////////////////////////////////////////////////////////currently not used
//doesn't work - null object exception
    private void _readJSON(){//TODO próba odczytania i sparsowania jsona (dodanie do arraylisty hashu z danymi logowania)
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
