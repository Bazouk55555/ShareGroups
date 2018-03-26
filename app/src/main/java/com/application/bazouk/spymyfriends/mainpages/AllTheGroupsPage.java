package com.application.bazouk.spymyfriends.mainpages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.application.bazouk.spymyfriends.R;
import com.application.bazouk.spymyfriends.connectionpages.ConnectionPage;
import com.application.bazouk.spymyfriends.sqliteservices.presencegroup.PGroup;

import java.util.List;

/**
 * Created by Adrien on 10/01/2018.
 */

public class AllTheGroupsPage extends AppCompatActivity {

    private List<PGroup> listOfGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_page);

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
