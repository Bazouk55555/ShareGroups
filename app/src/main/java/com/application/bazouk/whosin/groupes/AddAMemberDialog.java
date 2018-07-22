package com.application.bazouk.whosin.groupes;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.application.bazouk.whosin.R;
import com.application.bazouk.whosin.api.NotificationHelper;
import com.application.bazouk.whosin.api.UserGroupHelper;
import com.application.bazouk.whosin.api.UserHelper;
import com.application.bazouk.whosin.models.Notification;
import com.application.bazouk.whosin.models.User;
import com.application.bazouk.whosin.models.connection.ConnectionBaseDAO;
import com.application.bazouk.whosin.models.presencegroup.NotificationPresenceGroupBaseDAO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by Adrien on 24/03/2018.
 */

public class AddAMemberDialog extends Dialog {
    public AddAMemberDialog(@NonNull final Context context, final PresenceGroup presenceGroup, final String id, final DocumentSnapshot document) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_a_member_dialog);

        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = ((EditText) findViewById(R.id.username)).getText().toString();
                if(((List<String>)document.getData().get("usernames")).contains(username))
                {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Adding a member twice").setMessage("You cannot add a member twice")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setIcon(android.R.drawable.ic_dialog_alert).show();
                }
                else {
                    UserHelper.getUsersCollection().whereEqualTo("username", username).get().
                            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.getResult().isEmpty()) {
                                        AlertDialog.Builder builder;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                                        } else {
                                            builder = new AlertDialog.Builder(context);
                                        }
                                        builder.setTitle("username not existing").setMessage("This username does not exists")
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                                    } else {
                                        Notification notification = new Notification(id, username,(String) document.getData().get("name_of_the_group"));
                                        NotificationHelper.createNotification(notification);
                                    }
                                }
                            });
                }
                dismiss();
            }
        });
    }
}
