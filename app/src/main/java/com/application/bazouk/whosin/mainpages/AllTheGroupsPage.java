package com.application.bazouk.whosin.mainpages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.application.bazouk.whosin.R;
import com.application.bazouk.whosin.api.UserGroupHelper;
import com.application.bazouk.whosin.connectionpages.ConnectionPage;
import com.application.bazouk.whosin.groupes.PresenceGroup;
import com.application.bazouk.whosin.models.presencegroup.GroupsOfUsernamesBaseDAO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.application.bazouk.whosin.connectionpages.ConnectionPage.USERNAME;
import static com.application.bazouk.whosin.connectionpages.ConnectionPage.editor;
import static com.application.bazouk.whosin.connectionpages.ConnectionPage.preferences;

/**
 * Created by Adrien on 10/01/2018.
 */

public class AllTheGroupsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_the_groups_page);

        final String username = preferences.getString(USERNAME,"");
        final List<String[]> listOfGroups = new ArrayList<>();
        UserGroupHelper.getUsersCollection().get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(((List<String>)document.getData().get("usernames")).contains(username))
                            {
                                String [] newGroup = new String[2];
                                newGroup[0]=(String)document.getData().get("name_of_the_group");
                                newGroup[1]=document.getId();
                                listOfGroups.add(newGroup);
                            }
                        }

                        List<HashMap<String, String>> listMapOfEachGroup;
                        SimpleAdapter adapterAlarms;
                        listMapOfEachGroup = new ArrayList<>();

                        for (int i =0;i<listOfGroups.size();i++) {
                            HashMap<String, String> mapOfTheNewGroup= new HashMap<>();
                            mapOfTheNewGroup.put("group", listOfGroups.get(i)[0]);
                            listMapOfEachGroup.add(mapOfTheNewGroup);
                        }

                        final ListView listViewGroups = (ListView) findViewById(R.id.list_groups);
                        adapterAlarms = new SimpleAdapter(AllTheGroupsPage.this, listMapOfEachGroup, R.layout.list_view_group_item,
                                new String[]{"group"}, new int[]{R.id.group})
                        {
                            @Override
                            public View getView(final int position, View convertView, ViewGroup parent)
                            {
                                final View convertViewToReturn = super.getView(position, convertView, parent);
                                convertViewToReturn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent presenceGroupIntent = new Intent(AllTheGroupsPage.this,PresenceGroup.class);
                                        presenceGroupIntent.putExtra("id",listOfGroups.get(position)[1]);
                                        startActivity(presenceGroupIntent);
                                    }
                                });
                                return convertViewToReturn;
                            }
                        };
                        listViewGroups.setAdapter(adapterAlarms);
                    }
                });

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

        findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllTheGroupsPage.this,NotificationPage.class));
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
                editor.clear();
                editor.apply();
                startActivity(new Intent(AllTheGroupsPage.this,ConnectionPage.class));
            }
        });
    }
}
