package com.kejiwen.securitykeyboard;

import android.app.Activity;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.widget.EditText;

import com.kejiwen.securitykeyboardlibrary.SecKeyboardView;


public class MainActivityDemo extends Activity {

    private EditText mPassword;
    private KeyboardView mKeyboardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPassword = (EditText) findViewById(R.id.password_edit);
        mKeyboardView = (KeyboardView)findViewById(com.kejiwen.securitykeyboardlibrary.R.id.keyboard_view);
        new SecKeyboardView(this, mPassword,mKeyboardView);
    }
}
