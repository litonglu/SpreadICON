package com.wangdao.our.spread_2.activity_;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.wangdao.our.spread_2.MainActivity;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.activity_.MessageA;
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

import cn.jpush.android.api.JPushInterface;
/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();


    private Button bt_login;
    private TextView tv_register;
    private EditText et_tel, et_pwd;
    private AllUrl allurl = new AllUrl();
    private String myTel, myPwd;
    private String str_result;
    private LoginHandler loginHandler = new LoginHandler();
    private String rMobile, rUid, rNickname, rAvatar64, rAvatar128, rAvatar256, rUser_token;
  //private ProgressBar login_pb;
    private TextView tv_forgetPwd;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    private MessageA.MessageReceiver mMessageReceiver;
    public static boolean isForeground = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private ImageView iv_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bt_login = (Button) findViewById(R.id.activity_login_bt_login);
        tv_register = (TextView) findViewById(R.id.activity_login_tv_register);
      //  login_pb = (ProgressBar) findViewById(R.id.activity_login_pb);
        et_tel = (EditText) findViewById(R.id.activity_et_tel);
        et_pwd = (EditText) findViewById(R.id.activity_et_pwd);
        iv_icon = (ImageView) findViewById(R.id.activity_login_iv_icon);
        tv_forgetPwd = (TextView) findViewById(R.id.activity_login_tv_forgetpwd);

        httpPost = new HttpPost(allurl.getLogin_url());

        bt_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_forgetPwd.setOnClickListener(this);
        autoLogin();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_login_bt_login:
                myTel = et_tel.getText().toString();
                myPwd = et_pwd.getText().toString();
                if (myTel.length() != 0 && myPwd.length() != 0) {
                   // login_pb.setVisibility(View.VISIBLE);
                    startDialog();
                    startLogin();
                }
                break;
            case R.id.activity_login_tv_register:
                Intent rIntent = new Intent(this, com.wangdao.our.spread_2.activity_.RegisterActivity.class);
                startActivityForResult(rIntent, 1);
                break;

            case R.id.activity_login_tv_forgetpwd:
                Intent fIntent = new Intent(this, ForgetPwd.class);
                startActivity(fIntent);
                break;

        }
    }


    public static Bitmap toRoundBitmap(Bitmap bitmap) {

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



    private Dialog dia_wait;
    private void startDialog(){
        dia_wait = new Dialog(this,R.style.dialog);
        dia_wait.setContentView(R.layout.dialog_wait);
        dia_wait.show();
    }

    private void stopdialog_wait(){
        dia_wait.dismiss();
    }
    private void startLogin() {

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
                            rMobile = jo_data.getString("mobile");
                            rUid = jo_data.getString("uid");
                            rNickname = jo_data.getString("nickname");
                            rAvatar64 = jo_data.getString("avatar64");
                            rAvatar128 = jo_data.getString("avatar128");
                            rAvatar256 = jo_data.getString("avatar256");
                            rUser_token = jo_data.getString("user_token");
                            loginHandler.sendEmptyMessage(1);
                        } else {
                            str_result = jo.getString("info");
                            loginHandler.sendEmptyMessage(2);
                        }
                    } else {
                        str_result = "登录超时";
                        loginHandler.sendEmptyMessage(2);
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
    class LoginHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Intent lIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(lIntent);
                    finish();
                    SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("mobile", rMobile);
                    editor.putString("pwd", myPwd);
                    editor.putString("uid", rUid);
                    editor.putString("nickname", rNickname);
                    editor.putString("avatar64", rAvatar64);
                    editor.putString("avatar128", rAvatar128);
                    editor.putString("avatar256", rAvatar256);
                    editor.putString("user_token", rUser_token);
                    editor.commit();
                  //  login_pb.setVisibility(View.GONE);
stopdialog_wait();
                    break;
                case 2:
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("登录")
                            .setMessage(str_result)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                //    login_pb.setVisibility(View.GONE);
                    stopdialog_wait();
                    break;
                //自动登录成功
                case 3:
                    Intent lIntent_auto = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(lIntent_auto);
                    finish();
                 //   login_pb.setVisibility(View.GONE);
                    stopdialog_wait();
                    break;
                //自动登录失败
                case 4:
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("自动登录失败")
                            .setMessage(str_result)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                   // login_pb.setVisibility(View.GONE);
                    stopdialog_wait();
                    break;
                //自动登录报错
                case 5:
//                    new AlertDialog.Builder(LoginActivity.this)
//                            .setTitle("自动登录失败")
//                            .setMessage("未查找到账号的信息")
//                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            })
//                            .show();
                   // login_pb.setVisibility(View.GONE);
                    stopdialog_wait();
                    break;
            }
        }
    }

    private void autoLogin() {
       // login_pb.setVisibility(View.VISIBLE);
        startDialog();
        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("user", MODE_PRIVATE);
        String aMobile = sharedPreferences.getString("mobile", "");
        String aPwd = sharedPreferences.getString("pwd", "");

        params.add(new BasicNameValuePair("mobile", aMobile));
        params.add(new BasicNameValuePair("password", aPwd));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    try {
                        httpResponse = new DefaultHttpClient().execute(httpPost);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        JSONObject jo_data = jo.getJSONObject("data");

                        if (jo.getString("status").equals("1")) {
                            loginHandler.sendEmptyMessage(3);
                        } else {
                            str_result = jo_data.getString("info");
                            loginHandler.sendEmptyMessage(4);
                        }
                    }
                } catch (Exception e) {
                    loginHandler.sendEmptyMessage(5);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                et_tel.setText(data.getExtras().getString("tel"));
                et_pwd.setText(data.getExtras().getString("pwd"));
        }
    }




}
