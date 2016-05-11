package com.wangdao.our.spread_2.activity_.mine_activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.utils.L;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.activity_.TiXianHistory;
import com.wangdao.our.spread_2.activity_.TiXianToSomeHere;
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
 * Created by Administrator on 2016/5/9 0009.
 * 提现
 */
public class GetMoney extends Activity implements View.OnClickListener{

    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();
    private GetMoneyHandler gmHandler  = new GetMoneyHandler();

   private ImageView iv_cancle;
    private Button bt_getMoney;
    private TextView tv_myMoney;
    private  ImageView iv_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getmoney);
        initView();
        initClick();
        initMoney();
    }
    private void initView(){
        iv_cancle = (ImageView) findViewById(R.id.activity_mine_getmonet_iv_cancle);
        bt_getMoney = (Button) findViewById(R.id.activity_getmoney_bt_get);
        tv_myMoney = (TextView) findViewById(R.id.activity_getmoney_tv_mymoney);
        iv_history = (ImageView) findViewById(R.id.activity_getmoney_iv_history);
    }
    private void initClick(){
        iv_cancle.setOnClickListener(this);
        bt_getMoney.setOnClickListener(this);
        iv_history.setOnClickListener(this);
    }

    /**
     * 初始化余额
     */
    private String getResult = "0.00";
    private void initMoney(){
        httpPost = new HttpPost(allurl.getTixianIfo());
        SharedPreferences sharedPreferences = GetMoney.this.getSharedPreferences("user", MODE_PRIVATE);
        String mToken = sharedPreferences.getString("user_token", "");
        params.add(new BasicNameValuePair("user_token", mToken));
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
                            JSONObject jo_2 = jo.getJSONObject("data");
                            getResult = jo_2.getString("账户资金");
                            gmHandler.sendEmptyMessage(1);
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

    class GetMoneyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    tv_myMoney.setText(getResult);
                    break;


            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_mine_getmonet_iv_cancle:
                finish();
                break;
            case R.id.activity_getmoney_bt_get:
                showGetTypeDialog();
                break;
            //提现记录
            case R.id.activity_getmoney_iv_history:
                Intent hIntent = new Intent(GetMoney.this, TiXianHistory.class);
                startActivity(hIntent);
                break;
        }
    }


    private Dialog dialog_help_2;
    View view_2;
    helpdialog_item_2 hi_2 = null;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showGetTypeDialog() {

        hi_2 = new helpdialog_item_2();
        view_2 = getLayoutInflater().inflate(R.layout.dialog_out_login, null);
        hi_2.tv_help1 = (TextView) view_2.findViewById(R.id.bt_help1);
        hi_2.tv_help2 = (TextView) view_2.findViewById(R.id.bt_help2);

        hi_2.tv_helpcancle = (TextView) view_2.findViewById(R.id.bt_helpcancle);

        dialog_help_2 = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog_help_2.setContentView(view_2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog_help_2.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = this.getWindowManager().getDefaultDisplay().getHeight();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog_help_2.onWindowAttributesChanged(wl);
        dialog_help_2.setCanceledOnTouchOutside(true);
        hi_2.tv_help1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent txzIntent = new Intent(GetMoney.this,TiXianToSomeHere.class);
                txzIntent.putExtra("buy_type","支付宝");
                startActivity(txzIntent);
                dialog_help_2.dismiss();
            }
        });
        hi_2.tv_help2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent txIntent = new Intent(GetMoney.this,TiXianToSomeHere.class);
                txIntent.putExtra("buy_type","微信");
                startActivity(txIntent);
                dialog_help_2.dismiss();
            }
        });
        hi_2.tv_helpcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_help_2.dismiss();
            }
        });
        dialog_help_2.show();
    }

    class helpdialog_item_2 {
        TextView tv_help1;
        TextView tv_help2;
        TextView tv_helpcancle;
    }
}
