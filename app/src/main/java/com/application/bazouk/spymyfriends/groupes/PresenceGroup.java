package com.application.bazouk.spymyfriends.groupes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.bazouk.spymyfriends.R;
import com.application.bazouk.spymyfriends.connectionpages.ConnectionPage;
import com.application.bazouk.spymyfriends.mainpages.MainPage;
import com.application.bazouk.spymyfriends.mainpages.ProfilePage;
import com.application.bazouk.spymyfriends.mainpages.AllTheGroupsPage;
import com.application.bazouk.spymyfriends.sqliteservices.connection.ConnectionBaseDAO;
import com.application.bazouk.spymyfriends.sqliteservices.presencegroup.PresenceGroupBaseDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrien on 21/03/2018.
 */

public class PresenceGroup extends AppCompatActivity {

    public static int LAST_GROUP=1;
    private int numberOfPresence = 1;
    private TextView numberOfPeopleTextView;
    private LinearLayout layoutCheckBoxes;
    private int id;
    private String nameOfTheGroup;
    private List<String> usernamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presence_group);
        LAST_GROUP++;
        id = getIntent().getIntExtra("id",0);
        nameOfTheGroup = getIntent().getStringExtra("name_of_the_group");
        numberOfPeopleTextView = ((TextView) findViewById(R.id.number_of_people_present));
        ((CheckBox) findViewById(R.id.check_box)).setText(MainPage.preferences.getString(MainPage.GET_FIRST_NAME,"") + " " + MainPage.preferences.getString(MainPage.GET_LAST_NAME,""));
        updateTitle();
        layoutCheckBoxes = ((LinearLayout)findViewById(R.id.layout_checkbox));
        updateCheckBox((CheckBox)layoutCheckBoxes.getChildAt(0));

        usernamesList = new ArrayList<>();
        PresenceGroupBaseDAO presenceGroupBaseDAO = new PresenceGroupBaseDAO(this);
        presenceGroupBaseDAO.open();
        usernamesList = presenceGroupBaseDAO.getUsernames(id);
        presenceGroupBaseDAO.close();

        findViewById(R.id.add_a_member).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAMemberDialog addAMemberDialog = new AddAMemberDialog(PresenceGroup.this,PresenceGroup.this);
                addAMemberDialog.show();
            }
        });

        setToolbar();
    }

    public void addAMember(String username)
    {
        CheckBox checkBox = new CheckBox(this);
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (5*scale + 0.5f);
        checkBox.setPadding(dpAsPixels,0,0,0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dpAsPixels);
        checkBox.setLayoutParams(params);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkBox.setButtonDrawable(getResources().getDrawable(R.drawable.custom_checkbox,null));
        }
        else
        {
            checkBox.setButtonDrawable(getResources().getDrawable(R.drawable.custom_checkbox));
        }
        ConnectionBaseDAO connectionBaseDAO = new ConnectionBaseDAO(PresenceGroup.this);
        connectionBaseDAO.open();
        String [] firstAndLastName = connectionBaseDAO.getFirstAndLastName(username);
        connectionBaseDAO.close();
        if(firstAndLastName.length!=0)
        {
            checkBox.setText(firstAndLastName[0]+" "+firstAndLastName[1]);
            layoutCheckBoxes.addView(checkBox,layoutCheckBoxes.getChildCount());
            PresenceGroupBaseDAO presenceGroupBaseDAO = new PresenceGroupBaseDAO(this);
            presenceGroupBaseDAO.open();
            presenceGroupBaseDAO.addMember(id,nameOfTheGroup,username,false);
            updateCheckBox(checkBox);
            usernamesList.add(username);
            presenceGroupBaseDAO.close();
        }
        else
        {
            //Send a message to tell something bad happen
        }
    }

    private void updateTitle()
    {
        if(numberOfPresence>1) {
            numberOfPeopleTextView.setText("There are " + numberOfPresence + " people present");
        }
        else if(numberOfPresence==1)
        {
            numberOfPeopleTextView.setText("There is " + numberOfPresence + " person present");
        }
        else
        {
            numberOfPeopleTextView.setText("No one is present");
        }
    }

    private void updateCheckBox(final CheckBox checkBox)
    {
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked())
                {
                    numberOfPresence++;
                    updateTitle();
                    updateDatabaseWithPresence(true);
                }
                else
                {
                    numberOfPresence--;
                    updateTitle();
                    updateDatabaseWithPresence(false);
                }
            }
        });
    }

    private void updateDatabaseWithPresence(boolean isPresent)
    {
        PresenceGroupBaseDAO presenceGroupBaseDAO = new PresenceGroupBaseDAO(this);
        presenceGroupBaseDAO.open();
        presenceGroupBaseDAO.changePresence(id,usernamesList.get(layoutCheckBoxes.getChildCount()),isPresent);
        presenceGroupBaseDAO.close();
    }

    private void setToolbar()
    {
        findViewById(R.id.results).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PresenceGroup.this,AllTheGroupsPage.class));
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PresenceGroup.this,ProfilePage.class));
            }
        });

        findViewById(R.id.disconnection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPage.editor.clear();
                MainPage.editor.apply();
                startActivity(new Intent(PresenceGroup.this,ConnectionPage.class));
            }
        });
    }
}
