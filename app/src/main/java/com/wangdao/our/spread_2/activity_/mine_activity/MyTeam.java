package com.wangdao.our.spread_2.activity_.mine_activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wangdao.our.spread_2.R;

/**
 * Created by Administrator on 2016/5/9 0009.
 */
public class MyTeam extends Activity implements View.OnClickListener{
    private ImageView iv_cancle;
    private TextView tv_all,tv_1,tv_2,tv_3;
    private TextView tv_currentType;
    private ListView myTeam_lv;
    private MyTeamAdapter mtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myteam);
        initView();
        initClick();

        mtAdapter = new MyTeamAdapter();
        myTeam_lv.setAdapter(mtAdapter);
    }
    private void initView(){
        iv_cancle = (ImageView) findViewById(R.id.activity_myteam_iv_cancle);
        tv_all = (TextView) findViewById(R.id.activity_myteam_tv_1);
        tv_1 = (TextView) findViewById(R.id.activity_myteam_tv_2);
        tv_2 = (TextView) findViewById(R.id.activity_myteam_tv_3);
        tv_3 = (TextView) findViewById(R.id.activity_myteam_tv_4);
        tv_currentType = (TextView) findViewById(R.id.activity_myteam_tv_current_type);
        myTeam_lv = (ListView) findViewById(R.id.activity_myteam_lv);
    }
    private void initClick(){
        iv_cancle.setOnClickListener(this);
        tv_all.setOnClickListener(this);
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_myteam_iv_cancle:
                finish();
                break;
            case R.id.activity_myteam_tv_1:
                tv_all.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_currentType.setText(R.string.myTeam_all);
                break;
            case R.id.activity_myteam_tv_2:
                tv_all.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_1.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_currentType.setText(R.string.myTeam_1);
                break;
            case R.id.activity_myteam_tv_3:
                tv_all.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_2.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_currentType.setText(R.string.myTeam_2);
                break;
            case R.id.activity_myteam_tv_4:
                tv_all.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_3.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_currentType.setText(R.string.myTeam_3);
                break;

        }
    }

    class MyTeamAdapter extends BaseAdapter{
        MyTeamHolder mtHoledr = null;

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView ==null){
                convertView = getLayoutInflater().inflate(R.layout.item_myteam,null);
                mtHoledr = new MyTeamHolder();
                mtHoledr.tIv_icon = (ImageView) convertView.findViewById(R.id.item_myteam_iv_icon);
                mtHoledr.tTv_name = (TextView) convertView.findViewById(R.id.item_myteam_tv_name);
                mtHoledr.tTv_time = (TextView) convertView.findViewById(R.id.item_myteam_tv_time);
                mtHoledr.tTv_time_lately = (TextView) convertView.findViewById(R.id.item_myteam_tv_time_lately);
                mtHoledr.tTv_member = (TextView) convertView.findViewById(R.id.item_myteam_member);
                convertView.setTag(mtHoledr);
            }else{
                mtHoledr = (MyTeamHolder) convertView.getTag();
            }



            return convertView;
        }
    }

    class MyTeamHolder{
        ImageView tIv_icon;
        TextView tTv_name;
        TextView tTv_time;
        TextView tTv_time_lately;
        TextView tTv_member;
    }

}
