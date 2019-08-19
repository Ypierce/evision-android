package com.hs.common.socket;

import android.content.SharedPreferences;
import android.provider.ContactsContract;

import com.hs.common.clock.ComPortUtil;
import com.hs.common.constant.AppConfig;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class UdpClient {

    private static final String TAG = "UdpClient";
    private static UdpClient udpClient;

    private String serverIp;
    private int serverPort;

    private DatagramPacket sendPacket;
    private DatagramPacket receivePacket;
    private DatagramSocket datagramSocket;

    public String receiveString;


    private InputStream inputStream;

    /**
     * 单例模式
     */
    public static UdpClient getInstance(){

        if(udpClient == null);{
            synchronized (UdpClient.class){
                if(udpClient == null){
                    udpClient = new UdpClient();
                }
            }
        }
        return  udpClient;
    }

    public UdpClient(){
        serverIp = AppConfig.getInstance().getUdpServerIp();
        serverPort = AppConfig.getInstance().getUdpServerPort();
        try {
            datagramSocket = new DatagramSocket(8089);
        }catch (SocketException e){
            e.printStackTrace();
        }
    }

    public void sendSocket(String sendData) {
        Logger.d(sendData);
        byte[] bytes = ComPortUtil.StrToBCDBytes(sendData);
        try{
            sendPacket = new DatagramPacket(bytes,bytes.length,InetAddress.getByName(serverIp),serverPort);
            Logger.d(serverIp);
            Logger.d(serverPort);
            datagramSocket.send(sendPacket);
        }catch (IOException e){
            e.printStackTrace();
        }

    }



    public String receiveSocket(){
        byte[] bytes = new byte[12];
        try {
            receivePacket = new DatagramPacket(bytes,bytes.length,InetAddress.getByName(serverIp),serverPort);
           if(receivePacket.getLength() < 10){
               receiveString = "";
           }else {
               datagramSocket.receive(receivePacket);
               receiveString = ComPortUtil.bcdToString(receivePacket.getData());
           }
            Logger.d(receivePacket.getLength());
        }catch (IOException e){
            e.printStackTrace();
        }
        return receiveString;
    }

    public void closeUdpClient(){
        if(null != datagramSocket){
            try{
                datagramSocket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
