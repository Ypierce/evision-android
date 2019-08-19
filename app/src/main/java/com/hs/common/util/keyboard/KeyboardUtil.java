package com.hs.common.util.keyboard;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hs.base.MyApplication;

/**
 * Created by jilil on 2018/10/17.
 */

public class KeyboardUtil {

    public static void popUp(Context context, EditText et){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏键盘
     * 弹窗弹出的时候把键盘隐藏掉
     */
    public static void hideInputKeyboard(Context context,View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
