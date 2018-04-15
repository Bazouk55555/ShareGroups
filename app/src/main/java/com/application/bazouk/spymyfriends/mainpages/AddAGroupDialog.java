package com.application.bazouk.spymyfriends.mainpages;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.application.bazouk.spymyfriends.R;
import com.application.bazouk.spymyfriends.groupes.PresenceGroup;
import com.application.bazouk.spymyfriends.sqliteservices.connection.ConnectionBaseDAO;

/**
 * Created by Adrien on 24/03/2018.
 */

public class AddAGroupDialog extends Dialog {

    public AddAGroupDialog(@NonNull final Context context, final MainPage mainPage) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_a_group_dialog);

        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPage.createANewGroup(((EditText) findViewById(R.id.group_name)).getText().toString());
                dismiss();
            }
        });
    }
}
