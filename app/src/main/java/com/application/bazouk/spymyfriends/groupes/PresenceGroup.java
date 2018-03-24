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

/**
 * Created by Adrien on 21/03/2018.
 */

public class PresenceGroup extends AppCompatActivity {

    private int numberOfPresence = 1;
    private TextView numberOfPeopleTextView;
    private LinearLayout layoutCheckBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presence_group);
        numberOfPeopleTextView = ((TextView) findViewById(R.id.number_of_people_present));
        ((CheckBox) findViewById(R.id.check_box)).setText(getIntent().getStringExtra("first_name") + " " + getIntent().getStringExtra("last_name"));
        updateTitle();
        layoutCheckBoxes = ((LinearLayout)findViewById(R.id.layout_checkbox));
        updateCheckBox((CheckBox)layoutCheckBoxes.getChildAt(0));

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
        checkBox.setPadding(dpAsPixels,dpAsPixels,dpAsPixels,dpAsPixels);
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
        if(firstAndLastName.length!=0)
        {
            checkBox.setText(firstAndLastName[0]+" "+firstAndLastName[1]);
            layoutCheckBoxes.addView(checkBox,layoutCheckBoxes.getChildCount());
            updateCheckBox(checkBox);
        }
        else
        {

        }
        connectionBaseDAO.close();

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
                }
                else
                {
                    numberOfPresence--;
                    updateTitle();
                }
            }
        });
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
