package com.example.access_control;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MySessionModel {
    @SerializedName("Session_Info")
    public ArrayList<MyObject> list;

    static public class MyObject {
        @SerializedName("ID")
        public String id_;
        @SerializedName("Certificate")
        public String certificate_;
    }
}
