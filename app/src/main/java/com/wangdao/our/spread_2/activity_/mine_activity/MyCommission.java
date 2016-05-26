package com.wangdao.our.spread_2.activity_.mine_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.fragment_mymoney.Fragment_MyMoney_1;
import com.wangdao.our.spread_2.fragment_mymoney.Fragment_MyMoney_2;
import com.wangdao.our.spread_2.fragment_mymoney.Fragment_MyMoney_3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/9 0009.
 * 我的佣金
 */
public class MyCommission extends FragmentActivity implements View.OnClickListener{
    private ImageView iv_cancle;
    private ViewPager vp_myCommission;
    private List<Fragment> list_fragment = new ArrayList<>();
    private FragmentManager myFM;
    private TextView tv_1,tv_2,tv_3;
    private View v_1,v_2,v_3;

    private Fragment_MyMoney_1 fm_1 = new Fragment_MyMoney_1();
    private Fragment_MyMoney_2 fm_2 = new Fragment_MyMoney_2();
    private Fragment_MyMoney_3 fm_3 = new Fragment_MyMoney_3();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymoney);
        initView();
        initClick();
        myFM = getSupportFragmentManager();

        list_fragment.add(fm_1);
        list_fragment.add(fm_2);
        list_fragment.add(fm_3);
        vp_myCommission.setAdapter(new MyCommissionAdapter(myFM));
        vp_myCommission.setOffscreenPageLimit(3);
        vp_myCommission.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                Currentpage(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void Currentpage(int position){
        switch (position){
            case 0:
                tv_1.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
                v_1.setVisibility(View.VISIBLE);
                v_2.setVisibility(View.INVISIBLE);
                v_3.setVisibility(View.INVISIBLE);
                break;
            case 1:
                tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_2.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
                v_1.setVisibility(View.INVISIBLE);
                v_2.setVisibility(View.VISIBLE);
                v_3.setVisibility(View.INVISIBLE);
                break;

            case 2:
                tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_3.setTextColor(getResources().getColor(R.color.colorPrimary));
                v_1.setVisibility(View.INVISIBLE);
                v_2.setVisibility(View.INVISIBLE);
                v_3.setVisibility(View.VISIBLE);

                break;
        }

    }
    private void initView(){
        iv_cancle = (ImageView) findViewById(R.id.activity_mymoney_iv_cancle);
        vp_myCommission = (ViewPager) findViewById(R.id.activity_mymoney_vp);

        tv_1 = (TextView) findViewById(R.id.activity_mymoney_tv_1);
        tv_2 = (TextView) findViewById(R.id.activity_mymoney_tv_2);
        tv_3 = (TextView) findViewById(R.id.activity_mymoney_tv_3);

        v_1 = findViewById(R.id.activity_mymoney_line_1);
        v_2 = findViewById(R.id.activity_mymoney_line_2);
        v_3 = findViewById(R.id.activity_mymoney_line_3);
    }
    private void initClick(){
        iv_cancle.setOnClickListener(this);
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_mymoney_iv_cancle:
                finish();
                break;
            case R.id.activity_mymoney_tv_1:
                Currentpage(0);
                vp_myCommission.setCurrentItem(0);
                break;
            case R.id.activity_mymoney_tv_2:
                Currentpage(1);
                vp_myCommission.setCurrentItem(1);
                break;
            case R.id.activity_mymoney_tv_3:
                Currentpage(2);
                vp_myCommission.setCurrentItem(2);
                break;
        }
    }
    class MyCommissionAdapter extends FragmentPagerAdapter{

        public MyCommissionAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return list_fragment.get(position);
        }

        @Override
        public int getCount() {
            return list_fragment.size();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fm_2.initData();
        fm_3.initData();
    }
}
