package com.application.bazouk.spymyfriends.connectionpages;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.application.bazouk.spymyfriends.R;
import com.application.bazouk.spymyfriends.sqliteservices.connection.ConnectionBaseDAO;

public class SignInDialog extends Dialog {

    private Context context;

    public SignInDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_in_dialog);
        findViewById(R.id.sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionBaseDAO connectionBaseDAO = new ConnectionBaseDAO(context);
                connectionBaseDAO.open();
                connectionBaseDAO.add(((EditText) findViewById(R.id.username)).getText().toString(), ((EditText) findViewById(R.id.password)).getText().toString(),((EditText) findViewById(R.id.first_name)).getText().toString(),((EditText) findViewById(R.id.last_name)).getText().toString());
                connectionBaseDAO.close();
                dismiss();
            }
        });
    }
}
