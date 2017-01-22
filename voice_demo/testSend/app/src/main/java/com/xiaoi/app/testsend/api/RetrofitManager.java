package com.xiaoi.app.testsend.api;


import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author: Gary.shen
 * Date: 2016/7/6
 * Time: 18:30
 * des:Retrofit管理类
 */
public class RetrofitManager {
    private static final String TAG = RetrofitManager.class.getName();
    private static RetrofitManager manager;
    private final OkHttpClient onclient;
    private Retrofit retrofit;

    //单列模式
    public static RetrofitManager getManager() {
        if (manager == null) {
            synchronized (RetrofitManager.class) {
                if (manager == null) {
                    manager = new RetrofitManager();
                }
            }
        }
        return manager;
    }

    private RetrofitManager() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = null;
                try {
                    response = chain.proceed(chain.request());
                } catch (Exception e) {
                    Log.e(TAG, "服务器连接响应超时" + e.getMessage());
                }
                return response;
            }
        };


        onclient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
//                .addNetworkInterceptor(mTokenInterceptor)
                .build();


    }

    private Retrofit getRetrofit(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .client(onclient)
                .baseUrl(baseUrl)
                .addConverterFactory(new NullOnEmptyConverterFactory())//An empty pojo is {} in JSON.
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    //获得retrofit对象
    public static Retrofit get(String baseUrl) {
        return getManager().getRetrofit(baseUrl);

    }


    public class NullOnEmptyConverterFactory extends Converter.Factory {
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return new Converter<ResponseBody, Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException {
                    if (body.contentLength() == 0) return null;
                    return delegate.convert(body);
                }
            };
        }
    }

}

