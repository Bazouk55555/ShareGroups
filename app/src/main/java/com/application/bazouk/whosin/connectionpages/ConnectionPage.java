package com.application.bazouk.whosin.connectionpages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.application.bazouk.whosin.api.UserHelper;
import com.application.bazouk.whosin.mainpages.MainPage;
import com.application.bazouk.whosin.R;
import com.application.bazouk.whosin.models.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

public class ConnectionPage extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    private static final String REMIND_CONNECTED = "remind_connected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        if(preferences.getBoolean(REMIND_CONNECTED,false))
        {
            startActivity(new Intent(ConnectionPage.this, MainPage.class));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_page);

        findViewById(R.id.connect).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final String username = ((EditText) findViewById(R.id.username)).getText().toString();
                final String password= ((EditText) findViewById(R.id.password)).getText().toString();
                if(username.isEmpty() || password.isEmpty())
                {
                    ((TextView)findViewById(R.id.wrong_identification)).setText("Wrong username/password");
                }
                else {
                    UserHelper.getUsersCollection().whereEqualTo("username", username).whereEqualTo("password", password).get().
                            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (!task.getResult().isEmpty()) {
                                        editor.putString(USERNAME, username);
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            editor.putString(NAME, (String)document.getData().get("name"));
                                        }
                                        editor.apply();
                                        startActivity(new Intent(ConnectionPage.this, MainPage.class));
                                    } else {
                                        ((TextView) findViewById(R.id.wrong_identification)).setText("Wrong username/password");
                                    }
                                }
                            });
                }
            }
        });

        findViewById(R.id.sign_in).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new SignInDialog(ConnectionPage.this).show();
            }
        });

        CheckBox remindConnected = findViewById(R.id.remind_connected_checkbox);
        remindConnected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    editor.putBoolean(REMIND_CONNECTED, true);
                    editor.apply();
                }
                else{
                    editor.putBoolean(REMIND_CONNECTED, false);
                    editor.apply();
                }
            }
        });

        findViewById(R.id.connect_with_email).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setTheme(R.style.LoginTheme)
                                .setAvailableProviders(
                                        Arrays.asList(
                                                new AuthUI.IdpConfig.EmailBuilder().build(), // EMAIL
                                                new AuthUI.IdpConfig.GoogleBuilder().build(), // GOOGLE
                                                new AuthUI.IdpConfig.FacebookBuilder().build() // FACEBOOK
                                        ))
                                .setIsSmartLockEnabled(false, true)
                                .setLogo(R.drawable.notification)
                                .build(),
                        RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                String username = response.getIdpSecret();
                String name = response.getUser().getName();
                editor.putString(USERNAME, username);
                editor.putString(EMAIL, response.getUser().getEmail());
                editor.putString(NAME, name);
                editor.apply();
                /*User newUser = new User(username,"",name);
                if(!UserHelper.getUser(response.getIdpSecret()).isSuccessful())
                {
                    UserHelper.createUser(newUser);
                }*/
                startActivity(new Intent(ConnectionPage.this,MainPage.class));
            } else { // ERRORS
                if (response == null) {
                    ((TextView)findViewById(R.id.wrong_identification)).setText("Identification cancelled");
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    ((TextView)findViewById(R.id.wrong_identification)).setText("You do not have internet");
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    ((TextView)findViewById(R.id.wrong_identification)).setText("Unknown error");;
                }
            }
        }
    }
}
