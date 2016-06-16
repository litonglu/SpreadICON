package com.wangdao.our.spread_2.activity_;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangdao.our.spread_2.ExampleApplication;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.bean.SystemM;
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
 * Created by Administrator on 2016/6/12 0012.
 */
public class SystemMaterial extends Activity{

    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();
    private ListView lv_m;
    private ImageView iv_cancle;
    private List<SystemM> list_sysm = new ArrayList<>();
    private SysMaterialHandler sysMaterialHandler = new SysMaterialHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_material);

        initView();
        initData();
        lv_m.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent fIntent = new Intent();
                fIntent.putExtra("icon_url",list_sysm.get(position).getIconDemo());

                fIntent.putExtra("icon_id",list_sysm.get(position).getsId());

                setResult(88,fIntent);

                finish();

            }
        });
    }

    private void initView(){
        lv_m = (ListView) findViewById(R.id.activity_system_material_lv);
        iv_cancle = (ImageView) findViewById(R.id.activity_system_material_iv_cancle);
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initData(){

        httpPost = new HttpPost(allurl.getSystemMaterial());
        params.add(new BasicNameValuePair("", ""));
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
                            JSONArray ja = jo.getJSONArray("list");
                            for(int i = 0;i<ja.length();i++){

                                JSONObject jo_2 = ja.getJSONObject(i);
                                SystemM systemM = new SystemM();
                                systemM.setIconUrl(jo_2.getString("path"));
                                systemM.setsId(jo_2.getString("id"));
                                systemM.setIconDemo(jo_2.getString("demo"));
                                list_sysm.add(systemM);
                                sysMaterialHandler.sendEmptyMessage(1);

                            }
                        }else{

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


    class SysMaterialHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){

                case 1:
                    lv_m.setAdapter(new SysAdapter(list_sysm));
                    break;
            }
        }
    }


    class SysAdapter extends BaseAdapter{

        List<SystemM> list_;
        SysViewHolder sysViewHolder = null;

        public SysAdapter(List<SystemM> list_){
            this.list_ = list_;
        }


        @Override
        public int getCount() {
            return list_.size();
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
                sysViewHolder = new SysViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_material,null);
                sysViewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.item_material_daicon);
                sysViewHolder.ch_ = (CheckBox) convertView.findViewById(R.id.checkBox1);
                sysViewHolder.ch_.setVisibility(View.GONE);
                convertView.setTag(sysViewHolder);

            }else{
                sysViewHolder = (SysViewHolder) convertView.getTag();
            }

            ImageLoader.getInstance().displayImage(list_.get(position).getIconDemo() == null ? "" : list_.get(position).getIconDemo(), sysViewHolder.iv_icon,
                    ExampleApplication.getInstance().getOptions(R.drawable.moren)
            );

            return convertView;

        }
    }

    class SysViewHolder{
        ImageView iv_icon;
        CheckBox ch_;
    }



}
