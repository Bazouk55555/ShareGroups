package com.application.bazouk.whosin.mainpages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.application.bazouk.whosin.R;
import com.application.bazouk.whosin.connectionpages.ConnectionPage;
import com.application.bazouk.whosin.groupes.PresenceGroup;
import com.application.bazouk.whosin.groupes.ShareGroup;
import com.application.bazouk.whosin.models.connection.ConnectionBaseDAO;
import com.application.bazouk.whosin.models.presencegroup.PGroup;
import com.application.bazouk.whosin.models.presencegroup.GroupsOfUsernamesBaseDAO;
import com.application.bazouk.whosin.models.presencegroup.PresenceGroupBaseDAO;

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
        username = ConnectionPage.preferences.getString(ConnectionPage.USERNAME,"");
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

        findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this,NotificationPage.class));
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
                ConnectionPage.editor.clear();
                ConnectionPage.editor.apply();
                startActivity(new Intent(MainPage.this,ConnectionPage.class));
            }
        });
    }
}
