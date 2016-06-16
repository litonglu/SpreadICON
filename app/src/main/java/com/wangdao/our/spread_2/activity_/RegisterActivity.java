package com.wangdao.our.spread_2.activity_;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wangdao.our.spread_2.MainActivity;
import com.wangdao.our.spread_2.R;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class RegisterActivity extends Activity implements View.OnClickListener{
    private EditText et_tel,et_pwd,et_vf,et_nickname;
    private Button bt_send,bt_sendvf,bt_okr;
    private AllUrl allurl = new AllUrl();
    private String registerResult;
    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private String myTel,myPwd;
    private registerHandler rHandle = new registerHandler();
    private String dialog_vf;
    private ImageView iv_cancle;
    private String rMobile, rUid, rNickname, rAvatar64, rAvatar128, rAvatar256, rUser_token, rIsVip,rShareCount;
    private int miao = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        bt_okr.setOnClickListener(this);
        bt_send.setOnClickListener(this);
        bt_sendvf.setOnClickListener(this);
        iv_cancle.setOnClickListener(this);
    }


    private void initView(){
        et_tel = (EditText) findViewById(R.id.activity_register_et_tel);
        et_pwd = (EditText) findViewById(R.id.activity_register_et_pwd);
        et_vf = (EditText) findViewById(R.id.activity_register_et_vf);
        et_nickname = (EditText) findViewById(R.id.activity_register_et_nickname);
        bt_send = (Button) findViewById(R.id.activity_register_et_send);
        bt_okr = (Button) findViewById(R.id.activity_register_bt_okr);
        bt_sendvf = (Button) findViewById(R.id.activity_register_bt_sendvf);
        iv_cancle = (ImageView) findViewById(R.id.activity_register_iv_cancle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_register_iv_cancle:
                finish();
                break;
            //发送验证码
            case R.id.activity_register_et_send:
             //   showVfDialog();
                sendNote();
                break;
            case R.id.activity_register_bt_okr:
                register();
                break;
            //验证码dialog确定
            case R.id.dialog_logout_yes1:
                dialog_vf = et_vf_dialog.getText().toString();
                sendNote();
                materialDialog.dismiss();
                break;
            //验证码dialog取消
            case R.id.dialog_logout_no1:
                materialDialog.dismiss();
                break;
            //图形验证码
            case R.id.activity_register_bt_sendvf:
               // showVfDialog();
                break;
        }
    }

    private Dialog materialDialog;
    private View material_view;
    private TextView tv_cancle,tv_ok;
    private EditText et_vf_dialog;
    private WebView wb_dialog_vf;
    private ProgressBar pb_dialog;
    private void showVfDialog(){
        material_view = getLayoutInflater().inflate(R.layout.dialog_wb_vf,null);
        tv_cancle = (TextView) material_view.findViewById(R.id.dialog_logout_no1);
        tv_ok = (TextView) material_view.findViewById(R.id.dialog_logout_yes1);
        et_vf_dialog = (EditText) material_view.findViewById(R.id.dialog_wb_vf_et);
        wb_dialog_vf = (WebView) material_view.findViewById(R.id.dialog_wb_vf_wb);
        pb_dialog = (ProgressBar) material_view.findViewById(R.id.dialog_wb_pb);
        materialDialog = new Dialog(this,R.style.dialog);
        materialDialog.setContentView(material_view);
        pb_dialog.setVisibility(View.VISIBLE);
        materialDialog.show();
        wb_dialog_vf.getSettings().setJavaScriptEnabled(true);
//        WebSettings mWebSettings = wb_dialog_vf.getSettings();
//        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wb_dialog_vf.loadUrl(allurl.getVf_url());
        wb_dialog_vf.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb_dialog.setVisibility(View.GONE);
            }
        });
        tv_cancle.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
    }
    /**
     *  发送验证码
     */
    private String sendResult = "网络异常";
    private void sendNote(){
        httpPost = new HttpPost(allurl.getRegister_note());
        params.add(new BasicNameValuePair("mobile", et_tel.getText().toString()));
    //    params.add(new BasicNameValuePair("img_verify",dialog_vf));
   //     Log.i("qqqqq","输入的验证码是："+dialog_vf+"手机是"+et_tel.getText().toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        sendResult = jo.getString("info");

                        Log.i("qqqqq",jo.getString("status")+"-----"+sendResult);

                        if(jo.getString("status").equals("1")){
                            rHandle.sendEmptyMessage(11);
                        }else{
                            rHandle.sendEmptyMessage(12);
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

    private void register(){
            myTel = et_tel.getText().toString();
            myPwd = et_pwd.getText().toString();
            httpPost = new HttpPost(allurl.getRegister_url());

            params.add(new BasicNameValuePair("reg_verify", et_vf.getText().toString()));//手机验证

          //  params.add(new BasicNameValuePair("img_verify", dialog_vf));//图形验证码
          //  params.add(new BasicNameValuePair("invite_code", "1234"));//邀请码

            params.add(new BasicNameValuePair("mobile", myTel));
            params.add(new BasicNameValuePair("password", myPwd));
            params.add(new BasicNameValuePair("nickname", et_nickname.getText().toString()));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                        httpResponse = new DefaultHttpClient().execute(httpPost);
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                            String result = EntityUtils.toString(httpResponse.getEntity());
                            JSONObject jo = new JSONObject(result);
                            registerResult = jo.getString("info");
                            if(jo.getString("status").equals("1")){
                                rHandle.sendEmptyMessage(1);
                            }else{
                                rHandle.sendEmptyMessage(2);
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

    class registerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("RESULT:")
                            .setMessage(registerResult)
                            .setPositiveButton("登陆", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startLogin();

//                                    Intent register  = new Intent();
//                                    register.putExtra("tel", myTel);
//                                    register.putExtra("pwd", myPwd);
//                                    setResult(1, register);


                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                case 2:
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("RESULT:")
                            .setMessage(registerResult)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                //发送成功
                case 11:
                    miao = 60;
                    Toast.makeText(RegisterActivity.this,sendResult,Toast.LENGTH_SHORT).show();
                    bt_send.setClickable(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (true)
                                {
                                    Thread.sleep(1000);
                                    rHandle.sendEmptyMessage(31);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                //发送失败
                case 12:
                    Toast.makeText(RegisterActivity.this,sendResult,Toast.LENGTH_SHORT).show();
                    break;
                case 21:
                    initJPushTag();

                    Intent lIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(lIntent);
                    finish();
                    SharedPreferences sharedPreferences = RegisterActivity.this.getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("mobile", rMobile);
                    editor.putString("pwd", myPwd);
                    editor.putString("uid", rUid);
                    editor.putString("nickname", rNickname);
                    editor.putString("avatar64", rAvatar64);
                    editor.putString("avatar128", rAvatar128);
                    editor.putString("avatar256", rAvatar256);
                    editor.putString("user_token", rUser_token);
                    editor.putString("isvip", rIsVip);
                    editor.putString("shaecount", rShareCount);
                    editor.commit();
                    dia_wait.dismiss();
                    break;
                case 22:
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("提醒")
                            .setMessage(str_result)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    dia_wait.dismiss();
                    break;

                case 31:
                    if(miao == 0){
                        bt_send.setClickable(true);

                        bt_send.setText("获取验证码");
                        return;
                    }else if(miao > 0){

                        miao -= 1;
                        bt_send.setText(miao+"\t秒后重新发送");

                    }

                    break;
            }
        }
    }
    private String userIdTag;
    private void initJPushTag(){
        SharedPreferences sharedPreferences = RegisterActivity.this.getSharedPreferences("setTag", MODE_PRIVATE);
        String stat = sharedPreferences.getString("status", "");
        setAlias(userIdTag);
    }

    private void setAlias(String alias) {
        if (TextUtils.isEmpty(alias)) {
            Log.i("qqqqq","标签为空");
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            Log.i("qqqqq","标签无效");
            return;
        }

        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));

    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i("qqqqq", logs);
                    SharedPreferences sharedPreferences = RegisterActivity.this.getSharedPreferences("setTag", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("status", "1");
                    editor.commit();
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i("qqqqq", logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("qqqqq", logs);
            }
            //ExampleUtil.showToast(logs, getApplicationContext());
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d("qqqqq", "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i("qqqqq", "Unhandled msg - " + msg.what);
            }
        }
    };
    private Dialog dia_wait;
    private ImageView dialog_iv;
    private void startDialog(){

        View dialog_view = getLayoutInflater().inflate(R.layout.dialog_wait_2,null);
        dia_wait = new Dialog(this,R.style.dialog);
        dia_wait.setContentView(dialog_view);
        dialog_iv  = (ImageView) dialog_view.findViewById(R.id.dialog_wait_2_iv);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.dialog_zhuang);

        LinearInterpolator lir = new LinearInterpolator();
        anim.setInterpolator(lir);

        dialog_iv.startAnimation(anim);

        dia_wait.show();
    }
    private String str_result = "网络异常";
    private void startLogin() {
        startDialog();
        httpPost = new HttpPost(allurl.getLogin_url());
        params.add(new BasicNameValuePair("mobile", myTel));
        params.add(new BasicNameValuePair("password", myPwd));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);

                        if (jo.getString("status").equals("1")) {
                            JSONObject jo_data = jo.getJSONObject("data");

                            userIdTag = jo_data.getString("uid");



                            rMobile = jo_data.getString("mobile");

                            rUid = jo_data.getString("uid");

                            rNickname = jo_data.getString("nickname");
                            rAvatar64 = jo_data.getString("avatar64");
                            rAvatar128 = jo_data.getString("avatar128");
                            rAvatar256 = jo_data.getString("avatar256");
                            rUser_token = jo_data.getString("user_token");
                            rIsVip = jo_data.getString("agents_level");
                            rShareCount = jo_data.getString("share_count");

                            rHandle.sendEmptyMessage(21);

                        } else {
                            str_result = jo.getString("info");
                            rHandle.sendEmptyMessage(22);
                        }
                    } else {
                        str_result = "登录超时";
                        rHandle.sendEmptyMessage(22);
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



}
