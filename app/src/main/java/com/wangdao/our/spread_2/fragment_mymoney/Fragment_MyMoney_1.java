package com.wangdao.our.spread_2.fragment_mymoney;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangdao.our.spread_2.ExampleApplication;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.activity_.mine_activity.CommissionInfo;
import com.wangdao.our.spread_2.bean.Commission;
import com.wangdao.our.spread_2.slide_widget.AllUrl;
import com.wangdao.our.spread_2.slide_widget.CircleImageView;

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
 * Created by Administrator on 2016/5/11 0011.
 * 我的佣金-全部
 */
public class Fragment_MyMoney_1 extends Fragment{

    private View myView;
    private LayoutInflater myInflater;
    private Context myContext;

    private HttpPost httpPost;
    private HttpPost httpPost_2;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();

    private Fm1_Adapter fm1_adapter ;
    private List<Commission> list_commission = new ArrayList<>();
    private fm1_Handler fm1_handler = new fm1_Handler();
    private TextView tv_allMoney;
    private ListView lv_fm1;
    private  TextView tv_erro;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_mymoey_1,null);
        myContext = this.getActivity();
        myInflater = inflater;

        initView();
        initMoney();

        fm1_adapter = new Fm1_Adapter(list_commission);
        lv_fm1.setAdapter(fm1_adapter);

        lv_fm1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(myContext, CommissionInfo.class);
                intent.putExtra("jiao",list_commission.get(position).getcNum());
                intent.putExtra("price",list_commission.get(position).getcPrice());
                intent.putExtra("payway",list_commission.get(position).getPayWay());
                intent.putExtra("info",list_commission.get(position).getcRemark());
                intent.putExtra("time",list_commission.get(position).getcTime());
                intent.putExtra("id",list_commission.get(position).getcId());
                intent.putExtra("status",list_commission.get(position).getcStatus());

                startActivityForResult(intent,1);
            }
        });

        initData();
        return myView;
    }
private void initView(){
    tv_allMoney = (TextView) myView.findViewById(R.id.fragment_mymoney_1_tv_money);
    lv_fm1 = (ListView) myView.findViewById(R.id.fragment_mymoney_1_lv);
    tv_erro = (TextView) myView.findViewById(R.id.fragment_mymoney_1_tv_erro);
}

    /**
     * 初始化余额
     */
    private String getResult = "0.00";
    private void initMoney(){
        httpPost_2 = new HttpPost(allurl.getTixianIfo());
        SharedPreferences sharedPreferences = myContext.getSharedPreferences("user", myContext.MODE_PRIVATE);
        String mToken = sharedPreferences.getString("user_token", "");
        params.add(new BasicNameValuePair("user_token", mToken));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost_2.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost_2);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        if(jo.getString("status").equals("1")){
                            JSONObject jo_2 = jo.getJSONObject("data");
                            getResult = jo_2.getString("账户资金");
                            fm1_handler.sendEmptyMessage(1);
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

    /**
     * 初始化数据
     */
    private String myMoneyResulr = "网络异常";
    private JSONObject jo;
    private Commission commission;
    public void initData(){

        list_commission.clear();
        httpPost = new HttpPost(allurl.getCommission());
        SharedPreferences sharedPreferences = myContext.getSharedPreferences("user", myContext.MODE_PRIVATE);
        String mToken = sharedPreferences.getString("user_token", "");
        params.add(new BasicNameValuePair("user_token", mToken));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = null;
                        try {
                        result = EntityUtils.toString(httpResponse.getEntity());
                         jo = new JSONObject(result);
                        myMoneyResulr = jo.getString("info");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if(jo.getString("status").equals("1")){
                                JSONArray ja = jo.getJSONArray("data");
                                for(int i = 0;i<ja.length();i++){
                                    JSONObject jo_2 = ja.getJSONObject(i);
                                     commission = new Commission();


                                    commission.setcTime(jo_2.getString("create_time"));
                                    commission.setcPrice(jo_2.getString("price"));
                                    commission.setcRemark(jo_2.getString("remark"));

                                    commission.setcNum(jo_2.getString("order_no"));
                                    commission.setPayWay(jo_2.getString("pay_way"));
                                    commission.setcId(jo_2.getString("id"));
                                    commission.setcStatus(jo_2.getString("status"));

                                    JSONObject jo_userInfo = jo_2.getJSONObject("userinfo");


                                    if(jo_userInfo.length() > 3){
                                        commission.setcIconUrl(jo_userInfo.getString("avatar256"));
                                    }else{
                                        commission.setcIconUrl("");
                                    }


                                    list_commission.add(commission);
                                }
                                fm1_handler.sendEmptyMessage(11);
                            }else{
                                fm1_handler.sendEmptyMessage(12);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


            }
        }).start();

    }
    class fm1_Handler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    tv_allMoney.setText(getResult);
                    break;

                //查询成功
                case 11:
                    fm1_adapter.notifyDataSetChanged();
                    tv_erro.setVisibility(View.GONE);
                    lv_fm1.setVisibility(View.VISIBLE);
                    break;

                //查询失败
                case 12:
                    tv_erro.setText(myMoneyResulr);
                    tv_erro.setVisibility(View.VISIBLE);
                    lv_fm1.setVisibility(View.GONE);
                    break;
            }
        }
    }
    class Fm1_Adapter extends BaseAdapter{
        Fm1_ViewHolder fm1_viewHolder = null;
        private List<Commission> list_commission;

        public Fm1_Adapter(List<Commission> list_commission){
            this.list_commission = list_commission;
        }

        @Override
        public int getCount() {
            return list_commission.size();
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
            if(convertView == null){
                convertView = myInflater.inflate(R.layout.item_fragment_mymoney,null);
                fm1_viewHolder = new Fm1_ViewHolder();
                fm1_viewHolder.iv_ivon = (CircleImageView) convertView.findViewById(R.id.item_fragment_mymoney_iv_icon);
                fm1_viewHolder.tv_time = (TextView) convertView.findViewById(R.id.item_fragment_mymoney_tv_time);
                fm1_viewHolder.tv_info = (TextView) convertView.findViewById(R.id.item_fragment_mymoney_tv_info);
                fm1_viewHolder.tv_price = (TextView) convertView.findViewById(R.id.item_fragment_mymoney_tv_price);
                convertView.setTag(fm1_viewHolder);

            }else{
                fm1_viewHolder = (Fm1_ViewHolder) convertView.getTag();
            }
            fm1_viewHolder.tv_time.setText(list_commission.get(position).getcTime());
            fm1_viewHolder.tv_info.setText(list_commission.get(position).getcRemark());
            fm1_viewHolder.tv_price.setText(list_commission.get(position).getcPrice());

            ImageLoader.getInstance().displayImage(list_commission.get(position).getcIconUrl() ==null ? "": list_commission.get(position).getcIconUrl(),fm1_viewHolder.iv_ivon,
                    ExampleApplication.getInstance().getOptions(R.drawable.default_photo)
            );

            return convertView;
        }
    }

    class Fm1_ViewHolder{
        CircleImageView iv_ivon;
        TextView tv_time;
        TextView tv_info;
        TextView tv_price;
    }

}
