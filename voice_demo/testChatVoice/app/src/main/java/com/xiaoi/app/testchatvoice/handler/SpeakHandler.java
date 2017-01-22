package com.xiaoi.app.testchatvoice.handler;

import com.xiaoi.app.testchatvoice.VoiceActivity;
import com.yanzhenjie.andserver.AndServerRequestHandler;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by Gary.shen on 2016/11/18.
 */

public class SpeakHandler implements AndServerRequestHandler {
    VoiceActivity mConfigActivity;
    public SpeakHandler(VoiceActivity configActivity) {
        mConfigActivity=configActivity;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parse(request);

        if(params!=null){
            if(!isNull(params.get("content"))){
                String value = URLDecoder.decode(params.get("content"), "utf-8");
                mConfigActivity.speak(value);
            }else{
                mConfigActivity.speak("");//空
            }
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param in
     * @return
     */
    public static boolean isNull(String in) {
        return in == null || "".equals(in.trim());
    }
}
