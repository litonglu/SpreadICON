package com.wangdao.our.spread_2;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wangdao.our.spread_2.activity_.myZidingyi;
import com.wangdao.our.spread_2.fragment.FragmentCompile;
import com.wangdao.our.spread_2.fragment.FragmentMaterial;
import com.wangdao.our.spread_2.fragment.FragmentStatistics;
import com.wangdao.our.spread_2.fragment.Fragment_Mine;
import com.wangdao.our.spread_2.slide_widget.AllUrl;
import com.wangdao.our.spread_2.slide_widget.widget_push.ExampleUtil;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    private LinearLayout ll_statistics,ll_compile,ll_material,ll_mine;
    private FragmentManager fragmentManager;
    private FragmentStatistics f_statistics;
    private FragmentCompile f_compile;
    private FragmentMaterial f_material;
    private Fragment_Mine f_mine;
    private TextView tv_1,tv_2,tv_3,tv_4;
    private ImageView iv_1,iv_2,iv_3,iv_4;
    private NaviMainHandler naviMainHandler = new NaviMainHandler();
    public static boolean isForeground = false;

    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();

    /**
     * 自动更新链接
     */
    private  String apkUrl = "";

    // 文件分隔符
    private static final String FILE_SEPARATOR = "/";
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + FILE_SEPARATOR +"autoupdate" + FILE_SEPARATOR;
    // 下载应用存放全路径
    private static final String FILE_NAME = FILE_PATH + "autoupdate.apk";
    private int versionCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);

        if(f_compile ==null) {
            f_compile = new FragmentCompile();
            transaction.add(R.id.activity_main_fy, f_compile);
        }else{
            transaction.show(f_compile);
        }
        transaction.commit();

        registerMessageReceiver();  // used for receive msg




        try {
            versionCode = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
            initUpdate(versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * 检查更新
     */
    private void initUpdate(int versionCode_){
        httpPost = new HttpPost(allurl.getAutoUpdate());
        params.add(new BasicNameValuePair("version", versionCode_+""));

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);

                        if(jo.getString("status").equals("1")){
                            apkUrl = jo.getString("link");
                            naviMainHandler.sendEmptyMessage(11);
                        }else{
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();

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





    /**
     * 提示用户更新
     */
    private void TiShiUpdate(){
        new AlertDialog.Builder(this)
                .setTitle("更新提醒:")
                .setMessage("《拇指营销》有新版本，请更新。")
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogUpdateIng();
                        downloadApp();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 更新中
     */
    private AlertDialog mydialoig;
    private ProgressBar dialog_pb ;
    private TextView tv_progress_num;

    private void dialogUpdateIng(){
        View view_progress = getLayoutInflater().inflate(R.layout.dialog_progressbar,null);
        dialog_pb = (ProgressBar) view_progress.findViewById(R.id.dialog_progress_pb);
        tv_progress_num = (TextView) view_progress.findViewById(R.id.dialog_progressbar_tv_num);

        dialog_pb.setMax(100);
        mydialoig = new AlertDialog.Builder(this)
                .setTitle("更新中...")
                .setView(view_progress)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private boolean dialogisClose(){

        if(mydialoig.isShowing()){
            return true;
        }
        return false;

    }

    /**
     * 下载新版本应用
     */

    private int cuP = 0;
    private void downloadApp() {
        new Thread(new Runnable() {
        @Override
        public void run() {
            URL url = null;
            InputStream in = null;
            FileOutputStream out = null;
            HttpURLConnection conn = null;
            try {
                url = new URL(apkUrl);
                Log.i("qqqqqqq","指定URL");
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                long fileLength = conn.getContentLength();
                in = conn.getInputStream();
                File filePath = new File(FILE_PATH);
                if(!filePath.exists()) {
                    filePath.mkdir();
                    Log.i("qqqqqqq","创建文件夹");
                }
                Log.i("qqqqqqq","创建完成");
                out = new FileOutputStream(new File(FILE_NAME));
                Log.i("qqqqqq","=------=====1");
                byte[] buffer = new byte[1024];
                Log.i("qqqqqq","=------=====2");
                int len = 0;
                long readedLength = 0l;
                Log.i("qqqqqq","=------=====3");
                while((len = in.read(buffer)) != -1) {
                    Log.i("qqqqqqq","下载开始");
                    // 用户点击“取消”按钮，下载中断
                    if(!dialogisClose()) {
                        break;
                    }
                    out.write(buffer, 0, len);
                    readedLength += len;
                    cuP = (int) (((float) readedLength / fileLength) * 100);
                    naviMainHandler.sendEmptyMessage(2);
                    if(readedLength >= fileLength) {
                        Log.i("qqqqqqq","下载完成了");
                        naviMainHandler.sendEmptyMessage(1);
                        break;
                    }
                }
                out.flush();
            } catch (Exception e) {
                Log.i("qqqqqq","异常"+e);
                e.printStackTrace();
            } finally {
                if(out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(conn != null) {
                    conn.disconnect();
                }
            }
        }
    }).start();
}



    class NaviMainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    installApp();
                    mydialoig.dismiss();
                    break;
                case 2:
                    dialog_pb.setProgress(cuP);
                    tv_progress_num.setText(cuP+"%");
                    break;
                case 11:
                    TiShiUpdate();
                    break;
            }
            //冷冷的狗粮胡乱的往嘴里塞
        }
    }

    /**
     * 安装新版本应用
     */
    private void installApp() {
        File appFile = new File(FILE_NAME);
        if(!appFile.exists()) {
            return;
        }
        // 跳转到新版本应用安装页面
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
        this.startActivity(intent);
    }




    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_ll_statistics:
                OnTabSelected(1);
                iv_1.setImageDrawable(getResources().getDrawable(R.drawable.compile_edit_2));
                iv_2.setImageDrawable(getResources().getDrawable(R.drawable.sucai_1));
                iv_3.setImageDrawable(getResources().getDrawable(R.drawable.tongji_1));
                iv_4.setImageDrawable(getResources().getDrawable(R.drawable.wode_1));

                tv_1.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_4.setTextColor(getResources().getColor(R.color.textcolor_hui));
                break;
            case R.id.activity_main_ll_compile:
                OnTabSelected(2);

                iv_1.setImageDrawable(getResources().getDrawable(R.drawable.compile_edit_1));
                iv_2.setImageDrawable(getResources().getDrawable(R.drawable.sucai_2));
                iv_3.setImageDrawable(getResources().getDrawable(R.drawable.tongji_1));
                iv_4.setImageDrawable(getResources().getDrawable(R.drawable.wode_1));

                tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_2.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_4.setTextColor(getResources().getColor(R.color.textcolor_hui));
                break;
            case R.id.activity_main_ll_material:
                OnTabSelected(0);
                iv_1.setImageDrawable(getResources().getDrawable(R.drawable.compile_edit_1));
                iv_2.setImageDrawable(getResources().getDrawable(R.drawable.sucai_1));
                iv_3.setImageDrawable(getResources().getDrawable(R.drawable.tongji_2));
                iv_4.setImageDrawable(getResources().getDrawable(R.drawable.wode_1));

                tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_3.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_4.setTextColor(getResources().getColor(R.color.textcolor_hui));

                break;
            case R.id.activity_main_ll_mine:
                OnTabSelected(3);
                iv_1.setImageDrawable(getResources().getDrawable(R.drawable.compile_edit_1));
                iv_2.setImageDrawable(getResources().getDrawable(R.drawable.sucai_1));
                iv_3.setImageDrawable(getResources().getDrawable(R.drawable.tongji_1));
                iv_4.setImageDrawable(getResources().getDrawable(R.drawable.wode_2));

                tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
                tv_4.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init(){
        JPushInterface.init(getApplicationContext());
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        JPushInterface.onResume(this);
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
        JPushInterface.onPause(this);
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private MessageReceiver mMessageReceiver;
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }

    private void setCostomMsg(String msg){
        if (null != msg) {
            Log.i("qqqqq",msg);
        }
    }
}
