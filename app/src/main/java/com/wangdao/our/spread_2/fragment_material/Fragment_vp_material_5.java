package com.wangdao.our.spread_2.fragment_material;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wangdao.our.spread_2.ExampleApplication;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.activity_.AddMaterial;
import com.wangdao.our.spread_2.bean.Material;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/21 0021.
 */
public class Fragment_vp_material_5 extends Fragment{

    private TextView vp_material_page5_tvnull;
    private View myView;
    private Context myContext;
    private LayoutInflater myInflater;
    private ListView list_fm_1 ;
    private List<Material> list_my_Material;
    private PullToRefreshScrollView pull_scrollview;

    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allUrl = new AllUrl();
    private FvmHandler_5 Fh_5 = new FvmHandler_5();
    //适配器
    private MaterialAdapter ma_Adapter;
    private NetBroadcast netBroadcast;
    private IntentFilter intentFilter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.vp_material_page5,null);
        myContext = this.getActivity();
        myInflater = inflater;
        initView();
        initScrollView();
        list_my_Material = new ArrayList<>();
        //  linshi();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netBroadcast = new NetBroadcast();
        myContext.registerReceiver(netBroadcast, intentFilter);
        ma_Adapter = new MaterialAdapter(list_my_Material);
        list_fm_1.setAdapter(ma_Adapter);
        setListViewHeightBasedOnChildren(list_fm_1);
        list_fm_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent cIntent = new Intent(myContext, AddMaterial.class);
                cIntent.putExtra("type", 1);
                cIntent.putExtra("compile_m", list_my_Material.get(position));
                startActivityForResult(cIntent, 1);
            }
        });
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
    private void initView(){
        pull_scrollview = (PullToRefreshScrollView) myView.findViewById(R.id.vp_material_page5_pull);
        list_fm_1 = (ListView) myView.findViewById(R.id.vp_material_page5_listview);
        vp_material_page5_tvnull = (TextView) myView.findViewById(R.id.vp_material_page5_tvnull);
    }
    private void initScrollView(){
        pull_scrollview.setMode(PullToRefreshBase.Mode.BOTH);
        pull_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData();
            }
        });
    }

    class MaterialAdapter extends BaseAdapter {
        Fm1_ViewHolder FVH ;
        List<Material> my_Material;
        public MaterialAdapter(List<Material> my_Material) {
            this.my_Material = my_Material;
        }

        @Override
        public int getCount() {
            return my_Material.size();
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
            if(convertView==null){
                FVH = new Fm1_ViewHolder();
                convertView = myInflater.inflate(R.layout.item_material,null);
                FVH.mIcon = (ImageView) convertView.findViewById(R.id.item_material_icon);
                FVH.mTitle = (TextView) convertView.findViewById(R.id.item_material_title);
                FVH.mInfo = (TextView) convertView.findViewById(R.id.item_material_info);

                FVH.mAll_icon = (RoundedImageView) convertView.findViewById(R.id.item_material_daicon);

                FVH.ll_icon = (LinearLayout) convertView.findViewById(R.id.item_material_ll_icon);
                FVH.ll_info = (LinearLayout) convertView.findViewById(R.id.item_material_ll_info);

                FVH.tv_tag_1 = (TextView) convertView.findViewById(R.id.fragment_vp_material_1_tag1);
                FVH.tv_tag_2 = (TextView) convertView.findViewById(R.id.fragment_vp_material_1_tag2);
                FVH.tv_tag_3 = (TextView) convertView.findViewById(R.id.fragment_vp_material_1_tag3);
                FVH.mAdress = (TextView) convertView.findViewById(R.id.item_material_adress);
                FVH.ck_1 = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(FVH);
            }else{
                FVH = (Fm1_ViewHolder) convertView.getTag();
            }
            FVH.ck_1.setVisibility(View.GONE);
            FVH.ll_icon.setVisibility(View.VISIBLE);
            FVH.ll_info.setVisibility(View.VISIBLE);
            FVH.mAll_icon.setVisibility(View.INVISIBLE);
            FVH.tv_tag_1.setText(R.string.tag_erweima);
            FVH.mAdress.setVisibility(View.VISIBLE);
            FVH.mAdress.setVisibility(View.GONE);
            FVH.mTitle.setText(my_Material.get(position).getmTitle());
            FVH.mInfo.setText(my_Material.get(position).getmInfo());

            ImageLoader.getInstance().displayImage(my_Material.get(position).getIcon_url() == null ? "" : my_Material.get(position).getIcon_url(), FVH.mIcon,
                    ExampleApplication.getInstance().getOptions(R.drawable.nopilc_2)
//                    ,
//                    new SimpleImageLoadingListener() {
//                        @Override
//                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                            super.onLoadingComplete(imageUri, view, loadedImage);
//                            FVH.mAll_icon.setImageBitmap(loadedImage);
//                        }
//                    }
            );
        //    asynImageLoader.showImageAsyn(FVH.mIcon, my_Material.get(position).getIcon_url(), R.drawable.nopic);
            return convertView;
        }
    }

    class Fm1_ViewHolder{
        ImageView mIcon;
        TextView mTitle;
        TextView mInfo;
        TextView mAdress;
        RoundedImageView mAll_icon;
        LinearLayout ll_icon;
        LinearLayout ll_info;
        TextView tv_tag_1;
        TextView tv_tag_2;
        TextView tv_tag_3;
        CheckBox ck_1;
    }


    private String getDataResult;
    private void initData(){
        list_my_Material.clear();
        httpPost = new HttpPost(allUrl.getGgList());
        SharedPreferences sharedPreferences = myContext.getSharedPreferences("user", myContext.MODE_PRIVATE);
        String tokenTemp = sharedPreferences.getString("user_token", "");

        params.add(new BasicNameValuePair("user_token", tokenTemp));
        params.add(new BasicNameValuePair("type", "tdcode"));
        params.add(new BasicNameValuePair("page", "1"));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        getDataResult= jo.getString("info");

                        if(jo.getString("status").equals("1")){
                            JSONArray ja = jo.getJSONArray("data");
                            for(int i = 0;i<ja.length();i++){

                                JSONObject jo_2 = ja.getJSONObject(i);
                                Material material = new Material();
                                switch (jo_2.getString("type")){
                                    //二维码
                                    case "tdcode":
                                        material.setmId(jo_2.getString("id"));
                                        material.setmType(3);
                                        material.setIcon_url(jo_2.getString("ad_code_img"));
                                        material.setmTitle(jo_2.getString("ad_code_title"));
                                        material.setmInfo(jo_2.getString("ad_code_des"));

                                        if(jo_2.getString("ad_top").equals("1")){
                                            material.setmTop(true);
                                        }else{
                                            material.setmTop(false);
                                        }
                                        if(jo_2.getString("ad_bottom").equals("1")){
                                            material.setmBottom(true);
                                        }else{
                                            material.setmBottom(false);
                                        }
                                        if(jo_2.getString("ad_cut").equals("1")){
                                            material.setmTailor(true);
                                        }else{
                                            material.setmTailor(false);
                                        }
                                        list_my_Material.add(material);
                                        //   Log.i("qqqqq", "tdcode" + material.getmTitle());
                                        break;
                                }

                            }
                            Fh_5.sendEmptyMessage(1);



                        }else{
                            Fh_5.sendEmptyMessage(2);
                        }
                    }
                } catch (Exception e) {
                    Fh_5.sendEmptyMessage(2);
                    e.printStackTrace();
                }
            }
        }).start();

    }


    class FvmHandler_5 extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //获取数据成功
                case 1:
                    ma_Adapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(list_fm_1);
                    pull_scrollview.onRefreshComplete();

                    if(list_my_Material.size() ==0){
                        vp_material_page5_tvnull.setVisibility(View.VISIBLE);
                        list_fm_1.setVisibility(View.VISIBLE);
                    }else {
                        vp_material_page5_tvnull.setVisibility(View.GONE);
                        list_fm_1.setVisibility(View.VISIBLE);
                    }
                    break;
                //获取数据失败
                case 2:
                    vp_material_page5_tvnull.setVisibility(View.VISIBLE);
                    list_fm_1.setVisibility(View.GONE);
                    pull_scrollview.onRefreshComplete();
                    break;
            }
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 5:
                initData();
                break;
        }
    }
}
