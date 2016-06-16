package com.wangdao.our.spread_2.activity_;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangdao.our.spread_2.ExampleApplication;
import com.wangdao.our.spread_2.MainActivity;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.bean.Message_;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class MessageA extends Activity implements View.OnClickListener{

    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;

    private ImageView iv_cancle;
    private ListView lv_mg;
    private MessageAdapter mAdapter;
    private List<Message_> list_m = new ArrayList<>();

    private AllUrl allurl = new AllUrl();
    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private MessageHandler messageHandler = new MessageHandler();
    private  TextView tv_erro;
    private String currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_message);

        iv_cancle = (ImageView) findViewById(R.id.messagea_iv_cancle);
        lv_mg = (ListView) findViewById(R.id.activity_push_message_lv);
        tv_erro = (TextView) findViewById(R.id.activity_push_message_tv_erro);

        mAdapter = new MessageAdapter(list_m);
        lv_mg.setAdapter(mAdapter);
        initData();
        iv_cancle.setOnClickListener(this);

        initTag();

        lv_mg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentId = list_m.get(position).getmId();
                showDeleteDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.messagea_iv_cancle:
                setResult(1);
                finish();
                break;
            //确定删除
            case R.id.dialog_logout_cancle:
                DeleteMessage();
                materialDialog.dismiss();
                break;
            //取消删除
            case R.id.dialog_logout_yes:
                materialDialog.dismiss();
                break;
        }
    }

    Dialog materialDialog;
    View material_view;
    TextView tv_cancle,tv_ok,tv_dialog_info;
    private void showDeleteDialog(){
        material_view = getLayoutInflater().inflate(R.layout.dialog_logout,null);
        tv_cancle = (TextView) material_view.findViewById(R.id.dialog_logout_cancle);
        tv_ok = (TextView) material_view.findViewById(R.id.dialog_logout_yes);
        tv_dialog_info = (TextView) material_view.findViewById(R.id.dialog_logout_tv_info);
        materialDialog = new Dialog(this,R.style.dialog);
        materialDialog.setContentView(material_view);

        tv_dialog_info.setText("确定删除？");
        tv_cancle.setText("确定");
        tv_ok.setText("取消");
        materialDialog.show();

        tv_cancle.setOnClickListener(this);
        tv_ok.setOnClickListener(this);

    }

    /**
     * 删除消息
     */
    private String deleteM_result = "网络异常";
    private void DeleteMessage(){
        httpPost = new HttpPost(allurl.getUserMessage_delete());
        SharedPreferences sharedPreferences = MessageA.this.getSharedPreferences("user", MODE_PRIVATE);
        String mToken = sharedPreferences.getString("user_token", "");
        params.add(new BasicNameValuePair("user_token", mToken));
        params.add(new BasicNameValuePair("msg_id", currentId));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        deleteM_result = jo.getString("info");
                        if(jo.getString("status").equals("1")){
                            messageHandler.sendEmptyMessage(11);
                        }else{
                            messageHandler.sendEmptyMessage(12);
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
    private String getMessageResult = "网络异常";
    private void initData(){
        list_m.clear();
        httpPost = new HttpPost(allurl.getUserMessage());
        SharedPreferences sharedPreferences = MessageA.this.getSharedPreferences("user", MODE_PRIVATE);
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
                        getMessageResult = jo.getString("info");
                        if(jo.getString("status").equals("1")){
                            JSONArray ja = jo.getJSONArray("data");
                            for(int i = 0;i<ja.length(); i++){
                                JSONObject jo_2 = ja.getJSONObject(i);
                                Message_ message_ = new Message_();
                                message_.setmIconUrl(jo_2.getString("logo"));
                                message_.setmTitle(jo_2.getString("title"));
                                message_.setmInfo(jo_2.getString("content"));
                                message_.setmTime(jo_2.getString("create_time"));
                                message_.setmId(jo_2.getString("id"));
                                list_m.add(message_);
                            }
                            messageHandler.sendEmptyMessage(1);
                        }else{
                            messageHandler.sendEmptyMessage(2);
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

    class MessageHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //更新消息数据成功
                case 1:
                    tv_erro.setVisibility(View.GONE);
                    lv_mg.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                    break;
                //更新消息失败
                case 2:
                    tv_erro.setText(getMessageResult);
                    tv_erro.setVisibility(View.VISIBLE);
                    break;
                //删除成功
                case 11:
                    initData();
                    break;
                //删除失败
                case 12:

                    new AlertDialog.Builder(MessageA.this)
                            .setTitle("RESULT：")
                            .setMessage(deleteM_result)
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
    class MessageAdapter extends BaseAdapter{

        MessageViewHolder mvh = null;
        private List<Message_> list_m;

        public MessageAdapter(List<Message_> list_m){
            this.list_m = list_m;
        }

        @Override
        public int getCount() {
            return list_m.size();
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
                mvh = new MessageViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_message,null);
                mvh.iv_icon = (ImageView) convertView.findViewById(R.id.item_message_iv_icon);
                mvh.tv_info = (TextView) convertView.findViewById(R.id.item_messager_tv_info);
                mvh.tv_title = (TextView) convertView.findViewById(R.id.item_messager_tv_title);
                mvh.tv_time = (TextView) convertView.findViewById(R.id.item_messager_tv_time);
                convertView.setTag(mvh);
            }else{
                mvh = (MessageViewHolder) convertView.getTag();
            }
            mvh.tv_time.setText(list_m.get(position).getmTime());
            mvh.tv_title.setText(list_m.get(position).getmTitle());
            mvh.tv_info.setText(list_m.get(position).getmInfo());
            ImageLoader.getInstance().displayImage(list_m.get(position).getmIconUrl() == null ? "" : list_m.get(position).getmIconUrl(), mvh.iv_icon,
                    ExampleApplication.getInstance().getOptions(R.drawable.moren));
            return convertView;
        }
    }
    class MessageViewHolder{
        TextView tv_title;
        TextView tv_info;
        TextView tv_time;
        ImageView iv_icon;
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }

    private void setCostomMsg(String msg){
        if (null != msg) {
            Log.i("qqqqq","接受的消息-----"+msg);
        }
        Log.i("qqqqq","接受的消息-----kong");
    }

    /**
     * 阅读状态
     */
    private void initTag(){
        SharedPreferences sharedPreferences = MessageA.this.getSharedPreferences("tag", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("tg", "1");
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }
}
