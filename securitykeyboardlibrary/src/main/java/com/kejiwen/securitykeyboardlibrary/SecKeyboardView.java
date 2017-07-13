package com.kejiwen.securitykeyboardlibrary;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class SecKeyboardView {

    private boolean isUpper = false;// 是否大写

    private InputMethodManager mImm;
    private KeyboardView mKeyboardView;
    private Keyboard alphabetKeyBoard;
    private Keyboard numberKeyBoard;
    private Keyboard symbolKeyBoard;

    private EditText ed;


    public SecKeyboardView(Activity act, final EditText editText, KeyboardView keyboardView) {

        Activity activity = act;
        this.ed = editText;
        this.mKeyboardView = keyboardView;

        alphabetKeyBoard = new Keyboard(activity, R.xml.qwerty);
        numberKeyBoard = new Keyboard(activity, R.xml.number);
        symbolKeyBoard = new Keyboard(activity, R.xml.symbols);

        mImm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        mKeyboardView.setKeyboard(alphabetKeyBoard);
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
        private static final int CUSTOM_KEY_SYMBOL = -7;
        private static final int CUSTOM_KEY_NUMBER = -8;
        private static final int CUSTOM_KEY_ALPHABET = -9;

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
            switch (primaryCode) {
                case Keyboard.KEYCODE_DONE:
                    hideKeyboard();
                    break;
                case Keyboard.KEYCODE_DELETE:
                    if (editable != null && editable.length() > 0 & start > 0) {
                        editable.delete(start - 1, start);
                    }
                    break;
                case Keyboard.KEYCODE_SHIFT:
                    changeKey();
                    mKeyboardView.setKeyboard(alphabetKeyBoard);
                    break;
                case CUSTOM_KEY_NUMBER:
                    mKeyboardView.setKeyboard(numberKeyBoard);
                    break;
                case CUSTOM_KEY_ALPHABET:
                    mKeyboardView.setKeyboard(alphabetKeyBoard);
                    break;
                case CUSTOM_KEY_SYMBOL:
                    mKeyboardView.setKeyboard(symbolKeyBoard);
                    break;
                case Keyboard.EDGE_LEFT:
                    if (start > 0) {
                        ed.setSelection(start - 1);
                    }
                    break;
                case Keyboard.EDGE_RIGHT:
                    if (start < ed.length()) {
                        ed.setSelection(start + 1);
                    }
                    break;
                default:
                    editable.insert(start, Character.toString((char) primaryCode));
                    break;
            }
        }
    };

    /**
     * 键盘大小写切换
     */
    private void changeKey() {
        List<Key> keyList = alphabetKeyBoard.getKeys();
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

    private void showKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mKeyboardView.setVisibility(View.VISIBLE);
        }
    }

    private void hideKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            mKeyboardView.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isWord(String s) {
        return s.toLowerCase().matches("[a-z]");
    }

}
