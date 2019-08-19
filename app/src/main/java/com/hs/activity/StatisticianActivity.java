package com.hs.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileOutputStream;
import com.github.mjdev.libaums.partition.Partition;
import com.gyf.barlibrary.BarHide;
import com.hs.R;
import com.hs.activity.dialog.CalendarDialog;
import com.hs.activity.pop.CommonPopupWindow;
import com.hs.adapter.maintenance.CommonNameValueAdapter;
import com.hs.adapter.statistician.StatisticianListParentAdapter;
import com.hs.base.GlobalInfo;
import com.hs.bean.maintenance.CommonIdNameBean;
import com.hs.bean.maintenance.VarietyListBean;
import com.hs.bean.statistician.StatisticsDataBean;
import com.hs.bean.statistician.StatisticsHeadBean;
import com.hs.common.constant.SharedKeyConstant;
import com.hs.common.intent.IntentStartActivityHelper;
import com.hs.common.usb.USBReceiver;
import com.hs.common.util.ExcelUtil;
import com.hs.common.util.MySharedPreference;
import com.hs.common.util.ToastUtils;
import com.hs.common.util.Tools;
import com.hs.service.MaintenanceService;
import com.hs.service.StatisticianService;
import com.hs.service.listener.CommonResultListener;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * @Author: ljf
 * 时间:2019/6/4
 * 简述:验布统计元
 */
public class StatisticianActivity extends BaseActivity implements CalendarDialog.YearMonthDayListener
        , CommonNameValueAdapter.CommonIdNameListener {
    @BindView(R.id.rv_statistics_parent)
    RecyclerView rvStatisticsParent;
    @BindView(R.id.et_machineNo)
    EditText etMachineNo;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.et_variety)
    EditText etVariety;
    @BindView(R.id.im_search)
    ImageView imSerarch;

    private StatisticianListParentAdapter statisticianListAdapter;

    private List<StatisticsDataBean> statisticsDataBeanList;
    private List<StatisticsDataBean> statisticsDataHeadList;
    private List<StatisticsDataBean> statisticsDataShowList;

    private StatisticianService statisticianService;
    private MaintenanceService mMaintenanceService;
    private String strDate;
    private String mDate="";
    private String variety = "";
    private String machineNo = "";
    private int type;
    private static final int TYPE_VARIETIES = 0;
    private CommonPopupWindow mPopupWindow;
    private List<String> headList;
    private List<String> dataList;
    private USBReceiver mUsbReceiver;

    private UsbMassStorageDevice[] storageDevices;
    private UsbFile cFolder;
    //自定义U盘读写权限
    public static final String ACTION_USB_PERMISSION = "com.example.usbreadwriterdaemon.USB_PERMISSION";
    private final static String U_DISK_FILE_NAME = "taiyuan.doc";
    private boolean isConnected;
    private boolean hasPermission;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_statistician;
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.transparent;
    }

    @Override
    protected boolean fitSystemWindow() {
        return false;
    }

    @Override
    protected void beforeImmersionBarInit() {
        super.beforeImmersionBarInit();
        mImmersionBar
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .statusBarDarkFont(false)
                .keyboardEnable(true);
    }

    @Override
    protected void initView() {
        super.initView();
        statisticianService = new StatisticianService(this);
        mMaintenanceService = new MaintenanceService(this);
        strDate = getCurrentDate();
        initListView();
        initUSBReceiver();

        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }

    /* 获取系统时间 格式为："yyyy-MM-dd " */

    public static String getCurrentDate() {
        Date d = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
    }

    private void initListView() {
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvStatisticsParent.setLayoutManager(mManager);
        statisticsDataBeanList = new ArrayList<>();
        statisticianListAdapter = new StatisticianListParentAdapter(this, statisticsDataBeanList);
        rvStatisticsParent.setAdapter(statisticianListAdapter);

    }

    private void initUSBReceiver() {
        mUsbReceiver = new USBReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        mFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
        mFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        mFilter.addAction("android.hardware.usb.action.USB_STATE");
        mFilter.addAction(ACTION_USB_PERMISSION); //自定义广播
        registerReceiver(mUsbReceiver, mFilter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        etMachineNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                machineNo = charSequence.toString();
//                initData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etVariety.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                variety = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        getStatisticianHeadData();
    }

    private void getStatisticianHeadData() {
        statisticianService.getStatisticianHead(new CommonResultListener<StatisticsHeadBean>() {
            @Override
            public void successHandle(StatisticsHeadBean result) {
                statisticsDataHeadList = getListDataStatistician(result);
                statisticianListAdapter.setReallySize(result.headList.size());
                statisticianListAdapter.replaceAll(statisticsDataHeadList);
            }
        });
    }

    private void getStatisticianListData(String machineNo, String strDate, String variety) {
        statisticsDataShowList = new ArrayList<>();
        statisticsDataBeanList = new ArrayList<>();
        statisticianService.getStatisticianData(machineNo, strDate, variety, new CommonResultListener<List<StatisticsDataBean>>() {
            @Override
            public void successHandle(List<StatisticsDataBean> result) {
                statisticsDataShowList.clear();
                statisticsDataBeanList.clear();
                statisticsDataBeanList.addAll(statisticsDataHeadList);
                int setReallySize=1;
                for(StatisticsDataBean resultStatisticsDataBean :result){
                    statisticsDataShowList.add(resultStatisticsDataBean);
                    setReallySize += resultStatisticsDataBean.dataList.size();
                }
                statisticsDataBeanList.addAll(statisticsDataShowList);
                statisticianListAdapter.setReallySize(setReallySize);
                statisticianListAdapter.replaceAll(statisticsDataBeanList);
            }
        });
    }

    private void getStatisticianListData(final StatisticsHeadBean headBean, String machineNo, String strDate, String variety) {
        statisticianService.getStatisticianData(machineNo, strDate, variety, new CommonResultListener<List<StatisticsDataBean>>() {
            @Override
            public void successHandle(List<StatisticsDataBean> result) {
                statisticsDataBeanList = getListDataStatistician(headBean, result);
                dataList = getListStatisticianData(statisticsDataBeanList);
                statisticianListAdapter.setReallySize(headBean.headList.size());
                statisticianListAdapter.replaceAll(statisticsDataBeanList);
            }
        });
    }

    private List<String> getListStatisticianData(List<StatisticsDataBean> statisticsDataBeanList) {
        List<String> stringList = new ArrayList<>();
        for (StatisticsDataBean dataBean : statisticsDataBeanList) {
            String machineNo = dataBean.machineNo;
            stringList.add(machineNo);
            List<String> dataList = dataBean.dataList;
            stringList.addAll(dataList);
        }
        return stringList;
    }

    private List<StatisticsDataBean> getListDataStatistician(StatisticsHeadBean headBean) {
        headList = getHeadBeanList(headBean);
        StatisticsDataBean dataBean = new StatisticsDataBean();
        dataBean.machineNo="机号";
        dataBean.dataList = headList;
        statisticsDataBeanList.add(dataBean);
        return statisticsDataBeanList;
    }

    private List<StatisticsDataBean> getListDataStatistician(StatisticsHeadBean headBean, List<StatisticsDataBean> result) {
        headList = getHeadBeanList(headBean);
        StatisticsDataBean dataBean = new StatisticsDataBean();
        dataBean.dataList = headList;
        statisticsDataBeanList.add(dataBean);
        List<StatisticsDataBean> sortNumberList = getSortNumList(result);
        statisticsDataBeanList.addAll(sortNumberList);

        return statisticsDataBeanList;
    }

    private List<String> getHeadBeanList(StatisticsHeadBean headBean) {
        List<String> stringList = new ArrayList<>();
        List<String> headList = headBean.headList;
        if (headList == null || headList.isEmpty()) {
            return headList;
        }
        stringList.addAll(headBean.headList);
        return stringList;
    }

    private List<StatisticsDataBean> getSortNumList(List<StatisticsDataBean> result) {
        List<StatisticsDataBean> beanList = new ArrayList<>();
        if (result == null) {
            return beanList;
        }
        for (StatisticsDataBean dataBean : result) {
            int mIndex = result.indexOf(dataBean);

        }
        return beanList;
    }


    @OnClick({R.id.iv_back, R.id.iv_machineNo, R.id.tv_date, R.id.iv_variety, R.id.tv_derived_table,R.id.im_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_machineNo:
                break;
            case R.id.tv_date:
                showToast("选择日期");
                showDateDialog();
                break;
            case R.id.iv_variety:
                type = TYPE_VARIETIES;
                String queryKey = etVariety.getText().toString().trim();
                getCommonNameListByType(TYPE_VARIETIES, queryKey, etVariety);
                break;
            case R.id.im_search:
                if(machineNo.length() == 0)  {
                    showToast("请输入机号");
                }else if(mDate.length()==0){
                    showToast("请选择日期");
                }else if(variety.length() == 0){
                    showToast("请输入");
                }else {
                    getStatisticianListData(machineNo, mDate, variety);
                }
                break;
            case R.id.tv_derived_table:
                showToast("导出U盘");
                exportExcel();
                break;
        }
    }

    /**
     * 时间选择弹出框
     */
    private void showDateDialog() {
        CalendarDialog mCalendarDialog = new CalendarDialog();
        mCalendarDialog.setYearMonthDayListener(this);
        mCalendarDialog.show(getTargetActivity().getSupportFragmentManager(), "CalendarDialog");

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void yearMonthDay(int year, int month, int day) {
        tvDate.setText(year + "—" + month + "—" + day);
        mDate =year+"-"+month+"-"+day;
 //       getData();
    }

    /*
     * 展示pop
     * @param type          类型
     * @param queryKey      搜索关键字
     * @param mLocationView pop所在位置的View
     */
    private void getCommonNameListByType(final int type, String queryKey, final View mLocationView) {
        if (type == TYPE_VARIETIES) {
            mMaintenanceService.getVarietyList(queryKey, new CommonResultListener<List<VarietyListBean>>() {
                @Override
                public void successHandle(List<VarietyListBean> result) {
                    if (result == null || result.size() == 0) {
                        return;
                    }
                    showPopupWindow(type, mLocationView, result, null);
                }
            });
        }
    }

    private void showPopupWindow(int type, View mLocationView, List<VarietyListBean> result, List<CommonIdNameBean> idNameBeanList) {
        int intSize = 0;
        int intHeight = 0;
        if (type == TYPE_VARIETIES) {
            intSize = result.size();
            idNameBeanList = getListData(result);
        } else {
            intSize = idNameBeanList.size();
        }
        intHeight = 40 * intSize;
        mPopupWindow = new CommonPopupWindow(getTargetActivity(), mLocationView.getWidth(), Tools.dip2px(getTargetActivity(), intHeight) * 3 / 2, false, mLocationView, idNameBeanList);
        mPopupWindow.setCommonIdNameListener(this);
        mPopupWindow.setBackPressEnable(false);
        mPopupWindow.setOutSideDismiss(false);
        mPopupWindow.showPopupWindow(mLocationView);
    }

    private List<CommonIdNameBean> getListData(List<VarietyListBean> result) {
        List<CommonIdNameBean> list = new ArrayList<>();
        if (result.size() == 0) {
            return list;
        }
        for (VarietyListBean listBean : result) {
            int mIndex = result.indexOf(listBean);
            String name = listBean.name;
            CommonIdNameBean idNameBean = new CommonIdNameBean();
            idNameBean.id = mIndex;
            idNameBean.name = name;
            list.add(idNameBean);
        }
        return list;
    }

    @Override
    public void commonIdName(CommonIdNameBean idNameBean) {
        mPopupWindow.dismiss();
        String name = idNameBean.name;
        if (name == null) {
            return;
        }
        int id = idNameBean.id;
        if (type == TYPE_VARIETIES) {
            etVariety.setText(name);
//            initData();
        }
    }

    /**
     * 导出excel
     */
    public void exportExcel() {
        if (!isConnected) {
            showToast("请插入U盘");
            return;
        }
        if (!hasPermission) {
            showToast("获取权限失败,无法导入到U盘");
            return;
        }

        if (cFolder == null) {
            return;
        }


        if (headList == null || headList.size() == 0) {
            return;
        }
        headList.add(0,"机号");
        String[] title = headList.toArray(new String[headList.size()]);

        File file = new File(ExcelUtil.getSDPath() + "/bluetooth");
        ExcelUtil.makeDir(file);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());//Calendar.getInstance().toString();
        String fileName = file.toString() + "/" + "工号" + "-" + time + ".xls";
        Logger.d(fileName);

        ExcelUtil.initExcel(fileName, title);
        ExcelUtil.writeObjListToExcel(getRecordData(), fileName, this);

//        String folder = ExcelUtil.getSDPath() + "/bluetooth";
//        File mFile1 = getSaveFile(fileName,   "工号" + "-" + time + ".xls");
        File mFile1 = new File(fileName);
        saveSDFile2OTG(mFile1, cFolder);
    }

    /**
     * 将数据集合 转化成ArrayList<ArrayList<String>>
     *
     * @return
     */
    private ArrayList<List<String>> getRecordData() {
        ArrayList<List<String>> recordList = new ArrayList<>();
        recordList.clear();
        if(statisticsDataBeanList.size() >= 2 ) {
            for (int i = 1; i < statisticsDataBeanList.size(); i++) {
                List<String> dataItem =statisticsDataBeanList.get(i).dataList;
                dataItem.add(0,statisticsDataBeanList.get(i).machineNo);
                recordList.add(dataItem);
                Logger.d(recordList);
                return recordList;
            }
        }
        return recordList;
    }

    /**
     * 从指定文件夹获取文件
     *
     * @return 如果文件不存在则创建, 如果如果无法创建文件或文件名为空则返回null
     */
    public static File getSaveFile(String folderPath, String fileNmae) {
        File file = new File(getSavePath(folderPath) + File.separator + fileNmae);
        Logger.d(file.toString());
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 获取SD卡下指定文件夹的绝对路径
     *
     * @return 返回SD卡下的指定文件夹的绝对路径
     */
    public static String getSavePath(String folderName) {
        return getSaveFolder(folderName).getAbsolutePath();
    }

    /**
     * 获取文件夹对象
     *
     * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创建
     */
    public static File getSaveFolder(String folderName) {
        File file = new File(getExternalStorageDirectory()
                .getAbsoluteFile()
                + File.separator
                + folderName
                + File.separator);
        file.mkdirs();
        return file;
    }


    class USBReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            switch (action) {
                case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                    ToastUtils.showCenter("onReceive: USB设备已连接");
                    isConnected = true;
                    UsbDevice device_add = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device_add != null) {
                        //接收到U盘插入广播，尝试读取U盘设备数据
                        redUDiskDevsList();
                    } else {
                        ToastUtils.showCenter("onReceive: device is null");
                    }

                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED:
                    isConnected = false;
                    ToastUtils.showCenter("onReceive: USB设备已拔出");
                    break;
                case ACTION_USB_PERMISSION:
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    //允许权限申请
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbDevice != null) {
                            Log.i("TAG", "onReceive: 权限已获取");
                            hasPermission = true;
                            readDevice(getUsbMass(usbDevice));
                        } else {
                            isConnected = false;
                            ToastUtils.showCenter("没有插入U盘");
                        }
                    } else {
                        hasPermission = false;
                        ToastUtils.showCenter("未获取到U盘权限");
                    }
                    break;
                default:
                    //Log.i(TAG, "onReceive: action=" + action);
                    ToastUtils.showCenter("action= " + action);
                    Logger.d(action);
                    boolean connected = intent.getExtras().getBoolean("connected");
                    if (connected) {
                        ToastUtils.showCenter(context, "USB 已经连接");
                        //Toast.makeText(MainActivity.this, "USB 已经连接",Toast.LENGTH_SHORT).show();

                    } else {
                      //  if (BOOLEAN) {
                            ToastUtils.showCenter(context, "USB 断开");
                            //Toast.makeText(MainActivity.this, "USB 断开",Toast.LENGTH_SHORT).show();
                     //   }

                    }
                    break;
            }
        }
    }

    private void redUDiskDevsList() {
        //设备管理器
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        //获取U盘存储设备
        storageDevices = UsbMassStorageDevice.getMassStorageDevices(this);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        //一般手机只有1个OTG插口
        for (UsbMassStorageDevice device : storageDevices) {
            //读取设备是否有权限
            if (usbManager.hasPermission(device.getUsbDevice())) {
                ToastUtils.showCenter("有权限");
                readDevice(device);
            } else {
                ToastUtils.showCenter("没有权限，进行申请");
                //没有权限，进行申请
                usbManager.requestPermission(device.getUsbDevice(), pendingIntent);
            }
        }
        if (storageDevices.length == 0) {
            ToastUtils.showCenter("请插入可用的U盘");
        }
    }

    private UsbMassStorageDevice getUsbMass(UsbDevice usbDevice) {
        for (UsbMassStorageDevice device : storageDevices) {
            if (usbDevice.equals(device.getUsbDevice())) {
                return device;
            }
        }
        return null;
    }

    private void readDevice(UsbMassStorageDevice device) {
        try {
            device.init();//初始化
            //设备分区
            Partition partition = device.getPartitions().get(0);

            //文件系统
            FileSystem currentFs = partition.getFileSystem();
            currentFs.getVolumeLabel();//可以获取到设备的标识

            //通过FileSystem可以获取当前U盘的一些存储信息，包括剩余空间大小，容量等等
            Log.e("Capacity: ", currentFs.getCapacity() + "");
            Log.e("Occupied Space: ", currentFs.getOccupiedSpace() + "");
            Log.e("Free Space: ", currentFs.getFreeSpace() + "");
            Log.e("Chunk size: ", currentFs.getChunkSize() + "");
            ToastUtils.showCenter("可用空间：" + currentFs.getFreeSpace());
            cFolder = currentFs.getRootDirectory();//设置当前文件对象为根目录

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 把本地文件写入到U盘中
     * @author ldm
     * @time 2017/8/22 10:22
     */
    public void saveSDFile2OTG(final File f, final UsbFile usbFile) {
        UsbFile uFile = null;
        FileInputStream fis = null;
        try {//开始写入
            fis = new FileInputStream(f);//读取选择的文件的
            if (usbFile.isDirectory()) {//如果选择是个文件夹
                UsbFile[] usbFiles = usbFile.listFiles();
                if (usbFiles != null && usbFiles.length > 0) {
                    for (UsbFile file : usbFiles) {
                        if (file.getName().equals(f.getName())) {
                            file.delete();
                        }
                    }
                }
                uFile = usbFile.createFile(f.getName());
                UsbFileOutputStream uos = new UsbFileOutputStream(uFile);
                try {
                    redFileStream(uos, fis);
                    ToastUtils.showCenter("保存成功");
                } catch (IOException e) {
                    ToastUtils.showCenter("保存失败");
                    e.printStackTrace();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            ToastUtils.showCenter("保存失败");
        }
    }

    private void redFileStream(OutputStream os, InputStream is) throws IOException {
        int bytesRead = 0;
        byte[] buffer = new byte[1024 * 1024 * 8];
        while ((bytesRead = is.read(buffer)) != -1) {
            Logger.d(bytesRead);
            os.write(buffer, 0, bytesRead);
        }
        os.flush();
        os.close();
        is.close();
    }
}
