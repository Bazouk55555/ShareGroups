package com.application.bazouk.whosin.mainpages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.application.bazouk.whosin.R;
import com.application.bazouk.whosin.connectionpages.ConnectionPage;
import com.application.bazouk.whosin.models.presencegroup.GroupsOfUsernamesBaseDAO;
import com.application.bazouk.whosin.models.presencegroup.NotificationPresenceGroupBaseDAO;
import com.application.bazouk.whosin.models.presencegroup.PresenceGroupBaseDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.application.bazouk.whosin.connectionpages.ConnectionPage.USERNAME;
import static com.application.bazouk.whosin.connectionpages.ConnectionPage.editor;
import static com.application.bazouk.whosin.connectionpages.ConnectionPage.preferences;

/**
 * Created by Adrien on 10/01/2018.
 */

public class NotificationPage extends AppCompatActivity {

    private List<HashMap<String, String>> listMapOfEachGroup;
    private SimpleAdapter adapterGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);

        final String username = preferences.getString(USERNAME,"");
        NotificationPresenceGroupBaseDAO notificationPresenceGroupBaseDAO = new NotificationPresenceGroupBaseDAO(this);
        notificationPresenceGroupBaseDAO.open();
        final List<Integer> listOfGroupsId = notificationPresenceGroupBaseDAO.getAllGroupsNameForAUsername(username);
        notificationPresenceGroupBaseDAO.close();
        final ListView listViewNotificationGroups = (ListView) findViewById(R.id.list_notification_groups);
        listMapOfEachGroup = new ArrayList<>();
        PresenceGroupBaseDAO presenceGroupBaseDAO = new PresenceGroupBaseDAO(this);
        presenceGroupBaseDAO.open();

        for(int id: listOfGroupsId)
        {
            HashMap<String, String> mapOfTheNotificationGroups = new HashMap<>();
            mapOfTheNotificationGroups.put("name of the group", presenceGroupBaseDAO.getNameOfTheGroupFromId(id));
            mapOfTheNotificationGroups.put("yes", "Yes");
            mapOfTheNotificationGroups.put("no", "No");
            listMapOfEachGroup.add(mapOfTheNotificationGroups);
        }
        presenceGroupBaseDAO.close();

        adapterGroups = new SimpleAdapter(this, listMapOfEachGroup, R.layout.list_view_notification_item,
                new String[]{"name of the group", "yes", "no"}, new int[]{R.id.name_of_the_group, R.id.yes, R.id.no})
        {
            @Override
            public View getView(final int position, final View convertView, ViewGroup parent)
            {
                final View convertViewToReturn = super.getView(position, convertView, parent);
                convertViewToReturn.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addMemberToDatabase(username,listOfGroupsId.get(position));
                        removeMemberFromNotification(username,listOfGroupsId.get(position));
                        listOfGroupsId.remove(position);
                        listMapOfEachGroup.remove(position);
                        adapterGroups.notifyDataSetChanged();
                    }
                });
                convertViewToReturn.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeMemberFromNotification(username,listOfGroupsId.get(position));
                        listOfGroupsId.remove(position);
                        listMapOfEachGroup.remove(position);
                        adapterGroups.notifyDataSetChanged();
                    }
                });
                return convertViewToReturn;
            }
        };
        listViewNotificationGroups.setAdapter(adapterGroups);

        setToolbar();
    }

    private void addMemberToDatabase(String username, int id)
    {
        GroupsOfUsernamesBaseDAO groupsOfUsernamesBaseDAO = new GroupsOfUsernamesBaseDAO(this);
        groupsOfUsernamesBaseDAO.open();
        groupsOfUsernamesBaseDAO.addMember(id,username);
        groupsOfUsernamesBaseDAO.close();
    }

    private void removeMemberFromNotification(String username, int id)
    {
        NotificationPresenceGroupBaseDAO notificationPresenceGroupBaseDAO= new NotificationPresenceGroupBaseDAO(this);
        notificationPresenceGroupBaseDAO.open();
        notificationPresenceGroupBaseDAO.removeMember(id,username);
        notificationPresenceGroupBaseDAO.close();
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
