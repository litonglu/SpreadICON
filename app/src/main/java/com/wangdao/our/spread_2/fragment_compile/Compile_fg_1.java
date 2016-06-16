package com.wangdao.our.spread_2.fragment_compile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.activity_.LoginActivity;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_1;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_2;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_3;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_4;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_5;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_6;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_7;
import com.wangdao.our.spread_2.fragment_compile.Fragment_vp_compile_8;
import com.wangdao.our.spread_2.slide_widget.AllUrl;
import com.wangdao.our.spread_2.slide_widget.PagerSlidingTabStrip;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
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
 * Created by Administrator on 2016/4/21 0021.
 */
public class Compile_fg_1 extends Fragment{
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    private Drawable oldBackground = null;
    private int currentColor = 0xFF666666;
    private View myView;
    private LayoutInflater myInflater;
    private Context myContext;
    private List<Fragment> list_fragment = new ArrayList<>();
    private String[] allTitle = {"热文","我的"};

    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();
    private compile_1 c1Handler = new compile_1();
    private NetBroadcast netBroadcast;
    private IntentFilter intentFilter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.compile_fragment_1,null);
        myInflater = inflater;
        myContext = this.getActivity();

        tabs = (PagerSlidingTabStrip) myView.findViewById(R.id.fragment_compile_slide);
        pager = (ViewPager) myView.findViewById(R.id.fragment_compile_vp);

        list_fragment.add(new Fragment_vp_compile_1());
        list_fragment.add(new Fragment_vp_compile_2());
        list_fragment.add(new Fragment_vp_compile_3());
        list_fragment.add(new Fragment_vp_compile_4());
        list_fragment.add(new Fragment_vp_compile_5());
        list_fragment.add(new Fragment_vp_compile_6());
        list_fragment.add(new Fragment_vp_compile_7());
        list_fragment.add(new Fragment_vp_compile_8());
        list_fragment.add(new Fragment_vp_compile_9());
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netBroadcast = new NetBroadcast();
        myContext.registerReceiver(netBroadcast, intentFilter);

        return myView;

    }

    private String tagResult;
    private  SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    int n;
    private void initTag(){

        sharedPreferences = myContext.getSharedPreferences("tag", myContext.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        httpPost = new HttpPost(allurl.getWenZhangTag());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        tagResult = jo.getString("info");

                        if(jo.getString("status").equals("1")){
                            JSONArray ja = jo.getJSONArray("data");
                            allTitle = new String[ja.length()+2];

                            for(int i = 0; i < ja.length()+2;i++){
                                if(i==0){
                                    allTitle[0] = "热文";
                                }else if(i==1){
                                    allTitle[1] = "我的";
                                }else if(i == 2){
                                    allTitle[i] = ja.getJSONObject(0).getString("tags");
                                }else if( i == 3){
                                    allTitle[i] = ja.getJSONObject(1).getString("tags");
                                }else if( i == 4 ){
                                    allTitle[i] = ja.getJSONObject(2).getString("tags");
                                }else if( i == 5){
                                    allTitle[i] = ja.getJSONObject(3).getString("tags");
                                }else if( i == 6){
                                    allTitle[i] = ja.getJSONObject(4).getString("tags");
                                }else if( i == 7){
                                    allTitle[i] = ja.getJSONObject(5).getString("tags");
                                }else if( i == 8){
                                    allTitle[i] = ja.getJSONObject(6).getString("tags");
                                }
                                Log.i("qqqqq","length == "+ allTitle.length+"---"+allTitle.toString());
                            }
                            c1Handler.sendEmptyMessage(1);
                        }else{
                            c1Handler.sendEmptyMessage(2);
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

    private class NetBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            ConnectivityManager connectionManager =
                    (ConnectivityManager) myContext.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo netinfo = connectionManager.getActiveNetworkInfo();
            if(netinfo!=null&&netinfo.isAvailable()){
                initTag();
            }else{
            }
        }

    }

    class compile_1 extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:

                    for(int i = 0;i<allTitle.length;i++){
                        editor.putString("tag"+i,allTitle[i]);
                    }
                    editor.commit();

                    adapter = new MyPagerAdapter(getFragmentManager(),allTitle);
                    pager.setOffscreenPageLimit(allTitle.length);
                    pager.setAdapter(adapter);
                    final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                            .getDisplayMetrics());
                    pager.setPageMargin(pageMargin);
                    tabs.setViewPager(pager);
                    break;

                //获取文章标签失败
                case 2:
                    Log.i("qqqqqq","tag失败"+tagResult);
                    break;
            }
        }
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        private  String[] TITLES;

        
        public MyPagerAdapter(FragmentManager fm,String[] TITLES) {
            super(fm);
            this.TITLES = TITLES;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return list_fragment.get(position);
        }
    }
}
