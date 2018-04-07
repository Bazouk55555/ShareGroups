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
import com.application.bazouk.spymyfriends.sqliteservices.connection.ConnectionBaseDAO;
import com.application.bazouk.spymyfriends.sqliteservices.groupsofusernames.PGroup;
import com.application.bazouk.spymyfriends.sqliteservices.groupsofusernames.GroupsOfUsernamesBaseDAO;
import com.application.bazouk.spymyfriends.sqliteservices.presencegroup.PresenceGroupBaseDAO;

import static com.application.bazouk.spymyfriends.connectionpages.ConnectionPage.USERNAME;
import static com.application.bazouk.spymyfriends.connectionpages.ConnectionPage.editor;
import static com.application.bazouk.spymyfriends.connectionpages.ConnectionPage.preferences;

/**
 * Created by Adrien on 10/01/2018.
 */

public class MainPage extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        setToolbar();
        username = preferences.getString(USERNAME,"");;
        ConnectionBaseDAO connectionBaseDAO = new ConnectionBaseDAO(MainPage.this);
        connectionBaseDAO.open();
        String [] firstAndLastName = connectionBaseDAO.getFirstAndLastName(username);
        connectionBaseDAO.close();
        ((TextView) findViewById(R.id.welcome)).setText("Welcome "+firstAndLastName[0]+" "+firstAndLastName[1]);

        findViewById(R.id.presence_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAGroupDialog addAGroupDialog = new AddAGroupDialog(MainPage.this, MainPage.this);
                addAGroupDialog.show();
            }
        });

        findViewById(R.id.share_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this,ShareGroup.class));
            }
        });
    }

    void createANewGroup(String nameOfTheGroup)
    {
        PresenceGroupBaseDAO presenceGroupBaseDAO = new PresenceGroupBaseDAO(MainPage.this);
        presenceGroupBaseDAO.open();
        presenceGroupBaseDAO.addGroup(nameOfTheGroup);
        int id = presenceGroupBaseDAO.getTotalOfGroups();
        presenceGroupBaseDAO.close();
        PGroup pGroup = new PGroup(id,nameOfTheGroup);
        pGroup.addMember(username,true);
        GroupsOfUsernamesBaseDAO groupsOfUsernamesBaseDAO = new GroupsOfUsernamesBaseDAO(MainPage.this);
        groupsOfUsernamesBaseDAO.open();
        groupsOfUsernamesBaseDAO.addGroup(pGroup);
        groupsOfUsernamesBaseDAO.close();
        Intent presenceGroupIntent = new Intent(MainPage.this,PresenceGroup.class);
        presenceGroupIntent.putExtra("id",id);
        presenceGroupIntent.putExtra("name_of_the_group",nameOfTheGroup);
        startActivity(presenceGroupIntent);
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
