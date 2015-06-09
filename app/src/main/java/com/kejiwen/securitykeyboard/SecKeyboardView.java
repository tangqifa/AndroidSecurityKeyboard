package com.kejiwen.securitykeyboard;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class SecKeyboardView {

    private Activity mActivity;

    public boolean isNun = false;// 是否数据键盘
    public boolean isUpper = false;// 是否大写

    private InputMethodManager mImm;
    private KeyboardView mKeyboardView;
    private Keyboard k1;// 字母键盘
    private Keyboard k2;// 数字键盘

    private EditText ed;


    public SecKeyboardView(Activity act, final EditText editText) {

        this.mActivity = act;
        this.ed = editText;

        k1 = new Keyboard(mActivity, R.xml.qwerty);
        k2 = new Keyboard(mActivity, R.xml.symbols);

        mImm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

        mKeyboardView = (KeyboardView) mActivity.findViewById(R.id.keyboard_view);
        mKeyboardView.setKeyboard(k1);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(listener);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    hideKeyboard();
                } else {
                    mImm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                }
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inputType = editText.getInputType();
                hideSoftInputMethod(editText);
                showKeyboard();
                editText.setInputType(inputType);
                return false;
            }
        });
    }


    /**
     * 隐藏系统键盘 Edittext不显示系统键盘；并且要有光标； 4.0以上TYPE_NULL，不显示系统键盘，但是光标也没了；
     */
    private void hideSoftInputMethod(EditText et) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
            // 19 setShowSoftInputOnFocus
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }

        if (methodName == null) {
            et.setInputType(InputType.TYPE_NULL);
        } else {
            Class<TextView> cls = TextView.class;
            java.lang.reflect.Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(this, false);
            } catch (Exception e) {
                et.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            }
        }
    }

    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                hideKeyboard();
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
                changeKey();
                mKeyboardView.setKeyboard(k1);

            } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
                if (isNun) {
                    isNun = false;
                    mKeyboardView.setKeyboard(k1);
                } else {
                    isNun = true;
                    mKeyboardView.setKeyboard(k2);
                }
            } else if (primaryCode == 57419) { // go left
                if (start > 0) {
                    ed.setSelection(start - 1);
                }
            } else if (primaryCode == 57421) { // go right
                if (start < ed.length()) {
                    ed.setSelection(start + 1);
                }
            } else {
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    };

    /**
     * 键盘大小写切换
     */
    private void changeKey() {
        List<Key> keyList = k1.getKeys();
        if (isUpper) {//大写切换小写
            isUpper = false;
            for (Key key : keyList) {
                if (key.label != null && isWord(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                }
            }
        } else {//小写切换大写
            isUpper = true;
            for (Key key : keyList) {
                if (key.label != null && isWord(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0] - 32;
                }
            }
        }
    }

    public void showKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mKeyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            mKeyboardView.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isWord(String str) {
        String wordStr = "abcdefghijklmnopqrstuvwxyz";
        if (wordStr.indexOf(str.toLowerCase()) > -1) {
            return true;
        }
        return false;
    }

}
