package eu.execom.todolistgrouptwo.model.dto;

/**
 * Created by stefanos on 4.12.16..
 */

public class RegisterDTO {

    private String email;

    private String password;

    private String confirmPassword;


    public RegisterDTO(String email, String password, String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return confirmPassword;
    }

    public void setRepeatPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


}
