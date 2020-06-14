package com.example.access_control;
//WATCHDOG CLASS
import android.app.Activity;

public class EPasswordChange {
    public static void invalidChange(Activity activity) {
        Utils.showToast(activity,"Password was not changed");
    }
}
