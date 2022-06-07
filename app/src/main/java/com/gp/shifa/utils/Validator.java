package com.gp.shifa.utils;

public abstract class Validator {
    public static boolean isAlpha(String str) {
        return str.matches("^[\\u0600-\\u065F\\u066A-\\u06EF\\u06FA-\\u06FFa-zA-Z ]*$");
        //return true;
    }

    public static boolean isNumbersOnly(String str) {
        return str.matches("^[0-9]+$");
    }

    public static boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
    }

    public static boolean isValidMobile(String mobile) {
        return mobile.matches("^\\+?\\d+$");
    }
}
