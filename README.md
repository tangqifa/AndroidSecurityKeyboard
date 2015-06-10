#Android安全密码键盘
##简介

在财经和银行类应用中，app通常会提供自己安全键盘，以防密码泄露；
<img src="https://github.com/tangqifa/AndroidSecurityKeyboard/blob/master/Screenshot/Screenshot_20150610-022340.pngg" width = "300" height = "200" alt="图片名称" align=center />
![shot1](https://github.com/tangqifa/AndroidSecurityKeyboard/blob/master/Screenshot/Screenshot_20150610-022340.png "字母键盘")
![shot2](https://github.com/tangqifa/AndroidSecurityKeyboard/blob/master/Screenshot/Screenshot_20150610-022613.png "数字键盘")

##使用

###第一步：在需要安全键盘的Activity或则Fragment的布局文件中加入如下代码：

    <android.inputmethodservice.KeyboardView
           android:id="@+id/keyboard_view"
           android:layout_width="fill_parent"
           android:layout_height="wrap_content"
           android:layout_alignParentBottom="true"
           android:focusable="true"
           android:focusableInTouchMode="true"
           android:keyBackground="@drawable/btn_keyboard_key"
           android:keyTextColor="#ffffffff"
           android:visibility="gone" />

###如果根布局不是RelativeLayout,则在底部加入如下代码：

    <RelativeLayout
        android:layout_width="fill_parent"
        android:layout_height="wrap_content">
        <android.inputmethodservice.KeyboardView
           android:id="@+id/keyboard_view"
           android:layout_width="fill_parent"
           android:layout_height="wrap_content"
           android:layout_alignParentBottom="true"
           android:focusable="true"
           android:focusableInTouchMode="true"
           android:keyBackground="@drawable/btn_keyboard_key"
           android:keyTextColor="#ffffffff"
           android:visibility="gone" />
    <RelativeLayout/>

### 第二步：在需要安全键盘的Activity或则Fragment的onCreate()方法或则onCreateView()方法中加入如下代码：

    KeyboardView mKeyboardView;
    mKeyboardView =   (KeyboardView)findViewById(com.kejiwen.securitykeyboardlibrary.R.id.keyboard_view);
    new SecKeyboardView(this, mPassword,mKeyboardView);

##Android Studio快速集成

