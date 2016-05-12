package com.wangdao.our.spread_2.activity_;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wangdao.our.spread_2.R;
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
 * Created by Administrator on 2016/4/25 0025.
 */
public class ForgetPwd extends Activity implements View.OnClickListener{

    private EditText et_mobile,et_pwd,et_code;
    private Button bt_getCode,bt_rePwd;
    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();
    private String dialog_vf;
    private Forget_Handler forget_handler = new Forget_Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        initView();
        initClick();
    }

    private void initView(){
        et_mobile = (EditText) findViewById(R.id.activity_forget_pwd_et_tel);
        et_pwd = (EditText) findViewById(R.id.activity_forget_pwd_et_pwd);
        et_code = (EditText) findViewById(R.id.activity_forget_pwd_et_code);

        bt_getCode = (Button) findViewById(R.id.activity_forget_pwd_bt_getcode);
        bt_rePwd = (Button) findViewById(R.id.activity_forget_pwd_bt_repwd);
    }


    private void initClick(){
        bt_getCode.setOnClickListener(this);
        bt_rePwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_forget_pwd_bt_getcode :
                showVfDialog();
                break;
            case R.id.activity_forget_pwd_bt_repwd :
                StartRePwd();
                break;
            case R.id.dialog_logout_no1:
                materialDialog.dismiss();
                break;
            case R.id.dialog_logout_yes1:
                dialog_vf = et_vf_dialog.getText().toString();
                sendNote();
                materialDialog.dismiss();
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
        WebSettings mWebSettings = wb_dialog_vf.getSettings();
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
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
        params.add(new BasicNameValuePair("mobile", et_mobile.getText().toString()));
        params.add(new BasicNameValuePair("img_verify",dialog_vf));
        params.add(new BasicNameValuePair("is_mi","true"));
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
                        if(jo.getString("status").equals("1")){
                            forget_handler.sendEmptyMessage(11);
                        }else{
                            forget_handler.sendEmptyMessage(12);
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
class Forget_Handler extends Handler{
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            //重置密码成功
            case 1:

                new AlertDialog.Builder(ForgetPwd.this)
                        .setTitle("RESULT:")
                        .setMessage(sendResult)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                dialog.dismiss();
                            }
                        })
                        .show();

                break;
            //重置密码失败
            case 2:
                new AlertDialog.Builder(ForgetPwd.this)
                        .setTitle("RESULT:")
                        .setMessage(sendResult)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            //发送短信成功
            case 11:
                Toast.makeText(ForgetPwd.this,sendResult,Toast.LENGTH_SHORT).show();
                break;
            //发送短信失败
            case 12:
                Toast.makeText(ForgetPwd.this,sendResult,Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

    /**
     * 重置密码
     */
    private String RePwdResult = "网络异常";
    private void StartRePwd(){
        httpPost = new HttpPost(allurl.getRePwd());
        SharedPreferences sharedPreferences = ForgetPwd.this.getSharedPreferences("user", MODE_PRIVATE);
        String mToken = sharedPreferences.getString("user_token", "");

        params.add(new BasicNameValuePair("mobile", et_mobile.getText().toString()));
        params.add(new BasicNameValuePair("password", et_pwd.getText().toString()));
        params.add(new BasicNameValuePair("reg_verify", et_code.getText().toString()));
       // params.add(new BasicNameValuePair("user_token", mToken));

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        RePwdResult = jo.getString("info");
                        if(jo.getString("status").equals("1")){
                            forget_handler.sendEmptyMessage(1);
                        }else{
                            forget_handler.sendEmptyMessage(2);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

}
