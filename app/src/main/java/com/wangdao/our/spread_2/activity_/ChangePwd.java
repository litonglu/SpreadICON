package com.wangdao.our.spread_2.activity_;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
public class ChangePwd extends Activity implements View.OnClickListener{

    private  EditText et_old_pwd,et_new_pwd,et_new_aginpwd;
    private Button bt_okchange;
    private ChangePwdHandler cpHandler = new ChangePwdHandler();
    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allUrl = new AllUrl();
    private ImageView iv_cancle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        et_old_pwd = (EditText) findViewById(R.id.activity_change_pwd_et_oldpwd);
        et_new_pwd = (EditText) findViewById(R.id.activity_change_pwd_et_newpwd);
        et_new_aginpwd = (EditText) findViewById(R.id.activity_change_pwd_et_newpwdagin);
        bt_okchange = (Button) findViewById(R.id.activity_change_pwd_bt_okchange);
        iv_cancle = (ImageView) findViewById(R.id.activity_change_pwd_iv_cancle);
        bt_okchange.setOnClickListener(this);
        iv_cancle.setOnClickListener(this);
        httpPost = new HttpPost(allUrl.getChange_pwd());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_change_pwd_bt_okchange:

                changePwd(et_old_pwd.getText().toString(),et_new_pwd.getText().toString(),et_new_aginpwd.getText().toString());
                break;
            case R.id.activity_change_pwd_iv_cancle:
                finish();
                break;
        }
    }
    private String changePwd_result;
    private void changePwd(String oldPwd,String newPwd,String newPwd_agin){
        SharedPreferences sharedPreferences = ChangePwd.this.getSharedPreferences("user", MODE_PRIVATE);
        String user_token = sharedPreferences.getString("user_token","");
        params.add(new BasicNameValuePair("user_token", user_token));
        params.add(new BasicNameValuePair("old_password", oldPwd));
        params.add(new BasicNameValuePair("new_password", newPwd));
        params.add(new BasicNameValuePair("re_password", newPwd_agin));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        changePwd_result = jo.getString("info");
                        if(jo.getString("status").equals("1")){
                            cpHandler.sendEmptyMessage(1);
                        }else{
                            cpHandler.sendEmptyMessage(2);
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

    class ChangePwdHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //修改成功
                case 1:
                    SharedPreferences sharedPreferences = ChangePwd.this.getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_token", et_new_pwd.getText().toString());
                    editor.commit();

                    new AlertDialog.Builder(ChangePwd.this)
                            .setTitle("修改结果")
                            .setMessage(changePwd_result)
                            .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent loginIntent = new Intent(ChangePwd.this,LoginActivity.class);
                                    startActivity(loginIntent);

                                    setResult(66);
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                //修改失败
                case 2:
                    new AlertDialog.Builder(ChangePwd.this)
                            .setTitle("修改结果")
                            .setMessage(changePwd_result)
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
}
