package com.application.bazouk.whosin.groupes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.bazouk.whosin.R;
import com.application.bazouk.whosin.api.UserGroupHelper;
import com.application.bazouk.whosin.connectionpages.ConnectionPage;
import com.application.bazouk.whosin.mainpages.MainPage;
import com.application.bazouk.whosin.mainpages.NotificationPage;
import com.application.bazouk.whosin.mainpages.ProfilePage;
import com.application.bazouk.whosin.mainpages.AllTheGroupsPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import static com.firebase.ui.auth.AuthUI.TAG;

/**
 * Created by Adrien on 21/03/2018.
 */

public class PresenceGroup extends AppCompatActivity {

    private int numberOfPresence;
    private TextView numberOfPeopleTextView;
    private LinearLayout layoutCheckBoxes;
    private String id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presence_group);
        id = getIntent().getStringExtra("id");
        UserGroupHelper.getUserGroup(id).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        numberOfPeopleTextView = findViewById(R.id.number_of_people_present);
                        layoutCheckBoxes = findViewById(R.id.layout_checkbox);
                        for(int i =0; i<((List<String>)document.getData().get("names")).size();i++)
                        {
                            CheckBox checkBox = addCheckBox(((List<String>) document.getData().get("names")).get(i));
                            setCheckBox(checkBox, id, i);
                            updateCheckBox(checkBox,((List<Boolean>) document.getData().get("is_present")).get(i));
                        }
                        updateTitle();
                        findViewById(R.id.add_a_member).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AddAMemberDialog addAMemberDialog = new AddAMemberDialog(PresenceGroup.this,PresenceGroup.this,id,document);
                                addAMemberDialog.show();
                            }
                        });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        setToolbar();
    }

    private void updateCheckBox(CheckBox checkBox, Boolean isPresent)
    {
        checkBox.setChecked(isPresent);
        if(isPresent)
        {
            numberOfPresence++;
        }
    }

    public String getId()
    {
        return id;
    }

    private CheckBox addCheckBox(String name)
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
        checkBox.setText(name);
        layoutCheckBoxes.addView(checkBox,layoutCheckBoxes.getChildCount());
        return checkBox;
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

    private void setCheckBox(final CheckBox checkBox, final String id, final int i)
    {
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked())
                {
                    numberOfPresence++;
                    updateTitle();
                    UserGroupHelper.getUserGroup(id).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Boolean> isPresent = (List<Boolean>)task.getResult().getData().get("is_present");
                                isPresent.set(i, true);
                                UserGroupHelper.updateUserGroupPresence(id, isPresent);
                            }
                        }
                    });
                }
                else
                {
                    numberOfPresence--;
                    updateTitle();
                    UserGroupHelper.getUserGroup(id).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Boolean> isPresent = (List<Boolean>)task.getResult().getData().get("is_present");
                                isPresent.set(i, false);
                                UserGroupHelper.updateUserGroupPresence(id, isPresent);
                            }
                        }
                    });
                }
            }
        });
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

        findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PresenceGroup.this,NotificationPage.class));
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
                ConnectionPage.editor.clear();
                ConnectionPage.editor.apply();
                startActivity(new Intent(PresenceGroup.this,ConnectionPage.class));
            }
        });
    }
}
