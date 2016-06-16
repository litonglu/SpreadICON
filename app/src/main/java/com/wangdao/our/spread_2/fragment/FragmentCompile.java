package com.wangdao.our.spread_2.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.fragment_compile.Compile_fg_1;
import com.wangdao.our.spread_2.fragment_compile.Compile_fg_2;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_1;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_2;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_3;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_4;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_5;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_6;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_7;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_8;
import com.wangdao.our.spread_2.slide_widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/21 0021.
 */

public class FragmentCompile extends Fragment implements View.OnClickListener{

    private Context myContext;
    private View myView;
    private LayoutInflater myInflater;
    private TextView tv_recommend,tv_copy;

    private Compile_fg_1 fragment_compile_fg_1;
    private Compile_fg_2 fragment_compile_fg_2;

    FragmentManager fm_fCompile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_compile, null);
        myContext = this.getActivity();
        myInflater = inflater;

        fm_fCompile = this.getFragmentManager();

        tv_recommend = (TextView) myView.findViewById(R.id.fragment_compile_tv_recommend);
        tv_copy = (TextView) myView.findViewById(R.id.fragment_compile_tv_copy);
        tv_recommend.setOnClickListener(this);
        tv_copy.setOnClickListener(this);
        OnTabSelected(0);
        return myView;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_compile_tv_recommend:
                tv_recommend.setTextColor(getResources().getColor(R.color.colorPrimary));

                tv_recommend.setBackground(getResources().getDrawable(R.drawable.shap_left_no));
                tv_copy.setTextColor(getResources().getColor(R.color.white));

                tv_copy.setBackground(getResources().getDrawable(R.drawable.shap_right));

                OnTabSelected(0);
                break;
            case R.id.fragment_compile_tv_copy:
                tv_recommend.setTextColor(getResources().getColor(R.color.white));


                tv_recommend.setBackground(getResources().getDrawable(R.drawable.shap_left));
                tv_copy.setTextColor(getResources().getColor(R.color.colorPrimary));

                tv_copy.setBackground(getResources().getDrawable(R.drawable.shap_right_no));


                OnTabSelected(1);
                break;

        }
    }



    private void OnTabSelected(int index) {
        FragmentTransaction transaction = fm_fCompile.beginTransaction();
        switch (index) {
            case 0:
                hideFragments(transaction);
                if (fragment_compile_fg_1 == null) {
                    fragment_compile_fg_1 = new Compile_fg_1();
                    transaction.add(R.id.fragment_compile_fy, fragment_compile_fg_1);
                } else {
                    transaction.show(fragment_compile_fg_1);
                }
                break;
            case 1:
                hideFragments(transaction);
                if (fragment_compile_fg_2 == null) {
                    fragment_compile_fg_2 = new Compile_fg_2();
                    transaction.add(R.id.fragment_compile_fy, fragment_compile_fg_2);
                } else {
                    transaction.show(fragment_compile_fg_2);
                }
                break;
        }
        transaction.commit();
    }




    private void hideFragments(FragmentTransaction transaction) {

        if(fragment_compile_fg_1!=null){
            transaction.hide(fragment_compile_fg_1);
        }
        if(fragment_compile_fg_2!=null){
            transaction.hide(fragment_compile_fg_2);
        }
    }



}
