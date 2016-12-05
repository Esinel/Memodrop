package eu.execom.todolistgrouptwo.util;

import org.springframework.util.LinkedMultiValueMap;

import eu.execom.todolistgrouptwo.model.User;

/**
 * Created by Alex on 11/27/16.
 */

public class NetworkingUtils {

    public static LinkedMultiValueMap<String, String> packUserLoginCredentials(String email, String password) {
        final LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set("grant_type", "password");
        map.set("username", email);
        map.set("password", password);
        return map;
    }

    // for sake of name attribute
    public static LinkedMultiValueMap<String, String> packUserRegisterCredentials(User user){
        final LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set("email", user.getEmail());
        map.set("password", user.getPassword());
        map.set("confirmPassword", user.getPassword());
        return map;
    }
}
