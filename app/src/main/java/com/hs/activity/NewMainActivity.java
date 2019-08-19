package com.hs.activity;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.jws.JwsManager;
import com.android.jws.JwsSerialPort;
import com.gyf.barlibrary.BarHide;
import com.hs.R;
import com.hs.activity.dialog.CalendarDialog;
import com.hs.activity.pop.CommonPopupWindow;
import com.hs.adapter.DebugValueAdapter;
import com.hs.adapter.maintenance.CommonNameValueAdapter;
import com.hs.adapter.maintenance.DefectDescribeAdapter;
import com.hs.adapter.work.BlemishInfoListAdapter;
import com.hs.adapter.workbench.AddBlemishTypeListAdapter;
import com.hs.base.GlobalInfo;
import com.hs.bean.maintenance.CommonIdNameBean;
import com.hs.bean.maintenance.DefectInfoListBean;
import com.hs.bean.maintenance.DefectInfoListBean.DefectListBean;
import com.hs.bean.maintenance.DefectParamBean;
import com.hs.bean.maintenance.VarietyListBean;
import com.hs.bean.work.ClothIdBean;
import com.hs.bean.work.ClothInfoBean;
import com.hs.bean.work.DefectInfoBean;
import com.hs.bean.work.DefectListBan;
import com.hs.common.clock.ComPortUtil;
import com.hs.common.constant.AppConfig;
import com.hs.common.constant.SharedKeyConstant;
import com.hs.common.intent.IntentStartActivityHelper;
import com.hs.common.socket.SocketThreadManager;
import com.hs.common.socket.UdpClient;
import com.hs.common.util.MySharedPreference;
import com.hs.common.util.ToastUtils;
import com.hs.common.util.Tools;
import com.hs.common.util.keyboard.KeyboardUtil;
import com.hs.common.util.ui.WebViewUtil;
import com.hs.common.view.CustomerVideoView;
import com.hs.common.view.ListViewForScrollView;
import com.hs.listener.DefectDetailParentClickListener;
import com.hs.listener.ShowOrEditDefectInfoListener;
import com.hs.service.MaintenanceService;
import com.hs.service.WorkService;
import com.hs.service.listener.CommonResultListener;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.orhanobut.logger.Logger;

/**
 * @Author: ljf
 * 时间:2019/6/17
 * 简述:功能页面集中在一个页面
 */
public class NewMainActivity extends BaseActivity implements CommonNameValueAdapter.CommonIdNameListener
        , CalendarDialog.YearMonthDayListener, ShowOrEditDefectInfoListener
        , DefectDetailParentClickListener {
    @BindView(R.id.tv_workbench)
    TextView tvWorkbench;
    @BindView(R.id.tv_statistics)
    TextView tvStatistics;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.wv_chart)
    WebView wvChart;
    @BindView(R.id.tv_length)
    TextView tvLength;
    @BindView(R.id.tv_defect_count)
    TextView tvDefectCount;
    @BindView(R.id.tv_perching)
    TextView tvPerching;
    @BindView(R.id.tv_score_normal)
    TextView tvScoreNormal;
    @BindView(R.id.ll_platform_normal)
    LinearLayout llPlatformNormal;
    @BindView(R.id.tv_score_single)
    TextView tvScoreSingle;
    @BindView(R.id.ll_platform_single)
    LinearLayout llPlatformSingle;
    @BindView(R.id.tv_cloth_info)
    TextView tvClothInfo;
    @BindView(R.id.tv_defect_info_list)
    TextView tvDefectInfoList;
    @BindView(R.id.et_varieties)
    EditText etVarieties;
    @BindView(R.id.et_warp)
    EditText etWarp;
    @BindView(R.id.et_weft)
    EditText etWeft;
    @BindView(R.id.et_machine_num)
    EditText etMachineNum;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.ll_date)
    LinearLayout llDate;
    @BindView(R.id.tv_shift_edit)
    TextView tvShiftEdit;
    @BindView(R.id.tv_thickness_edit)
    TextView tvThicknessEdit;
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;
    @BindView(R.id.tv_varieties)
    TextView tvVarieties;
    @BindView(R.id.tv_warp_and_weft)
    TextView tvWarpAndWeft;
    @BindView(R.id.tv_machine_num)
    TextView tvMachineNum;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_shift)
    TextView tvShift;
    @BindView(R.id.tv_thickness)
    TextView tvThickness;
    @BindView(R.id.ll_thickness_un_edit)
    LinearLayout llThicknessUnEdit;
    @BindView(R.id.ll_un_edit)
    LinearLayout llUnEdit;
    @BindView(R.id.tv_add_cancel)
    TextView tvAddCancel;
    @BindView(R.id.tv_sure_edit)
    TextView tvSureEdit;
    @BindView(R.id.ll_cloth_info)
    LinearLayout llClothInfo;
    @BindView(R.id.lv_blemish_info_list)
    ListView lvBlemishInfoList;
    @BindView(R.id.ll_defect_list)
    LinearLayout llDefectList;
    @BindView(R.id.tv_empty_data_left)
    TextView tvEmptyDataLeft;
    @BindView(R.id.rl_defect_list)
    RelativeLayout rlDefectList;
    @BindView(R.id.ll_tab_left)
    LinearLayout llTabLeft;
    @BindView(R.id.tv_ai_analysis)
    TextView tvAiAnalysis;
    @BindView(R.id.tv_add_blemish)
    TextView tvAddBlemish;
    @BindView(R.id.tv_defect_info)
    TextView tvDefectInfo;
    @BindView(R.id.tv_edit_defect)
    TextView tvEditDefect;
    @BindView(R.id.video_view)
    CustomerVideoView videoView;
    @BindView(R.id.tv_empty_data_right)
    TextView tvEmptyDataRight;
    @BindView(R.id.fl_video)
    FrameLayout flVideo;
    @BindView(R.id.lv_blemish_type_list)
    ListViewForScrollView lvBlemishTypeList;
    @BindView(R.id.lv_remark_des)
    ListViewForScrollView lvRemarkDes;
    @BindView(R.id.ll_remark_detailed)
    LinearLayout llRemarkDetailed;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.ll_remark_simple)
    LinearLayout llRemarkSimple;
    @BindView(R.id.ll_add_edit_defect)
    LinearLayout llAddEditDefect;
    @BindView(R.id.tv_defect_name)
    TextView tvDefectName;
    @BindView(R.id.tv_defect_des)
    TextView tvDefectDes;
    @BindView(R.id.lv_defect_des)
    ListViewForScrollView lvDefectDes;
    @BindView(R.id.tv_remark_des)
    TextView tvRemarkDes;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.rl_defect_info)
    RelativeLayout rlDefectInfo;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.ll_defect)
    LinearLayout llDefect;
    @BindView(R.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R.id.ll_tab_right)
    LinearLayout llTabRight;
    @BindView(R.id.iv_full_screen)
    ImageView ivFullScreen;
    @BindView(R.id.ll_perching_workbench)
    LinearLayout llPerchingWorkbench;
    @BindView(R.id.wv_perching_statistics)
    WebView wvPerchingStatistics;
    @BindView(R.id.rv_varieties)
    RecyclerView rvVarieties;
    @BindView(R.id.ll_varieties)
    LinearLayout llVarieties;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.rl_varieties)
    RelativeLayout rlVarieties;
    @BindView(R.id.tv_debug_video)
    TextView tvDebugVideo;
    @BindView(R.id.tv_debug_clock)
    TextView tvDebugClock;
    @BindView(R.id.tv_debug_clock_data)
    TextView tvDebugClockData;
    @BindView(R.id.tv_debug_video_data)
    TextView tvDebugVideoData;
    @BindView(R.id.rl_debug_clock)
    ListView rlDebugClock;
    @BindView(R.id.tv_debug_clock_clear)
    TextView tvDebugClockClear;
    @BindView(R.id.fl_debug_clock)
    FrameLayout flDebugClock;
    @BindView(R.id.rl_debug_video)
    ListView rlDebugVideo;
    @BindView(R.id.tv_debug_video_clear)
    TextView tvDebugVideoClear;
    @BindView(R.id.fl_debug_video)
    FrameLayout flDebugVideo;

    @BindView(R.id.udp_set)
    RelativeLayout layoutUdp;
    @BindView(R.id.tv_udp_set)
    TextView tvUdpSet;
    @BindView(R.id.et_udpIp)
    EditText etUdpIp;
    @BindView(R.id.et_udpPort)
    EditText etUdpPort;
    @BindView(R.id.udp_cancel)
    TextView udpCancel;
    @BindView(R.id.udp_add)
    TextView udpAdd;
    @BindView(R.id.mcu_ip)
    TextView mcuIp;
    @BindView(R.id.mcu_port)
    TextView mcuPort;
    /**
     * 平台类型
     */
    private Integer intAppPlatform;
    private WorkService mWorkService;
    /**
     * 验布工作台相关
     */
    private int perchingStatus = 0;
    private int defectCount = 0;
    /**
     * 布匹信息相关
     */
    private MaintenanceService mMaintenanceService;
    private static final int TYPE_VARIETIES = 0;
    private static final int TYPE_WARP = 1;
    private static final int TYPE_WEFT = 2;
    private static final int TYPE_SHIFT = 3;
    private static final int TYPE_PLY = 4;
    private ClothInfoBean mClothInfoBean;
    private ClothIdBean mClothIdBean;
    private List<CommonIdNameBean> clothPlyList;
    private int searchFlag = 0;
    private int type;
    /**
     * 编辑or新增的标志,flag为0,新增,flag为1,编辑
     */
    private CommonPopupWindow mPopupWindow;
    private Integer clothInfoStatus;
    /**
     * 疵点明细相关
     */
    private BlemishInfoListAdapter blemishInfoListAdapter;
    private List<DefectListBan> list;

    /**
     * AI相关
     */
    private MediaPlayer player;
    private SurfaceHolder holder;

    /**
     * 新增疵点与编辑疵点相关
     */
    private AddBlemishTypeListAdapter addBlemishTypeListAdapter;
    private List<DefectInfoListBean> beanList;
    private DefectDescribeAdapter defectDescribeAdapter;
    private List<DefectListBean> defectListBeanList;
    private List<DefectParamBean> paramBeanList;
    private List<Object> paramObjectList;
    public Integer flagDefectAddOrEdit;


    /**
     * 码表相关
     */
    private static final String SERIAL_PORT = "/dev/ttyS3";
    private static final String GET_LENGTH_CODE = "01 03 00 01 00 02 95 CB";
    private static final String PAUSE_CODE = "01 06 00 00 00 04 88 09";
    private static final String REGAIN_CODE = "01 06 00 00 00 08 88 0C";
    private static final String ZERO_SETTING_CODE = "01 06 00 00 00 03 C9 CB";    //    private static final String ZERO_SETTING_CODE = "01 06 00 00 00 01 48 0a";
    //    private static final String ZERO_SETTING_CODE = "01 06 00 00 00 02 08 0b";
    private static JwsManager mJwsManagerLock;
    private static JwsSerialPort serialPort;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private String clothLength = "";
    private double dbClothLength = 0.00;
    private Handler handler = new Handler();
    private boolean openFlag;
    /**
     * 主动监听  0(添加布匹时主动查询机器的状态)
     * 被动监听  1(软件端手动开始验布停止验布)
     */
    private String strClockSendCommandName = "";
    private String strClockSendTime = "";
    private String strClockSendCommandValue = "";
    private String strClockReceiveCommandName = "";
    private String strClockReceiveTime = "";
    private String strClockReceiveCommandValue = "";
    private int clockCount = 0;
    private Runnable mClockRunnable = new Runnable() {
        @Override
        public void run() {
            receiveLockData();
        }
    };

    /**
     * UDP设置信息持久化
     *
     */
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;



    @SuppressLint("SetTextI18n")
    private void receiveLockData() {
        clockCount++;
        sendClockStartCommand();
        //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
        handler.removeCallbacks(mClockRunnable);
        strClockReceiveCommandName = "接收：";
        strClockReceiveTime = getCurrentTime();
        strClockReceiveCommandValue = "返回数据为空";
        String str = ComPortUtil.receive(inputStream);
        if ("".equals(str) || str == null) {
            strClockReceiveCommandValue = "返回数据为空";

        } else {
            strClockReceiveCommandValue = str;
        }

        if (clockCount == 30) {
            clockCount = 0;
            if (!strClockReceiveCommandValue.startsWith("A5")) {
                sendCommandAndReceiveLockData(strClockReceiveCommandName, strClockReceiveTime, strClockReceiveCommandValue);
            }
        }
        if ("".equals(str) || str == null) {
            handler.postDelayed(mClockRunnable, 150);
            return;
        }
        if (str.length() < 18) {
            handler.postDelayed(mClockRunnable, 150);
            strClockReceiveCommandValue = "数据解析异常(位数小于18位)";
            return;
        }
        String strClothLength = str.substring(8, 14);
        if (!"".equals(strClothLength)) {
            try {
                dbClothLength = Double.valueOf(strClothLength) / 1000;
                strClockReceiveCommandValue = String.valueOf(dbClothLength);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                handler.postDelayed(mClockRunnable, 150);
                strClockReceiveCommandValue = "数据解析异常(不全为数字)" + strClothLength;
                return;
            }
        }
        tvLength.setText("长度：" + dbClothLength + "M");
        tvLocation.setText(dbClothLength + "");

        if (openFlag) {
            handler.postDelayed(mClockRunnable, 150);
        }
    }

    private void sendCommandAndReceiveLockData(String strCommand, String strTime, String strValue) {
        tvDebugClockData.append("\n" + strCommand + strTime + "-->" + strValue + "\n");
        String value = strCommand + strTime + "-->" + strValue;
        valueClockList.add(value);
//        Collections.reverse(valueClockList);
        valueClockAdapter.replaceAll(valueClockList);
    }

    private String getCurrentTime() {
        String strCurrentTime = "";
        long currentTimeMillis = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        strCurrentTime = sd.format(currentTimeMillis);
        return strCurrentTime;
    }

    /**
     * 码表置零
     */
    public void zeroSetting() {
        strClockSendCommandName = "发送：";
        strClockSendTime = getCurrentTime();
        strClockSendCommandValue = "";
        try {
            ComPortUtil.send(ComPortUtil.StrToBCDBytes(ZERO_SETTING_CODE), outputStream);
            strClockSendCommandValue = ZERO_SETTING_CODE + "-->码表归零";
        } catch (IOException e) {
            showToast("发送命令异常");
            strClockSendCommandValue = "发送命令异常";
        }
        sendCommandAndReceiveLockData(strClockSendCommandName, strClockSendTime, strClockSendCommandValue);
    }

    /**
     * 视频分析仪停止
     */
    //停止
    public void sendVideoStopCommand() {
        strVideoSendCommandName = "发送：";
        strVideoSendTime = getCurrentTime();
        try {
            ComPortUtil.send(ComPortUtil.StrToBCDBytes(STOP_CODE), videoOutputStream);
            SocketThreadManager.getInstance().sendMsg(STOP_COMMAND);
            strVideoSendCommandValue = STOP_CODE + "-->停止";
        } catch (IOException e) {
            showToast("发送命令异常");
            strVideoSendCommandValue = "发送命令异常";
        }
        sendCommandAndReceiveVideoData(strVideoSendCommandName, strVideoSendTime, strVideoSendCommandValue);
    }

    private static final String SERIAL_PORT_VIDEO = "/dev/ttyS4";
    private static final String RECEIVE_DATA_CODE = "A5 06 00 22 FE 00";
    private static final String STOP_CODE = "A5 06 00 23 FE 00";
    private static final String START_COMMAND = "A5060022FE00";
    private static final String STOP_COMMAND = "A5060023FE00";
    private static final String X_COMMAND = "A5070022FE00";
    private static final String Y_COMMAND = "A5070022FE0000";
    private static final String Z_COMMAND = "A5060021FE00";
    private static JwsManager mJwsManager;
    private static JwsSerialPort videoSerialPort;
    private static InputStream videoInputStream;
    private static OutputStream videoOutputStream;
    private boolean videoOpenFlag;
    private String strVideoSendCommandName = "";
    private String strVideoSendTime = "";
    private String strVideoSendCommandValue = "";
    private String strVideoReceiveCommandName = "";
    private String strVideoReceiveTime = "";
    private String strVideoReceiveCommandValue = "";
    private Runnable videoRunnable = new Runnable() {
        @Override
        public void run() {
            receiveVideoData();
        }
    };

    private void receiveVideoData() {
        //要做的事情，这里再次调用此Runnable对象，以实现每500毫秒实现一次的定时器操作
        handler.removeCallbacks(videoRunnable);
        strVideoReceiveCommandName = "接收：";
        strVideoReceiveTime = getCurrentTime();
        strVideoReceiveCommandValue = "";
        String  str = "";
        str = ComPortUtil.receive(videoInputStream);
        str = SocketThreadManager.getInstance().getCmd();

        if ("".equals(str) || str == null) {
            handler.postDelayed(videoRunnable, 1000);
            return;
        }
        strVideoReceiveCommandValue = str;
        if (strVideoReceiveCommandValue.startsWith("A5")) {
            sendCommandAndReceiveVideoData(strVideoReceiveCommandName, strVideoReceiveTime, strVideoReceiveCommandValue);
        }
        if (str.length() < 12) {
            handler.postDelayed(videoRunnable, 290);
            return;
        }

        String substring = str.substring(0, 12);
        String substring1 = substring.substring(0, 12);

        if (X_COMMAND.equals(substring1)) {
            perchingStatus = 3;
            setTvPerchingStatus(perchingStatus);
            String lengthStr = str.substring(12, 14);
            //上传疵点信息
            int value = Integer.valueOf(lengthStr, 16);
            machineNoList.add(value);
        }
        if (Y_COMMAND.equals(substring)) {
            perchingStatus = 3;
            setTvPerchingStatus(perchingStatus);
        }
        if (Z_COMMAND.equals(substring1)) {
            perchingStatus = 2;
            setTvPerchingStatus(perchingStatus);
        }
        if (START_COMMAND.equals(substring1)) {
            perchingStatus = 2;
            setTvPerchingStatus(perchingStatus);
        }
        if (STOP_COMMAND.equals(substring1)) {
            perchingStatus = 3;
            setTvPerchingStatus(perchingStatus);
        }

        handler.postDelayed(videoRunnable, 290);
        if (videoOpenFlag) {
            handler.postDelayed(videoRunnable, 290);
        }
    }

    @SuppressLint("SetTextI18n")
    private void sendCommandAndReceiveVideoData(String strCommand, String strTime, String strValue) {
        tvDebugVideoData.setText("\n" + strCommand + "-->" + strTime + "-->" + strValue + "\n");
        String value = strCommand + strTime + "-->" + strValue;
        valueVideoList.add(value);
        valueVideoAdapter.replaceAll(valueVideoList);
    }

    /**
     * 暂停计数
     */
    public void sendClockPauseCommand() {
        try {
            ComPortUtil.send(ComPortUtil.StrToBCDBytes(PAUSE_CODE), outputStream);
        } catch (IOException e) {
            ToastUtils.showCenter(NewMainActivity.this, "发送命令异常");
        }
    }

    /**
     * 继续计数
     */
    public void sendClockRestartCommand() {
        try {
            ComPortUtil.send(ComPortUtil.StrToBCDBytes(REGAIN_CODE), outputStream);
        } catch (IOException e) {
            ToastUtils.showCenter(NewMainActivity.this, "发送命令异常");
        }
    }

    /**
     * 布匹厚度相关
     */
    private static final String THICKNESS_1 = "A5 05 00 14 01";
    private static final String THICKNESS_2 = "A5 05 00 14 02";
    private static final String THICKNESS_3 = "A5 05 00 14 03";
    private List<Integer> machineNoList;
    private List<Integer> defectList;

    /**
     *
     */
    private CommonNameValueAdapter nameValueAdapter;
    private List<CommonIdNameBean> nameIdList;

    private DebugValueAdapter valueClockAdapter;
    private List<String> valueClockList;
    private DebugValueAdapter valueVideoAdapter;
    private List<String> valueVideoList;


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
    protected int getLayoutId() {
        return R.layout.activity_new_main;
    }

    @Override
    protected void initView() {
        super.initView();
        mWorkService = new WorkService(this);
        mMaintenanceService = new MaintenanceService(this);

        tabTopUiHandler(true);

        initWvChart();

        intAppPlatform = AppConfig.APP_PLATFORM;
        showDifferentUiByAppPlatform(intAppPlatform);

        clothInfoStatus = 0;
        tabLeftUiHandler(true);
        initVarietiesAndSetListener();
        machineNoList = new ArrayList<>();
        defectList = new ArrayList<>();
        initDefectListAdapter();

        tabRightUiHandlerByPlatform(intAppPlatform);
        initDefectCategoryAdapter();
        initDefectDesAdapter();
        valueVideoList = new ArrayList<>();
        valueVideoAdapter = new DebugValueAdapter(this, valueVideoList);
        rlDebugVideo.setAdapter(valueVideoAdapter);
        valueClockList = new ArrayList<>();
        valueClockAdapter = new DebugValueAdapter(this, valueClockList);
        rlDebugClock.setAdapter(valueClockAdapter);


        mJwsManagerLock = JwsManager.create(this);
        mJwsManager = JwsManager.create(this);
        initClock();
        initVideoAnalyzer();
    }


    /**
     * 验布工作台
     * 验布统计
     * tab选中处理
     *
     * @param isTabWorkBench 是否是验布工作台(默认是)
     */
    private void tabTopUiHandler(boolean isTabWorkBench) {
        tvWorkbench.setSelected(isTabWorkBench);
        tvStatistics.setSelected(!isTabWorkBench);
        if (isTabWorkBench) {
            llPerchingWorkbench.setVisibility(View.VISIBLE);
            wvPerchingStatistics.setVisibility(View.GONE);
        } else {
            llPerchingWorkbench.setVisibility(View.GONE);
            wvPerchingStatistics.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化验布工作台下方的心电图效果图
     */
    private void initWvChart() {
        WebViewUtil.initWebView(wvChart, this);
        wvChart.setBackgroundColor(0); // 设置背景色
        wvChart.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
    }

    /**
     * 0单独使用
     * 1联动使用
     *
     * @param intAppPlatform 平台类型
     */
    private void showDifferentUiByAppPlatform(Integer intAppPlatform) {
        if (intAppPlatform == null) {
            return;
        }
        switch (intAppPlatform) {
            case 0:
                showSingleUseUi();
                break;
            case 1:
                showLinkageUi();
                break;
        }
    }

    /**
     * 单独使用
     */
    private void showSingleUseUi() {
        llPlatformSingle.setVisibility(View.VISIBLE);
        llPlatformNormal.setVisibility(View.GONE);

        tvAiAnalysis.setVisibility(View.GONE);
        ivFullScreen.setVisibility(View.GONE);

        tvAddBlemish.setVisibility(View.VISIBLE);
        tvDefectInfo.setVisibility(View.GONE);
        tvEditDefect.setVisibility(View.GONE);
    }

    /**
     * 联动使用
     */
    private void showLinkageUi() {
        llPlatformSingle.setVisibility(View.GONE);
        llPlatformNormal.setVisibility(View.VISIBLE);

        perchingStatus = 0;
        setTvPerchingStatus(perchingStatus);

        tvAiAnalysis.setVisibility(View.VISIBLE);
        ivFullScreen.setVisibility(View.VISIBLE);
        tvAddBlemish.setVisibility(View.VISIBLE);
        tvDefectInfo.setVisibility(View.GONE);
        tvEditDefect.setVisibility(View.GONE);

        tvUdpSet.setVisibility(View.VISIBLE);

    }

    private void setTvPerchingStatus(int perchingStatus) {
        int bgColor = 0;
        String statusDes = "";
        switch (perchingStatus) {
            case 0://未开始
                tvPerching.setClickable(false);
                statusDes = "开始验布";
                bgColor = getResources().getColor(R.color.color_4ed966_transparent_70);
                break;
            case 1://开始
                tvPerching.setClickable(true);
                statusDes = "开始验布";
                bgColor = getResources().getColor(R.color.color_4ed966);
                break;
            case 2://验布中
                tvPerching.setClickable(true);
                statusDes = "停止验布";
                bgColor = getResources().getColor(R.color.color_ff3750);
                break;
            case 3://验布暂停
                tvPerching.setClickable(true);
                statusDes = "开始验布";
                bgColor = getResources().getColor(R.color.color_4ed966);
                break;
        }
        tvPerching.setText(statusDes);
        tvPerching.setBackgroundColor(bgColor);
    }

    /**
     * 布匹信息Tab
     * 布匹疵点列表Tab
     *
     * @param isTabClothInfo 布匹信息Tab(默认是)
     */
    private void tabLeftUiHandler(boolean isTabClothInfo) {
        tvClothInfo.setSelected(isTabClothInfo);
        tvDefectInfoList.setSelected(!isTabClothInfo);

        if (isTabClothInfo) {
            showClothInfoUi(intAppPlatform);
        } else {
            showDefectListEmptyUi();
        }
    }

    private void showClothInfoUi(Integer intAppPlatform) {
        llClothInfo.setVisibility(View.VISIBLE);
        rlDefectList.setVisibility(View.GONE);
        llVarieties.setVisibility(View.GONE);

        type = TYPE_VARIETIES;
        getCommonNameListByType(type, "", etVarieties);
        if (intAppPlatform == 0)//单独使用
        {
            llThicknessUnEdit.setVisibility(View.GONE);
        } else if (intAppPlatform == 1)//正常使用
        {
            llThicknessUnEdit.setVisibility(View.VISIBLE);
        }
        showClothInfoInitUi();
    }

    private void initVarietiesAndSetListener() {
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvVarieties.setLayoutManager(mManager);
        nameIdList = new ArrayList<>();
        nameValueAdapter = new CommonNameValueAdapter(this, nameIdList);
        rvVarieties.setAdapter(nameValueAdapter);
        nameValueAdapter.setIdNameListener(this);
    }

    /**
     * 可编辑页面影藏
     * 不可编辑页面隐藏
     * 左边为新增布匹按钮
     * 右边什么都没有
     */
    private void showClothInfoInitUi() {
        llEdit.setVisibility(View.GONE);
        llUnEdit.setVisibility(View.GONE);
        tvAddCancel.setText("新增布匹");
        tvAddCancel.setVisibility(View.VISIBLE);
        tvSureEdit.setVisibility(View.GONE);
    }

    private void showDefectListEmptyUi() {
        llClothInfo.setVisibility(View.GONE);
        rlDefectList.setVisibility(View.VISIBLE);
        llDefectList.setVisibility(View.GONE);
        tvEmptyDataLeft.setVisibility(View.VISIBLE);
    }

    /**
     * AI分析选中或者新增疵点选中
     * intAppPlatform 0 新增疵点选中
     * intAppPlatform 1  AI分析选中
     *
     * @param intAppPlatform 0单独使用 1正常使用
     */
    private void tabRightUiHandlerByPlatform(int intAppPlatform) {
        switch (intAppPlatform) {
            case 0:
                tabRightUiHandler(false, true, false, false, false, false,false);
                break;
            case 1:
                tabRightUiHandler(true, false, false, false, false, false,false);
                break;
        }
    }

    /**
     * AI分析
     * 新增疵点
     * 疵点信息
     * 编辑疵点
     * tab选中处理
     *
     * @param isTabAi             AI分析
     * @param isTanAddDefect      新增疵点
     * @param isTabDefectInfo     疵点信息
     * @param isTabDefectInfoEdit 编辑疵点
     */
    private void tabRightUiHandler(boolean isTabAi, boolean isTanAddDefect, boolean isTabDebugVideo
            , boolean isTabDebugClock, boolean isTabDefectInfo, boolean isTabDefectInfoEdit,boolean isTabUdpSet) {
        tvAiAnalysis.setSelected(isTabAi);
        ivFullScreen.setSelected(false);
        ivFullScreen.setVisibility(isTabAi ? View.VISIBLE : View.GONE);

        tvAddBlemish.setSelected(isTanAddDefect);
        tvDebugVideo.setSelected(isTabDebugVideo);
        tvDebugClock.setSelected(isTabDebugClock);
        tvDefectInfo.setSelected(isTabDefectInfo);
        tvEditDefect.setSelected(isTabDefectInfoEdit);

        tvUdpSet.setSelected(isTabUdpSet);

        if (isTabAi) {
            tvDefectInfo.setVisibility(View.GONE);
            tvEditDefect.setVisibility(View.GONE);

            flVideo.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            tvEmptyDataRight.setVisibility(View.VISIBLE);
            flDebugVideo.setVisibility(View.GONE);
            flDebugClock.setVisibility(View.GONE);
            llDefect.setVisibility(View.GONE);

            layoutUdp.setVisibility(View.GONE);


        } else if (isTanAddDefect) {
            tvDefectInfo.setVisibility(View.GONE);
            tvEditDefect.setVisibility(View.GONE);

            flVideo.setVisibility(View.GONE);
            flDebugVideo.setVisibility(View.GONE);
            flDebugClock.setVisibility(View.GONE);
            llDefect.setVisibility(View.VISIBLE);
            llAddEditDefect.setVisibility(View.VISIBLE);
            llRemarkDetailed.setVisibility(View.GONE);
            rlDefectInfo.setVisibility(View.GONE);
            tvCancel.setText("取消");
            tvCancel.setVisibility(View.VISIBLE);
            tvAdd.setText("确定");
            tvAdd.setVisibility(View.VISIBLE);

            layoutUdp.setVisibility(View.GONE);
        } else if (isTabDebugVideo) {
            tvDefectInfo.setVisibility(View.GONE);
            tvEditDefect.setVisibility(View.GONE);

            flVideo.setVisibility(View.GONE);
            flDebugVideo.setVisibility(View.VISIBLE);
            flDebugClock.setVisibility(View.GONE);
            llDefect.setVisibility(View.GONE);

            layoutUdp.setVisibility(View.GONE);
        } else if (isTabDebugClock) {
            tvDefectInfo.setVisibility(View.GONE);
            tvEditDefect.setVisibility(View.GONE);

            flVideo.setVisibility(View.GONE);
            flDebugVideo.setVisibility(View.GONE);
            flDebugClock.setVisibility(View.VISIBLE);
            llDefect.setVisibility(View.GONE);

            layoutUdp.setVisibility(View.GONE);
        } else if (isTabDefectInfo) {
            tvDefectInfo.setVisibility(View.VISIBLE);
            tvEditDefect.setVisibility(View.GONE);

            flVideo.setVisibility(View.GONE);
            flDebugVideo.setVisibility(View.GONE);
            flDebugClock.setVisibility(View.GONE);
            llDefect.setVisibility(View.VISIBLE);
            llAddEditDefect.setVisibility(View.GONE);
            rlDefectInfo.setVisibility(View.VISIBLE);
            tvCancel.setText("取消");
            tvCancel.setVisibility(View.GONE);
            tvAdd.setText("编辑");
            tvAdd.setVisibility(View.VISIBLE);

            layoutUdp.setVisibility(View.GONE);
        } else if (isTabDefectInfoEdit) {
            tvDefectInfo.setVisibility(View.GONE);
            tvEditDefect.setVisibility(View.VISIBLE);
            tvDebugVideo.setVisibility(View.VISIBLE);
            tvDebugClock.setVisibility(View.VISIBLE);


            flVideo.setVisibility(View.GONE);
            flDebugVideo.setVisibility(View.GONE);
            flDebugClock.setVisibility(View.GONE);
            llDefect.setVisibility(View.VISIBLE);

            llAddEditDefect.setVisibility(View.VISIBLE);
            llRemarkDetailed.setVisibility(View.VISIBLE);
            rlDefectInfo.setVisibility(View.GONE);

            tvCancel.setText("取消");
            tvCancel.setVisibility(View.VISIBLE);
            tvAdd.setText("确定");
            tvAdd.setVisibility(View.VISIBLE);
            layoutUdp.setVisibility(View.GONE);
        }
        else if(isTabUdpSet){
            tvDefectInfo.setVisibility(View.GONE);
            tvEditDefect.setVisibility(View.GONE);
            flVideo.setVisibility(View.GONE);
            flDebugVideo.setVisibility(View.GONE);
            flDebugClock.setVisibility(View.GONE);
            llDefect.setVisibility(View.GONE);

            layoutUdp.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化码表
     */
    private void initClock() {
        serialPort = mJwsManagerLock.jwsOpenSerialPort(SERIAL_PORT, 9600);
        if (serialPort == null) {
            showToast("码表打开端口失败");
            return;
        }
        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();
        openFlag = false;
    }

    /**
     * 初始化视频分析仪
     */
    private void initVideoAnalyzer() {
        videoSerialPort = mJwsManager.jwsOpenSerialPort(SERIAL_PORT_VIDEO, 9600);
        if (videoSerialPort == null) {
            showToast("视频打开端口失败");
            return;
        }
        videoInputStream = videoSerialPort.getInputStream();
        videoOutputStream = videoSerialPort.getOutputStream();
        videoOpenFlag = false;
    }

    @Override
    protected void initData() {
        super.initData();
        showWorkbenchDataBy(intAppPlatform);
        flagDefectAddOrEdit = 0;
        getDefectDetailList();
        wvChartInitData();
        showAi();
        prepareSaveUdp();
    }

    /**
     * 默认数据
     *
     * @param intAppPlatform 平台类型
     */
    private void showWorkbenchDataBy(Integer intAppPlatform) {
        if (intAppPlatform == 0) {
            showSingleData();
        } else if (intAppPlatform == 1) {
            showLinkageData();
        }
    }

    private void showSingleData() {
        tvScoreSingle.setText("布匹评分：");
    }

    @SuppressLint("SetTextI18n")
    private void showLinkageData() {
        tvLength.setText("长度：0M");
        tvDefectCount.setText("瑕点：0个");
        tvScoreNormal.setText("布匹评分：");
    }

    /**
     * 验布工作台数据
     */
    private void loadWorkbenchData() {
        String url = AppConfig.BASE_IMAGE_URL + "fchart/point.html?defectScore=5&defectColor=00BC9B&cleanFlag=0";
//        wvChart.loadDataWithBaseURL(null, url, mimeType, enCoding, null);
        wvChart.loadUrl(url);
    }

    /**
     * 设置日期为当天日期
     */
    @SuppressLint("SetTextI18n")
    private void setCurrentDate() {
        String strDate = getNowDate();
        String[] strArrayDate = strDate.split("-");
        int year = Integer.parseInt(strArrayDate[0]);
        int month = Integer.parseInt(strArrayDate[1]);
        int day = Integer.parseInt(strArrayDate[2]);
        tvYear.setText(year + "");
        tvMonth.setText(month + "");
        tvDay.setText(day + "");
    }

    /**
     * 7    * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDate() {
        Date currentTime = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(currentTime);
    }

    /**
     * 展示Ai分析的内容(视频)
     */
    private void showAi() {
        videoView.setVideoURI(Uri.parse(AppConfig.RTSP_URL));
        videoView.requestFocus();
        videoView.start();
        flVideo.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.VISIBLE);
        tvEmptyDataRight.setVisibility(View.GONE);
    }

    /**
     * 设置监听事件
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initListener() {
        super.initListener();
        blemishInfoListAdapter.setShowOrEditDefectInfoListener(this);
        addBlemishTypeListAdapter.setParentClickListener(this);

        etVarieties.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                flag++;
                if (flag == 2) {
                    flag = 0;//不要忘记这句话
                    //处理逻辑
                    searchFlag = 0;
                }
                return false;
            }
        });


        etVarieties.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                if (searchFlag == 0) {
                    if (mClothInfoBean == null) {
                        mClothInfoBean = new ClothInfoBean();
                    }
                    mClothInfoBean.variety = charSequence.toString();
                    if (nameIdList != null) {
                        int mHeight = Tools.dip2px(getTargetActivity(), 40) * 3;
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llVarieties.getLayoutParams();
                        params.height = mHeight;
                        llVarieties.setLayoutParams(params);
                        List<CommonIdNameBean> sortList = getSortList(charSequence.toString(), nameIdList);
                        if (sortList.size() > 0) {
                            nameValueAdapter.replaceAll(sortList);
                            llVarieties.setVisibility(View.VISIBLE);
                        }
                    } else {
                        type = TYPE_VARIETIES;
                        getCommonNameListByType(type, charSequence.toString(), etVarieties);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        etWarp.setOnTouchListener(new View.OnTouchListener() {
//            //按住和松开的标识
//            int flag = 0;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                flag++;
//                if (flag == 2) {
//                    flag = 0;//不要忘记这句话
//                    //处理逻辑
//                    searchFlag = 0;
//                }
//                return false;
//            }
//        });
//        etWarp.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (searchFlag == 0) {
//                    type = TYPE_WARP;
//                    getCommonNameListByType(type, charSequence.toString(), etWarp);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//
//            }
//        });
//        etWeft.setOnTouchListener(new View.OnTouchListener() {
//            //按住和松开的标识
//            int flag = 0;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                flag++;
//                if (flag == 2) {
//                    flag = 0;//不要忘记这句话
//                    //处理逻辑
//                    searchFlag = 0;
//                }
//                return false;
//            }
//        });
//        etWeft.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (searchFlag == 0) {
//                    type = TYPE_WEFT;
//                    getCommonNameListByType(type, charSequence.toString(), etWeft);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
    }

    private List<CommonIdNameBean> getSortList(String s, List<CommonIdNameBean> nameIdList) {
        List<CommonIdNameBean> mNameIdList = new ArrayList<>();
        if ("".equals(s) || s == null) {
            return nameIdList;
        }
        Pattern p = Pattern.compile("[A-Z]+");
        Pattern p1 = Pattern.compile("[a-z]+");
        Matcher m = p.matcher(s);
        Matcher m1 = p1.matcher(s);
        for (CommonIdNameBean nameBean : nameIdList) {
            String name = nameBean.name;
            if (m.matches()) {
                String upperCaseStr = name.toUpperCase();
                if (upperCaseStr.startsWith(s)) {
                    mNameIdList.add(nameBean);
                }

            } else if (m1.matches()) {
                String upperCaseStr = s.toUpperCase();
                String upperCaseStr2 = name.toUpperCase();
                if (upperCaseStr2.startsWith(upperCaseStr)) {
                    mNameIdList.add(nameBean);
                }

            } else {
                if (name.startsWith(s)) {
                    mNameIdList.add(nameBean);
                }
            }

        }
        return mNameIdList;
    }

    /**
     * 展示pop
     *
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
                    //展示Pop
                    showPopupWindow(type, mLocationView, result, null);
                }
            });
        } else if (type == TYPE_WARP) {
            mMaintenanceService.getWarpSupplyList(queryKey, new CommonResultListener<List<CommonIdNameBean>>() {
                @Override
                public void successHandle(List<CommonIdNameBean> result) {
                    if (result == null || result.size() == 0) {
                        return;
                    }
                    KeyboardUtil.hideInputKeyboard(getTargetActivity(), mLocationView);
                    showPopupWindow(type, mLocationView, null, result);
                    searchFlag = 1;
                }
            });
        } else if (type == TYPE_WEFT) {
            mMaintenanceService.getWeftSupplyList(queryKey, new CommonResultListener<List<CommonIdNameBean>>() {
                @Override
                public void successHandle(List<CommonIdNameBean> result) {
                    if (result == null || result.size() == 0) {
                        return;
                    }
                    KeyboardUtil.hideInputKeyboard(getTargetActivity(), mLocationView);
                    showPopupWindow(type, mLocationView, null, result);
                    searchFlag = 1;
                }
            });
        } else if (type == TYPE_SHIFT) {
            mMaintenanceService.getShiftList(new CommonResultListener<List<CommonIdNameBean>>() {
                @Override
                public void successHandle(List<CommonIdNameBean> result) {
                    if (result == null || result.size() == 0) {
                        return;
                    }
                    showPopupWindow(type, mLocationView, null, result);
                }
            });
        } else if (type == TYPE_PLY) {
            mMaintenanceService.getClothPlyList(new CommonResultListener<List<CommonIdNameBean>>() {
                @Override
                public void successHandle(List<CommonIdNameBean> result) {
                    if (result == null || result.size() == 0) {
                        return;
                    }

                    clothPlyList = result;
                    showPopupWindow(type, mLocationView, null, result);
                }
            });
        }
    }

    private void showPopupWindow(int type, View mLocationView, List<VarietyListBean> result, List<CommonIdNameBean> idNameBeanList) {
        int intSize;
        int intHeight;
        if (type == TYPE_VARIETIES) {
            int mHeight = Tools.dip2px(getTargetActivity(), 40) * 3 / 2;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llVarieties.getLayoutParams();
            params.height = mHeight;
            llVarieties.setLayoutParams(params);
            nameIdList = getIdNameListData(result);
            nameValueAdapter.clear();
            nameValueAdapter.replaceAll(nameIdList);
            searchFlag = 1;
            return;
        } else {
            intSize = idNameBeanList.size();
        }
        intHeight = 40 * intSize;
        mPopupWindow = new CommonPopupWindow(getTargetActivity(), mLocationView.getWidth()
                , Tools.dip2px(getTargetActivity(), intHeight) * 3 / 2, false, mLocationView, idNameBeanList);
        mPopupWindow.setCommonIdNameListener(this);
        mPopupWindow.setBackPressEnable(false);
        mPopupWindow.setOutSideDismiss(false);
        mPopupWindow.showPopupWindow(mLocationView);
    }

    /**
     * 自行组装数据(只有品种需要组装)
     *
     * @param result
     * @return
     */
    private List<CommonIdNameBean> getIdNameListData(List<VarietyListBean> result) {
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
        String name = idNameBean.name;
        if (name == null) {
            return;
        }
        if (mClothInfoBean == null) {
            mClothInfoBean = new ClothInfoBean();
        }
        int id = idNameBean.id;
        if (type == TYPE_VARIETIES) {
            etVarieties.setText(name);
            mClothInfoBean.variety = name;
            llVarieties.setVisibility(View.GONE);
            //隐藏输入法键盘
            KeyboardUtil.hideInputKeyboard(getTargetActivity(), etVarieties);
            return;
        } else if (type == TYPE_WARP) {
            etWarp.setText(name);
            mClothInfoBean.strWarp = name;
            mClothInfoBean.warpId = id;
        } else if (type == TYPE_WEFT) {
            etWeft.setText(name);
            mClothInfoBean.strWeft = name;
            mClothInfoBean.weftId = id;
        } else if (type == TYPE_SHIFT) {
            tvShiftEdit.setText(name);
            mClothInfoBean.strShift = name;
            mClothInfoBean.shiftId = id;
        } else if (type == TYPE_PLY) {
            tvThicknessEdit.setText(name);
            mClothInfoBean.strPly = name;
            mClothInfoBean.plyId = id;
        }
        mPopupWindow.dismiss();
    }

    @OnClick({R.id.tv_workbench, R.id.tv_statistics, R.id.iv_back, R.id.tv_perching
            , R.id.tv_cloth_info, R.id.tv_defect_info_list, R.id.et_warp, R.id.et_weft
            , R.id.ll_date, R.id.tv_shift_edit, R.id.tv_thickness_edit, R.id.tv_add_cancel
            , R.id.tv_sure_edit, R.id.tv_ai_analysis, R.id.tv_add_blemish, R.id.tv_defect_info
            , R.id.tv_debug_clock, R.id.tv_debug_video_clear, R.id.tv_debug_video, R.id.tv_debug_clock_clear
            , R.id.tv_edit_defect, R.id.tv_cancel
            , R.id.tv_add, R.id.iv_close, R.id.iv_full_screen,R.id.tv_udp_set,R.id.et_udpIp,R.id.et_udpPort,R.id.udp_cancel,R.id.udp_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //验布工作台
            case R.id.tv_workbench:
                tabTopUiHandler(true);
                loadWorkbenchData();
                break;
            //验布统计
            case R.id.tv_statistics:
                tabTopUiHandler(false);
                loadStatisticsData();
                break;
            case R.id.iv_back:
                checkExit();
                break;
            case R.id.tv_perching:
                startOrStopPerching();
                break;
            case R.id.tv_cloth_info:
                tabLeftUiHandler(true);
                if (clothInfoStatus == 1 || clothInfoStatus == 0) {
                    clothInfoStatus = 0;
                    return;
                }
                getClothInfoDetail();
                break;
            case R.id.tv_defect_info_list:
                tabLeftUiHandler(false);
                getDefectList();
                break;
            case R.id.et_warp:
                llVarieties.setVisibility(View.GONE);
                type = TYPE_WARP;
                getCommonNameListByType(type, "", etWarp);
                break;
            case R.id.et_weft:
                llVarieties.setVisibility(View.GONE);
                type = TYPE_WEFT;
                getCommonNameListByType(type, "", etWeft);
                break;

            case R.id.ll_date:
                llVarieties.setVisibility(View.GONE);
                showDateDialog();
                break;
            case R.id.tv_shift_edit:
                llVarieties.setVisibility(View.GONE);
                type = TYPE_SHIFT;
                getCommonNameListByType(type, "", tvShiftEdit);
                break;
            case R.id.tv_thickness_edit:
                llVarieties.setVisibility(View.GONE);
                type = TYPE_PLY;
                getCommonNameListByType(type, "", tvThicknessEdit);
                break;
            case R.id.tv_add_cancel:
                addNewClothOrCancelAdd();
                break;
            case R.id.tv_sure_edit:
                showEditAbleUiOrUpdate();
                break;
            case R.id.tv_ai_analysis:
                tabRightUiHandler(true, false, false, false, false, false,false);
                showAi();
                break;
            case R.id.tv_add_blemish:
                flagDefectAddOrEdit = 0;
                tabLeftUiHandler(false);
                tabRightUiHandler(false, true, false, false, false, false,false);
                getDefectList();
                break;
            case R.id.tv_debug_video:
                tabRightUiHandler(false, false, true, false, false, false,false);
                break;
            case R.id.tv_debug_video_clear:
                valueVideoList.clear();
                valueVideoAdapter.replaceAll(valueVideoList);
                break;
            case R.id.tv_debug_clock:
                tabRightUiHandler(false, false, false, true, false, false,false);
                break;
            case R.id.tv_debug_clock_clear:
                valueClockList.clear();
                valueClockAdapter.replaceAll(valueClockList);
                break;
            case R.id.tv_defect_info:
                flagDefectAddOrEdit = 1;
                tabRightUiHandler(false, false, false, true, false, false,false);
                getDefectInfoById();
                break;
            case R.id.tv_edit_defect:
                flagDefectAddOrEdit = 1;
                tabRightUiHandler(false, false, true, false, false, false,false);
                initDefectCategoryAdapter();
                initDefectDesAdapter();
                getDefectDetailList();
                addBlemishTypeListAdapter.setParentClickListener(this);
                break;
            case R.id.tv_cancel:
                tabRightUiHandler(true, false, false, false, false, false,false);
                showAi();
                break;
            case R.id.tv_add:
                addOrEditDefect();
                break;
            case R.id.iv_close:
                tabRightUiHandler(true, false, false, false, false, false,false);
                showAi();
                break;
            case R.id.iv_full_screen:
                enterOrExitFullScreen();
                break;
            case R.id.tv_udp_set:
                tabRightUiHandler(false, false, false, false, false, false,true);
                break;
            case R.id.udp_cancel:
                tabRightUiHandler(true, false, false, false, false, false,false);
                showAi();
                break;
            case R.id.udp_add:
                setUdpIpAndPort();
                break;
        }
    }


    /**
     * 验布统计员数据
     */
    private void loadStatisticsData() {
        WebViewUtil.initWebView(wvPerchingStatistics, this);
        wvPerchingStatistics.setBackgroundColor(0); // 设置背景色
        wvPerchingStatistics.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
        String url = AppConfig.BASE_IMAGE_URL + "fchart/all.html?token=" + GlobalInfo.userToken;
        Log.v("验布统计",url);
//        wvPerchingStatistics.loadDataWithBaseURL(null, url, mimeType, enCoding, null);
        wvPerchingStatistics.loadUrl(url);
    }

    /**
     * 验布中不可退回登录页面(perchingStatus为2时)
     * 其他状态直接返回登录页面
     */
    private void checkExit() {
        if (perchingStatus == 2) {
            showToast("正在验布中,确定返回登录页面?");
            return;
        }
        GlobalInfo.userToken = "";
        MySharedPreference.save(SharedKeyConstant.TOKEN, "", this);
        GlobalInfo.userIdentity = null;
        MySharedPreference.save(SharedKeyConstant.USER_IDENTITY, String.valueOf(GlobalInfo.userIdentity), this);
        IntentStartActivityHelper.startActivityClearAll(this, LoginActivity.class);
    }

    /**
     * 未开始验布(perchingStatus=0 按钮为深绿色)
     * 开始验布
     * 停止验布
     */
    private void startOrStopPerching() {
        if (mClothIdBean == null) {
            showToast("请点击新增布匹按钮\n点击新增布匹按钮后\n   方可进行此操作");
            return;
        }
        //新增布匹成功,还未点击开始验布(开始验布 未开始 绿色)
        if (perchingStatus == 0) {
            perchingStatus = 1;
            setTvPerchingStatus(perchingStatus);
            return;
        }
        //新增布匹成功,点击开始验布(验布停止 验布中,红色)
        if (perchingStatus == 1) {
            perchingStatus = 2;
            setTvPerchingStatus(perchingStatus);
            //发送指令启动验布机,更改开始验布按钮(绿色)为停止验布(红色),并且隐藏新增布匹按钮
            hideTvAddShowTvEdit();
            openFlag = true;
            sendClockStartCommand();
            videoOpenFlag = true;
            sendVideoStartCommand();
            loadWorkbenchData();
            handler.postDelayed(mClockRunnable, 0);
            handler.postDelayed(videoRunnable, 0);
            if (machineNoList != null && machineNoList.size() > 0) {
                uploadDefectList(machineNoList, defectList);
            }
            return;
        }
        if (perchingStatus == 2) //验布中 接收到有疵点信息,暂停验布
        {
            perchingStatus = 3;
            //发送指令停止验布机,更改停止验布(红色)为开始验布按钮(绿色),并且隐藏新增布匹按钮
            setTvPerchingStatus(perchingStatus);
            showTvAddAndTvEditUnEditUi();
            openFlag = false;
            sendVideoStopCommand();
            videoOpenFlag = false;
            return;
        }

        if (perchingStatus == 3)//
        {
            perchingStatus = 2;
            setTvPerchingStatus(perchingStatus);
            hideTvAddShowTvEdit();
            openFlag = true;
            sendClockStartCommand();
            videoOpenFlag = true;
            sendVideoStartCommand();
            loadWorkbenchData();
            handler.postDelayed(mClockRunnable, 0);
            handler.postDelayed(videoRunnable, 0);
        }
    }

    /**
     * 布匹信息界面(左边为新增布匹按钮,右边为编辑按钮)(不可编辑)
     */
    private void showTvAddAndTvEditUnEditUi() {
        llEdit.setVisibility(View.GONE);
        llUnEdit.setVisibility(View.VISIBLE);
        tvAddCancel.setVisibility(View.VISIBLE);
        tvAddCancel.setText("新增布匹");
        tvSureEdit.setVisibility(View.VISIBLE);
        tvSureEdit.setText("编辑");
    }

    /**
     * 布匹信息界面(左边为新增布匹按钮,右边为编辑按钮)(不可编辑,点击编辑按钮方可编辑,验布中用)
     */
    private void hideTvAddShowTvEdit() {
        llUnEdit.setVisibility(View.VISIBLE);
        llEdit.setVisibility(View.GONE);
        tvAddCancel.setText("新增布匹");
        tvAddCancel.setVisibility(View.GONE);
        tvSureEdit.setText("编辑");
        tvSureEdit.setVisibility(View.VISIBLE);
    }

    /**
     * 码表启动命令
     */
    private void sendClockStartCommand() {
        strClockSendCommandName = "发送：";
        strClockSendTime = getCurrentTime();
        strClockSendCommandValue = "";
        try {
            ComPortUtil.send(ComPortUtil.StrToBCDBytes(GET_LENGTH_CODE), outputStream);
            strClockSendCommandValue = GET_LENGTH_CODE + "-->查询指令";
        } catch (IOException e) {
            showToast("发送命令异常");
            strClockSendCommandValue = "发送命令异常";
        }
        if (clockCount == 30) {
            sendCommandAndReceiveLockData(strClockSendCommandName, strClockSendTime, strClockSendCommandValue);
        }
    }

    /**
     * 视频分析仪启动命令
     */
    private void sendVideoStartCommand() {
        strVideoSendCommandName = "发送：";
        strVideoSendTime = getCurrentTime();
        try {
            ComPortUtil.send(ComPortUtil.StrToBCDBytes(RECEIVE_DATA_CODE), videoOutputStream);
            SocketThreadManager.getInstance().sendMsg(START_COMMAND);
            strVideoSendCommandValue = RECEIVE_DATA_CODE + "-->启动";
        } catch (IOException e) {
            showToast("发送命令异常");
            strVideoSendCommandValue = "发送命令异常";
        }
        sendCommandAndReceiveVideoData(strVideoSendCommandName, strVideoSendTime, strVideoSendCommandValue);

    }

    /**
     * 布匹信息界面(左边为取消按钮,右边为确定按钮)(可输入编辑,布匹信息更新用)
     */
    private void showTvCancelAndTvSure() {
        llUnEdit.setVisibility(View.GONE);
        llEdit.setVisibility(View.VISIBLE);
        tvAddCancel.setText("取消");
        tvAddCancel.setVisibility(View.VISIBLE);
        tvSureEdit.setText("确定");
        tvSureEdit.setVisibility(View.VISIBLE);
    }

    /**
     * 获得布匹信息
     */
    private void getClothInfoDetail() {
        if (mClothInfoBean == null) {
            setCurrentDate();
            return;
        }
        Integer intClothId = mClothInfoBean.id;
        if (intClothId == null) {
            return;
        }
        String strClothId = String.valueOf(intClothId);
        mWorkService.getClothInfoById(strClothId, new CommonResultListener<ClothInfoBean>() {
            @Override
            public void successHandle(ClothInfoBean result) {
                showClothInfoByResult(result, clothInfoStatus);
            }
        });
    }

    /**
     * @param result
     * @param clothInfoStatus
     */
    private void showClothInfoByResult(ClothInfoBean result, Integer clothInfoStatus) {
        if (clothInfoStatus == null) {
            return;
        }
        if (clothInfoStatus == 0) {
            return;
        }
        if (clothInfoStatus == 1) {
            hideTvAddShowTvSure();
            showClothEditAbleInfo(result);
            return;
        }
        if (clothInfoStatus == 2) {
            showTvAddAndTvEdit();
            showClothUnEditAbleInfo(mClothInfoBean);
            return;
        }
        if (clothInfoStatus == 3) {
            showTvCancelAndTvSure();
            showClothEditAbleInfo(mClothInfoBean);
            return;
        }
        if (clothInfoStatus == 4) {
            showTvCancelAndTvSure();
            showClothEditAbleInfo(mClothInfoBean);
        }

    }

    /**
     * 显示可编辑页面,左边隐藏"新增布匹"按钮,右边显示"确定按钮"
     */
    private void hideTvAddShowTvSure() {
        llEdit.setVisibility(View.VISIBLE);
        llUnEdit.setVisibility(View.GONE);
        tvAddCancel.setText("新增布匹");
        tvAddCancel.setVisibility(View.GONE);
        tvSureEdit.setText("确定");
        tvSureEdit.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void showClothEditAbleInfo(ClothInfoBean result) {
        etVarieties.setText(result.variety);

        final Integer warpId = result.warpId;
        mMaintenanceService.getWarpSupplyList("", new CommonResultListener<List<CommonIdNameBean>>() {
            @Override
            public void successHandle(List<CommonIdNameBean> result) {
                final String strWarp = getStringPlyByIdResult(warpId, result);
                etWarp.setText(strWarp);
            }
        });

        final Integer weftId = result.weftId;
        mMaintenanceService.getShiftList(new CommonResultListener<List<CommonIdNameBean>>() {
            @Override
            public void successHandle(List<CommonIdNameBean> result) {
                String strWeft = getStringPlyByIdResult(weftId, result);
                etWeft.setText(strWeft + "");
            }
        });

        etMachineNum.setText(result.machineNo);
        tvYear.setText(result.year + "");
        tvMonth.setText(result.month + "");
        tvDay.setText(result.day + "");

        final Integer shiftId = result.shiftId;
        mMaintenanceService.getShiftList(new CommonResultListener<List<CommonIdNameBean>>() {
            @Override
            public void successHandle(List<CommonIdNameBean> result) {
                String strPly = getStringPlyByIdResult(shiftId, result);
                tvShiftEdit.setText(strPly);
            }
        });


        final Integer plyId = result.plyId;
        mMaintenanceService.getClothPlyList(new CommonResultListener<List<CommonIdNameBean>>() {
            @Override
            public void successHandle(List<CommonIdNameBean> result) {
                String strPly = getStringPlyByIdResult(plyId, result);
                tvThicknessEdit.setText(strPly);
            }
        });
    }

    private String getStringPlyByIdResult(Integer plyId, List<CommonIdNameBean> result) {
        String strPly = "";
        if (plyId == null) {
            return strPly;
        }
        if (result == null || result.size() == 0) {
            return strPly;
        }
        for (CommonIdNameBean idNameBean : result) {
            int id = idNameBean.id;
            if (id == plyId) {
                strPly = idNameBean.name;
            }
        }
        return strPly;
    }


    /**
     * 左边"新增布匹"
     * 右边"编辑"
     */
    private void showTvAddAndTvEdit() {
        llEdit.setVisibility(View.GONE);
        llUnEdit.setVisibility(View.VISIBLE);
        tvAddCancel.setVisibility(View.VISIBLE);
        tvAddCancel.setText("新增布匹");
        tvSureEdit.setVisibility(View.VISIBLE);
        tvSureEdit.setText("编辑");
    }

    @SuppressLint("SetTextI18n")
    private void showClothUnEditAbleInfo(ClothInfoBean result) {
        tvVarieties.setText(result.variety);
        tvWarpAndWeft.setText(result.strWarp + "—" + result.strWeft);
        tvMachineNum.setText(result.machineNo);
        tvDate.setText(result.year + "—" + result.month + "—" + result.day);
        tvShift.setText(result.strShift);
        tvThickness.setText(result.strPly);
    }

    /**
     * 布匹疵点列表Tab初始化
     */
    private void initDefectListAdapter() {
        list = new ArrayList<>();
        blemishInfoListAdapter = new BlemishInfoListAdapter(getTargetActivity(), list);
        lvBlemishInfoList.setAdapter(blemishInfoListAdapter);
        llDefectList.setVisibility(View.GONE);
        tvEmptyDataLeft.setVisibility(View.VISIBLE);
    }

    private void getDefectList() {
        if (mClothInfoBean == null) {
            return;
        }
        Integer clothId = mClothInfoBean.id;
        if (clothId == null) {
            return;
        }
        mWorkService.getDefectList(clothId, new CommonResultListener<List<DefectListBan>>() {
            @Override
            public void successHandle(List<DefectListBan> result) {
                if (result == null || result.size() == 0) {
                    return;
                }
                llDefectList.setVisibility(View.VISIBLE);
                tvEmptyDataLeft.setVisibility(View.GONE);
                blemishInfoListAdapter.replaceAll(result);
                list = getListByResult(result);
            }
        });
    }

    private List<DefectListBan> getListByResult(List<DefectListBan> result) {
        List<DefectListBan> beanList = new ArrayList<>();
        if (result == null || result.size() == 0) {
            return beanList;
        }
        for (DefectListBan listBan : result) {
            DefectListBan defectListBan = new DefectListBan();
            defectListBan.id = listBan.id;
            defectListBan.color = listBan.color;
            defectListBan.backgroundFlag = 0;
            defectListBan.score = listBan.score;
            defectListBan.location = listBan.location;
            defectListBan.defectName = listBan.defectName;
            Integer mIndex = getCurrentIndexById(listBan.id, result);
            if (mIndex >= 99) {
                listBan.defectNum = (mIndex + 1) + "";
            } else if (mIndex >= 9) {
                listBan.defectNum = "0" + (mIndex + 1);
            } else {
                listBan.defectNum = "00" + (mIndex + 1);
            }
            beanList.add(listBan);
        }

        return beanList;
    }

    private Integer getCurrentIndexById(Integer currentId, List<DefectListBan> list) {
        Integer mIndex = null;
        if (currentId != null) {
            if (list != null && list.size() > 0) {
                for (DefectListBan listBan : list) {
                    if (listBan != null) {
                        int id = listBan.id;
                        if (currentId == id) {
                            mIndex = list.indexOf(listBan);
                        }
                    }
                }
            }
        }
        return mIndex;
    }

    @Override
    public void showDefectInfo(final Integer defectId, final String color) {
        if (defectId == null) {
            return;
        }
        mWorkService.getDefectDetailById(defectId, new CommonResultListener<DefectInfoBean>() {
            @Override
            public void successHandle(DefectInfoBean result) {
                if (result == null) {
                    return;
                }
                getColorDefectList(result.defectId, result);
            }
        });
    }

    private void getColorDefectList(final Integer defectId, final DefectInfoBean defectInfoBean) {
        mMaintenanceService.getDefectList(new CommonResultListener<List<DefectInfoListBean>>() {
            @Override
            public void successHandle(List<DefectInfoListBean> result) {
                String color = getStrColor(defectId, result);
                showDefectDetailByResult(defectInfoBean, color);
            }
        });
    }

    private String getStrColor(Integer defectId, List<DefectInfoListBean> result) {
        String color = "";
        if (defectId == null) {
            return color;
        }
        if (result == null) {
            return color;
        }
        for (DefectInfoListBean bean : result) {
            if (bean == null) {
                return color;
            }
            List<DefectListBean> beanList = bean.defectList;
            if (bean.defectList == null) {
                return color;
            }
            for (DefectListBean listBean : beanList) {
                if (listBean == null) {
                    return color;
                }
                int id = listBean.id;
                if (defectId == id) {
                    color = bean.color;
                }
            }
        }

        return color;
    }

    private void showDefectDetailByResult(DefectInfoBean result, String color) {
        String defectName = result.defectName;
        if (!"".equals(defectName) && defectName != null) {
            tvDefectName.setText(defectName);
        }
        if (!"".equals(color) && color != null) {
            int intColor = Color.parseColor(color);
            tvDefectName.setBackgroundColor(intColor);
        }


        int score = result.score;
        String defectDes = getDefectDes(result.extraParamList, score);
        tvDefectDes.setText(defectDes);

        String remark = result.remark;
        if (remark != null) {
            tvRemarkDes.setText(remark);
        }
        tabRightUiHandler(false, false, false, false, true, false,false);
    }

    private String getDefectDes(List<DefectInfoBean.ExtraParamListBean> extraParamList, int score) {
        StringBuilder des = new StringBuilder();
        if (extraParamList == null || extraParamList.size() == 0) {
            return des.toString().toString();
        }
        for (DefectInfoBean.ExtraParamListBean listBean : extraParamList) {
            if (listBean != null) {
                String name = listBean.name;
                String value = listBean.value;
                String unit = listBean.unit;
                des.insert(0, name + "   " + value + "  " + unit + "      ");
            }
        }
        return des + "   " + score + "    " + "分";
    }

    @Override
    public void editDefectInfo(Integer id) {
        tabRightUiHandler(false, false, false, false, false, true,false);
        mClothInfoBean.clothDefectId = id;
        flagDefectAddOrEdit = 1;
        getDefectDetailList();
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
        tvYear.setText(year + "");
        tvMonth.setText(month + "");
        tvDay.setText(day + "");
    }


    private void addNewClothOrCancelAdd() {

        if (clothInfoStatus == 0)//初次进入此页面(只有新增布匹按钮)
        {
            clothInfoStatus = 1;
            //显示可编辑页面,左边隐藏"新增布匹"按钮,右边显示"确定按钮"
            hideTvAddShowTvSure();
            setCurrentDate();
            return;
        }
        if (clothInfoStatus == 1)//显示可编辑页面,左边隐藏"新增布匹"按钮,右边显示"确定按钮"
        {
            return;
        }
        if (clothInfoStatus == 2)//显示不可编辑页面,左边显示"新增布匹"按钮,右边显示"编辑"按钮"
        {

            clothInfoStatus = 3;
            showTvCancelAndTvSure();//显示可编辑页面,左边显示"取消"按钮,右边显示"确定"按钮"
            return;
        }
        if (clothInfoStatus == 3)//显示可编辑页面,左边显示"取消"按钮,右边显示"确定"按钮"
        {

            clothInfoStatus = 2;
            showClothUnEditAbleInfo(mClothInfoBean);
            showTvAddAndTvEdit();//显示不可编辑页面,左边显示"新增布匹"按钮,右边显示"编辑"按钮"
            return;
        }
        if (clothInfoStatus == 4)//初次进入此页面(只有新增布匹按钮)
        {
            clothInfoStatus = 2;
            showClothUnEditAbleInfo(mClothInfoBean);
            showTvAddAndTvEdit();//显示不可编辑页面,左边显示"新增布匹"按钮,右边显示"编辑"按钮"
        }
    }


    private void showEditAbleUiOrUpdate() {
        if (clothInfoStatus == 0) //初次进入此页面(只有新增布匹按钮,右边,没有按钮)
        {
            return;
        }
        if (clothInfoStatus == 1)//显示可编辑页面,左边隐藏"新增布匹"按钮,右边显示"确定按钮" (新增布匹)
        {
            addNewCloth();
            return;
        }
        if (clothInfoStatus == 2)//显示不可编辑页面,左边显示"新增布匹"按钮,右边显示"编辑"按钮"
        {
            clothInfoStatus = 4;
            showTvCancelAndTvSure();
            showClothEditInfo();
            return;
        }
        if (clothInfoStatus == 3)////显示可编辑页面,左边显示"取消"按钮,右边显示"确定"按钮"
        {
            addNewCloth();
            return;
        }
        if (clothInfoStatus == 4)////显示可编辑页面,左边显示"取消"按钮,右边显示"确定"按钮"
        {
            updateClothInfo();
        }
    }


    private void initDefectCategoryAdapter() {
        beanList = new ArrayList<>();
        addBlemishTypeListAdapter = new AddBlemishTypeListAdapter(getTargetActivity(), beanList);
        lvBlemishTypeList.setAdapter(addBlemishTypeListAdapter);
    }

    private void initDefectDesAdapter() {
        defectListBeanList = new ArrayList<>();
        defectDescribeAdapter = new DefectDescribeAdapter(getTargetActivity()
                , R.layout.adapter_defect_des_un_edit, defectListBeanList);
        lvRemarkDes.setAdapter(defectDescribeAdapter);
    }


    private void getDefectDetailList() {
        mMaintenanceService.getDefectList(new CommonResultListener<List<DefectInfoListBean>>() {
            @Override
            public void successHandle(List<DefectInfoListBean> result) {
                if (flagDefectAddOrEdit == 0) {
                    paramBeanList = new ArrayList<>();
                    addBlemishTypeListAdapter.replaceAll(result);
                    beanList = getBeanListByResult(result);
                } else if (flagDefectAddOrEdit == 1) {
                    if (mClothInfoBean == null) {
                        return;
                    }
                    Integer clothDefectId = mClothInfoBean.clothDefectId;
                    getDefectDetailById(clothDefectId, result);
                }
            }
        });
    }

    private List<DefectInfoListBean> getBeanListByResult(List<DefectInfoListBean> result) {
        List<DefectInfoListBean> beanList = new ArrayList<>();
        if (result == null || result.size() == 0) {
            return beanList;
        }
        for (DefectInfoListBean listBean : result) {
            DefectInfoListBean bean = new DefectInfoListBean();
            if (listBean != null) {
                bean.categoryId = listBean.categoryId;
                bean.categoryName = listBean.categoryName;
                bean.color = listBean.color;
                bean.selectColor = listBean.color;
                List<DefectListBean> beanDefectList = new ArrayList<>();
                List<DefectListBean> defectList = listBean.defectList;
                if (defectList != null && defectList.size() > 0) {
                    for (DefectListBean defectListBean : defectList) {
                        DefectListBean defectListBean1 = new DefectListBean();
                        defectListBean1.id = defectListBean.id;
                        defectListBean1.color = listBean.color;
                        defectListBean1.name = defectListBean.name;
                        defectListBean1.selectFlag = 0;
                        defectListBean1.score = defectListBean.score;
                        defectListBean1.extraParamList = defectListBean.extraParamList;
                        beanDefectList.add(defectListBean1);
                        bean.defectList = beanDefectList;
                    }
                }

            }
            beanList.add(bean);
        }
        return beanList;
    }

    private void getDefectDetailById(Integer clothDefectId, final List<DefectInfoListBean> defectInfoListBeans) {
        if (clothDefectId == null) {
            return;
        }
        mWorkService.getDefectDetailById(clothDefectId, new CommonResultListener<DefectInfoBean>() {
            @Override
            public void successHandle(DefectInfoBean result) {
                if (result == null) {
                    return;
                }
                paramBeanList = new ArrayList<>();
                beanList = getSelectListData(result, defectInfoListBeans);
                addBlemishTypeListAdapter.replaceAll(beanList);
                showDefectInfoByResult(result);
            }
        });
    }

    private List<DefectInfoListBean> getSelectListData(DefectInfoBean result, List<DefectInfoListBean> beanList) {
        List<DefectInfoListBean> listBeanList = new ArrayList<>();
        if (beanList != null && beanList.size() > 0) {
            if (result != null) {
                Integer defectId = result.defectId;
                if (defectId != null) {
                    for (DefectInfoListBean listBean : beanList) {
                        if (listBean != null) {
                            String color = listBean.color;
                            List<DefectListBean> defectList = listBean.defectList;
                            if (defectList != null) {
                                for (DefectListBean defectListBean : defectList) {
                                    if (defectListBean != null) {
                                        int id = defectListBean.id;
                                        defectListBean.color = color;
                                        if (defectId == id) {
                                            defectListBean.selectFlag = 1;
                                            DefectParamBean paramBean = getDefectParamBean(listBean, defectListBean);
                                            if (paramBean == null) {
                                                return listBeanList;
                                            }
                                            paramBeanList.add(paramBean);
                                        } else {
                                            defectListBean.selectFlag = 0;
                                        }

                                    }
                                }
                            }
                        }
                        listBeanList.add(listBean);
                    }
                }
            }
        }
        return listBeanList;
    }

    @SuppressLint("SetTextI18n")
    private void showDefectInfoByResult(DefectInfoBean result) {
        List<DefectListBean> beanList = getDefectInfoListData(result);
        defectDescribeAdapter.replaceAll(beanList);

        String remark = result.remark;
        if (remark != null) {
            etRemark.setText(remark);
        }

        double location = result.location;
        tvLocation.setText(location + "");
    }

    private List<DefectListBean> getDefectInfoListData(DefectInfoBean result) {
        List<DefectListBean> beanList = new ArrayList<>();
        if (result == null) {
            return beanList;
        }


        List<DefectInfoBean.ExtraParamListBean> extraParamList = result.extraParamList;
        if (extraParamList == null || extraParamList.size() == 0) {
            DefectListBean bean = new DefectListBean();
            bean.name = result.defectName;
            bean.score = result.score;
            beanList.add(bean);
            return beanList;
        }
        for (DefectInfoBean.ExtraParamListBean listBean : extraParamList) {
            DefectListBean bean = new DefectListBean();
            if (listBean != null) {
                bean.id = result.defectId;
                bean.name = result.defectName;
                bean.score = result.score;
                bean.extraParamList = getExtraParamList(result.extraParamList);
                beanList.add(bean);
            }

        }
        return beanList;
    }

    private List<DefectListBean.ExtraParamBean> getExtraParamList(List<DefectInfoBean.ExtraParamListBean> extraParamList) {
        List<DefectListBean.ExtraParamBean> beanList = new ArrayList<>();
        for (DefectInfoBean.ExtraParamListBean listBean : extraParamList) {
            DefectListBean.ExtraParamBean bean = new DefectListBean.ExtraParamBean();
            if (listBean != null) {
                bean.id = listBean.id;
                bean.value = listBean.value;
                bean.name = listBean.name;
                bean.editFlag = listBean.editFlag;
                bean.unit = listBean.unit;
            }
        }
        return beanList;
    }


    /**
     * 新增布匹信息
     */
    private void addNewCloth() {
        final String strVarieties = etVarieties.getText().toString();
        final String strWrap = etWarp.getText().toString();
        final String strWeft = etWeft.getText().toString();
        final String strMachineNum = etMachineNum.getText().toString();
        final String strYear = tvYear.getText().toString();
        final String strMonth = tvMonth.getText().toString();
        final String strDay = tvDay.getText().toString();
        final String strShift = tvShiftEdit.getText().toString();
        final String strThickness = tvThicknessEdit.getText().toString();

        if ("".equals(strVarieties)) {
            getTargetActivity().showToast("请输入品种");
            return;
        }
        if ("".equals(strWrap)) {
            getTargetActivity().showToast("请输入经纱");
            return;
        }
        if ("".equals(strWeft)) {
            getTargetActivity().showToast("请输入纬纱");
            return;
        }
        if ("".equals(strMachineNum)) {
            getTargetActivity().showToast("请输入机器编号");
            return;
        }
        if ("".equals(strShift)) {
            getTargetActivity().showToast("请选择班次");
            return;
        }
        if ("".equals(strThickness)) {
            getTargetActivity().showToast("请选择厚度");
            return;
        }
        final int warpId = mClothInfoBean.warpId;
        final int weftId = mClothInfoBean.weftId;
        final int shiftId = mClothInfoBean.shiftId;
        final int plyId = mClothInfoBean.plyId;
        mWorkService.addClothInfo(strVarieties, warpId, weftId, strMachineNum, Integer.parseInt(strYear)
                , Integer.parseInt(strMonth), Integer.parseInt(strDay), shiftId, plyId, new CommonResultListener<ClothIdBean>() {
                    @Override
                    public void successHandle(ClothIdBean result) {
                        KeyboardUtil.hideInputKeyboard(getTargetActivity(), tvThicknessEdit);
                        clearEditText();
                        showClothInfo(strVarieties, strWrap, strWeft, strMachineNum, strYear, strMonth, strDay, strShift, strThickness);
                        //清除数据
                        wvChartInitData();
                        showTvAddAndTvEdit();
                        refreshUI(result);
                        mClothIdBean = result;
                        saveClothInfo(strVarieties, strWrap, warpId, strWeft, weftId, strMachineNum
                                , strYear, strMonth, strDay, strShift, shiftId, strThickness, plyId, result);

                        zeroSetting();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendClothPlyCommand(mClothInfoBean.plyId, clothPlyList);
                            }
                        }, 200);

                        tvPerching.setClickable(true);
                        openFlag = true;
                        handler.postDelayed(mClockRunnable, 200);
                        dbClothLength = 0.00;
                        tvLength.setText("长度："+dbClothLength+"M");
                        tvLocation.setText("位置："+dbClothLength+"M");
                        videoOpenFlag = true;
                        handler.postDelayed(videoRunnable, 200);
                    }
                });
    }

    private void wvChartInitData() {
        String url = AppConfig.BASE_IMAGE_URL + "fchart/point.html?defectScore=0&defectColor=&cleanFlag=1";
//        wvChart.loadDataWithBaseURL(null, url, mimeType, enCoding, null);
        wvChart.loadUrl(url);

    }


    private void clearEditText() {
        etVarieties.setText("");
        etVarieties.setHint("请选择品种");
        etMachineNum.setText("");
        etMachineNum.setHint("请输入机器编号");
        etWarp.setText("");
        etWarp.setHint("请选择经纱");
        etWeft.setText("");
        etWeft.setHint("请选择纬纱");
        tvShiftEdit.setText("");
        tvShiftEdit.setHint("请选择班次");
        tvThicknessEdit.setText("");
        tvThicknessEdit.setHint("请选择厚度");
    }

    @SuppressLint("SetTextI18n")
    private void showClothInfo(String strVarieties, String strWrap, String strWeft
            , String strMachineNum, String strYear, String strMonth, String strDay, String strShift
            , String strThickness) {
        tvVarieties.setText(strVarieties);
        tvWarpAndWeft.setText(strWrap + "—" + strWeft);
        tvMachineNum.setText(strMachineNum);
        tvDate.setText(strYear + "—" + strMonth + "—" + strDay);
        tvShift.setText(strShift);
        tvThickness.setText(strThickness);
    }


    private void refreshUI(ClothIdBean result) {
        defectCount = 0;
        clothInfoStatus = 2;
        perchingStatus = 1;
        setTvPerchingStatus(perchingStatus);
        mClothInfoBean.id = result.id;
    }

    private void saveClothInfo(String strVarieties, String strWrap, int warpId, String strWeft
            , int weftId, String strMachineNum, String strYear, String strMonth, String strDay
            , String strShift, int shiftId, String strThickness, int plyId, ClothIdBean result) {

        mClothInfoBean.variety = strVarieties;
        mClothInfoBean.strWarp = strWrap;
        mClothInfoBean.warpId = warpId;
        mClothInfoBean.strWeft = strWeft;
        mClothInfoBean.weftId = weftId;
        mClothInfoBean.machineNo = strMachineNum;
        mClothInfoBean.year = Integer.parseInt(strYear);
        mClothInfoBean.month = Integer.parseInt(strMonth);
        mClothInfoBean.day = Integer.parseInt(strDay);
        mClothInfoBean.strShift = strShift;
        mClothInfoBean.shiftId = shiftId;
        mClothInfoBean.strPly = strThickness;
        mClothInfoBean.plyId = plyId;
        mClothInfoBean.id = result.id;
    }

    private void sendClothPlyCommand(int clothPlyId, List<CommonIdNameBean> clothPlyList) {
        String strPly = getStrPly(clothPlyId, clothPlyList);
        if (!"".equals(strPly) && strPly != null) {
            switch (strPly) {
                case "薄":
                    thickness_1();
                    break;
                case "中":
                    thickness_2();
                    break;
                case "厚":
                    thickness_3();
                    break;

            }
        }
    }


    private String getStrPly(Integer clothPlyId, List<CommonIdNameBean> clothPlyList) {
        String strPly = "";
        if (clothPlyId == null) {
            return strPly;
        }
        if (clothPlyList == null || clothPlyList.size() == 0) {
            return strPly;
        }
        for (CommonIdNameBean idNameBean : clothPlyList) {
            if (idNameBean != null) {
                Integer id = idNameBean.id;
                if (id == clothPlyId) {
                    strPly = idNameBean.name;
                }
            }
        }
        return strPly;
    }

    //厚度——薄
    public void thickness_1() {
        strVideoSendCommandName = "发送：";
        strVideoSendTime = getCurrentTime();
        try {
            ComPortUtil.send(ComPortUtil.StrToBCDBytes(THICKNESS_1), videoOutputStream);
            strVideoSendCommandValue = THICKNESS_1 + "-->薄";
        } catch (IOException e) {
            showToast("发送命令异常");
            strVideoSendCommandValue = "发送命令异常";
        }
        sendCommandAndReceiveVideoData(strVideoSendCommandName, strVideoSendTime, strVideoSendCommandValue);
    }

    //厚度——中
    public void thickness_2() {
        strVideoSendCommandName = "发送：";
        strVideoSendTime = getCurrentTime();
        try {
            ComPortUtil.send(ComPortUtil.StrToBCDBytes(THICKNESS_2), videoOutputStream);
            strVideoSendCommandValue = THICKNESS_2 + "-->中";
        } catch (IOException e) {
            showToast("发送命令异常");
            strVideoSendCommandValue = "发送命令异常";
        }
        sendCommandAndReceiveVideoData(strVideoSendCommandName, strVideoSendTime, strVideoSendCommandValue);
    }

    //厚度——厚
    public void thickness_3() {
        strVideoSendCommandName = "发送：";
        strVideoSendTime = getCurrentTime();
        try {
            ComPortUtil.send(ComPortUtil.StrToBCDBytes(THICKNESS_3), videoOutputStream);
            strVideoSendCommandValue = THICKNESS_3 + "-->厚";
        } catch (IOException e) {
            showToast("发送命令异常");
            strVideoSendCommandValue = "发送命令异常";
        }
        sendCommandAndReceiveVideoData(strVideoSendCommandName, strVideoSendTime, strVideoSendCommandValue);
    }


    private void showClothEditInfo() {
        final String strVarieties = tvVarieties.getText().toString();

        final String strWrapWeft = tvWarpAndWeft.getText().toString();
        String[] strArrayWrapWeft = strWrapWeft.split("—");
        String strWrap = strArrayWrapWeft[0];
        String strWeft = strArrayWrapWeft[1];
        final String strMachineNum = tvMachineNum.getText().toString();
        final String strDate = tvDate.getText().toString();
        String[] strArrayDate = strDate.split("—");
        String strYear = strArrayDate[0];
        String strMonth = strArrayDate[1];
        String strDay = strArrayDate[2];


        final String strShift = tvShift.getText().toString();

        final String strThickness = tvThickness.getText().toString();


        etVarieties.setText(strVarieties);
        etWarp.setText(strWrap);
        etWeft.setText(strWeft);
        etMachineNum.setText(strMachineNum);
        tvYear.setText(strYear);
        tvMonth.setText(strMonth);
        tvDay.setText(strDay);
        tvShiftEdit.setText(strShift);
        tvThicknessEdit.setText(strThickness);
    }

    private void updateClothInfo() {
        final String strVarieties = etVarieties.getText().toString();
        final String strWrap = etWarp.getText().toString();
        final String strWeft = etWeft.getText().toString();
        final String strMachineNum = etMachineNum.getText().toString();
        final String strDate = tvDate.getText().toString();
        final String[] strArrayDate = strDate.split("—");
        final String strYear = strArrayDate[0];
        final String strMonth = strArrayDate[1];
        final String strDay = strArrayDate[2];
        final String strShift = tvShiftEdit.getText().toString();
        final String strThickness = tvThicknessEdit.getText().toString();
        Integer clothId = mClothInfoBean.id;
        if (clothId == null) {
            return;
        }
        final int warpId = mClothInfoBean.warpId;
        final int weftId = mClothInfoBean.weftId;
        final int shiftId = mClothInfoBean.shiftId;
        final int plyId = mClothInfoBean.plyId;
        mWorkService.updateClothInfo(clothId, strVarieties, warpId, weftId, strMachineNum
                , Integer.parseInt(strYear), Integer.parseInt(strMonth), Integer.parseInt(strDay)
                , shiftId, plyId, new CommonResultListener<JSONObject>() {
                    @Override
                    public void successHandle(JSONObject result) {
                        showToast("更新布匹信息成功");
                        clearEditText();
                        showTvAddAndTvEdit();
                        showClothInfo(strVarieties, strWrap, strWeft, strMachineNum, strYear, strMonth, strDay, strShift, strThickness);
                        saveClothInfo(strVarieties, strWrap, warpId, strWeft, weftId, strMachineNum
                                , strYear, strMonth, strDay, strShift, shiftId, strThickness, plyId, mClothIdBean);
                        sendClothPlyCommand(mClothInfoBean.plyId, clothPlyList);
                        clothInfoStatus = 2;
                    }
                });
    }

    @Override
    public void defectParentClick(DefectInfoListBean mParentBean, DefectListBean mChildBeam, int mFlagAddOrRemove) {
        etRemark.setEnabled(false);
        llRemarkDetailed.setVisibility(View.VISIBLE);


        if (flagDefectAddOrEdit == 0)//新增
        {
            if (mFlagAddOrRemove == 0)//没有选中,需要新增
            {
                defectDescribeAdapter.add(mChildBeam);
                int size = defectDescribeAdapter.getData().size();
                hideOrShowDefectDesBySize(size);

                if (mClothInfoBean == null) {
                    return;
                }

                DefectParamBean paramBean = getDefectParamBean(mParentBean, mChildBeam);
                if (paramBean == null) {
                    return;
                }
                paramBeanList.add(paramBean);
            } else if (mFlagAddOrRemove == 1)//已经选中不需组装DefectParamBean
            {
                defectDescribeAdapter.remove(mChildBeam);
                int size = defectDescribeAdapter.getData().size();
                hideOrShowDefectDesBySize(size);

                Integer defectId = mChildBeam.id;
                if (defectId == null) {
                    return;
                }
                DefectParamBean paramBean = getCurrentParamBeanById(defectId, paramBeanList);
                if (paramBean == null) {
                    return;
                }
                if (paramBeanList == null) {
                    return;
                }
                paramBeanList.remove(paramBean);
            }


        } else if (flagDefectAddOrEdit == 1)//编辑
        {


            defectDescribeAdapter.clear();
            if (mFlagAddOrRemove == 0)//没有选中,需要新增
            {
                List<DefectInfoListBean> unSelectList = getBeanListByResult(beanList);
                Integer mChildIndex = getCurrentChildIndexById(mChildBeam, unSelectList);
                Integer mParentIndex = getParentIndex(mParentBean, unSelectList);
                unSelectList.get(mParentIndex).defectList.set(mChildIndex, mChildBeam);
                addBlemishTypeListAdapter.replaceAll(unSelectList);

                defectDescribeAdapter.add(mChildBeam);
                int size = defectDescribeAdapter.getData().size();
                hideOrShowDefectDesBySize(size);

                if (mClothInfoBean == null) {
                    return;
                }
                DefectParamBean paramBean = getDefectParamBean(mParentBean, mChildBeam);
                if (paramBean == null) {
                    return;
                }
                paramBeanList = new ArrayList<>();
                paramBeanList.add(paramBean);

            } else if (mFlagAddOrRemove == 1)//已经选中不需组装DefectParamBean
            {
                List<DefectInfoListBean> unSelectList = getBeanListByResult(beanList);
                addBlemishTypeListAdapter.replaceAll(unSelectList);
                defectDescribeAdapter.remove(mChildBeam);
                int size = defectDescribeAdapter.getData().size();
                hideOrShowDefectDesBySize(size);
                Integer defectId = mChildBeam.id;
                if (defectId == null) {
                    return;
                }
                DefectParamBean paramBean = getCurrentParamBeanById(defectId, paramBeanList);
                if (paramBean == null) {
                    return;
                }
                if (paramBeanList == null) {
                    return;
                }
                paramBeanList.remove(paramBean);
            }

        }
    }

    private void hideOrShowDefectDesBySize(int size) {
        if (size > 0) {
            llRemarkDetailed.setVisibility(View.VISIBLE);
        } else {
            llRemarkDetailed.setVisibility(View.GONE);
        }
    }

    private Integer getCurrentChildIndexById(DefectListBean currentChildBean, List<DefectInfoListBean> beanList) {
        Integer mIndex = null;
        Integer id = currentChildBean.id;
        if (id != null) {
            if (beanList != null && beanList.size() > 0) {
                for (DefectInfoListBean listBean : beanList) {
                    if (listBean != null) {
                        List<DefectListBean> listBeanList = listBean.defectList;
                        if (listBeanList != null && listBeanList.size() > 0) {
                            for (DefectListBean bean : listBeanList) {
                                if (bean != null) {
                                    int beanId = bean.id;
                                    if (beanId == id) {
                                        mIndex = listBeanList.indexOf(bean);
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        return mIndex;
    }

    private DefectListBean getStatusDefectListBean(DefectListBean listBean) {
        Integer selectFlag = listBean.selectFlag;
        if (selectFlag == 0) {
            listBean.selectFlag = 1;
        } else if (selectFlag == 1) {
            listBean.selectFlag = 0;
        }
        return listBean;
    }


    private DefectParamBean getDefectParamBean(DefectInfoListBean defectInfoListBean, DefectListBean statusDefectListBean) {
        DefectParamBean paramBean = new DefectParamBean();
        if (defectInfoListBean == null) {
            return paramBean;
        }
        if (statusDefectListBean == null) {
            return paramBean;
        }
        if (flagDefectAddOrEdit != null) {
            if (flagDefectAddOrEdit == 0)//新增
            {
                paramBean.clothId = mClothInfoBean.id;
            } else if (flagDefectAddOrEdit == 1) {
                paramBean.clothId = mClothInfoBean.clothDefectId;
            }
        }

        paramBean.defectId = statusDefectListBean.id;
        paramBean.name = statusDefectListBean.name;
        paramBean.score = statusDefectListBean.score;
        String strLocation = tvLocation.getText().toString();
        Logger.d(strLocation );
        if("位置：0.0M".equals(strLocation)){
            showToast("请确定长度信息");
            return null;
        }
        else if (!"".equals(strLocation)) {
            paramBean.location = Double.parseDouble(strLocation);
        }
        paramBean.remark = etRemark.getText().toString();
        paramBean.extraParamList = getListData(statusDefectListBean.extraParamList);
        paramBean.color = defectInfoListBean.color;
        return paramBean;
    }

    private Integer getParentIndex(DefectInfoListBean defectInfoListBean, List<DefectInfoListBean> beanList) {
        Integer mIndex = null;
        Integer id = defectInfoListBean.categoryId;
        if (id != null) {
            if (beanList != null && beanList.size() > 0) {
                for (DefectInfoListBean listBean : beanList) {
                    int categoryId = listBean.categoryId;
                    if (categoryId == id) {
                        mIndex = beanList.indexOf(listBean);
                    }
                }
            }
        }
        return mIndex;
    }


    private List<DefectParamBean.ExtraParamListBean> getListData(List<DefectListBean.ExtraParamBean> extraParamList) {
        List<DefectParamBean.ExtraParamListBean> beanList = new ArrayList<>();
        if (extraParamList == null) {
            return beanList;
        }
        for (DefectListBean.ExtraParamBean bean : extraParamList) {
            if (bean == null) {
                return beanList;
            }
            DefectParamBean.ExtraParamListBean listBean = new DefectParamBean.ExtraParamListBean();
            listBean.id = bean.id;
            listBean.value = bean.name;
            beanList.add(listBean);
        }
        return beanList;
    }

    private DefectParamBean getCurrentParamBeanById(Integer currentDefectId, List<DefectParamBean> paramBeanList) {
        DefectParamBean currentParamBean = null;
        if (currentDefectId != null) {
            if (paramBeanList != null && paramBeanList.size() > 0) {
                for (DefectParamBean paramBean : paramBeanList) {
                    if (paramBean != null) {
                        int defectId = paramBean.defectId;
                        if (currentDefectId == defectId) {
                            currentParamBean = paramBean;
                        }
                    }
                }
            }
        }
        return currentParamBean;
    }

    private void getDefectInfoById() {
        int defectId = mClothInfoBean.clothDefectId;
        mWorkService.getDefectDetailById(defectId, new CommonResultListener<DefectInfoBean>() {
            @Override
            public void successHandle(DefectInfoBean result) {
                if (result == null) {
                    return;
                }
                showDefectInfoByResult(result);
            }
        });
    }

    /**
     * 添加疵点
     * 编辑疵点
     */
    private void addOrEditDefect() {
        if (tvAddBlemish.isSelected())//新增疵点
        {
            addClothDefect();
        } else if (tvDefectInfo.isSelected()) {
            tabRightUiHandler(false, false, false, false, false, true,false);
        } else if (tvEditDefect.isSelected()) {
            editClothDefect();
        }
    }

    /**
     * 新增布匹疵点
     * 新增
     */
    private void addClothDefect() {
        if (mClothInfoBean == null) {
            showToast("请先添加布匹信息,将清空选择项");
            getDefectDetailList();
            return;
        }
        if (paramBeanList == null || paramBeanList.size() == 0) {
            showToast("请选择疵点");
            return;
        }
        paramObjectList = new ArrayList<>();
        for (final DefectParamBean paramBean : paramBeanList) {
            defectCount++;
            Map<String, Object> paramMap = new HashMap<>();
            Integer clothId = mClothIdBean.id;
            final Integer defectId = paramBean.defectId;
            defectList.add(defectId);
            String remark = paramBean.remark;
            double location = paramBean.location;
            if(location <=(1e-6) || location>=(1e6)){
                showToast("请确定长度信息");
                return;
            }
            List<DefectParamBean.ExtraParamListBean> extraParamList = paramBean.extraParamList;
            paramMap.put("clothId", clothId);
            paramMap.put("defectId", defectId);
            paramMap.put("remark", remark);
            paramMap.put("location", location);
            if (extraParamList != null && extraParamList.size() > 0) {
                List<Object> objectList = new ArrayList<>();
                for (DefectParamBean.ExtraParamListBean listBean : extraParamList) {
                    Map<String, Object> extraMap = new HashMap<>();
                    extraMap.put("id", listBean.id);
                    extraMap.put("value", listBean.value);
                    objectList.add(extraMap);
                }
                paramMap.put("extraParamList", objectList);
            } else {
                paramMap.put("extraParamList", extraParamList);
            }

            paramObjectList.add(paramMap);
        }
        mWorkService.addNewDefect(paramObjectList, new CommonResultListener<JSONObject>() {
            @Override
            public void successHandle(JSONObject result) {
                if (flagDefectAddOrEdit == 0) {
                    showToast("添加疵点成功");
                    loadWvChartData(paramBeanList);

                    defectCount(defectCount);

                    perchingStatus = 3;
                    setTvPerchingStatus(3);

                    tabLeftUiHandler(false);
                    getDefectList();

                    beanList = getBeanListByResult(beanList);
                    addBlemishTypeListAdapter.replaceAll(beanList);
                    defectListBeanList = new ArrayList<>();
                    defectDescribeAdapter.replaceAll(defectListBeanList);
                    hideOrShowDefectDesBySize(0);
                    uploadDefectList(machineNoList, defectList);
                    paramBeanList = new ArrayList<>();
                    getDefectList();
                }
            }
        });
    }

    private void uploadDefectList(List<Integer> list, List<Integer> list1) {
        mWorkService.addDefectRecord(list, list1, new CommonResultListener<JSONObject>() {
            @Override
            public void successHandle(JSONObject result) {
                machineNoList = new ArrayList<>();
                defectList = new ArrayList<>();
            }
        });
    }

    private void loadWvChartData(List<DefectParamBean> paramBeanList) {
        for (DefectParamBean paramBean : paramBeanList) {
            int score = paramBean.score;
            String color = paramBean.color;
            String url = "http://118.178.182.6:8028/fchart/point.html?defectScore=" + score + "&defectColor=" + color + "&cleanFlag=0";
            String mimeType = "text/html";
            String enCoding = "utf-8";
//            wvChart.loadDataWithBaseURL(null, url, mimeType, enCoding, null);
            wvChart.loadUrl(url);
        }
    }

    /**
     * 设置疵点个数
     *
     * @param defectCount 瑕点个数
     */
    @SuppressLint("SetTextI18n")
    public void defectCount(int defectCount) {
        tvDefectCount.setText("瑕点：" + defectCount + "个");
    }

    /**
     * 编辑布匹疵点
     * 更新
     */
    private void editClothDefect() {
        for (final DefectParamBean paramBean : paramBeanList) {
            int clothId = paramBean.clothId;
            int defectId = paramBean.defectId;
            String remark = paramBean.remark;
            double location = paramBean.location;
            List<DefectParamBean.ExtraParamListBean> extraParamList = paramBean.extraParamList;
            mWorkService.updateDefect(clothId, defectId, remark, location, extraParamList, new CommonResultListener<JSONObject>() {
                @Override
                public void successHandle(JSONObject result) {
                    showToast("更新疵点信息成功");
                    tabRightUiHandler(true, false, false, false, false, false,false);
                    showAi();
                    blemishInfoListAdapter.clear();
                    defectDescribeAdapter.clear();
                    getDefectList();
                    getDefectDetailList();
                }
            });
        }
    }


    /**
     * 全屏或者退出全屏
     */
    private void enterOrExitFullScreen() {
        if (!ivFullScreen.isSelected()) {
            enterFullScreen();
        } else {
            exitFullScreen();
        }

    }

    /**
     * 全屏
     */
    private void enterFullScreen() {
        ivFullScreen.setSelected(true);
        llTabLeft.setVisibility(View.GONE);
        llTabRight.setVisibility(View.GONE);
    }

    /**
     * 退出全屏
     */
    private void exitFullScreen() {
        ivFullScreen.setSelected(false);
        llTabLeft.setVisibility(View.VISIBLE);
        llTabRight.setVisibility(View.VISIBLE);
    }


    private class MyCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    private void  prepareSaveUdp(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        AppConfig.getInstance().setUdpServerIp(sharedPreferences.getString("udpIp",""));
        AppConfig.getInstance().setUdpServerPort(Integer.valueOf(sharedPreferences.getString("udpPort","0")));

    }

    /**
     * 设置UDP通信协议的IP地址和端口号
     */
    private void setUdpIpAndPort(){
        String strUdpIP = etUdpIp.getText().toString();
        String strUdpPort = etUdpPort.getText().toString();


        if("".equals(strUdpIP) || "".equals(strUdpPort) ){
            showToast("请确定IP地址和端口号");
        }else {
            editor.putString("udpIp",strUdpIP);
            editor.putString("udpPort",strUdpPort);
            editor.apply();
            AppConfig.getInstance().setUdpServerIp(sharedPreferences.getString("udpIp",""));
            AppConfig.getInstance().setUdpServerPort(Integer.valueOf(sharedPreferences.getString("udpPort","")));
        }

    }
}
