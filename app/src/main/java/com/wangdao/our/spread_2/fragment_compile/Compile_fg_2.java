package com.wangdao.our.spread_2.fragment_compile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.wangdao.our.spread_2.MainActivity;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.activity_.Article_info;
import com.wangdao.our.spread_2.activity_.LoginActivity;
import com.wangdao.our.spread_2.slide_widget.AllUrl;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/21 0021.
 */
public class Compile_fg_2 extends Fragment implements View.OnClickListener{

    private View myView;
    private LayoutInflater myInflater;
    private Context myContext;
    private Button bt_clip;
    private AllUrl allurl = new AllUrl();
    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private Compile2Handler comHandler = new Compile2Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.compile_fragment_2,null);
        myInflater = inflater;
        myContext = this.getActivity();

        bt_clip = (Button) myView.findViewById(R.id.compile_fragment_2_bt_clip);
        bt_clip.setOnClickListener(this);
        return myView;
    }
    private ClipboardManager clip;
    private void getC(){
         clip = (ClipboardManager)myContext.getSystemService(Context.CLIPBOARD_SERVICE);
        if(!clip.hasText()){
            new AlertDialog.Builder(myContext)
                    .setTitle("提示")
                    .setMessage("粘贴板无信息")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }else {
            new AlertDialog.Builder(myContext)
                    .setTitle("扫描到的文章链接")
                    .setMessage(clip.getText().toString())
                    .setCancelable(false)
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent uIntent = new Intent(myContext, Article_info.class);
                            uIntent.putExtra("url",clip.getText().toString());
                            uIntent.putExtra("uid","zhan");
                            uIntent.putExtra("title","zhan");
                            startActivity(uIntent);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.compile_fragment_2_bt_clip:
        SharedPreferences sharedPreferences = myContext.getSharedPreferences("user", myContext.MODE_PRIVATE);
        String isVip = sharedPreferences.getString("isvip", "");
        if(isVip.equals("0")){
            new AlertDialog.Builder(myContext)
                    .setTitle("提醒：")
                    .setMessage("非代理无权限使用此功能")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        }else {
            getC();
        }
        break;
}
    }
    /**
     * 发送链接
     */
    private String copyResult;
    private String myUrl;
    private void sendUrlTo(String wUrl){
        httpPost = new HttpPost(allurl.getCopyUrl());
        SharedPreferences sharedPreferences = myContext.getSharedPreferences("user", myContext.MODE_PRIVATE);
        String userToken = sharedPreferences.getString("user_token", "");
        params.add(new BasicNameValuePair("user_token", userToken));
        params.add(new BasicNameValuePair("wechat_url", wUrl));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        copyResult = jo.getString("info");
                        if(jo.getString("status").equals("1")){
                            myUrl = jo.getString("url");
                            comHandler.sendEmptyMessage(1);
                        }else{
                            comHandler.sendEmptyMessage(2);
                        }
                    }
                } catch (Exception e) {
                    comHandler.sendEmptyMessage(2);
                    e.printStackTrace();
                }
            }
        }).start();

    }

    class Compile2Handler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //抓取成功
                case 1:

                    Intent uIntent = new Intent(myContext, Article_info.class);
                    uIntent.putExtra("url",myUrl);
                    uIntent.putExtra("uid","0");
                    uIntent.putExtra("title","0");
                    startActivity(uIntent);

                    break;
                //抓取失败
                case 2:
                    Toast.makeText(myContext,copyResult,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
