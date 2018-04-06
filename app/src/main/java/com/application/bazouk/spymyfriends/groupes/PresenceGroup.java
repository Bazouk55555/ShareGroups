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
import com.application.bazouk.spymyfriends.sqliteservices.groupsofusernames.GroupsOfUsernamesBaseDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrien on 21/03/2018.
 */

public class PresenceGroup extends AppCompatActivity {

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
        id = getIntent().getIntExtra("id",0);
        usernamesList = new ArrayList<>();
        GroupsOfUsernamesBaseDAO groupsOfUsernamesBaseDAO = new GroupsOfUsernamesBaseDAO(this);
        groupsOfUsernamesBaseDAO.open();
        usernamesList = groupsOfUsernamesBaseDAO.getUsernames(id);
        groupsOfUsernamesBaseDAO.close();
        nameOfTheGroup = getIntent().getStringExtra("name_of_the_group");
        numberOfPeopleTextView = ((TextView) findViewById(R.id.number_of_people_present));
        ((CheckBox) findViewById(R.id.check_box)).setText(MainPage.preferences.getString(MainPage.GET_FIRST_NAME,"") + " " + MainPage.preferences.getString(MainPage.GET_LAST_NAME,""));
        layoutCheckBoxes = ((LinearLayout)findViewById(R.id.layout_checkbox));
        if(getIntent().getBooleanExtra("existing_group",false)) {
            for(String username: usernamesList)
            {
                if(!username.equals(MainPage.preferences.getString(MainPage.USERNAME,"")))
                {
                    String [] firstAndLastName = findNameFromDatabase(username);
                    if(firstAndLastName.length!=0)
                    {
                        CheckBox checkBox = addCheckBox(username, firstAndLastName);
                        setCheckBox(checkBox);
                        updateCheckBox(checkBox,id,username);
                    }
                }
            }
            updateCheckBox((CheckBox)layoutCheckBoxes.getChildAt(0),id,MainPage.preferences.getString(MainPage.USERNAME,""));
        }
        setCheckBox((CheckBox) layoutCheckBoxes.getChildAt(0));
        updateTitle();
        findViewById(R.id.add_a_member).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAMemberDialog addAMemberDialog = new AddAMemberDialog(PresenceGroup.this,PresenceGroup.this);
                addAMemberDialog.show();
            }
        });

        setToolbar();
    }

    private void updateCheckBox(CheckBox checkBox, int id_group,String username)
    {
        GroupsOfUsernamesBaseDAO groupsOfUsernamesBaseDAO = new GroupsOfUsernamesBaseDAO(this);
        groupsOfUsernamesBaseDAO.open();
        checkBox.setChecked(groupsOfUsernamesBaseDAO.getPresence(id_group,username));
        groupsOfUsernamesBaseDAO.close();
    }

    public void addAMember(String username)
    {
        String [] firstAndLastName = findNameFromDatabase(username);
        if(firstAndLastName.length!=0)
        {
            usernamesList.add(username);
            CheckBox checkBox = addCheckBox(username, firstAndLastName);
            addMemberToDatabase(username);
            setCheckBox(checkBox);
        }
        else
        {
            //Send a message to tell something bad happen
        }
    }

    private String [] findNameFromDatabase(String username)
    {
        ConnectionBaseDAO connectionBaseDAO = new ConnectionBaseDAO(PresenceGroup.this);
        connectionBaseDAO.open();
        String [] firstAndLastName = connectionBaseDAO.getFirstAndLastName(username);
        connectionBaseDAO.close();
        return firstAndLastName;
    }

    private CheckBox addCheckBox(String username, String [] firstAndLastName)
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
        checkBox.setText(firstAndLastName[0]+" "+firstAndLastName[1]);
        layoutCheckBoxes.addView(checkBox,layoutCheckBoxes.getChildCount());
        return checkBox;
    }

    private void addMemberToDatabase(String username)
    {
        GroupsOfUsernamesBaseDAO groupsOfUsernamesBaseDAO = new GroupsOfUsernamesBaseDAO(this);
        groupsOfUsernamesBaseDAO.open();
        groupsOfUsernamesBaseDAO.addMember(id,nameOfTheGroup,username,false);
        groupsOfUsernamesBaseDAO.close();
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

    private void setCheckBox(final CheckBox checkBox)
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
        GroupsOfUsernamesBaseDAO groupsOfUsernamesBaseDAO = new GroupsOfUsernamesBaseDAO(this);
        groupsOfUsernamesBaseDAO.open();
        groupsOfUsernamesBaseDAO.changePresence(id,usernamesList.get(layoutCheckBoxes.getChildCount()-1),isPresent);
        groupsOfUsernamesBaseDAO.close();
    }

    private void setToolbar()
    {
        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PresenceGroup.this,MainPage.class));
            }
        });

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
