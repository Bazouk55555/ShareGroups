package com.application.bazouk.spymyfriends.mainpages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.application.bazouk.spymyfriends.R;
import com.application.bazouk.spymyfriends.connectionpages.ConnectionPage;
import com.application.bazouk.spymyfriends.sqliteservices.presencegroup.PresenceGroupBaseDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.application.bazouk.spymyfriends.mainpages.MainPage.USERNAME;
import static com.application.bazouk.spymyfriends.mainpages.MainPage.preferences;

/**
 * Created by Adrien on 10/01/2018.
 */

public class AllTheGroupsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_the_groups_page);

        String username = preferences.getString(USERNAME,"");
        PresenceGroupBaseDAO presenceGroupBaseDAO = new PresenceGroupBaseDAO(this);
        presenceGroupBaseDAO.open();
        List<String> listOfGroups = presenceGroupBaseDAO.getAllGroupsName(username);
        presenceGroupBaseDAO.close();
        List<HashMap<String, String>> listMapOfEachGroup;
        SimpleAdapter adapterAlarms;
        listMapOfEachGroup = new ArrayList<>();

        for (String group : listOfGroups) {
            HashMap<String, String> mapOfTheNewGroup= new HashMap<>();
            mapOfTheNewGroup.put("group", group);
            listMapOfEachGroup.add(mapOfTheNewGroup);
        }

        final ListView listViewGroups = (ListView) findViewById(R.id.list_groups);
        adapterAlarms = new SimpleAdapter(this, listMapOfEachGroup, R.layout.list_view_group_item,
                new String[]{"group"}, new int[]{R.id.group})
        {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent)
            {
                final View convertViewToReturn = super.getView(position, convertView, parent);
                convertViewToReturn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(AllTheGroupsPage.this,MainPage.class));
                    }
                });
                return convertViewToReturn;
            }
        };
        listViewGroups.setAdapter(adapterAlarms);

        setToolbar();
    }

    private void setToolbar()
    {
        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllTheGroupsPage.this,MainPage.class));
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllTheGroupsPage.this,ProfilePage.class));
            }
        });

        findViewById(R.id.disconnection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPage.editor.clear();
                MainPage.editor.apply();
                startActivity(new Intent(AllTheGroupsPage.this,ConnectionPage.class));
            }
        });
    }
}
