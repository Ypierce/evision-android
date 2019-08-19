package com.hs.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.hs.R;
import com.hs.base.GlobalInfo;
import com.hs.bean.login.LoginInfoBean;
import com.hs.common.constant.SharedKeyConstant;
import com.hs.common.util.MySharedPreference;
import com.hs.service.LoginService;
import com.hs.service.listener.CommonResultListener;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_input_account)
    EditText etInputAccount;
    @BindView(R.id.et_input_password)
    EditText etInputPassword;
    @BindView(R.id.fl_root)
    FrameLayout flRoot;

    private LoginService mLoginService;
    private int mHeight = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
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

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        mLoginService = new LoginService(this);
        checkLogin();
    }


    private void checkLogin() {
        String token = MySharedPreference.get(SharedKeyConstant.TOKEN, null, this);
        if (token == null || token.length() == 0) {
            return;
        }
        GlobalInfo.userToken = token;
        String userIdentity = MySharedPreference.get(SharedKeyConstant.USER_IDENTITY, "", this);
//        skipToDifferentUiByResult(Integer.parseInt(userIdentity));
    }

    //填充数据
    protected void initData() {
        GlobalInfo.needGoLogin = false;
    }

    @OnClick(R.id.tv_login)
    public void onViewClicked() {
        submit();
    }

    private void submit() {
        String strJobNum = etInputAccount.getText().toString();
        String strPassword = etInputPassword.getText().toString();
        String strPerching = "ybj002";
        if (TextUtils.isEmpty(strJobNum)) {
            showToast("请输入账号");
            return;
        }
        if (TextUtils.isEmpty(strPassword)) {
            showToast("请输入密码");
            return;
        }

        mLoginService.login(strJobNum, strPassword, strPerching, new CommonResultListener<LoginInfoBean>() {
            @Override
            public void successHandle(LoginInfoBean result) throws JSONException {
                saveResult(result);
                int userIdentity = result.userType;
                skipToDifferentUiByResult(userIdentity);
            }
        });
    }

    private void saveResult(LoginInfoBean result) {
        if (result == null) {
            return;
        }
        String token = result.token;
        if (token == null || "".equals(token)) {
            return;
        }
        MySharedPreference.save(SharedKeyConstant.TOKEN, token, this);
        GlobalInfo.userToken = token;
        MySharedPreference.save(SharedKeyConstant.USER_IDENTITY, String.valueOf(result.userType), this);
        GlobalInfo.userIdentity = result.userType;
    }

    private void skipToDifferentUiByResult(int userIdentity) {
        if (userIdentity == 1) {
            skipToMain();
        } else if (userIdentity == 2) {
            skipToStatistician();
        }
    }

    private void skipToMain() {
        startActivity(NewMainActivity.class);
        finish();
    }

    private void skipToStatistician() {
        startActivity(StatisticianActivity.class);
        finish();
    }
}
