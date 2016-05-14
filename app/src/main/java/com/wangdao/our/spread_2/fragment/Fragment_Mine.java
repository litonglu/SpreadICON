package com.wangdao.our.spread_2.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.activity_.AboutInfo;
import com.wangdao.our.spread_2.activity_.LoginActivity;
import com.wangdao.our.spread_2.activity_.MessageA;
import com.wangdao.our.spread_2.activity_.TellWe;
import com.wangdao.our.spread_2.activity_.mine_activity.BecomeAgency;
import com.wangdao.our.spread_2.activity_.mine_activity.GetMoney;
import com.wangdao.our.spread_2.activity_.mine_activity.MyCommission;
import com.wangdao.our.spread_2.activity_.mine_activity.MyTeam;
import com.wangdao.our.spread_2.activity_.mine_activity.Popularize;
import com.wangdao.our.spread_2.widget_pull.BadgeView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Administrator on 2016/4/21 0021.
 */


public class Fragment_Mine extends Fragment implements View.OnClickListener{

    private View myView;
    private LayoutInflater myInflater;
    private Context myContext;
    private ImageView iv_icon;
    private LinearLayout ll_aboutinfo;
    private String myNickName;
    private FmHandler fh_handler = new FmHandler();
    private TextView tv_nickName;
    private LinearLayout rl_tellme;
    private NetBroadcast netBroadcast;
    private IntentFilter intentFilter;
    private LinearLayout ll_beAgency,ll_popularize,ll_myCommission,ll_myTeam,ll_getMoney;
    private ImageView iv_order;
    private BadgeView bv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_mine,null);
        myInflater = inflater;
        myContext = this.getActivity();
        initView();
        initOnClick();

        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netBroadcast = new NetBroadcast();
        myContext.registerReceiver(netBroadcast, intentFilter);

         bv = new BadgeView(myContext,iv_order);
        bv.setText("");
        bv.setTextColor(getResources().getColor(R.color.colorPrimary));
        bv.setTextSize(10);
        bv.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);

        bv.show();
        initTag();
        return myView;
    }

    private void initView(){
        ll_beAgency = (LinearLayout) myView.findViewById(R.id.fragment_mine_ll_agency);
        ll_popularize = (LinearLayout) myView.findViewById(R.id.fragment_mine_ll_tuiguang);
        ll_myCommission = (LinearLayout) myView.findViewById(R.id.fragment_mine_ll_mycommission);
        ll_myTeam = (LinearLayout) myView.findViewById(R.id.fragment_mine_ll_myteam);
        ll_getMoney = (LinearLayout) myView.findViewById(R.id.fragment_mine_ll_getmoney);
        iv_order = (ImageView) myView.findViewById(R.id.fragment_mine_iv_order);

        iv_icon = (ImageView) myView.findViewById(R.id.fragment_mine_iv_icon);
        ll_aboutinfo= (LinearLayout) myView.findViewById(R.id.fragment_mine_ll_aboutinfo);
        tv_nickName = (TextView) myView.findViewById(R.id.fragment_mine_tv_nickname);
        rl_tellme = (LinearLayout) myView.findViewById(R.id.fragment_mint_rl_tellme);

    }
    private class NetBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            ConnectivityManager connectionManager =
                    (ConnectivityManager) myContext.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo netinfo = connectionManager.getActiveNetworkInfo();
            if(netinfo!=null&&netinfo.isAvailable()){
                initIcon();
            }else{
            }
        }
    }

private void initOnClick(){
    iv_icon.setOnClickListener(this);
    ll_aboutinfo.setOnClickListener(this);
    rl_tellme.setOnClickListener(this);

    ll_beAgency.setOnClickListener(this);
    ll_popularize.setOnClickListener(this);
    ll_myCommission.setOnClickListener(this);
    ll_myTeam.setOnClickListener(this);
    ll_getMoney.setOnClickListener(this);
    iv_order.setOnClickListener(this);
    //ll_message.setOnClickListener(this);
}

    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.fragment_mine_iv_icon:
    case R.id.fragment_mine_ll_aboutinfo:
        Intent aIntent = new Intent(myContext, AboutInfo.class);
        startActivityForResult(aIntent, 1);
        break;
    case R.id.fragment_mint_rl_tellme:
        Intent tellIntent = new Intent(myContext, TellWe.class);
        startActivity(tellIntent);
        break;

    //成为代理
    case R.id.fragment_mine_ll_agency:
        Intent agencyIntent = new Intent(myContext, BecomeAgency.class);
        startActivity(agencyIntent);
        break;
    //推广
    case R.id.fragment_mine_ll_tuiguang:
        Intent tuiIntent = new Intent(myContext, Popularize.class);
        startActivity(tuiIntent);
        break;
    //我的佣金
    case R.id.fragment_mine_ll_mycommission:
        Intent commissionIntent = new Intent(myContext, MyCommission.class);
        startActivity(commissionIntent);
        break;
    //我的团队
    case R.id.fragment_mine_ll_myteam:
        Intent teamIntent = new Intent(myContext, MyTeam.class);
        startActivity(teamIntent);
        break;
    //提现
    case R.id.fragment_mine_ll_getmoney:
        Intent getMoneyIntent = new Intent(myContext, GetMoney.class);
        startActivity(getMoneyIntent);
        break;
    //消息
    case R.id.fragment_mine_iv_order:
        Intent mgIntent = new Intent(myContext,MessageA.class);
        startActivityForResult(mgIntent,1);
        break;

}
    }

    /**
     * 初始化标签
     */
    private void initTag(){


        SharedPreferences sharedPreferences_tag = this.getActivity().getSharedPreferences("tag", myContext.MODE_PRIVATE);
        final String Tag = sharedPreferences_tag.getString("tg", "");
        if(Tag.equals("1")){
            bv.hide();
        }else{
            bv.show();
        }

    }
    /**
     * 初始化头像
     */
    private Bitmap myIcon;
    private void initIcon(){

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("user", myContext.MODE_PRIVATE);
        final String url = sharedPreferences.getString("avatar256", "");
        myNickName =  sharedPreferences.getString("nickname","");
        tv_nickName.setText(myNickName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    myIcon = getImage(url);
                    Log.i("sssss","=============="+url);
                    fh_handler.sendEmptyMessage(1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 获取指定路径的图片
    public static Bitmap getImage(String urlpath) throws IOException {
        URL url = new URL(urlpath);
        InputStream is=url.openStream();
        return  BitmapFactory.decodeStream(is);
    }

    class FmHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if(myIcon != null){
                        iv_icon.setImageBitmap(toRoundBitmap(myIcon));
                    }
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initIcon();

        switch (resultCode){
            case 66:
                this.getActivity().finish();
                break;
            case 1:
                bv.hide();
                break;
        }
    }

    public static  Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            left = 0;
            bottom = width;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);


        //鐢荤櫧鑹插渾鍦�
        paint.reset();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0);
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, width / 2, width / 2 - 4 / 2, paint);
        return output ;
    }
}
