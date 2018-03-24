package com.application.bazouk.spymyfriends.groupes;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.application.bazouk.spymyfriends.R;

/**
 * Created by Adrien on 24/03/2018.
 */

public class AddAMemberDialog extends Dialog {
    public AddAMemberDialog(@NonNull Context context, final PresenceGroup presenceGroup) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_a_member_dialog);

        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenceGroup.addAMember(((EditText)findViewById(R.id.username)).getText().toString());
                dismiss();
            }
        });
    }
}
