package com.angcyo.dexfixdemo;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2017/03/22 16:28
 * 修改人员：Robi
 * 修改时间：2017/03/22 16:28
 * 修改备注：
 * Version: 1.0.0
 */
public class Fix {
    /**
     * 合并注入
     *
     * @param context
     * @param patchFilePath 补丁所在文件夹(尽量是SD卡的路径, 否则会打开文件失败)
     * @throws Exception
     */
    public static void injectDexElements(Context context, String patchFilePath) throws Exception {
        ClassLoader pathClassLoader = context.getClassLoader();

        /**dex, 优化后的路径, 必须在要App data目录下, 否则会没有权限*/
        File oDexFile = new File(context.getDir("odex", Context.MODE_PRIVATE).getAbsolutePath());
        /**dex 补丁文件路径(文件夹)*/
        File patchFile = new File(patchFilePath);

        if (!patchFile.exists()) {
            patchFile.mkdirs();
        }

        // 合并成一个数组
        Object applicationDexElement = getDexElementByClassLoader(pathClassLoader);

        for (File dexFile : patchFile.listFiles()) {
            ClassLoader classLoader = new DexClassLoader(dexFile.getAbsolutePath(),// dexPath
                    oDexFile.getAbsolutePath(),// optimizedDirectory
                    null,
                    pathClassLoader
            );
            // 获取这个classLoader中的Element
            Object classElement = getDexElementByClassLoader(classLoader);
            //Log.e("TAG", classElement.toString());
            applicationDexElement = combineArray(classElement, applicationDexElement);
        }

        // 注入到pathClassLoader中
        injectDexElements(pathClassLoader, applicationDexElement);
    }

    /**
     * 把dexElement注入到已运行classLoader中
     *
     * @param classLoader
     * @param dexElement
     * @throws Exception
     */
    private static void injectDexElements(ClassLoader classLoader, Object dexElement) throws Exception {
        Class<?> classLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
        Field pathListField = classLoaderClass.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        Class<?> pathListClass = pathList.getClass();
        Field dexElementsField = pathListClass.getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        dexElementsField.set(pathList, dexElement);
    }

    /**
     * 合并两个dexElements数组
     *
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    /**
     * 获取classLoader中的DexElement
     *
     * @param classLoader ClassLoader
     */
    private static Object getDexElementByClassLoader(ClassLoader classLoader) throws Exception {
        Class<?> classLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
        Field pathListField = classLoaderClass.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        Class<?> pathListClass = pathList.getClass();
        Field dexElementsField = pathListClass.getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        Object dexElements = dexElementsField.get(pathList);

        return dexElements;
    }
}
