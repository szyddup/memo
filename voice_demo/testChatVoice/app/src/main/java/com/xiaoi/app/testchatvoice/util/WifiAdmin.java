package com.xiaoi.app.testchatvoice.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * @author: Gary.shen
 * Date: 2016/8/10
 * Time: 11:30
 * des:wifi基本操作工具类
 */
public class WifiAdmin {
    private static WifiAdmin wifiAdmin = null;

    private List<WifiConfiguration> mWifiConfiguration; //无线网络配置信息类集合(网络连接列表)
    private List<ScanResult> mWifiList; //检测到接入点信息类 集合

    //描述任何Wifi连接状态
    private WifiInfo mWifiInfo;

    WifiManager.WifiLock mWifilock; //能够阻止wifi进入睡眠状态，使wifi一直处于活跃状态
    public WifiManager mWifiManager;

    private static final byte AUTHMODE_OPEN = 0x00;
    private static final byte AUTHMODE_SHARED = 0x01;
    private static final byte AUTHMODE_AUTOSWITCH = 0x02;
    private static final byte AUTHMODE_WPA = 0x03;
    private static final byte AUTHMODE_WPAPSK = 0x04;
    private static final byte AUTHMODE_WPANONE = 0x05;
    private static final byte AUTHMODE_WPA2 = 0x06;
    private static final byte AUTHMODE_WPA2PSK = 0x07;
    private static final byte AUTHMODE_WPA1WPA2 = 0x08;
    private static final byte AUTHMODE_WPA1PSKWPA2PSK = 0x09;

    /**
     * 获取该类的实例（懒汉）
     *
     * @param context
     * @return
     */
    public static WifiAdmin getInstance(Context context) {
        if (wifiAdmin == null) {
            wifiAdmin = new WifiAdmin(context);
            return wifiAdmin;
        }
        return wifiAdmin;
    }

    private WifiAdmin(Context context) {
        //获取系统Wifi服务   WIFI_SERVICE
        this.mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //获取连接信息
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
    }

    /**
     * 是否存在网络信息
     *
     * @param str 热点名称
     * @return
     */
    private WifiConfiguration isExsits(String str) {
        Iterator localIterator = this.mWifiManager.getConfiguredNetworks().iterator();
        WifiConfiguration localWifiConfiguration;
        do {
            if (!localIterator.hasNext()) return null;
            localWifiConfiguration = (WifiConfiguration) localIterator.next();
        } while (!localWifiConfiguration.SSID.equals("\"" + str + "\""));
        return localWifiConfiguration;
    }

    /**
     * 锁定WifiLock，当下载大文件时需要锁定
     **/
    public void AcquireWifiLock() {
        this.mWifilock.acquire();
    }

    /**
     * 创建一个WifiLock
     **/
    public void CreateWifiLock() {
        this.mWifilock = this.mWifiManager.createWifiLock("Test");
    }

    /**
     * 解锁WifiLock
     **/
    public void ReleaseWifilock() {
        if (mWifilock.isHeld()) { //判断时候锁定
            mWifilock.acquire();
        }
    }






    /**
     * 根据wifi信息创建或关闭一个热点
     *
     * @param paramWifiConfiguration
     * @param paramBoolean           关闭标志
     */
    public void createWifiAP(WifiConfiguration paramWifiConfiguration, boolean paramBoolean) {
        try {
            Class localClass = this.mWifiManager.getClass();
            Class[] arrayOfClass = new Class[2];
            arrayOfClass[0] = WifiConfiguration.class;
            arrayOfClass[1] = Boolean.TYPE;
            Method localMethod = localClass.getMethod("setWifiApEnabled", arrayOfClass);
            WifiManager localWifiManager = this.mWifiManager;
            Object[] arrayOfObject = new Object[2];
            arrayOfObject[0] = paramWifiConfiguration;
            arrayOfObject[1] = Boolean.valueOf(paramBoolean);
            localMethod.invoke(localWifiManager, arrayOfObject);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取热点名
     **/
    public String getApSSID() {
        try {
            Method localMethod = this.mWifiManager.getClass().getDeclaredMethod("getWifiApConfiguration", new Class[0]);
            if (localMethod == null) return null;
            Object localObject1 = localMethod.invoke(this.mWifiManager, new Object[0]);
            if (localObject1 == null) return null;
            WifiConfiguration localWifiConfiguration = (WifiConfiguration) localObject1;
            if (localWifiConfiguration.SSID != null) return localWifiConfiguration.SSID;
            Field localField1 = WifiConfiguration.class.getDeclaredField("mWifiApProfile");
            if (localField1 == null) return null;
            localField1.setAccessible(true);
            Object localObject2 = localField1.get(localWifiConfiguration);
            localField1.setAccessible(false);
            if (localObject2 == null) return null;
            Field localField2 = localObject2.getClass().getDeclaredField("SSID");
            localField2.setAccessible(true);
            Object localObject3 = localField2.get(localObject2);
            if (localObject3 == null) return null;
            localField2.setAccessible(false);
            String str = (String) localObject3;
            return str;
        } catch (Exception localException) {
        }
        return null;
    }

    /**
     * 获取wifi名
     **/
    public String getBSSID() {
        if (this.mWifiInfo == null)
            return "NULL";
        return this.mWifiInfo.getBSSID();
    }

    /**
     * 得到配置好的网络
     **/
    public List<WifiConfiguration> getConfiguration() {
        return this.mWifiConfiguration;
    }

    /**
     * 获取ip地址
     **/
    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    /**
     * 获取物理地址(Mac)
     **/
    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    /**
     * 获取网络id
     **/
    public int getNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    /**
     * 获取热点创建状态
     **/
    public int getWifiApState() {
        try {
            int i = ((Integer) this.mWifiManager.getClass()
                    .getMethod("getWifiApState", new Class[0])
                    .invoke(this.mWifiManager, new Object[0])).intValue();
            return i;
        } catch (Exception localException) {
        }
        return 4;   //未知wifi网卡状态
    }

    /**
     * 获取wifi连接信息
     **/
    public WifiInfo getWifiInfo() {
        return this.mWifiManager.getConnectionInfo();
    }

    /**
     * 得到网络列表
     **/
    public List<ScanResult> getWifiList() {
        return this.mWifiList;
    }

    /**
     * 查看扫描结果
     **/
    public StringBuilder lookUpScan() {
        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            localStringBuilder.append("Index_" + new Integer(i + 1).toString() + ":");
            //将ScanResult信息转换成一个字符串包
            //其中把包括：BSSID、SSID、capabilities、frequency、level
            localStringBuilder.append((mWifiList.get(i)).toString());
            localStringBuilder.append("\n");
        }
        return localStringBuilder;
    }

    /**
     * 设置wifi搜索结果
     **/
    public void setWifiList() {
        this.mWifiList = this.mWifiManager.getScanResults();
    }



    /**
     * 得到接入点的BSSID
     **/
    public String GetBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    public static  byte getSecurity(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            return AUTHMODE_WPAPSK;
        }
        if(config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.NONE)){
            return AUTHMODE_OPEN;
        }
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) || config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
            return AUTHMODE_OPEN;
        }
        return (config.wepKeys[0] != null) ? AUTHMODE_OPEN : AUTHMODE_SHARED;
    }

}