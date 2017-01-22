package com.xiaoi.app.testchatvoice;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.xiaoi.app.testchatvoice.handler.SpeakHandler;
import com.xiaoi.app.testchatvoice.util.WifiAdmin;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.AndServerBuild;

import butterknife.Bind;
import butterknife.ButterKnife;


public class VoiceActivity extends AppCompatActivity {
    public static final String APPID = "57833929";// 科大讯飞所注册应用的标识ID
    private static final String TAG = "VoiceActivity";
    @Bind(R.id.ip)
    TextView ip;
    private SpeechSynthesizer mTts;

    private AndServer andServer;
    private WifiAdmin wifiAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        wifiAdmin = WifiAdmin.getInstance(this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        SpeechUtility.createUtility(VoiceActivity.this, SpeechConstant.APPID + "=" + APPID);
        initServer();
        if (SpeechSynthesizer.getSynthesizer() == null) {
            mTts = SpeechSynthesizer.createSynthesizer(this, null);
        } else {
            mTts = SpeechSynthesizer.getSynthesizer();
        }
        ip.setText(formatIpAddress(wifiAdmin.getWifiInfo().getIpAddress()));
    }


    private String formatIpAddress(int ipAdress) {
        String ip = (ipAdress & 0xFF) + "." +
                ((ipAdress >> 8) & 0xFF) + "." +
                ((ipAdress >> 16) & 0xFF) + "." +
                (ipAdress >> 24 & 0xFF);
        return ip;
    }


    private void initServer() {
        if (andServer == null || !andServer.isRunning()) {
            AndServerBuild andServerBuild = AndServerBuild.create();
            andServerBuild.setTimeout(5 * 1000);//实际似乎
            andServerBuild.setPort(5570);// 指定http端口号。
            // 注册接口。
            andServerBuild.add("speak", new SpeakHandler(this));
            // 构建AndServer并启动服务器。
            andServer = andServerBuild.build();
            andServer.launch();
        }
    }

    public void speak(String content) {
        voiceCompound(content);
    }


    private void voiceCompound( String answer) {
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        if (mTts != null) {
            mTts.stopSpeaking();//防止说话重叠
        }
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaowanzi");//设置发音人：小丸子
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码c
//        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/mode_school_hint.wav");
        //合成监听器
        try {
            SynthesizerListener mSynListener = new SynthesizerListener() {
                //会话结束回调接口，没有错误时，error为null
               public void onCompleted(SpeechError error) {
                   Toast.makeText(VoiceActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                }

                //缓冲进度回调
                //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
                public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
                }

                //开始播放
                public void onSpeakBegin() {
                }

                //暂停播放
                public void onSpeakPaused() {
                }

                //播放进度回调
                //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
                public void onSpeakProgress(int percent, int beginPos, int endPos) {
                }

                //恢复播放回调接口
                public void onSpeakResumed() {
                }

                //会话事件回调接口
                public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
                }
            };
            mTts.startSpeaking(answer, mSynListener);
        } catch (Exception e) {
            Log.e(TAG, "启动说话报错" + e.getMessage());
        }
    }
}
