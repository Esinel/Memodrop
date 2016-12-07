package eu.execom.todolistgrouptwo.util;

/**
 * Created by stefanos on 5.12.16..
 */

public class InputValidator {


    // validating password
    public static boolean isValidPassword(String pass) {
        final String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{5,}$";
        if (pass != null && pass.length() > 6 && pass.matches(pattern)) {
            return true;
        }
        return false;
    }
}
