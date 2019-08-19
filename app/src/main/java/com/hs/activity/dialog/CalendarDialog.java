package com.hs.activity.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.hs.R;
import com.hs.common.util.Tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @Author: ljf
 * 时间:2019/6/10
 * 简述:<功能简述>
 */
public class CalendarDialog extends BaseDialogFragment implements CalendarView.OnDateChangeListener {
    @BindView(R.id.clv_date)
    CalendarView clvDate;
    private int year;
    private int month;
    private int day;
    private YearMonthDayListener yearMonthDayListener;

    public void setYearMonthDayListener(YearMonthDayListener yearMonthDayListener) {
        this.yearMonthDayListener = yearMonthDayListener;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_calendar;
    }


    @Override
    public boolean isDismissTouchOutSide() {
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setLayout(Tools.dip2px(mActivity, 300)
                , Tools.dip2px(mActivity, 300));
    }


    @Override
    protected void initView() {
        super.initView();
        long longDate = clvDate.getDate();
        String strDate = longToDate(longDate);
        String[] strArrayDate = strDate.split("-");
        this.year= Integer.parseInt(strArrayDate[0]);
        this.month= Integer.parseInt(strArrayDate[1]);
        this.day= Integer.parseInt(strArrayDate[2]);
    }

    @Override
    protected void setListener() {
        super.setListener();
        clvDate.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        if (yearMonthDayListener==null){
            return;
        }
        dismiss();
        yearMonthDayListener.yearMonthDay(year,month+1,day);
    }


    @OnClick({R.id.tv_cancel, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_sure:
                if (yearMonthDayListener==null){
                    return;
                }
                yearMonthDayListener.yearMonthDay(year,month,day);
                break;
        }
    }

    public static String longToDate(long lo) {
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return sd.format(date);
    }

    public interface YearMonthDayListener {
        void yearMonthDay(int year, int month, int day);
    }
}
