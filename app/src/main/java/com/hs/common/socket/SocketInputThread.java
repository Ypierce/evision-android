package com.hs.common.socket;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;

public class SocketInputThread extends Thread{

    private static final String TAG = "SocketInputThread";

    private volatile boolean isStart = false;

    private volatile String  recvBuffer = "";

    private volatile boolean isBlocked = false;

    public SocketInputThread(){

    }

    public void setStart(boolean start){
        this.isStart = start;
    }

    public void setRecvBuffer() {
        if(isBlocked){
            recvBuffer="";
        }
    }

    public String getRecvBuffer(){
        return recvBuffer;
    }
    public void stopThread(){
        isStart = false;
        this.interrupt();
    }


    @Override
    public void run(){
        super.run();
        while(isStart){
            Logger.d("接受线程");
                recvBuffer = UdpClient.getInstance().receiveSocket();
                isBlocked = true;
        }
    }

    public int isBlocked(){
        recvBuffer = UdpClient.getInstance().receiveSocket();
        if(recvBuffer.startsWith("A5")){
            return 0;
        }else {
            return 1;
        }
    }


}
