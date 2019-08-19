package com.hs.common.usb;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;

import com.hs.common.constant.SharedKeyConstant;
import com.hs.common.util.MySharedPreference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: ljf
 * 时间:2019/6/28
 * 简述:<功能简述>
 */
public class USBReceiver extends BroadcastReceiver {
    private static final String TAG = USBReceiver.class.getSimpleName();
    private static final String MOUNTS_FILE = "/YanBu/Work";
    private StorageManager mStorageManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        mStorageManager = (StorageManager) context.getSystemService(Activity.STORAGE_SERVICE);
        String action = intent.getAction();
        assert action != null;
        switch (action) {
            case Intent.ACTION_MEDIA_MOUNTED:
                String mountPath = Objects.requireNonNull(intent.getData()).getPath();
                Uri data = intent.getData();
                Log.d(TAG, "mountPath = " + mountPath);
                if (!TextUtils.isEmpty(mountPath)) {
                    //读取到U盘路径再做其他业务逻辑
                    MySharedPreference.save(SharedKeyConstant.USB_PATH, mountPath, context);
                }
                break;
            case Intent.ACTION_MEDIA_UNMOUNTED:
                break;
            case Intent.ACTION_MEDIA_EJECT:
                Log.d(TAG, "onReceive: " + "U盘移除了");
                MySharedPreference.save(SharedKeyConstant.USB_PATH, "", context);
                break;
            case "android.intent.action.BOOT_COMPLETED":
                //如果是开机完成，则需要调用另外的方法获取U盘的路径
                break;
        }
    }

    /**
     * 判断是否有U盘插入,当U盘开机之前插入使用该方法.
     *
     * @param path
     * @return
     */
    public static boolean isMounted(String path) {
        boolean blnRet = false;
        String strLine = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(MOUNTS_FILE));

            while ((strLine = reader.readLine()) != null) {
                if (strLine.contains(path)) {
                    blnRet = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reader = null;
            }
        }
        return blnRet;
    }

    public static List getUSBPaths(Context con) {//反射获取路径
        String[] paths = null;
        List data = new ArrayList();    // include sd and usb devices
        StorageManager storageManager = (StorageManager) con.getSystemService(Context.STORAGE_SERVICE);
        try {
            paths = (String[]) storageManager.getClass().getMethod("getVolumePaths", new Class[0]).invoke(storageManager, new Object[]{});
            for (String path : paths) {
                String state = (String) storageManager.getClass().getMethod("getVolumeState", String.class).invoke(storageManager, path);
                if (state.equals(Environment.MEDIA_MOUNTED) && !path.contains("emulated")) {
                    data.add(path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * 获取U盘的路径和名称
     */
    private void getUName() {
        Class<?> volumeInfoClazz = null;
        Method getDescriptionComparator = null;
        Method getBestVolumeDescription = null;
        Method getVolumes = null;
        Method isMountedReadable = null;
        Method getType = null;
        Method getPath = null;
        List<?> volumes = null;
        try {
            volumeInfoClazz = Class.forName("android.os.storage.VolumeInfo");
            getDescriptionComparator = volumeInfoClazz.getMethod("getDescriptionComparator");
            getBestVolumeDescription = StorageManager.class.getMethod("getBestVolumeDescription", volumeInfoClazz);
            getVolumes = StorageManager.class.getMethod("getVolumes");
            isMountedReadable = volumeInfoClazz.getMethod("isMountedReadable");
            getType = volumeInfoClazz.getMethod("getType");
            getPath = volumeInfoClazz.getMethod("getPath");
            volumes = (List<?>) getVolumes.invoke(mStorageManager);

            for (Object vol : volumes) {
                if (vol != null && (boolean) isMountedReadable.invoke(vol) && (int) getType.invoke(vol) == 0) {
                    File path2 = (File) getPath.invoke(vol);
                    String p1 = (String) getBestVolumeDescription.invoke(mStorageManager, vol);
                    String p2 = path2.getPath();
                    Log.d(TAG, "-----------path1-----------------" + p1);               //打印U盘卷标名称
                    Log.d(TAG, "-----------path2 @@@@@-----------------" + p2);         //打印U盘路径
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
