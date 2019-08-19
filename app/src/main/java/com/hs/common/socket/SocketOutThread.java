package com.hs.common.socket;

import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */
public class SocketOutThread extends Thread {
    private final static  String TAG = "SocketOutThread";

    private volatile boolean isStart = false;

    private List<String> sendMsgList;

    public SocketOutThread(){
        sendMsgList = new CopyOnWriteArrayList<String>();
        sendMsgList.add(null);
    }

    public void setStart(boolean start){
        isStart = start;
        synchronized (this){
            notify();
        }
    }

    public void stopThread(){
        this.isStart = false;
        this.notify();
    }

    public void addMsgtoList(String cmd){
        synchronized (this){
            sendMsgList.add(cmd);
            notify();
        }
    }

    @Override
    public void run(){
        super.run();
        while (isStart){
            synchronized (sendMsgList){
                for(String str : sendMsgList){
                    if(str != null){
                        Logger.d(str);
                        UdpClient.getInstance().sendSocket(str);
                    }
                    sendMsgList.remove(str);
                }
            }
        }
    }
}
