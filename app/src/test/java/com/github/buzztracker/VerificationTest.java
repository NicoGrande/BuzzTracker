package com.github.buzztracker;

import com.github.buzztracker.model.Verification;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;


/**
 * Guillermo Nicolas Grande's JUnit tests for Verification class.
 */
public class VerificationTest {

    @Test
    public void testIsPotentialEmail() {

        ArrayList<String> testEmails = new ArrayList<>();
        testEmails.add("thisisnotanemail");
        testEmails.add("thisisslightlybetter.com");
        testEmails.add("042023490234");
        testEmails.add("nowthisiswhaticallan@email.com");
        int i = 0;

        for (String email : testEmails) {

            Boolean isEmail = Verification.isPotentialEmail(email);

            if (i == 0) {

                assertEquals(isEmail, false);
                i++;

            } else if (i == 1) {

                assertEquals(isEmail, false);
                i++;

            } else if (i == 2) {

                assertEquals(isEmail, false);
                i++;

            } else {

                assertEquals(isEmail, true);

            }
        }
    }

    @Test
    public void testIsStrongPassword() {

        ArrayList<String> testPasswords = new ArrayList<>();
        testPasswords.add("notverygood");
        testPasswords.add("short");
        testPasswords.add("1234567890");
        testPasswords.add("ALLCAPSNOTGOOD");
        testPasswords.add("Agoodpassword123");
        int i = 0;

        for (String password : testPasswords) {

            Boolean isGoodPassword = Verification.isStrongPassword(password);

            if (i == 0) {

                assertEquals(isGoodPassword, false);
                i++;

            } else if (i == 1) {

                assertEquals(isGoodPassword, false);
                i++;

            } else if (i == 2) {

                assertEquals(isGoodPassword, false);
                i++;

            } else if (i == 3) {

                assertEquals(isGoodPassword, false);
                i++;

            } else {

                assertEquals(isGoodPassword, true);

            }
        }
    }

    @Test
    public void isNameLegal() {

        ArrayList<String> testNames = new ArrayList<>();
        testNames.add("Bob");
        testNames.add("B0b");
        testNames.add("Nicolás");
        testNames.add("");
        testNames.add("notaName47");
        int i = 0;

        for (String name : testNames) {

            Boolean isGoodName = Verification.isNameLegal(name);

            if (i == 0) {

                assertEquals(isGoodName, true);
                i++;

            } else if (i == 1) {

                assertEquals(isGoodName, false);
                i++;

            } else if (i == 2) {

                assertEquals(isGoodName, true);
                i++;

            } else if (i == 3) {

                assertEquals(isGoodName, false);
                i++;

            } else {

                assertEquals(isGoodName, false);

            }
        }

    }

    @Test
    public void testRemoveCommonNameChars() {
        String testName = "Bob -Waters ''''";
        String correctName = "BobWaters";
        assertEquals(Verification.removeCommonNameChars(testName), correctName);

    }

    @Test
    public void testIfPhoneValid() {

        ArrayList<String> testNumber = new ArrayList<>();
        testNumber.add("");
        testNumber.add("B0bnkjgui");
        testNumber.add("1234567890");
        int i = 0;

        for (String number : testNumber) {

            Boolean isGoodNumber = Verification.isPhoneValid(number);

            if (i == 0) {

                assertEquals(isGoodNumber, false);
                i++;

            } else if (i == 1) {

                assertEquals(isGoodNumber, false);
                i++;

            } else {

                assertEquals(isGoodNumber, true);

            }
        }

    }

    @Test
    public void testParsePhoneNumber() {

        String badNumber = "+54 38.293-478 (463)";
        String solution = "5438293478463";
        String goodNumber = Verification.parsePhoneNumber(badNumber);
        assertEquals(solution, goodNumber);


    }



}
