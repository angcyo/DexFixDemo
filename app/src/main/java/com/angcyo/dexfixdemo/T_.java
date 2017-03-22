package com.angcyo.dexfixdemo;

import android.widget.Toast;

/**
 * Created by angcyo on 2016-12-23.
 */

public class T_ {
    public static void show(final CharSequence text) {
        Toast.makeText(App.getApp(), text, Toast.LENGTH_SHORT).show();
    }

}
