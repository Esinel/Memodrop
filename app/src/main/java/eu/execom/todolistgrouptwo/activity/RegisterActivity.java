package eu.execom.todolistgrouptwo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import eu.execom.todolistgrouptwo.R;
import eu.execom.todolistgrouptwo.api.RestApi;
import eu.execom.todolistgrouptwo.database.wrapper.UserDAOWrapper;
import eu.execom.todolistgrouptwo.model.User;
import eu.execom.todolistgrouptwo.model.dto.RegisterDTO;
import eu.execom.todolistgrouptwo.util.InputValidator;
import eu.execom.todolistgrouptwo.util.NetworkingUtils;
import okhttp3.OkHttpClient;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {

    protected static final int LOGIN_REQUEST_CODE = 421; // BLAZE IT

    @Bean
    UserDAOWrapper userDAOWrapper;


    @ViewById
    EditText email;

    @ViewById
    EditText password;

    @ViewById
    EditText confirmPassword;

    @ViewById
    RelativeLayout loadingPanel;

    @RestService
    RestApi restApi;

    @EditorAction(R.id.confirmPassword)
    @Click
    void register() {
        loadingPanel.setVisibility(View.VISIBLE);

        final String email = this.email.getText().toString();
        final String password = this.password.getText().toString();
        final String confirmPassword = this.confirmPassword.getText().toString();

        if (validRegistrationInfo(email, password, confirmPassword)){
            final RegisterDTO registerDTO = new RegisterDTO(email, password, confirmPassword);
            registerUser(registerDTO);
        }else{
            loadingPanel.setVisibility(View.GONE);
        }
    }

    @Background
    void registerUser(RegisterDTO registerDTO) {
        try {
            restApi.register(registerDTO);

            final Intent loginIntent = new Intent();
            loginIntent.putExtra("email", registerDTO.getEmail());
            loginIntent.putExtra("password", registerDTO.getPassword());
            setResult(RESULT_OK, loginIntent);
            finish();
        }catch (ResourceAccessException e){
            showNetworkError();
        }catch (HttpClientErrorException e) {
            showRegisterError();
        }
    }

    @UiThread
    void login(User user) {
        final Intent intent = new Intent();
        intent.putExtra("user_id", user.getId());

        setResult(RESULT_OK, intent);
        finish();
    }

    @UiThread
    void showRegisterError() {
        email.setError("Username already exists");
        Toast.makeText(this, "Username already exists", Toast.LENGTH_LONG).show();
        loadingPanel.setVisibility(View.GONE);
    }

    @UiThread
    void showNetworkError(){
        Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show();
        loadingPanel.setVisibility(View.GONE);
    }


    private boolean validRegistrationInfo(String email, String password, String confirmPassword){
        Boolean valid = true;
        Boolean emailValid = Patterns.EMAIL_ADDRESS.matcher((CharSequence) email).matches();
        Boolean passwordValid = InputValidator.isValidPassword(password);
        Boolean confirmPasswordValid = password.equals(confirmPassword);

        if (!emailValid){
            this.email.setError("Invalid email format");
            valid = false;
        }
        if (!passwordValid) {
            this.password.setError("Password has to contain min 6 characters with at least 1 number");
            valid = false;
        }
        if (!confirmPasswordValid){
            this.confirmPassword.setError("Doesn't match password field");
            valid = false;
        }
        return valid;
    }



}
