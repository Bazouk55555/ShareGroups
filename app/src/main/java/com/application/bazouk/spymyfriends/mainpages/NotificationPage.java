package com.application.bazouk.spymyfriends.mainpages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.application.bazouk.spymyfriends.R;
import com.application.bazouk.spymyfriends.connectionpages.ConnectionPage;
import com.application.bazouk.spymyfriends.sqliteservices.connection.ConnectionBaseDAO;
import com.application.bazouk.spymyfriends.sqliteservices.groupsofusernames.DatabaseGroupsOfUsernamesHandler;
import com.application.bazouk.spymyfriends.sqliteservices.groupsofusernames.GroupsOfUsernamesBaseDAO;
import com.application.bazouk.spymyfriends.sqliteservices.notificationpresencegroup.presencegroup.NotificationPresenceGroupBaseDAO;
import com.application.bazouk.spymyfriends.sqliteservices.presencegroup.PresenceGroupBaseDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.application.bazouk.spymyfriends.connectionpages.ConnectionPage.USERNAME;
import static com.application.bazouk.spymyfriends.connectionpages.ConnectionPage.editor;
import static com.application.bazouk.spymyfriends.connectionpages.ConnectionPage.preferences;

/**
 * Created by Adrien on 10/01/2018.
 */

public class NotificationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);

        String username = preferences.getString(USERNAME,"");
        NotificationPresenceGroupBaseDAO notificationPresenceGroupBaseDAO = new NotificationPresenceGroupBaseDAO(this);
        notificationPresenceGroupBaseDAO.open();
        final List<Integer> listOfGroups = notificationPresenceGroupBaseDAO.getAllGroupsNameForAUsername(username);
        notificationPresenceGroupBaseDAO.close();
        ListView listViewNotificationGroups = (ListView) findViewById(R.id.list_notification_groups);
        List<HashMap<String, String>> listMapOfEachGroup;
        SimpleAdapter adapterAlarms;
        listMapOfEachGroup = new ArrayList<>();
        GroupsOfUsernamesBaseDAO groupsOfUsernamesBaseDAO = new GroupsOfUsernamesBaseDAO(this);
        groupsOfUsernamesBaseDAO.open();

        for(int id: listOfGroups)
        {
            HashMap<String, String> mapOfTheNotificationGroups = new HashMap<>();
            mapOfTheNotificationGroups.put("name of the group", groupsOfUsernamesBaseDAO.getNameOfTheGroupFromId(id));
            mapOfTheNotificationGroups.put("yes", "Yes");
            mapOfTheNotificationGroups.put("no", "No");
            listMapOfEachGroup.add(mapOfTheNotificationGroups);
        }
        groupsOfUsernamesBaseDAO.close();

        adapterAlarms = new SimpleAdapter(this, listMapOfEachGroup, R.layout.list_view_notification_item,
                new String[]{"name of the group", "yes", "no"}, new int[]{R.id.group, R.id.yes, R.id.no});
        listViewNotificationGroups.setAdapter(adapterAlarms);

        setToolbar();
    }

    private void setToolbar()
    {
        findViewById(R.id.results).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotificationPage.this,AllTheGroupsPage.class));
            }
        });

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotificationPage.this,MainPage.class));
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotificationPage.this,ProfilePage.class));
            }
        });

        findViewById(R.id.disconnection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.apply();
                startActivity(new Intent(NotificationPage.this,ConnectionPage.class));
            }
        });
    }
}
