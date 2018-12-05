package com.github.buzztracker.model;

/**
 * Series of static text verifications for login and registration
 */
public class Verification {

    /**
     * Determines if an email is valid with some scary regex
     *
     * @param email the email to be checked
     * @return true if the email is valid, false otherwise
     */
    public static boolean isPotentialEmail(String email) {
        return email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}" +
                "~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|" +
                "\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*" +
                "[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|" +
                "1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|" +
                "[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:" +
                "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|" +
                "\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");
    }

    /**
     * Verifies if password is strong enough
     * Requires at least:
     *      8 characters
     *      One uppercase letter
     *      One lowercase letter
     *      One number
     *
     * @param password the password to be checked
     * @return true if it is a strong password, false otherwise
     */
    public static boolean isStrongPassword(String password) {
        return (password.length() >= 8)
                && password.matches(".*[A-Z].*") // Has at least one uppercase letter
                && password.matches(".*[a-z].*") // Has at least one lowercase letter
                && password.matches(".*\\d+.*"); // Has at least one number
    }

    /**
     * Determines if a name is possible
     *
     * @param name the name to be checked
     * @return true if it's a legal name, false otherwise
     */
    public static boolean isNameLegal(String name) {
        return name.matches("^[\\p{L}]+$"); // Matches all international characters
    }

    /**
     * Removes -, ', and whitespace from names
     *
     * @param name the name to be edited
     * @return the name without those removed characters
     */
    public static String removeCommonNameChars(String name) {
        String newName;
        newName = name.replaceAll("\\s+", "");
        newName = newName.replaceAll("-", "");
        newName = newName.replaceAll("'", "");
        return newName;
    }

    /**
     * Determines if parsed phone number has only numbers and is not empty
     *
     * @param number the phone number to be checked
     * @return true if the phone number if valid, false otherwise
     */
    public static boolean isPhoneValid(String number) {
        return number.matches("\\d+");
    }

    /**
     * Pulls only numbers out of phone number; removes (), ., -, +, and white space
     *
     * @param phoneNumber the potential phone number to be parsed
     * @return the phone number without the above characters
     */
    public static String parsePhoneNumber(String phoneNumber) {
        String[] splitNumber = phoneNumber.split("(\\s|\\(|\\)|-|\\.|\\+)*");
        StringBuilder parsedNumber = new StringBuilder();
        for (String numbers: splitNumber) {
            parsedNumber.append(numbers);
        }
        return parsedNumber.toString();
    }
}