package com.application.bazouk.spymyfriends.groupes;

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
import com.application.bazouk.spymyfriends.sqliteservices.connection.ConnectionBaseDAO;
import com.application.bazouk.spymyfriends.sqliteservices.presencegroup.NotificationPresenceGroupBaseDAO;

/**
 * Created by Adrien on 24/03/2018.
 */

public class AddAMemberDialog extends Dialog {
    public AddAMemberDialog(@NonNull final Context context, final PresenceGroup presenceGroup) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_a_member_dialog);

        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                ConnectionBaseDAO connectionBaseDAO = new ConnectionBaseDAO(context);
                connectionBaseDAO.open();
                if(connectionBaseDAO.isUsernameReal(username))
                {
                    NotificationPresenceGroupBaseDAO notificationPresenceGroupBaseDAO = new NotificationPresenceGroupBaseDAO(context);
                    notificationPresenceGroupBaseDAO.open();
                    notificationPresenceGroupBaseDAO.addNotificationGroup(presenceGroup.getId(),username);
                    //presenceGroup.addAMember(((EditText) findViewById(R.id.username)).getText().toString());
                    notificationPresenceGroupBaseDAO.close();
                }
                else
                {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Not  existing username").setMessage("This username does not exist")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setIcon(android.R.drawable.ic_dialog_alert).show();
                }
                dismiss();
            }
        });
    }
}
