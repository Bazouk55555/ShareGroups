package com.application.bazouk.whosin.mainpages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.application.bazouk.whosin.R;
import com.application.bazouk.whosin.api.UserGroupHelper;
import com.application.bazouk.whosin.api.UserHelper;
import com.application.bazouk.whosin.connectionpages.ConnectionPage;
import com.application.bazouk.whosin.groupes.PresenceGroup;
import com.application.bazouk.whosin.models.connection.ConnectionBaseDAO;
import com.application.bazouk.whosin.models.presencegroup.GroupsOfUsernamesBaseDAO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.application.bazouk.whosin.connectionpages.ConnectionPage.NAME;
import static com.application.bazouk.whosin.connectionpages.ConnectionPage.editor;
import static com.application.bazouk.whosin.connectionpages.ConnectionPage.preferences;

/**
 * Created by Adrien on 10/01/2018.
 */

public class ProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        final String username = preferences.getString(ConnectionPage.USERNAME,"");
        final String name = preferences.getString(NAME,"");
        ((TextView)findViewById(R.id.username)).setText(username);
        ((TextView)findViewById(R.id.name)).setText(name);
        UserGroupHelper.getUsersCollection().get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ((TextView) findViewById(R.id.number_of_groups)).setText(Integer.toString(task.getResult().size()));
                    }
                });

        final EditText nameEditText = (EditText)findViewById(R.id.name);
        final Button modifyButton = (Button)findViewById(R.id.modify);

        findViewById(R.id.modify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout linearLayoutButtons = ((LinearLayout)findViewById(R.id.layout_buttons));
                nameEditText.setFocusable(true);
                nameEditText.setClickable(true);
                nameEditText.setFocusableInTouchMode(true);
                modifyButton.setClickable(false);
                final Button okButton = new Button(ProfilePage.this);
                okButton.setText("OK");
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String newName = nameEditText.getText().toString();
                        UserHelper.getUsersCollection().whereEqualTo("username", username).get().
                                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            UserHelper.updateName(document.getId(),newName);
                                        }
                                    }
                                });
                        editor.putString(NAME, newName);
                        editor.apply();
                        nameEditText.setFocusable(false);
                        nameEditText.setClickable(false);
                        nameEditText.setFocusableInTouchMode(false);
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
