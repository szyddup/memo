package com.xiaoi.app.testsend.api;

import rx.Subscriber;

/**
 * @author: Gary.shen
 * Date: 2016/8/10
 * Time: 11:33
 * des:回调类
 */


public abstract class SmartSubscriber<T extends Object> extends Subscriber<T> {

    public static String TAG = "SmartSubscriber";

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e != null) {
            e.printStackTrace();
//            Logger.e(TAG, e.getMessage());
            onFailure("联网发生错误，错误信息" + e.getMessage());
        }
    }

    @Override
    public void onNext(T t) {
//        if (t != null && t.isSuccess()) {
        onSuccess(t);
//        } else {
//            onFailure(t != null ? t.getMessage() : "");
//            Log.e(TAG, t.getMessage());
//        }
    }

    public abstract void onSuccess(T t);

    //
    public abstract void onFailure(String message);
}
