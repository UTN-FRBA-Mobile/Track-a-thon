package com.trackathon.utn.track_a_thon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.trackathon.utn.track_a_thon.model.User;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        if (AccessToken.getCurrentAccessToken() != null) {

            fetchProfile(AccessToken.getCurrentAccessToken());

        } else {

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    AccessToken accessToken = loginResult.getAccessToken();
                    fetchProfile(accessToken);
                    startHomeActivity();
                }

                @Override
                public void onCancel() {}

                @Override
                public void onError(FacebookException e) {}
            });
        }

    }

    private void fetchProfile(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, (profile, graphResponse) -> {
            setCurrentUser(profile);
            startHomeActivity();
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void startHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void setCurrentUser(JSONObject profile) {
        User user = new User();
        user.setName(profile.optString("name"));
        user.setEmail(profile.optString("email"));
        user.setImageUrl(profile.optJSONObject("picture").optJSONObject("data").optString("url"));
        User.setCurrentUser(user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}