package com.application.bazouk.spymyfriends.mainpages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.bazouk.spymyfriends.R;
import com.application.bazouk.spymyfriends.connectionpages.ConnectionPage;
import com.application.bazouk.spymyfriends.sqliteservices.connection.ConnectionBaseDAO;
import com.application.bazouk.spymyfriends.sqliteservices.presencegroup.GroupsOfUsernamesBaseDAO;
import com.application.bazouk.spymyfriends.sqliteservices.presencegroup.PresenceGroupBaseDAO;

import static com.application.bazouk.spymyfriends.connectionpages.ConnectionPage.editor;
import static com.application.bazouk.spymyfriends.connectionpages.ConnectionPage.preferences;

/**
 * Created by Adrien on 10/01/2018.
 */

public class ProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        final String username = preferences.getString(ConnectionPage.USERNAME,"");
        final EditText firstNameEditText = (EditText)findViewById(R.id.first_name);
        final EditText lastNameEditText = (EditText)findViewById(R.id.last_name);
        ConnectionBaseDAO connectionBaseDAO = new ConnectionBaseDAO(ProfilePage.this);
        connectionBaseDAO.open();
        String [] firstAndLastName = connectionBaseDAO.getFirstAndLastName(username);
        connectionBaseDAO.close();
        firstNameEditText.setText(firstAndLastName[0]);
        lastNameEditText.setText(firstAndLastName[1]);
        ((TextView)findViewById(R.id.username)).setText(username);
        GroupsOfUsernamesBaseDAO groupsOfUsernamesBaseDAO = new GroupsOfUsernamesBaseDAO(this);
        groupsOfUsernamesBaseDAO.open();
        ((TextView) findViewById(R.id.number_of_groups)).setText(Integer.toString(groupsOfUsernamesBaseDAO.getNumberOfGroupsForUsername(username)));
        groupsOfUsernamesBaseDAO.close();

        final Button modifyButton = (Button)findViewById(R.id.modify);

        findViewById(R.id.modify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout linearLayoutButtons = ((LinearLayout)findViewById(R.id.layout_buttons));
                firstNameEditText.setFocusable(true);
                lastNameEditText.setFocusable(true);
                firstNameEditText.setClickable(true);
                lastNameEditText.setClickable(true);
                firstNameEditText.setFocusableInTouchMode(true);
                lastNameEditText.setFocusableInTouchMode(true);
                modifyButton.setClickable(false);
                final Button okButton = new Button(ProfilePage.this);
                okButton.setText("OK");
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConnectionBaseDAO connectionBaseDAO = new ConnectionBaseDAO(ProfilePage.this);
                        connectionBaseDAO.open();
                        connectionBaseDAO.modifyFirstAndLastName(username,firstNameEditText.getText().toString(),lastNameEditText.getText().toString());
                        connectionBaseDAO.close();
                        firstNameEditText.setFocusable(false);
                        lastNameEditText.setFocusable(false);
                        firstNameEditText.setClickable(false);
                        lastNameEditText.setClickable(false);
                        firstNameEditText.setFocusableInTouchMode(false);
                        lastNameEditText.setFocusableInTouchMode(false);
                        modifyButton.setClickable(true);
                        linearLayoutButtons.removeView(okButton);

                    }
                });
                linearLayoutButtons.addView(okButton,1);
            }
        });

        setToolbar();
    }

    private void setToolbar()
    {
        findViewById(R.id.results).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilePage.this,AllTheGroupsPage.class));
            }
        });

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilePage.this,MainPage.class));
            }
        });

        findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilePage.this,NotificationPage.class));
            }
        });

        findViewById(R.id.disconnection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.apply();
                startActivity(new Intent(ProfilePage.this,ConnectionPage.class));
            }
        });
    }
}
