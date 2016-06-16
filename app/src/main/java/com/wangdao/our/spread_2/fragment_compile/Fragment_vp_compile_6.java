package com.wangdao.our.spread_2.fragment_compile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wangdao.our.spread_2.ExampleApplication;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.activity_.Article_info;
import com.wangdao.our.spread_2.bean.CompileResult;
import com.wangdao.our.spread_2.bean.RecommendArticle;
import com.wangdao.our.spread_2.slide_widget.AllUrl;
import com.wangdao.our.spread_2.slide_widget.widget_image.RoundedImageView;
import com.wangdao.our.spread_2.widget_pull.PullToRefreshBase;
import com.wangdao.our.spread_2.widget_pull.PullToRefreshScrollView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/21 0021.
 */
public class Fragment_vp_compile_6 extends Fragment{

    private PullToRefreshScrollView pull_ScrollView;
    private ListView myListView;
    private View myView;
    private Context myContext;
    private LayoutInflater myInflater;
    private List<RecommendArticle> list_reArticle;
    private AllUrl allurl = new AllUrl();

    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private FVC_Adapter fAdapter;
    private fcHandler_6 fhandler_6 = new fcHandler_6();
    private TextView tvnull;

    private final String myUrl = "http://wz.ijiaque.com/app/article/articledetail.html";


    private NetBroadcast netBroadcast;
    private IntentFilter intentFilter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.vp_compile_page6, null);
        myContext = this.getActivity();
        myInflater = inflater;
        initView();
        initListView();

        list_reArticle = new ArrayList<>();
        fAdapter = new FVC_Adapter(list_reArticle);
        myListView.setAdapter(fAdapter);


        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netBroadcast = new NetBroadcast();
        myContext.registerReceiver(netBroadcast, intentFilter);
        return myView;
    }

    private class NetBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            ConnectivityManager connectionManager =
                    (ConnectivityManager) myContext.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo netinfo = connectionManager.getActiveNetworkInfo();
            if(netinfo!=null&&netinfo.isAvailable()){
                initData();
            }else{
            }
        }

    }
    /**
     * 初始化数据
     */
    private int cNum = 1;
    private String queryResult;
    private void initData(){
        cNum = 1;
        list_reArticle.clear();
        SharedPreferences sharedPreferences = myContext.getSharedPreferences("tag", myContext.MODE_PRIVATE);
        httpPost = new HttpPost(allurl.getWenZhangAll());
        params.add(new BasicNameValuePair("keywordtags",sharedPreferences.getString("tag5","")));
        params.add(new BasicNameValuePair("page", "1"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo =new JSONObject(result);
                        //queryResult = jo.getString("info");




                        Type type = new TypeToken<CompileResult>() {}.getType();
                        Gson gson = new Gson();
                        CompileResult compileResult = gson.fromJson(result, type);
                        queryResult = compileResult.info;
                        for(RecommendArticle temp__:compileResult.data){
                            list_reArticle.add(temp__);
                        }
                        fhandler_6.sendEmptyMessage(1);




//                        if(jo.getString("status").equals("1")){
//
//                            JSONArray ja = jo.getJSONArray("data");
//
//                            RecommendArticle mWenZ = new RecommendArticle();
//
//                            for(int i = 0;i<ja.length();i++){
//                                JSONObject jo_2 = ja.getJSONObject(i);
//
//                                mWenZ.setTitle(jo_2.getString("writing_title"));
//                                mWenZ.setTryNum(jo_2.getString("writing_use"));
//                                mWenZ.setaId(jo_2.getString("id"));
//                                mWenZ.setIconUrl(jo_2.getString("writing_img"));
//                                mWenZ.setContent_(jo_2.getString("writing_brief"));
//                                list_reArticle.add(mWenZ);
//                            }
//                            fhandler_6.sendEmptyMessage(1);
//                        }else{
//                            fhandler_6.sendEmptyMessage(2);
//                        }
                    }
                } catch (Exception e) {
                    fhandler_6.sendEmptyMessage(2);
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void initData_2(){
        cNum+=1;
        SharedPreferences sharedPreferences = myContext.getSharedPreferences("tag", myContext.MODE_PRIVATE);
        httpPost = new HttpPost(allurl.getWenZhangAll());
        params.add(new BasicNameValuePair("keywordtags",sharedPreferences.getString("tag5","")));
        params.add(new BasicNameValuePair("page", cNum+""));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo =new JSONObject(result);
                        //queryResult = jo.getString("info");


                        Type type = new TypeToken<CompileResult>() {}.getType();
                        Gson gson = new Gson();
                        CompileResult compileResult = gson.fromJson(result, type);
                        queryResult = compileResult.info;
                        for(RecommendArticle temp__:compileResult.data){
                            list_reArticle.add(temp__);
                        }
                        fhandler_6.sendEmptyMessage(1);

//                        if(jo.getString("status").equals("1")){
//                            JSONArray ja = jo.getJSONArray("data");
//                            for(int i = 0;i<ja.length();i++){
//                                JSONObject jo_2 = ja.getJSONObject(i);
//                                RecommendArticle mWenZ = new RecommendArticle();
//                                mWenZ.setTitle(jo_2.getString("writing_title"));
//                                mWenZ.setTryNum(jo_2.getString("writing_use"));
//                                mWenZ.setaId(jo_2.getString("id"));
//                                mWenZ.setIconUrl(jo_2.getString("writing_img"));
//                                mWenZ.setContent_(jo_2.getString("writing_brief"));
//                                list_reArticle.add(mWenZ);
//                            }
//                            fhandler_6.sendEmptyMessage(1);
//                        }else{
//                            fhandler_6.sendEmptyMessage(2);
//                        }
                    }
                } catch (Exception e) {
                    fhandler_6.sendEmptyMessage(2);
                    e.printStackTrace();
                }
            }
        }).start();


    }



    class fcHandler_6 extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    fAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(myListView);
                    pull_ScrollView.onRefreshComplete();
                    tvnull.setVisibility(View.GONE);
                    myListView.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    Log.i("qqqqqq", "queryResult" + queryResult);
                    Toast.makeText(myContext,queryResult,Toast.LENGTH_SHORT).show();
                    pull_ScrollView.onRefreshComplete();
//                    tvnull.setVisibility(View.VISIBLE);
//                    tvnull.setText(queryResult);
//                    myListView.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void initView(){
        pull_ScrollView = (PullToRefreshScrollView) myView.findViewById(R.id.vp_compile_page6_scrollview);
        myListView = (ListView) myView.findViewById(R.id.vp_compile_page6_pull);
        tvnull = (TextView) myView.findViewById(R.id.vp_compile_page6_tvnull);
    }
    private void initListView(){
        pull_ScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        pull_ScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData_2();

            }
        });
    }

    private String auid;
    class FVC_Adapter extends BaseAdapter {
        FVC_ViewHolder fvc_viewHolder;
        List<RecommendArticle> reArticles;
        public FVC_Adapter(List<RecommendArticle> reArticles){
            this.reArticles = reArticles;
        }
        @Override
        public int getCount() {
            return reArticles.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = myInflater.inflate(R.layout.item_compile,null);
                fvc_viewHolder = new FVC_ViewHolder();
                fvc_viewHolder.iv_icon = (RoundedImageView) convertView.findViewById(R.id.item_compile_iv_icon);
                fvc_viewHolder.tv_title = (TextView) convertView.findViewById(R.id.item_compile_tv_title);
                fvc_viewHolder.tv_num = (TextView) convertView.findViewById(R.id.item_compile_tv_num);
                fvc_viewHolder.bt_compile = (Button) convertView.findViewById(R.id.item_compile_bt);
                convertView.setTag(fvc_viewHolder);
            }else{
                fvc_viewHolder = (FVC_ViewHolder) convertView.getTag();
            }

            SharedPreferences sharedPreferences = myContext.getSharedPreferences("user", myContext.MODE_PRIVATE);
            auid = sharedPreferences.getString("uid", "");
            fvc_viewHolder.bt_compile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(myContext, Article_info.class);
                    myIntent.putExtra("url",myUrl+"?writing_id="+reArticles.get(position).getId()+"&uid="+auid);
                    myIntent.putExtra("uid", reArticles.get(position).getId());
                    myIntent.putExtra("title", reArticles.get(position).getWriting_title());
                    myIntent.putExtra("img",reArticles.get(position).getWriting_img());
                    myIntent.putExtra("content",reArticles.get(position).getWriting_brief());
                    startActivity(myIntent);
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(myContext, Article_info.class);
                    myIntent.putExtra("url",myUrl+"?writing_id="+reArticles.get(position).getId()+"&uid="+auid);
                    myIntent.putExtra("uid", reArticles.get(position).getId());
                    myIntent.putExtra("title", reArticles.get(position).getWriting_title());
                    myIntent.putExtra("img",reArticles.get(position).getWriting_img());
                    myIntent.putExtra("content",reArticles.get(position).getWriting_brief());
                    startActivity(myIntent);
                }
            });
            fvc_viewHolder.tv_title.setText(reArticles.get(position).getWriting_title());
            fvc_viewHolder.tv_num.setText("使用次数：\t"+reArticles.get(position).getWriting_use());

            ImageLoader.getInstance().displayImage(reArticles.get(position).getWriting_img() ==null ? "": reArticles.get(position).getWriting_img(),fvc_viewHolder.iv_icon,
                    ExampleApplication.getInstance().getOptions(R.drawable.moren)

            );


         //   asynImageLoader.showImageAsyn(fvc_viewHolder.iv_icon, reArticles.get(position).getIconUrl(), R.drawable.nopic);
            return convertView;
        }
    }
    class FVC_ViewHolder{
        RoundedImageView iv_icon;
        TextView tv_title;
        TextView tv_num;
        Button bt_compile;
    }
    /***
     * 动态设置listview的高度 item 总布局必须是linearLayout
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1))
                + 15;
        listView.setLayoutParams(params);
    }
}