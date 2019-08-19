package com.hs.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.hs.R;
import com.hs.base.GlobalInfo;
import com.hs.base.LoadingAction;
import com.hs.base.MyActivityManager;
import com.hs.base.Viewable;
import com.hs.common.constant.SharedKeyConstant;
import com.hs.common.intent.IntentStartActivityHelper;
import com.hs.common.util.MySharedPreference;
import com.hs.common.util.ToastUtils;

import java.util.Map;

import butterknife.ButterKnife;


/**
 * Created by jill on 2016/11/8.
 */

public abstract class BaseActivity extends AppCompatActivity implements Viewable {

    private LoadingAction loadingAction;

    protected ImmersionBar mImmersionBar;

    protected long exitTime = 0L;

    public BaseActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        loadingAction = new LoadingAction(this);
        ButterKnife.bind(this);
        initImmersionBar();
        initView();
        initListener();
        initData();
        MyActivityManager.getInstance().addActivity(this);
    }

    protected abstract int getLayoutId();

    protected int getStatusBarColor() {
        return Color.parseColor("#FFFFFF");
    }

    protected void initImmersionBar() {
        boolean darkFont = true;
        if (getStatusBarColor() != R.color.white) {
            darkFont = false;
        }
        mImmersionBar = ImmersionBar
                .with(this)
                .statusBarColor(getStatusBarColor())
                .fitsSystemWindows(fitSystemWindow())
                .statusBarDarkFont(darkFont)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR);
        beforeImmersionBarInit();
        mImmersionBar.init();   //所有子类都将继承这些相同的属性
    }

    protected boolean fitSystemWindow() {
        return true;
    }

    protected void beforeImmersionBarInit() {

    }

    /**
     * 初始化视图
     */
    protected void initView() {

    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 初始化监听
     */
    protected void initListener() {
    }

    /**
     * 显示提示
     *
     * @param message
     */
    public void showToast(String message) {
        ToastUtils.showCenter(this, message);
    }

    public void startActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        this.startActivity(intent);
    }

    public void startActivity(Class clazz, Map<String, String> map) {
        Intent intent = new Intent(this, clazz);
        if (map != null) {
            for (String key : map.keySet()) {
                intent.putExtra(key, map.get(key));
            }
        }
        this.startActivity(intent);
    }

    public void startActivityForResult(Class clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        super.startActivityForResult(intent, requestCode);
    }

    /**
     * 等待提示框
     */
    public void addLoading() {
        loadingAction.add();
    }

    /**
     * 等待框消失
     */
    public void removeLoading() {
        loadingAction.remove();
    }

    public String getIntentValue(String key) {
        return getIntent().getStringExtra(key);
    }

    public void refresh() {
        initView();
        initListener();
        initData();
    }

    protected double getDoubleTextValue(TextView textView) {
        if (textView == null)
            return 0.0;
        try {
            return Double.parseDouble(textView.getText().toString());
        } catch (Exception e) {
            return 0.0;
        }
    }

    protected long getLongTextValue(TextView textView) {
        return (long) getDoubleTextValue(textView);
    }

    protected int getIntValue(String value) {
        if (value == null)
            return 0;
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    protected double getDoubleValue(String value) {
        if (value == null)
            return 0;
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0;
        }
    }

    protected String getTextTrim(EditText et) {
        return et.getText().toString().trim();
    }

    public BaseActivity getTargetActivity() {
        return this;
    }

    protected boolean isLogin() {
        String userToken = GlobalInfo.userToken;
        return userToken != null && userToken.length() > 0;
    }

    protected String getToken() {
        String token = MySharedPreference.get(SharedKeyConstant.TOKEN, null, this);

        return token;
    }

    protected int getScreenWidth() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getWidth();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mImmersionBar != null) {
            mImmersionBar.destroy();  //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        }

        MyActivityManager.getInstance().finishActivity(this);
    }

//    public void onBackPressed() {
//        if ((System.currentTimeMillis() - exitTime) > 2000) {
//            showToast("如果确定要退出当前页面请重按一次");
//            exitTime = System.currentTimeMillis();
//        } else {
//            ToastUtils.hide();
//            GlobalInfo.userToken="";
//            MySharedPreference.save(SharedKeyConstant.TOKEN,"",this);
//            GlobalInfo.userIdentity=null;
//            MySharedPreference.save(SharedKeyConstant.USER_IDENTITY, String.valueOf(GlobalInfo.userIdentity), this);
//            IntentStartActivityHelper.startActivityClearAll(this, LoginActivity.class);
//        }
//
//    }

}
