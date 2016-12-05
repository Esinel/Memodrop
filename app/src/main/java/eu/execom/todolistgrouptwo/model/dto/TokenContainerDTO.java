package eu.execom.todolistgrouptwo.model.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alex on 11/27/16.
 */

public class TokenContainerDTO {

    @SerializedName("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String toString() {
        return "TokenContainerDTO{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}
