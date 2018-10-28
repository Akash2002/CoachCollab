package com.example.akash.coachcollab;

/**
 * Created by akash on 12/23/2017.
 */

public class InputChecker{

    public static final String EMAIL_ERROR_MESSAGE = "Email entered is incorrect. Please try again.";
    public static final String PASSWORD_ERROR_MESSAGE = "Please check your password. They are either too short or they don't match";

    public static boolean checkEmail(String email){
        if(email.length() > 10){
            if(email.contains("@")){
                if(email.substring(0,email.indexOf('@')).length() > 0){
                    if(email.substring(email.indexOf('@')).length() > 3){
                        if(email.contains(".com")){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkPassword(String password, String repassword){
        if(password.length()>7){
          if(repassword.equals(password)){
              return true;
          }
        }
        return false;
    }

}
