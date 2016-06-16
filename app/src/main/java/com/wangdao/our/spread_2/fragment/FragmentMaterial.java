package com.wangdao.our.spread_2.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.activity_.AddMaterial;
import com.wangdao.our.spread_2.fragment_material.Fragment_vp_material_1;
import com.wangdao.our.spread_2.fragment_material.Fragment_vp_material_2;
import com.wangdao.our.spread_2.fragment_material.Fragment_vp_material_3;
import com.wangdao.our.spread_2.fragment_material.Fragment_vp_material_4;
import com.wangdao.our.spread_2.fragment_material.Fragment_vp_material_5;
import com.wangdao.our.spread_2.slide_widget.AllUrl;

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
public class FragmentMaterial extends Fragment implements ViewPager.OnPageChangeListener,View.OnClickListener{


    private View myView;
    private LayoutInflater myInflater;
    private Context myContex;
    private List<Fragment> list_fragment = new ArrayList<>();
    private FragmentManager myFM;
    private ViewPager material_vp;
    private View line_1,line_2,line_3,line_4,line_5;
    private TextView tv_addm;

    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allUrl = new AllUrl();
    private TextView fm_tv_1,fm_tv_2,fm_tv_3,fm_tv_4,fm_tv_5;
    private FmHandler fHandler = new FmHandler();
    private Fragment_vp_material_2 fvm_2 = new Fragment_vp_material_2();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_material,null);
        myInflater = inflater;
        myContex = this.getActivity();
        myFM = this.getFragmentManager();
        initView();
        initGetType();
     //   list_fragment.add(new Fragment_vp_material_1());
          list_fragment.add(fvm_2);
     //   list_fragment.add(new Fragment_vp_material_3());
       // list_fragment.add(new Fragment_vp_material_4());
      //  list_fragment.add(new Fragment_vp_material_5());

        MaterialPagerAdapter mpa = new MaterialPagerAdapter(myFM);
        material_vp.setAdapter(mpa);
        material_vp.setOnPageChangeListener(this);
        return myView;
    }


    /**
     * 初始化广告类型
     */
    List<GgType> allGgType = new ArrayList<>();
    private String getTypeResult;
    private void initGetType(){
        httpPost = new HttpPost(allUrl.getGetGgType());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);

                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        getTypeResult = jo.getString("info");
                        if(jo.getString("status").equals("1")){
                            JSONObject ja = jo.getJSONObject("data");

                            GgType gt = new GgType();
                            JSONObject jo_1 =  ja.getJSONObject("全部");
                            gt.setGt_tag(jo_1.getString("tag"));
                            gt.setGt_word(jo_1.getString("word"));
                            allGgType.add(gt);

                            GgType gt_2 = new GgType();
                            JSONObject jo_2 =ja.getJSONObject("通栏");
                            gt_2.setGt_tag(jo_2.getString("tag"));
                            gt_2.setGt_word(jo_2.getString("word"));
                            allGgType.add(gt_2);


                            GgType gt_3 = new GgType();
                            JSONObject jo_3 = ja.getJSONObject("图文");
                            gt_3.setGt_tag(jo_3.getString("tag"));
                            gt_3.setGt_word(jo_3.getString("word"));
                            allGgType.add(gt_3);

                            GgType gt_4 = new GgType();
                            JSONObject jo_4 =ja.getJSONObject("名片");
                            gt_4.setGt_tag(jo_4.getString("tag"));
                            gt_4.setGt_word(jo_4.getString("word"));
                            allGgType.add(gt_4);

                            GgType gt_5 = new GgType();
                            JSONObject jo_5 = ja.getJSONObject("二维码");
                            gt_5.setGt_tag(jo_5.getString("tag"));
                            gt_5.setGt_word(jo_5.getString("word"));
                            allGgType.add(gt_5);
                            fHandler.sendEmptyMessage(2);

                        }else{
                            fHandler.sendEmptyMessage(1);
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

    class FmHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //获取失败，自动使用默认分类
                case 1:
                    Log.i("qqqqq","获取失败，自动使用默认分类");
                    break;
                //获取成功初始化页面
                case 2:
                    if(allGgType.size() != 0 && allGgType.size() < 6){
                        fm_tv_1.setText(allGgType.get(0).getGt_word());
                        fm_tv_2.setText(allGgType.get(1).getGt_word());
                        fm_tv_3.setText(allGgType.get(2).getGt_word());
                        fm_tv_4.setText(allGgType.get(3).getGt_word());
                        fm_tv_5.setText(allGgType.get(4).getGt_word());
                    }
                    break;
            }
        }
    }
    class GgType{
        private String gt_tag;
        private String gt_word;

        public String getGt_tag() {
            return gt_tag;
        }

        public void setGt_tag(String gt_tag) {
            this.gt_tag = gt_tag;
        }

        public String getGt_word() {
            return gt_word;
        }

        public void setGt_word(String gt_word) {
            this.gt_word = gt_word;
        }
    }
    private void initView(){
        material_vp = (ViewPager) myView.findViewById(R.id.fragment_material_vp);
        line_1 = myView.findViewById(R.id.fragment_view_1);
        line_2 = myView.findViewById(R.id.fragment_view_2);
        line_3 = myView.findViewById(R.id.fragment_view_3);
        line_4 = myView.findViewById(R.id.fragment_view_4);
        line_5 = myView.findViewById(R.id.fragment_view_5);
        tv_addm = (TextView) myView.findViewById(R.id.fragment_material_tv_addm);


        fm_tv_1 = (TextView) myView.findViewById(R.id.fragment_material_tag_1);
        fm_tv_2 = (TextView) myView.findViewById(R.id.fragment_material_tag_2);
        fm_tv_3 = (TextView) myView.findViewById(R.id.fragment_material_tag_3);
        fm_tv_4 = (TextView) myView.findViewById(R.id.fragment_material_tag_4);
        fm_tv_5 = (TextView) myView.findViewById(R.id.fragment_material_tag_5);

        tv_addm.setOnClickListener(this);
        fm_tv_1.setOnClickListener(this);
        fm_tv_2.setOnClickListener(this);
        fm_tv_3.setOnClickListener(this);
        fm_tv_4.setOnClickListener(this);
        fm_tv_5.setOnClickListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                initf_1();
                break;
            case 1:
                initf_2();
                break;
            case 2:
                initf_3();
                break;
            case 3:
                initf_4();
                break;
            case 4:
                initf_5();
                break;

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void initf_1(){
        fm_tv_1.setTextColor(getResources().getColor(R.color.colorPrimary));
        fm_tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_4.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_5.setTextColor(getResources().getColor(R.color.textcolor_hui));
        line_1.setVisibility(View.VISIBLE);
        line_2.setVisibility(View.INVISIBLE);
        line_3.setVisibility(View.INVISIBLE);
        line_4.setVisibility(View.INVISIBLE);
        line_5.setVisibility(View.INVISIBLE);
    }

    private void initf_2(){
        fm_tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_2.setTextColor(getResources().getColor(R.color.colorPrimary));
        fm_tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_4.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_5.setTextColor(getResources().getColor(R.color.textcolor_hui));
        line_1.setVisibility(View.INVISIBLE);
        line_2.setVisibility(View.VISIBLE);
        line_3.setVisibility(View.INVISIBLE);
        line_4.setVisibility(View.INVISIBLE);
        line_5.setVisibility(View.INVISIBLE);
    }
    private void initf_3(){
        fm_tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_3.setTextColor(getResources().getColor(R.color.colorPrimary));
        fm_tv_4.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_5.setTextColor(getResources().getColor(R.color.textcolor_hui));
        line_1.setVisibility(View.INVISIBLE);
        line_2.setVisibility(View.INVISIBLE);
        line_3.setVisibility(View.VISIBLE);
        line_4.setVisibility(View.INVISIBLE);
        line_5.setVisibility(View.INVISIBLE);
    }
    private void initf_4(){
        fm_tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_4.setTextColor(getResources().getColor(R.color.colorPrimary));
        fm_tv_5.setTextColor(getResources().getColor(R.color.textcolor_hui));
        line_1.setVisibility(View.INVISIBLE);
        line_2.setVisibility(View.INVISIBLE);
        line_3.setVisibility(View.INVISIBLE);
        line_4.setVisibility(View.VISIBLE);
        line_5.setVisibility(View.INVISIBLE);
    }
    private void initf_5(){
        fm_tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_4.setTextColor(getResources().getColor(R.color.textcolor_hui));
        fm_tv_5.setTextColor(getResources().getColor(R.color.colorPrimary));
        line_1.setVisibility(View.INVISIBLE);
        line_2.setVisibility(View.INVISIBLE);
        line_3.setVisibility(View.INVISIBLE);
        line_4.setVisibility(View.INVISIBLE);
        line_5.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_material_tv_addm:
                Intent setIntent = new Intent(myContex, AddMaterial.class);
                setIntent.putExtra("type",0);
                startActivityForResult(setIntent, 1);
                break;
            case R.id.fragment_material_tag_1:
                initf_1();
                material_vp.setCurrentItem(0);
                break;
            case R.id.fragment_material_tag_2:
                initf_2();
                material_vp.setCurrentItem(1);
                break;
            case R.id.fragment_material_tag_3:
                initf_3();
                material_vp.setCurrentItem(2);
                break;
            case R.id.fragment_material_tag_4:
               initf_4();
                material_vp.setCurrentItem(3);
                break;
            case R.id.fragment_material_tag_5:
                initf_5();
                material_vp.setCurrentItem(4);
                break;
        }
    }


    class MaterialPagerAdapter extends FragmentPagerAdapter{
        public MaterialPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return list_fragment.get(position);
        }
        @Override
        public int getCount() {
            return list_fragment.size();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fvm_2.initData();

    }
}
