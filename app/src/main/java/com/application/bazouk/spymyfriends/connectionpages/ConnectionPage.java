package com.application.bazouk.spymyfriends.connectionpages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.application.bazouk.spymyfriends.mainpages.MainPage;
import com.application.bazouk.spymyfriends.sqliteservices.connection.ConnectionBaseDAO;
import com.application.bazouk.spymyfriends.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class ConnectionPage extends AppCompatActivity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.connection_page);

        findViewById(R.id.connect).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                String password= ((EditText) findViewById(R.id.password)).getText().toString();
                if(username.isEmpty() || password.isEmpty())
                {
                    ((TextView)findViewById(R.id.wrong_identification)).setText("Wrong username/password");
                    return;
                }
                ConnectionBaseDAO connectionBaseDAO = new ConnectionBaseDAO(ConnectionPage.this);
                connectionBaseDAO.open();
                if(connectionBaseDAO.canConnect(username,password))
                {
                    String [] firstAndLastName = connectionBaseDAO.getFirstAndLastName(username);
                    sendFirstAndLastName(firstAndLastName);
                }
                else
                {
                    ((TextView)findViewById(R.id.wrong_identification)).setText("Wrong username/password");
                }
                connectionBaseDAO.close();
            }
        });

        findViewById(R.id.facebook_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackManager = CallbackManager.Factory.create();
                LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
                loginButton.setReadPermissions(Arrays.asList(
                        "public_profile", "email", "user_friends"));
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                        try {
                                            String [] firstAndLastName = {jsonObject.getString("first_name"),jsonObject.getString("last_name")};
                                            sendFirstAndLastName(firstAndLastName);
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                        Bundle bundle = new Bundle();
                        bundle.putString(
                                "fields",
                                "id,name,link,email,gender,last_name,first_name,locale,timezone,updated_time,verified"
                        );
                        graphRequest.setParameters(bundle);
                        graphRequest.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("HERE I AM IN CANCEL");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("JE SUIS DANS ERROR");
                    }
                });
            }
        });

        findViewById(R.id.sign_in).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new SignInDialog(ConnectionPage.this).show();
            }
        });
    }

    private void sendFirstAndLastName(String [] firstAndLastName)
    {
        Intent intentToMainPage = new Intent(this,MainPage.class);
        intentToMainPage.putExtra("LastNameAndFirstName",firstAndLastName);
        startActivity(intentToMainPage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
