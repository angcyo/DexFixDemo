package com.angcyo.dexfixdemo;

import android.app.Application;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2017/03/22 16:25
 * 修改人员：Robi
 * 修改时间：2017/03/22 16:25
 * 修改备注：
 * Version: 1.0.0
 */
public class App extends Application {
    static App mApp;

    public static App getApp() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.e("call: onCreate([])-> ");
        mApp = this;
    }
}
