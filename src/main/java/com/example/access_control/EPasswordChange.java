package com.example.access_control;
//WATCHDOG CLASS
import android.app.Activity;

public class EPasswordChange {
    public static void invalidChange(Activity activity) {
        Utils.showToast(activity,"Password was not changed");
    }
<<<<<<< HEAD
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
=======
>>>>>>> 4e1dbdc1ad89c03a136a02ea17bbcc13f344b908
}
