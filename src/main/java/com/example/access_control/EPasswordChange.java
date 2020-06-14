package com.example.access_control;
//WATCHDOG CLASS
import android.app.Activity;

public class EPasswordChange {
    public static void invalidChange(Activity activity) {
        Utils.showToast(activity,"Password was not changed");
    }
    public static void passwordsAreNotTheSame(Activity activity){
        Utils.showToast(activity, "Passwords are NOT the same");
    }
    public static void fieldsLoginAndPasswordAreTheSame(Activity activity){
        Utils.showToast(activity,"Login and password cannot be the same");
    }
    public static void oldLoginNotProvidedWrong(Activity activity){
        Utils.showToast(activity, "Please reenter old login");
    }
    public static void oldPasswordNotProvidedWrong(Activity activity){
        Utils.showToast(activity, "Please reenter old password");
    }
}
