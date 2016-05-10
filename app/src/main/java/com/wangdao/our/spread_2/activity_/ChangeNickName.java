package com.wangdao.our.spread_2.activity_;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
public class ChangeNickName extends Activity implements View.OnClickListener{
    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private ImageView iv_cancle;
    private EditText et_new_name;
    private Button bt_okChange;
    private AllUrl allUrl = new AllUrl();
    private ChangeNkHandler cnHandler = new ChangeNkHandler();
    private ProgressBar changeNick_pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nickname);
        iv_cancle = (ImageView) findViewById(R.id.activity_change_nickname_iv_cancle);
        et_new_name = (EditText) findViewById(R.id.activity_change_nickname_et_newname);
        bt_okChange = (Button) findViewById(R.id.activity_change_nickname_bt_okchange);
        changeNick_pb = (ProgressBar) findViewById(R.id.activity_change_nickname_pb);
        iv_cancle.setOnClickListener(this);
        bt_okChange.setOnClickListener(this);


        httpPost = new HttpPost(allUrl.getChange_user_info());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_change_nickname_iv_cancle:
                finish();
                break;
            case R.id.activity_change_nickname_bt_okchange:
                if(et_new_name.getText().toString().length() != 0){
                    changeNick_pb.setVisibility(View.VISIBLE);
                    ChangeNickName(et_new_name.getText().toString());
                }
                break;
        }
    }

    private String changeResule;
    private void ChangeNickName(String newNickname){
        SharedPreferences sharedPreferences = ChangeNickName.this.getSharedPreferences("user", MODE_PRIVATE);

        params.add(new BasicNameValuePair("user_token", sharedPreferences.getString("user_token", "")));
        params.add(new BasicNameValuePair("nickname", newNickname));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);

                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        changeResule = jo.getString("info");
                        if(jo.getString("status").equals("1")){
                            cnHandler.sendEmptyMessage(1);
                        }else{
                            cnHandler.sendEmptyMessage(2);
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

    class ChangeNkHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //修改成功
                case 1:
                    SharedPreferences sharedPreferences = ChangeNickName.this.getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("nickname",et_new_name.getText().toString());
                    editor.commit();

                    new AlertDialog.Builder(ChangeNickName.this)
                            .setTitle("修改结果")
                            .setMessage(changeResule)
                            .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setResult(11);
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    changeNick_pb.setVisibility(View.GONE);
                    break;
                //修改失败
                case 2:
                    new AlertDialog.Builder(ChangeNickName.this)
                            .setTitle("修改结果")
                            .setMessage(changeResule)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    changeNick_pb.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
