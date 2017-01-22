package com.xiaoi.app.testsend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoi.app.testsend.api.RetrofitManager;
import com.xiaoi.app.testsend.api.SmartServer;
import com.xiaoi.app.testsend.api.SmartSubscriber;

import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText editText;
    private TextView textView;
    private String ip;
    private SmartServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Retrofit retrofit = RetrofitManager.get(SmartServer.BASE_URl);
        server = retrofit.create(SmartServer.class);
        editText = (EditText)findViewById(R.id.et_message);
        textView = (TextView)findViewById(R.id.tv_hint);
        ip="192.168.0.113";

    }

    public void sendMessage(View view){
        textView.setText("");
        String message=editText.getText().toString();
        if (!isNull(message)) {
            init(message);
        }else{
            Toast.makeText(MainActivity.this, "尚没有输入文字", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateIp(View view){
        String edittextString=editText.getText().toString();
        if (!isNull(edittextString)&&edittextString.length()>10&&"1".equals(edittextString.substring(0,1))&&".".equals(edittextString.substring(3,4))) {
            ip=editText.getText().toString().trim();
            Toast.makeText(MainActivity.this, "已更换", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this, "连接地址格式错误", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 判断字符串是否为空
     *
     * @param in
     * @return
     */
    public  boolean isNull(String in) {
        return in == null || "".equals(in.trim());
    }
    private void init(String message){
//        try {
//            RequestParams param = new RequestParams();
//            param.addQueryStringParameter("content",message);
//            HttpUtils http = new HttpUtils();
//            http.send(HttpRequest.HttpMethod.GET, "http://"+ip+":5570/speak", param, new RequestCallBack<String>() {
//                @Override
//                public void onStart() {
//
//                }
//                @Override
//                public void onFailure(HttpException error, String msg) {
//                    Toast.makeText(MainActivity.this, "亲，网络开小差了", Toast.LENGTH_SHORT).show();
//                    textView.setText("亲，网络开小差了");
//                }
//
//                @Override
//                public void onSuccess(ResponseInfo<String> response) {
//                    try {
//                        textView.setText("发送成功");
//                    } catch (Exception e) {
//                        Toast.makeText(MainActivity.this,"服务器响应语义返回时发生错误" + e.getMessage(),Toast.LENGTH_SHORT).show();
//                        textView.setText("服务器响应语义返回时发生错误");
//                        Log.e(TAG, "服务器响应语义返回时发生错误" + e.getMessage());
//                    }
//                }
//            });
//        } catch (Exception e) {
//            Log.e(TAG, "语义返回发生错误" + e.getMessage());
//        }

        server.sendMessages("http://"+ip+":5570/speak", message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SmartSubscriber<Object>() {

                    @Override
                    public void onSuccess(Object o) {
                        try {
                            textView.setText("发送成功");
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this,"服务器响应语义返回时发生错误" + e.getMessage(),Toast.LENGTH_SHORT).show();
                            textView.setText("服务器响应语义返回时发生错误");
                            Log.e(TAG, "服务器响应语义返回时发生错误" + e.getMessage());
                        }
                    }
                    @Override
                    public void onFailure(String message) {
                    textView.setText("亲，网络开小差了");
                    }
                });

    }
}
