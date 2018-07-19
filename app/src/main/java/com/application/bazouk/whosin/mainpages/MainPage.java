package com.application.bazouk.whosin.mainpages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.application.bazouk.whosin.R;
import com.application.bazouk.whosin.api.UserGroupHelper;
import com.application.bazouk.whosin.connectionpages.ConnectionPage;
import com.application.bazouk.whosin.groupes.PresenceGroup;
import com.application.bazouk.whosin.groupes.ShareGroup;
import com.application.bazouk.whosin.models.UserGroup;
import com.application.bazouk.whosin.models.connection.ConnectionBaseDAO;
import com.application.bazouk.whosin.models.presencegroup.PGroup;
import com.application.bazouk.whosin.models.presencegroup.GroupsOfUsernamesBaseDAO;
import com.application.bazouk.whosin.models.presencegroup.PresenceGroupBaseDAO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.TAG;

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
        ((TextView) findViewById(R.id.welcome)).setText("Welcome "+ConnectionPage.preferences.getString(ConnectionPage.NAME,""));

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
        final Intent presenceGroupIntent = new Intent(MainPage.this,PresenceGroup.class);
        List<String> usernames = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<Boolean> arePresent = new ArrayList<>();
        usernames.add(username);
        names.add(ConnectionPage.preferences.getString(ConnectionPage.NAME,""));
        arePresent.add(true);
        UserGroupHelper.createUserGroup(new UserGroup(usernames,names,arePresent,nameOfTheGroup)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                presenceGroupIntent.putExtra("id",documentReference.getId());
                startActivity(presenceGroupIntent);
            }
        }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
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
