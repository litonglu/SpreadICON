package com.wangdao.our.spread_2.fragment_myteam;

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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangdao.our.spread_2.ExampleApplication;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.activity_.mine_activity.Team_info;
import com.wangdao.our.spread_2.bean.Team;
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
 * Created by Administrator on 2016/5/12 0012.
 * 我的团队--   一级
 */
public class MyTeam_1 extends Fragment{

    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();

    private ListView mT1_lv;
    private View myView;
    private Context myContext;
    private LayoutInflater myInflater;
    private MyTeam1_Handler myTeam1_handler = new MyTeam1_Handler();
    private List<Team> list_team = new ArrayList<>();


    private TextView tv_num;
    private TextView tv_erro;
    private MyTeamAdapter my1Adapter ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_myteam_1,null);
        myContext = this.getActivity();
        myInflater = inflater;

        initView();

        my1Adapter = new MyTeamAdapter(list_team);
        mT1_lv.setAdapter(my1Adapter);
        initData();

        mT1_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent pIntent = new Intent(myContext,Team_info.class);
                pIntent.putExtra("id",list_team.get(position).getUid());
                startActivity(pIntent);

            }
        });
        return myView;
    }

    private void initView(){
        tv_num = (TextView) myView.findViewById(R.id.fragment_myteam_1_tv_num);
        mT1_lv = (ListView) myView.findViewById(R.id.fragment_myteam_1_lv);
        tv_erro = (TextView) myView.findViewById(R.id.fragment_myteam_1_tv_erro);
    }

    /**
     * 初始化数据
     */
    private String initDataResult = "网络异常";
    private void initData(){
        httpPost = new HttpPost(allurl.getUserTeam());
        SharedPreferences sharedPreferences = myContext.getSharedPreferences("user", myContext.MODE_PRIVATE);
        String mToken = sharedPreferences.getString("user_token", "");
        params.add(new BasicNameValuePair("user_token", mToken));
        params.add(new BasicNameValuePair("type", "1"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        initDataResult = jo.getString("info");
                        if(jo.getString("status").equals("1")){

                            JSONArray ja = jo.getJSONArray("data");
                            for(int i = 0;i<ja.length();i++){
                                JSONObject jo_2 = ja.getJSONObject(i);
                                Team uTeam = new Team();

                                uTeam.setUid(jo_2.getString("uid"));

                                uTeam.setAddTime(jo_2.getString("create_time"));
                                JSONObject jo_3 = jo_2.getJSONObject("userinfo");
                                uTeam.setIcon_url(jo_3.getString("avatar256"));
                                uTeam.setName(jo_3.getString("nickname"));
                                uTeam.setLoginTime(jo_3.getString("last_login_time"));
                                uTeam.setIsVip(jo_3.getString("agents_level"));

                                list_team.add(uTeam);
                            }

                            myTeam1_handler.sendEmptyMessage(1);
                        }else{
                            myTeam1_handler.sendEmptyMessage(2);
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

    class MyTeam1_Handler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //获取数据成功
                case 1:
                    tv_num.setText("("+list_team.size()+")");


                    my1Adapter.notifyDataSetChanged();
                    tv_erro.setVisibility(View.GONE);
                    break;
                //获取数据失败
                case 2:
                    tv_erro.setText(initDataResult);
                    tv_erro.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }


    class MyTeamAdapter extends BaseAdapter {
        MyTeamHolder mtHoledr = null;

        private List<Team> teams;
        public MyTeamAdapter(List<Team> list_team){
            this.teams = list_team;
        }

        @Override
        public int getCount() {
            return teams.size();
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
            if(convertView ==null){
                convertView = myInflater.inflate(R.layout.item_myteam,null);
                mtHoledr = new MyTeamHolder();
                mtHoledr.tIv_icon = (CircleImageView) convertView.findViewById(R.id.item_myteam_iv_icon);
                mtHoledr.tTv_name = (TextView) convertView.findViewById(R.id.item_myteam_tv_name);
                mtHoledr.tTv_time = (TextView) convertView.findViewById(R.id.item_myteam_tv_time);
                mtHoledr.tTv_time_lately = (TextView) convertView.findViewById(R.id.item_myteam_tv_time_lately);
                mtHoledr.tTv_member = (TextView) convertView.findViewById(R.id.item_myteam_member);
                mtHoledr.iv_vip = (ImageView) convertView.findViewById(R.id.item_myteam_iv_vip);
                convertView.setTag(mtHoledr);
            }else{
                mtHoledr = (MyTeamHolder) convertView.getTag();
            }

            ImageLoader.getInstance().displayImage(teams.get(position).getIcon_url() == null ? "" : teams.get(position).getIcon_url(), mtHoledr.tIv_icon,
                    ExampleApplication.getInstance().getOptions(R.drawable.nopilc_2));


            if(teams.get(position).getIsVip().equals("3")){
                mtHoledr.iv_vip.setVisibility(View.VISIBLE);
            }else{
                mtHoledr.iv_vip.setVisibility(View.INVISIBLE);
            }


            mtHoledr.tTv_name.setText(teams.get(position).getName());
            mtHoledr.tTv_time.setText(teams.get(position).getAddTime());
            mtHoledr.tTv_time_lately.setText(teams.get(position).getLoginTime());
            mtHoledr.tTv_member.setText("一级会员");
            return convertView;
        }
    }

    class MyTeamHolder{
        CircleImageView tIv_icon;
        TextView tTv_name;
        TextView tTv_time;
        TextView tTv_time_lately;
        TextView tTv_member;
        ImageView iv_vip;
    }
}
