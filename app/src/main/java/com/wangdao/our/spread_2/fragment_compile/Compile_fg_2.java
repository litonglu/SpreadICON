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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
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


    private myHandler_1 myHandler_1 = new myHandler_1();

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


                            goCompile(clip.getText().toString());
                            startDialog();
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


    private String compile_result = "网络异常";
    private void goCompile(String myUrl_){

        httpPost = new HttpPost(allurl.getCopyUrl());
        SharedPreferences sharedPreferences = myContext.getSharedPreferences("user", myContext.MODE_PRIVATE);
        String mToken = sharedPreferences.getString("user_token", "");

        params.add(new BasicNameValuePair("user_token", mToken));
        params.add(new BasicNameValuePair("url", myUrl_ ));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        Log.i("qqqqqq","啊啊啊啊啊啊啊");
                        compile_result = jo.getString("info");

                        if (jo.getString("status").equals("1")) {

                            Log.i("qqqqqq",jo.toString());

                        //    myUrl = jo.getString("url");
                            copyId = jo.getString("id");
                            copyTitle = jo.getString("title");
                            copyIcon = jo.getString("writing_default_img");
                            copyContent = jo.getString("brief");

                            myHandler_1.sendEmptyMessage(1);



                        }else{

                            Log.i("qqqqq","错误");
                            myHandler_1.sendEmptyMessage(2);

                        }
                    }
                } catch (Exception e) {
                    myHandler_1.sendEmptyMessage(2);
                    Log.i("qqqqq",e.toString());
                    e.printStackTrace();
                }


            }
        }).start();

    }

    private final String myUrl_2 = "http://wz.ijiaque.com/app/article/articledetail.html";

    class myHandler_1 extends Handler{
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:

                    SharedPreferences sharedPreferences = myContext.getSharedPreferences("user", myContext.MODE_PRIVATE);
                    String auid = sharedPreferences.getString("uid", "");

                    Intent uIntent = new Intent(myContext, Article_info.class);
                    uIntent.putExtra("url",myUrl_2+"?writing_id="+copyId+"&uid="+auid);

                    uIntent.putExtra("uid",copyId);
                    uIntent.putExtra("title",copyTitle);
                    uIntent.putExtra("img",copyIcon);
                    uIntent.putExtra("content",copyContent);

                    Log.i("qqqqqqqqqq",myUrl_2+"?writing_id="+copyId+"&uid="+auid);

                    startActivity(uIntent);

                    dia_wait.dismiss();

                    break;
                case 2:
                    dia_wait.dismiss();
                    new AlertDialog.Builder(myContext)
                            .setTitle("提示")
                            .setMessage(compile_result)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
            }
        }
    }


    private Dialog dia_wait;
    private ImageView dialog_iv;
    private void startDialog(){
        View dialog_view = myInflater.inflate(R.layout.dialog_wait_2,null);
        dia_wait = new Dialog(myContext,R.style.dialog);
        dia_wait.setContentView(dialog_view);
        dialog_iv  = (ImageView) dialog_view.findViewById(R.id.dialog_wait_2_iv);
        Animation anim = AnimationUtils.loadAnimation(myContext, R.anim.dialog_zhuang);
        LinearInterpolator lir = new LinearInterpolator();
        anim.setInterpolator(lir);
        dialog_iv.startAnimation(anim);
        dia_wait.show();
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
    private String copyId;
    private String myUrl;
    private String copyTitle;
    private String copyIcon;
    private String copyContent;
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
                            copyId = jo.getString("id");
                            copyTitle = jo.getString("title");
                            copyIcon = jo.getString("writing_default_img");
                            copyContent = jo.getString("brief");
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
                    SharedPreferences sharedPreferences = myContext.getSharedPreferences("user", myContext.MODE_PRIVATE);
                    String auid = sharedPreferences.getString("uid", "");

                    Intent uIntent = new Intent(myContext, Article_info.class);
                    uIntent.putExtra("url",myUrl+"?writing_id="+copyId+"&uid="+auid);

                    uIntent.putExtra("uid",copyId);
                    uIntent.putExtra("title",copyTitle);
                    uIntent.putExtra("img",copyIcon);
                    uIntent.putExtra("content",copyContent);

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
