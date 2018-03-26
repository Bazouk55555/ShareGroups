package com.application.bazouk.spymyfriends.mainpages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.application.bazouk.spymyfriends.R;
import com.application.bazouk.spymyfriends.connectionpages.ConnectionPage;
import com.application.bazouk.spymyfriends.groupes.PresenceGroup;
import com.application.bazouk.spymyfriends.groupes.ShareGroup;
import com.application.bazouk.spymyfriends.sqliteservices.presencegroup.PGroup;
import com.application.bazouk.spymyfriends.sqliteservices.presencegroup.PresenceGroupBaseDAO;

/**
 * Created by Adrien on 10/01/2018.
 */

public class MainPage extends AppCompatActivity {

    public static final String GET_FIRST_NAME = "first_name";
    public static final String GET_LAST_NAME = "last name";
    public static final String USERNAME = "username";
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        setToolbar();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        final String firstName;
        final String lastName;
        final String username;
        if(preferences.getString(USERNAME,"").isEmpty() && preferences.getString(GET_LAST_NAME,"").isEmpty() && preferences.getString(GET_FIRST_NAME,"").isEmpty())
        {
            firstName = getIntent().getStringArrayExtra("LastNameAndFirstName")[0];
            lastName = getIntent().getStringArrayExtra("LastNameAndFirstName")[1];
            username = getIntent().getStringExtra("username");
            editor.putString(GET_FIRST_NAME, firstName);
            editor.putString(GET_LAST_NAME, lastName);
            editor.putString(USERNAME, username);
            editor.apply();
        }
        else
        {
            lastName = preferences.getString(GET_LAST_NAME,"");
            firstName = preferences.getString(GET_FIRST_NAME,"");
            username = preferences.getString(USERNAME,"");
        }
        TextView welcomeTextView = (TextView) findViewById(R.id.welcome);
        welcomeTextView.setText("Welcome "+firstName+" "+lastName);

        findViewById(R.id.presence_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = PresenceGroup.LAST_GROUP;
                String nameOfTheGroup = "Random name";
                PGroup pGroup = new PGroup(id,nameOfTheGroup);
                pGroup.addMember(username);
                PresenceGroupBaseDAO presenceGroupBaseDAO = new PresenceGroupBaseDAO(MainPage.this);
                presenceGroupBaseDAO.open();
                presenceGroupBaseDAO.addGroup(pGroup);
                presenceGroupBaseDAO.close();
                Intent presenceGroupIntent = new Intent(MainPage.this,PresenceGroup.class);
                presenceGroupIntent.putExtra("id",id);
                presenceGroupIntent.putExtra("name_of_the_group",nameOfTheGroup);
                startActivity(presenceGroupIntent);
            }
        });

        findViewById(R.id.share_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this,ShareGroup.class));
            }
        });
    }

    private void setToolbar()
    {
        findViewById(R.id.results).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this,AllTheGroupsPage.class));
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this,ProfilePage.class));
            }
        });

        findViewById(R.id.disconnection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.apply();
                startActivity(new Intent(MainPage.this,ConnectionPage.class));
            }
        });
    }
}
