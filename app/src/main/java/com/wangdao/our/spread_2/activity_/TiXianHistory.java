package com.wangdao.our.spread_2.activity_;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.bean.HistiryOrder;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/10 0010.
 */
public class TiXianHistory extends Activity implements View.OnClickListener{

    private ImageView iv_cancle;
    private ListView lv_history;
    private List<HistiryOrder> list_history = new ArrayList<>();
    private TxAdapter txAdapter;

    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();
    private TxHistoryHandler txHistoryHandler = new TxHistoryHandler();
    private TextView tv_erro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tixian_history);
        initView();
        initClick();

        txAdapter = new TxAdapter(list_history);
        lv_history.setAdapter(txAdapter);
        initData();
    }
    private void initView(){
        iv_cancle = (ImageView) findViewById(R.id.activity_tixian_history_iv_cancle);
        lv_history = (ListView) findViewById(R.id.activity_tixian_history_lv);
        tv_erro = (TextView) findViewById(R.id.activity_tixian_history_tv_erro);
    }

    private void initClick(){
        iv_cancle.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_tixian_history_iv_cancle:
                finish();
                break;
        }
    }

    /**
     * 初始化提现记录
     */
    private String txHistoryResult = "网络异常";
    private void initData(){
        httpPost = new HttpPost(allurl.getTixianHistory());

        SharedPreferences sharedPreferences = TiXianHistory.this.getSharedPreferences("user", MODE_PRIVATE);
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
                        txHistoryResult = jo.getString("info");
                        if(jo.getString("status").equals("1")){
                            JSONArray ja = jo.getJSONArray("data");
                            for(int i = 0;i < ja.length(); i++){
                                JSONObject jo_2 = ja.getJSONObject(i);
                                HistiryOrder histiryOrder = new HistiryOrder();
                                histiryOrder.setTime(jo_2.getString("create_time"));
                                histiryOrder.setPrice(jo_2.getString("price"));
                                histiryOrder.setAccount(jo_2.getString("account"));
                                histiryOrder.setResult(jo_2.getString("status"));
                                histiryOrder.setPayWay(jo_2.getString("pay_way"));
                                list_history.add(histiryOrder);
                            }
                            txHistoryHandler.sendEmptyMessage(1);
                        }else{
                            txHistoryHandler.sendEmptyMessage(2);
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

    class TxHistoryHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //获取提箱记录成功
                case 1:
                    txAdapter.notifyDataSetChanged();
                    tv_erro.setVisibility(View.GONE);
                    lv_history.setVisibility(View.VISIBLE);
                    break;
                //获取提箱记录失败
                case 2:
                    tv_erro.setText(txHistoryResult);
                    tv_erro.setVisibility(View.VISIBLE);
                    lv_history.setVisibility(View.GONE);

                    break;
            }
        }
    }
    class TxAdapter extends BaseAdapter{
        List<HistiryOrder> list_history;
        public TxAdapter(List<HistiryOrder> list_history){
            this.list_history = list_history;
        }
        @Override
        public int getCount() {
            return list_history.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.item_tixian_history,null);
            TextView tv_time = (TextView) convertView.findViewById(R.id.item_tixian_history_tv_time);
            TextView tv_money = (TextView) convertView.findViewById(R.id.item_tixian_history_tv_money);
            TextView tv_account = (TextView) convertView.findViewById(R.id.item_tixian_history_tv_account);
            TextView tv_state = (TextView) convertView.findViewById(R.id.item_tixian_history_tv_state);
            TextView tv_iswx = (TextView) convertView.findViewById(R.id.item_tixian_history_tv_tag);


          tv_time.setText(list_history.get(position).getTime());

            tv_money.setText(list_history.get(position).getPrice());
            tv_account.setText(list_history.get(position).getAccount());
            tv_state.setText(list_history.get(position).getResult());

            if(list_history.get(position).getPayWay().equals("wx_pub")){
                tv_iswx.setText("微信");
            }else{
                tv_iswx.setText("支付宝");
            }
            return convertView;
        }
    }

}
