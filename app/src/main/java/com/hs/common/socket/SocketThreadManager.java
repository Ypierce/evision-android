package com.hs.common.socket;

import com.hs.common.clock.ComPortUtil;
import com.orhanobut.logger.Logger;

public class SocketThreadManager {

    private static final String TAG = "SocketThreadManager";

    private SocketOutThread socketOutThread;

    private SocketInputThread socketInputThread;

    private static SocketThreadManager instance;

    public static SocketThreadManager getInstance(){
        if(instance == null){
            synchronized (SocketThreadManager.class){
                if(instance == null){
                    instance = new SocketThreadManager();
                }
            }
        }
        return instance;
    }

    public SocketThreadManager(){
        socketInputThread = new SocketInputThread();
        socketInputThread.setStart(true);
        socketInputThread.start();

        socketOutThread = new SocketOutThread();
        socketOutThread.setStart(true);
        socketOutThread.start();
    }

    public void stopThread(){
        socketOutThread.stopThread();
        socketInputThread.stopThread();
    }

    public void sendMsg(String cmd){
        socketOutThread.addMsgtoList(cmd);
    }

    public String getCmd(){
        String rev = socketInputThread.getRecvBuffer();
        socketInputThread.setRecvBuffer();
       return rev;
    }

}
