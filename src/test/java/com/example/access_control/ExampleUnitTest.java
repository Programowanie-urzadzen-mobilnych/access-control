package com.example.access_control;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    /*ODKOMENTOWAĆ PRZY CHĘCI PRZEPROWADZENIA TESTÓW JEDNOSTKOWYCH
    @Test
    public void CheckHash() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String check = "example";
        System.out.println(check);
        check = Hash(check,check);
        System.out.println(check);
    }

    @Test
    public void CheckLogin() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String login_from_file = "User";
        String passwd_from_file = "User";
        login_from_file = Hash(login_from_file,login_from_file);
        passwd_from_file = Hash(passwd_from_file,login_from_file);
        validate("User", "User",login_from_file,passwd_from_file,3);
    }

    @Test
    public void CheckChangePasswd() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String login_from_file = "User";
        String passwd_from_file = "User";
        login_from_file = Hash(login_from_file,login_from_file);
        passwd_from_file = Hash(passwd_from_file,login_from_file);
        validate_2("OldLogin", "OldPasswd", "NewLogin",
                "NewPasswd","NewPasswd",login_from_file,passwd_from_file);
    }
    */
    private void validate_2(String oldLogin, String oldPassword, String newLogin, String newPassword, String confirmPassword, String sensorUSERLOGIN, String sensorUSERPASSWORD) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        //method to validate if entered value are correct and if so, then update login information
        //parsing json to model in MyLoginModel

        oldLogin = Hash(oldLogin,oldLogin);
        newLogin = Hash(newLogin,newLogin);
        oldPassword = Hash(oldPassword, oldLogin);
        newPassword = Hash(newPassword, newLogin);
        confirmPassword = Hash(confirmPassword, newLogin);

        if((oldLogin.equals(sensorUSERLOGIN)) && (oldPassword.equals(sensorUSERPASSWORD))) {
            if(!newLogin.equals("") || !newPassword.equals("")){
                if(newPassword.equals(confirmPassword)){
                    System.out.println("Zmieniono hasło i login");
                    }
                else {
                    System.out.println("Nowe hasło i potwierdzenie hasła nie są takie same");
                }
            }
            else{
                System.out.println("Pola nowy login i nowe hasło nie mogą być puste");
            }
        }
        else if(!oldLogin.equals(sensorUSERLOGIN)){
            System.out.println("Stary login jest niepoprawny");
        }
        else if(!oldPassword.equals(sensorUSERPASSWORD)){
            System.out.println("Stare hasło jest niepoprawne");
        }
    }


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

    private void validate(String userLogin, String userPassword, String sensorUSERLOGIN, String sensorUSERPASSWORD, int counter) throws InvalidKeySpecException, NoSuchAlgorithmException {
        userLogin = Hash(userLogin,userLogin);
        userPassword = Hash(userPassword, userLogin);

        if((userLogin.equals(sensorUSERLOGIN)) && (userPassword.equals(sensorUSERPASSWORD))){
            System.out.println("Zalogowano się");
        }
        else{
            counter--;
            System.out.println("Niepoprawna próba logowania");
            if(counter==0)
            {
                System.out.println("Blokada możliwości zalogowania się");
            }
        }
    }
}