package com.wangdao.our.spread_2.activity_.mine_activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.fragment_myteam.MyTeam_1;
import com.wangdao.our.spread_2.fragment_myteam.MyTeam_2;
import com.wangdao.our.spread_2.fragment_myteam.MyTeam_3;
import com.wangdao.our.spread_2.fragment_myteam.MyTeam_All;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/9 0009.
 */
public class MyTeam extends FragmentActivity implements View.OnClickListener{
    private ImageView iv_cancle;
    private TextView tv_all,tv_1,tv_2,tv_3;
    private TextView tv_currentType;
    private ViewPager vp_MyTeam;
    private FragmentManager myFM;
    private List<Fragment> list_fragmet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myteam);
        initView();
        initClick();
        myFM = getSupportFragmentManager();
        list_fragmet.add(new MyTeam_All());
        list_fragmet.add(new MyTeam_1());
        list_fragmet.add(new MyTeam_2());
        list_fragmet.add(new MyTeam_3());
        vp_MyTeam.setAdapter(new MyTeamAdapter_2(myFM));
        vp_MyTeam.setOffscreenPageLimit(4);

        vp_MyTeam.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    private void initView(){
        iv_cancle = (ImageView) findViewById(R.id.activity_myteam_iv_cancle);
        tv_all = (TextView) findViewById(R.id.activity_myteam_tv_1);
        tv_1 = (TextView) findViewById(R.id.activity_myteam_tv_2);
        tv_2 = (TextView) findViewById(R.id.activity_myteam_tv_3);
        tv_3 = (TextView) findViewById(R.id.activity_myteam_tv_4);
        vp_MyTeam = (ViewPager) findViewById(R.id.activity_myteam_vp);
    }
    private void initClick(){
        iv_cancle.setOnClickListener(this);
        tv_all.setOnClickListener(this);
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
    }

    /**
     * 当前页的属性改变
     * @param pos
     */
    private void Currentpage(int pos){
        switch (pos){
            case 0:
                tv_all.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
                break;
            case 1:
                tv_all.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_1.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
                break;
            case 2:

                tv_all.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_2.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
                break;
            case 3:

                tv_all.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_3.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_myteam_iv_cancle:
                finish();
                break;
            case R.id.activity_myteam_tv_1:
                Currentpage(0);
                vp_MyTeam.setCurrentItem(0);
                break;
            case R.id.activity_myteam_tv_2:
                Currentpage(1);
                vp_MyTeam.setCurrentItem(1);
                break;
            case R.id.activity_myteam_tv_3:
                Currentpage(2);
                vp_MyTeam.setCurrentItem(2);
                break;
            case R.id.activity_myteam_tv_4:
                Currentpage(3);
                vp_MyTeam.setCurrentItem(3);
                break;
        }
    }




    class MyTeamAdapter_2 extends FragmentPagerAdapter{
        public MyTeamAdapter_2(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list_fragmet.get(position);
        }

        @Override
        public int getCount() {
            return list_fragmet.size();
        }
    }

}
