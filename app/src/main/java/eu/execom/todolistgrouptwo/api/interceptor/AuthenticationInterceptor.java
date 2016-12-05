package eu.execom.todolistgrouptwo.api.interceptor;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import eu.execom.todolistgrouptwo.preference.UserPreferences_;
import okhttp3.Interceptor;

@EBean
public class AuthenticationInterceptor implements ClientHttpRequestInterceptor {

    @Pref
    UserPreferences_ userPreferences;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders()
                .add("Authorization",
                        "bearer " + userPreferences.accessToken().getOr(""));
        return execution.execute(request, body);
    }
}
