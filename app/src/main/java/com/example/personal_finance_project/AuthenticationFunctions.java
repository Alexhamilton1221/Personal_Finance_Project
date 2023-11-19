package com.example.personal_finance_project;

import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationFunctions {

    public static boolean validate(String name, String password) {
        // Check if fields are empty
        return !(name.isEmpty() && password.isEmpty());
    }

    public static void showErrorMessage(RegisterActivity activity, String errorMessage) {
        // Provide more specific error messages based on the exception
        if (errorMessage != null) {
            if (errorMessage.contains("email address is badly formatted")) {
                Toast.makeText(activity, "Invalid email format.", Toast.LENGTH_SHORT).show();
            } else if (errorMessage.contains("email address is already in use")) {
                Toast.makeText(activity, "Email address is already in use.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Failed Account Creation: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "Failed Account Creation.", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9_+&*-]+(?:\\.[A-Za-z0-9_+&*-]+)*@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
