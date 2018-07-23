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
import com.application.bazouk.whosin.api.NotificationHelper;
import com.application.bazouk.whosin.api.UserGroupHelper;
import com.application.bazouk.whosin.connectionpages.ConnectionPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.application.bazouk.whosin.connectionpages.ConnectionPage.NAME;
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
        final String name = preferences.getString(NAME,"");
        NotificationHelper.getNotificationCollection().whereEqualTo("username",username).get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        final List<String> listOfNotificationIds = new ArrayList<>();
                        final List<String> listOfGroupsId = new ArrayList<>();
                        final ListView listViewNotificationGroups = findViewById(R.id.list_notification_groups);
                        listMapOfEachGroup = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if((document.getData().get("username")).equals(username))
                            {
                                listOfNotificationIds.add(document.getId());
                                listOfGroupsId.add((String)document.getData().get("id_group"));
                                HashMap<String, String> mapOfTheNotificationGroups = new HashMap<>();
                                mapOfTheNotificationGroups.put("name of the group", (String)document.getData().get("name_of_the_group"));
                                mapOfTheNotificationGroups.put("yes", "Yes");
                                mapOfTheNotificationGroups.put("no", "No");
                                listMapOfEachGroup.add(mapOfTheNotificationGroups);
                            }
                        }

                        adapterGroups = new SimpleAdapter(NotificationPage.this, listMapOfEachGroup, R.layout.list_view_notification_item,
                                new String[]{"name of the group", "yes", "no"}, new int[]{R.id.name_of_the_group, R.id.yes, R.id.no})
                        {
                            @Override
                            public View getView(final int position, final View convertView, ViewGroup parent)
                            {
                                final View convertViewToReturn = super.getView(position, convertView, parent);
                                convertViewToReturn.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UserGroupHelper.addMemberUserGroup(listOfGroupsId.get(position),username, name);
                                        NotificationHelper.deleteUser(listOfNotificationIds.get(position));
                                        listOfNotificationIds.remove(position);
                                        listOfGroupsId.remove(position);
                                        listMapOfEachGroup.remove(position);
                                        adapterGroups.notifyDataSetChanged();
                                    }
                                });
                                convertViewToReturn.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        NotificationHelper.deleteUser(listOfNotificationIds.get(position));
                                        listOfGroupsId.remove(position);
                                        listMapOfEachGroup.remove(position);
                                        adapterGroups.notifyDataSetChanged();
                                    }
                                });
                                return convertViewToReturn;
                            }
                        };
                        listViewNotificationGroups.setAdapter(adapterGroups);
                    }
                });

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
