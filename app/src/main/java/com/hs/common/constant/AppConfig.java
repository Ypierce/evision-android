package com.hs.common.constant;

/**
 * Created by jill on 2017/8/10.
 */

public class AppConfig {
    //阿里云地址
//    public static String BASE_IMAGE_URL = "http://118.178.182.6:8028/";
   public static String BASE_IMAGE_URL = "http://192.168.31.100:8080/";


//    public static String BASE_IMAGE_URL = "http://192.168.31.122:8080/";

    //
    public static String BASE_URL = BASE_IMAGE_URL + "api/";

    public static int APP_PLATFORM = 1;//0单独使用 1正常使用

    public static String RTSP_URL = "rtsp://192.168.31.120:554/live/0";

    private volatile String udpServerIp ;

    private volatile int udpServerPort ;

    private  volatile static AppConfig instance;

    public static AppConfig getInstance(){
        if(instance == null){
            synchronized (AppConfig.class){
                if(instance == null){
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }

    public String getUdpServerIp() {
        return udpServerIp;
    }

    public void setUdpServerIp(String udpServerIp) {
        this.udpServerIp = udpServerIp;
    }

    public int getUdpServerPort() {
        return udpServerPort;
    }

    public void setUdpServerPort(int udpServerPort) {
        this.udpServerPort = udpServerPort;
    }
}
