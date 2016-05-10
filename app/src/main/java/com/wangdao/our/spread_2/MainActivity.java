package com.wangdao.our.spread_2;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangdao.our.spread_2.fragment.FragmentCompile;
import com.wangdao.our.spread_2.fragment.FragmentMaterial;
import com.wangdao.our.spread_2.fragment.FragmentStatistics;
import com.wangdao.our.spread_2.fragment.Fragment_Mine;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    private LinearLayout ll_statistics,ll_compile,ll_material,ll_mine;
    private FragmentManager fragmentManager;
    private FragmentStatistics f_statistics;
    private FragmentCompile f_compile;
    private FragmentMaterial f_material;
    private Fragment_Mine f_mine;
    private TextView tv_1,tv_2,tv_3,tv_4;
    private ImageView iv_1,iv_2,iv_3,iv_4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState!=null){
            Log.i("qqqqq","fragment被回收");
        }else {
            Log.i("qqqqq","null");
            super.onCreate(savedInstanceState);
        }
        setContentView(R.layout.activity_main);
        initView();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);

        if(f_statistics ==null) {
            f_statistics = new FragmentStatistics();
            transaction.add(R.id.activity_main_fy, f_statistics);
        }else{
            transaction.show(f_statistics);
        }
        transaction.commit();
    }
            private void initView(){
                ll_statistics = (LinearLayout) findViewById(R.id.activity_main_ll_statistics);
                ll_compile = (LinearLayout) findViewById(R.id.activity_main_ll_compile);
                ll_material = (LinearLayout) findViewById(R.id.activity_main_ll_material);
                ll_mine = (LinearLayout) findViewById(R.id.activity_main_ll_mine);

                tv_1 = (TextView) findViewById(R.id.activity_main_tv_1);
                tv_2 = (TextView) findViewById(R.id.activity_main_tv_2);
                tv_3 = (TextView) findViewById(R.id.activity_main_tv_3);
                tv_4 = (TextView) findViewById(R.id.activity_main_tv_4);

                iv_1 = (ImageView) findViewById(R.id.activity_main_iv_1);
                iv_2 = (ImageView) findViewById(R.id.activity_main_iv_2);
                iv_3 = (ImageView) findViewById(R.id.activity_main_iv_3);
                iv_4 = (ImageView) findViewById(R.id.activity_main_iv_4);
                ll_statistics.setOnClickListener(this);
                ll_compile.setOnClickListener(this);
                ll_material.setOnClickListener(this);
                ll_mine.setOnClickListener(this);
            }
    private void OnTabSelected(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (index) {
            case 0:
                hideFragments(transaction);
                if (f_statistics == null) {
                    f_statistics = new FragmentStatistics();
                    transaction.add(R.id.activity_main_fy, f_statistics);
                } else {
                    transaction.show(f_statistics);
                }
                break;
            case 1:
                hideFragments(transaction);
                if (f_compile == null) {
                    f_compile = new FragmentCompile();
                    transaction.add(R.id.activity_main_fy, f_compile);
                } else {
                    transaction.show(f_compile);
                }
                break;
            case 2:

                hideFragments(transaction);
                if (f_material == null) {
                    f_material = new FragmentMaterial();
                    transaction.add(R.id.activity_main_fy, f_material);
                } else {
                    transaction.show(f_material);
                }
                break;
            case 3:

                hideFragments(transaction);
                if (f_mine == null) {
                    f_mine = new Fragment_Mine();
                    transaction.add(R.id.activity_main_fy, f_mine);
                } else {
                    transaction.show(f_mine);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if(f_statistics!=null){
            transaction.hide(f_statistics);
        }
        if(f_compile!=null){
            transaction.hide(f_compile);
        } if(f_material!=null){
            transaction.hide(f_material);
        }if(f_mine!=null){
            transaction.hide(f_mine);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("qqqqq","重新走ReStart");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_ll_statistics:
                OnTabSelected(0);
                iv_1.setImageDrawable(getResources().getDrawable(R.drawable.tongji_2));
                iv_2.setImageDrawable(getResources().getDrawable(R.drawable.compile_edit_1));
                iv_3.setImageDrawable(getResources().getDrawable(R.drawable.sucai_1));
                iv_4.setImageDrawable(getResources().getDrawable(R.drawable.wode_1));

                tv_1.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_4.setTextColor(getResources().getColor(R.color.textcolor_hui));
                break;
            case R.id.activity_main_ll_compile:
                OnTabSelected(1);
                iv_1.setImageDrawable(getResources().getDrawable(R.drawable.tongji_1));
                iv_2.setImageDrawable(getResources().getDrawable(R.drawable.compile_edit_2));
                iv_3.setImageDrawable(getResources().getDrawable(R.drawable.sucai_1));
                iv_4.setImageDrawable(getResources().getDrawable(R.drawable.wode_1));

                tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_2.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_4.setTextColor(getResources().getColor(R.color.textcolor_hui));
                break;
            case R.id.activity_main_ll_material:
                OnTabSelected(2);
                iv_1.setImageDrawable(getResources().getDrawable(R.drawable.tongji_1));
                iv_2.setImageDrawable(getResources().getDrawable(R.drawable.compile_edit_1));
                iv_3.setImageDrawable(getResources().getDrawable(R.drawable.sucai_2));
                iv_4.setImageDrawable(getResources().getDrawable(R.drawable.wode_1));
                tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_3.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_4.setTextColor(getResources().getColor(R.color.textcolor_hui));

                break;
            case R.id.activity_main_ll_mine:
                OnTabSelected(3);
                iv_1.setImageDrawable(getResources().getDrawable(R.drawable.tongji_1));
                iv_2.setImageDrawable(getResources().getDrawable(R.drawable.compile_edit_1));
                iv_3.setImageDrawable(getResources().getDrawable(R.drawable.sucai_1));
                iv_4.setImageDrawable(getResources().getDrawable(R.drawable.wode_2));

                tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_4.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

}