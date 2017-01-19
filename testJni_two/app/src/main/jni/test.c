//// Created by XY on 16/1/4.//
#include "com_xiaoi_app_testjni_two_JniMeasurement.h"
/*
* Class:     Java_com_wobiancao_ndkjnidemo_ndk_JniUtils
* Method:    getStringFormC
* Signature: ()Ljava/lang/String;
*/
JNIEXPORT jstring JNICALL Java_com_xiaoi_app_testjni_1two_JniMeasurement_getString
        (JNIEnv *env, jobject obj){
   return (*env)->NewStringUTF(env,"这里是来自c的string");
   }