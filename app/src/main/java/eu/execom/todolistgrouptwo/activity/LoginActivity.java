package eu.execom.todolistgrouptwo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOError;

import eu.execom.todolistgrouptwo.R;
import eu.execom.todolistgrouptwo.api.RestApi;
import eu.execom.todolistgrouptwo.database.wrapper.UserDAOWrapper;
import eu.execom.todolistgrouptwo.model.dto.TokenContainerDTO;
import eu.execom.todolistgrouptwo.util.InputValidator;
import eu.execom.todolistgrouptwo.util.NetworkingUtils;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    public static final int REGISTER_RESULT = 1;
    private static final String TAG =
            LoginActivity.class.getSimpleName();


    @Bean
    UserDAOWrapper userDAOWrapper;

    @ViewById
    EditText email;

    @ViewById
    EditText password;

    @ViewById
    RelativeLayout loadingPanel;

    @RestService
    RestApi restApi;


    @EditorAction(R.id.password)
    @Click
    void login() {
        loadingPanel.setVisibility(View.VISIBLE);

        final String email = this.email.getText().toString();
        final String password = this.password.getText().toString();

        if (validLoginInfo(email, password)) {
            tryLogin(email, password);
        }else{
            loadingPanel.setVisibility(View.GONE);
        }

    }

    @Background
    void tryLogin(String email, String password) {

        try {
            final TokenContainerDTO tokenContainerDTO =
                    restApi.login(NetworkingUtils.packUserLoginCredentials(email, password));

            loginSuccess(tokenContainerDTO.getAccessToken());
        } catch (ResourceAccessException e) {
            showNetworkError();
        } catch (HttpClientErrorException e){
            showLoginError();
        }

    }

    @UiThread
    void showLoginError() {
        Toast.makeText(this,
                "Check your login credentials",
                Toast.LENGTH_SHORT)
                .show();
        loadingPanel.setVisibility(View.GONE);
    }

    @UiThread
    void showNetworkError() {
        Toast.makeText(this,
                "Check your internet connection",
                Toast.LENGTH_SHORT)
                .show();
        loadingPanel.setVisibility(View.GONE);
    }



    @Click
    void register() {
        RegisterActivity_.intent(this).startForResult(REGISTER_RESULT);
    }

    @OnActivityResult(value = REGISTER_RESULT)
    void loginUser(int resultCode, @OnActivityResult.Extra("email") String email, @OnActivityResult.Extra("password") String password) {
        if (resultCode == RESULT_OK) {
            this.email.setText(email);
            this.password.setText(password);

            Toast toast = Toast.makeText(this, "Registration successful!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @UiThread
    void loginSuccess(String accessToken) {
        loadingPanel.setVisibility(View.GONE);

        final Intent intent = new Intent();
        intent.putExtra("token", accessToken);

        setResult(RESULT_OK, intent);
        finish();
    }


    private boolean validLoginInfo(String email, String password) {
        Boolean valid = true;
        Boolean emailValid = Patterns.EMAIL_ADDRESS.matcher((CharSequence) email).matches();
        Boolean passwordValid = InputValidator.isValidPassword(password);

        if (!emailValid){
            this.email.setError("Invalid email format");
            valid = false;
        }
        if (!passwordValid) {
            this.password.setError("Password has to contain min 6 characters with at least 1 number");
            valid = false;
        }

        return valid;
    }



}
