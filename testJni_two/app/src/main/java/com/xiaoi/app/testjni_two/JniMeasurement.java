package com.xiaoi.app.testjni_two;

/**
 * Created by Gary.shen on 2017/1/19.
 */

public class JniMeasurement {

    static {
        //jniutil这个参数，可根据需要任意修改
//        System.loadLibrary("jniutil");
    }

    //java调C/C++中的方法都需要用native声明且方法名必须和C/C++的方法名一样
    public native String getString();

}
