package com.github.buzztracker.model;

public class Verification {

    // Verifies all possible email addresses with complicated regex
    public static boolean isPotentialEmail(String email) {
        if (email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
                "|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|" +
                "\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*" +
                "[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|" +
                "1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|" +
                "[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:" +
                "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|" +
                "\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])")) {
            return true;
        }
        return false;
    }

    // Verify strength of password
    // Requires at least:
    //     8 characters
    //     One uppercase letter
    //     One lowercase letter
    //     One number
    public static boolean isStrongPassword(String password) {
        if (password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*\\d+.*")) {
            return true;
        }
        return false;
    }

    // Determines if entered name is a possible name
    public static boolean isNameLegal(String name) {
        // Matches all international characters
        return name.matches("^[\\p{L}]+$");
    }

    // Removes -, ', and whitespace from names
    public static String removeCommonNameChars(String name) {
        String newName;
        newName = name.replaceAll("\\s+", "");
        newName = newName.replaceAll("-", "");
        newName = newName.replaceAll("'", "");
        return newName;
    }

    // Determines if parsed phone number has only numbers and is not empty
    public static boolean isPhoneValid(String number) {
        return number.matches("\\d+");
    }

    // Pulls only numbers out of phone number; removes (), ., -, +, and white space
    public static String parsePhoneNumber(String phoneNumber) {
        String[] splitNumber = phoneNumber.split("(\\s|\\(|\\)|-|\\.|\\+)*");
        String parsedNumber = "";
        for (String numbers: splitNumber) {
            parsedNumber += numbers;
        }
        return parsedNumber;
    }
}