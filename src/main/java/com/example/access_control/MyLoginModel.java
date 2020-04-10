package com.example.access_control;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyLoginModel{
    @SerializedName("Login_Info")
    public ArrayList<MyObject> list;

    static public class MyObject {
        @SerializedName("Login")
        public String login_;
        @SerializedName("Password")
        public String password_;
    }
}
