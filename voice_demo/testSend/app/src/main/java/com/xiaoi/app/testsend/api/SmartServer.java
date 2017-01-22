package com.xiaoi.app.testsend.api;


import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author: Gary.shen
 * Date: 2016/7/7
 * Time: 9:09
 * des: des: 接口类
 */
public interface SmartServer {

    String BASE_URl = "https://www.baidu.com/";//红米 Robot-Test
//    String BASE_URl = "http://api.open.qingting.fm/v6/media/programs/";//红米 Robot-Test
//  String VOICE_EXPLAIN="http://poc.cluster.xiaoi.com/robot/app/znzkxtpoc/template/ask.action";
  public static String IP_URL="";

//    @FormUrlEncoded
//    @POST("access?&grant_type=client_credentials")
//    Observable<Results> login(@FieldMap Map<String, String> options);
//
//    @POST("v6/media/programs/{programId}")
//    Observable<ShowPath> getPath(@Path("programId") String id, @QueryMap Map<String, String> options);
//
//    @POST("newsearch/{keyword}/type/{type}")
//    Observable<SearchResult> search(@Path("keyword") String keyword, @Path("type") String type, @QueryMap Map<String, String> options);
//
//    //语义解析
//    @POST(VOICE_EXPLAIN)
//    Observable<ResposeResult> explainVoice(@QueryMap Map<String, String> options);

  //将ip地址传递给服务端
    @POST
    Observable<Object> sendMessages(@Url String url, @Query("content") String options);

}



