package com.kejiwen.securitykeyboard;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;


public class MainActivityDemo extends Activity {

    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPassword = (EditText) findViewById(R.id.password_edit);
        new SecKeyboardView(this, mPassword);
    }
}
