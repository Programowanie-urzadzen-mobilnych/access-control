package com.example.access_control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

//TODO - RZECZY DO POPRAWIENIA/DOROBIENIA/PÓŹNIEJSZEGO UŻYCIA
public class TemporaryActivity extends AppCompatActivity {
    //TODO tymczasowa klasa żeby wracać do ekranu logowania/przechodzić do zmiany hasła -
    // TODO dopóki nie bedzie zrobionego symulatora
    private Button button,btnChangePassword,btnLogOut;
    private TextView txtView;
    private String USER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary);

        button = (Button)findViewById(R.id.buttonBack);
        btnChangePassword = (Button)findViewById(R.id.buttonChangeLoginInformationForm);
        btnLogOut = (Button)findViewById(R.id.button_log_out);
        txtView = (TextView)findViewById(R.id.txtUserID);

        USER_ID = getIntent().getStringExtra("USER_ID");
        if(USER_ID.equals("1"))
            txtView.setText("ADMIN");
        else
            txtView.setText("NORMAL USER");

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
                intent.putExtra("USER_ID",USER_ID);
                startActivity(intent);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndSession();
                Intent intent = new Intent(TemporaryActivity.this, LoginMain.class);
                startActivity(intent);
            }
        });
    }

    private void EndSession()
    {
        String certificate = "";
        File file = new File(this.getFilesDir(), "session_information.json");
        String filename = "session_information.json";
        String fileContents = "{\n" +
                "  \"Session_Info:\":\n" +
                "  [\n" +
                "    {\n" +
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
}
