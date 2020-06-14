package com.example.access_control;
//WATCHDOG CLASS
import android.app.Activity;

public class ELogin {

    public static void loginError(Activity activity) {
        Utils.showToast(activity,"Wrong login data");
    }
}
