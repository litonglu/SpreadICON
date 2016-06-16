package com.wangdao.our.spread_2.activity_;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;
import com.wangdao.our.spread_2.ExampleApplication;
import com.wangdao.our.spread_2.MainActivity;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.bean.Material;
import com.wangdao.our.spread_2.fragment_dialog.FragmentDialog_1;
import com.wangdao.our.spread_2.fragment_dialog.FragmentDialog_2;
import com.wangdao.our.spread_2.fragment_dialog.FragmentDialog_3;
import com.wangdao.our.spread_2.fragment_material.Fragment_vp_material_1;
import com.wangdao.our.spread_2.fragment_material.Fragment_vp_material_2;
import com.wangdao.our.spread_2.fragment_material.Fragment_vp_material_3;
import com.wangdao.our.spread_2.fragment_material.Fragment_vp_material_4;
import com.wangdao.our.spread_2.fragment_material.Fragment_vp_material_5;
import com.wangdao.our.spread_2.slide_widget.AllUrl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/23 0023.
 */
public class Article_info extends FragmentActivity implements View.OnClickListener{


    private boolean isPush = false;
    private final String default_url = "http://wz.ijiaque.com/app/article/articledetail.html";
    private ImageView iv_bottom;
    private TextView tv_bottom;
    private ImageView iv_cancle;
    private TextView tv_share;
    private IWXAPI api;
    private final String APP_ID = "wx24f1f8198ba7b121";
    private WebView webView;
    private boolean fristOpen = true;
    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();
    private ArticleHandler ahandler = new ArticleHandler();
    private String myUid;
    private String compielResult;
    private String myUrl;
    private  List<Material> myChoose = new ArrayList<>();
    private List<Material> myChoose_2 = new ArrayList<>();
    private List<Material> myChoose_3 = new ArrayList<>();
    private List<Material> myChoose_4 = new ArrayList<>();
    private String mt_title,my_icon_url;
    private List<Material> list_myChoose = new ArrayList<>();
    private ImageView iv_temp;
    private String myImgUrl;
    private String myContent_ = "";

    /**
     * 当前位置
     */
    private int current_int = 0;

    private Document doc;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        initView();
        initOnClick();
        api = WXAPIFactory.createWXAPI(Article_info.this, APP_ID);
        Intent gIntent = getIntent();
        myUrl = gIntent.getExtras().getString("url");
        myUid = gIntent.getExtras().getString("uid");
        mt_title = gIntent.getExtras().getString("title");
        myImgUrl = gIntent.getExtras().getString("img");
        myContent_ = gIntent.getExtras().getString("content");


            share_content = myContent_;



//        if(myUid.equals("zhan")){
//            startDialog();
//            load();
//        }


        Log.i("qqqqqqq","-----"+myUrl);
        init(myUrl);

        webView.setWebChromeClient(new WebChromeClient() {

            /**
             * 添加广告点击事件
             * @param consoleMessage
             * @return
             */
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                if (fristOpen) {
                    fristOpen = false;
                } else {
                }
                Log.i("qqqqq", consoleMessage + "---" + consoleMessage.message());
                return false;
            }
        });
    }

    private Dialog dia_wait;
    private ImageView dialog_iv;
    private void startDialog(){

        View dialog_view = getLayoutInflater().inflate(R.layout.dialog_wait_2,null);

        dia_wait = new Dialog(this,R.style.dialog);
        dia_wait.setContentView(dialog_view);

        dia_wait.setCanceledOnTouchOutside(false);

        dia_wait.setCancelable(false);

        dialog_iv  = (ImageView) dialog_view.findViewById(R.id.dialog_wait_2_iv);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.dialog_zhuang);

        LinearInterpolator lir = new LinearInterpolator();
        anim.setInterpolator(lir);

        dialog_iv.startAnimation(anim);

        dia_wait.show();

    }

    String myContent;
    private String share_content = "";
//    String myTitle;
    handler ah = new handler();
    protected void load() {
        new Thread(new Runnable() {
            @Override
            public void run() {
        try {
            doc = Jsoup.parse(new URL(myUrl), 5000);
                doc.getElementsByClass("rich_media_content");
                Element content_ = doc.getElementById("js_content");
                Elements title_ = doc.getElementsByTag("title");

                 Elements cTitle = title_.get(0).getElementsByTag("title");

                if(cTitle.isEmpty() || content_ == null){

                    ah.sendEmptyMessage(6);
                    return;
                }else {
                    share_content = content_.text();
                    mt_title = cTitle.text().toString();
                    myContent = content_.toString();

                }
        } catch (MalformedURLException e1) {
            ah.sendEmptyMessage(3);
            e1.printStackTrace();
        } catch (IOException e1) {

            ah.sendEmptyMessage(3);
            e1.printStackTrace();
        }
                ah.sendEmptyMessage(0);
            }
        }).start();
    }

class handler extends Handler{
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                sendDataToH();
                break;
            case 1:
                SharedPreferences sharedPreferences = Article_info.this.getSharedPreferences("user", MODE_PRIVATE);
                String mUid = sharedPreferences.getString("uid", "");
                myUrl = default_url+"?writing_id="+myUid+"&uid="+mUid;
                init(myUrl);
                dia_wait.dismiss();
                break;

            case 2:
                new AlertDialog.Builder(Article_info.this)
                        .setTitle("抓取失败")
                        .setMessage(sendResult)
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Article_info.this.finish();
                                dialog.dismiss();
                            }
                        })
                        .show();
                dia_wait.dismiss();
                break;

            case 3:
                new AlertDialog.Builder(Article_info.this)
                        .setTitle("抓取失败")
                        .setMessage("网址错误")
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Article_info.this.finish();
                                dialog.dismiss();
                            }
                        })
                        .show();
                dia_wait.dismiss();
                break;
            case 6:
                AlertDialog s;
                s = new AlertDialog.Builder(Article_info.this)
                        .setTitle("抓取失败")
                        .setMessage("网址错误")
                        .setCancelable(false)
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                dia_wait.dismiss();

                s.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Article_info.this.finish();
                    }
                });

            break;
        }
    }
}


    private String sendResult = "网络异常";
private void sendDataToH(){
    httpPost = new HttpPost(allurl.getCopyUrl());
    SharedPreferences sharedPreferences = Article_info.this.getSharedPreferences("user", MODE_PRIVATE);
    String mToken = sharedPreferences.getString("user_token", "");
    params.add(new BasicNameValuePair("user_token", mToken));
    params.add(new BasicNameValuePair("writing_title", mt_title));
    params.add(new BasicNameValuePair("writing_content", myContent));

new Thread(new Runnable() {
    @Override
    public void run() {
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            httpResponse = new DefaultHttpClient().execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jo = new JSONObject(result);
                 Log.i("qqqqqq",jo.toString());

                sendResult = jo.getString("info");

                if(jo.getString("status").equals("1")){
                    myUid = jo.getString("writing_id");
                    ah.sendEmptyMessage(1);
                }else{
                    ah.sendEmptyMessage(2);
                }
            }
        } catch (Exception e) {
            ah.sendEmptyMessage(2);
            e.printStackTrace();
        }
    }
}).start();
}

    private void initView(){
        iv_temp = (ImageView) findViewById(R.id.activity_article_iv_temp);
        iv_bottom = (ImageView) findViewById(R.id.activity_article_iv_bottom);
        tv_bottom = (TextView) findViewById(R.id.activity_article_tv_bottom);
        iv_cancle = (ImageView) findViewById(R.id.action_article_iv_cancle);
        tv_share = (TextView) findViewById(R.id.action_article_tv_share);
        webView = (WebView) findViewById(R.id.activity_article_wb);
    }

    private void initOnClick(){
        iv_bottom.setOnClickListener(this);
        tv_bottom.setOnClickListener(this);
        iv_cancle.setOnClickListener(this);
        tv_share.setOnClickListener(this);
    }

    private String addType = "top";

    private void init(String url){

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                        + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");

            }




            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("qqqqqqq",url);


                if (url.toString().equals("activity:2:[object Window]") || url.toString().equals("activity:1:[object Window]") || url.toString().equals("activity:1:[object DOMWindow]")|| url.toString().equals("activity:2:[object DOMWindow]") ) {

                    if (url.toString().equals("activity:2:[object DOMWindow]") || url.toString().equals("activity:2:[object Window]")) {
                        addType = "bottom";

                    } else if (url.toString().equals("activity:1:[object DOMWindow]") || url.toString().equals("activity:1:[object Window]")) {
                        addType = "top";
                    }
                    if (fristOpen) {
                        fristOpen = false;
                    } else {
                        showMaterialDialog();
                    }
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
    }



    @Override
    public void onClick(View v) {
switch (v.getId()) {
    case R.id.activity_article_iv_bottom:
    case R.id.activity_article_tv_bottom:
        showMaterialDialog();
        break;
    case R.id.action_article_iv_cancle:
        finish();
        break;
    case R.id.action_article_tv_share:
        showShareDialog();
        break;
    case R.id.dialog_select_m_tv_cancle:
        materialDialog.dismiss();
        break;
    //编辑文章 提交广告
    case R.id.dialog_select_m_tv_ok:
        httpPost = new HttpPost(allurl.getWzCompile());

        SharedPreferences sharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);
        String userToken = sharedPreferences.getString("user_token", "");
        String name_str = "";

        //使用逗号将id隔开
        for (int i = 0; i < list_myChoose.size(); i++) {
            if (i != 0) {
                if (i > 0 || i < list_myChoose.size()) {
                    name_str += ",";
                }
            }
            name_str += list_myChoose.get(i).getmId();
        }

        params.add(new BasicNameValuePair("writing_id", myUid));
        params.add(new BasicNameValuePair("user_token", userToken));
        if(addType.equals("bottom")){
            params.add(new BasicNameValuePair("writing_ads_bottom",name_str));
        }else if(addType.equals("top")){
            params.add(new BasicNameValuePair("writing_ads_top",name_str));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        compielResult = jo.getString("info");

                        if(jo.getString("status").equals("1")){
                            ahandler.sendEmptyMessage(11);
                        }else{
                            ahandler.sendEmptyMessage(12);
                        }

                    }
                } catch (Exception e) {
                    ahandler.sendEmptyMessage(12);
                    e.printStackTrace();
                }
            }
        }).start();

        materialDialog.dismiss();
        break;

    //分享到微信
    case R.id.dialog_share_iv_wx:

        SharedPreferences sharedPreferences_2 = Article_info.this.getSharedPreferences("user", MODE_PRIVATE);
        String aisVip = sharedPreferences_2.getString("isvip", "");
        String shaecount = sharedPreferences_2.getString("shaecount", "");
        Log.i("qqqqq","========"+shaecount);
        if(aisVip.equals("0")){
            if(!shaecount.equals("0")){
                new AlertDialog.Builder(Article_info.this)
                        .setTitle("提醒：")
                        .setMessage("非代理只能分享一次哦")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }else{
                TuiJianToFriend(mt_title);
            }

        }else {
            TuiJianToFriend(mt_title);
        }

        break;
    //分享到朋友圈
    case R.id.dialog_share_iv_pyq:
        SharedPreferences sharedPreferences_3 = Article_info.this.getSharedPreferences("user", MODE_PRIVATE);
        String aisVip1 = sharedPreferences_3.getString("isvip", "");
        String shaecount1 = sharedPreferences_3.getString("shaecount", "");
        Log.i("qqqqq","========"+shaecount1);
        if(aisVip1.equals("0")){
            if(!shaecount1.equals("0")){
                new AlertDialog.Builder(Article_info.this)
                        .setTitle("提醒：")
                        .setMessage("非代理只能分享一次哦")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }else{

                TuiJianToWX(mt_title);

            }

        }else {
            TuiJianToWX(mt_title);
        }
        break;
    case R.id.dialog_select_tv_1:
        current_int = 0;

        line_1.setVisibility(View.VISIBLE);
        line_2.setVisibility(View.INVISIBLE);
        line_3.setVisibility(View.INVISIBLE);
        line_4.setVisibility(View.INVISIBLE);
    //    tv_allNum.setText("5");
        if(myChoose.size() ==0){
            getTong("banner");
        }else{
            ahandler.sendEmptyMessage(1);
        }
       // myChoose.clear();
        break;
    case R.id.dialog_select_tv_2:
        current_int = 1;

        line_2.setVisibility(View.VISIBLE);
        line_1.setVisibility(View.INVISIBLE);
        line_3.setVisibility(View.INVISIBLE);
        line_4.setVisibility(View.INVISIBLE);
     //   tv_allNum.setText("5");
        if(myChoose_2.size() ==0){
            getTong("imgtext");
        }else{
            ahandler.sendEmptyMessage(1);
        }
        break;

    case R.id.dialog_select_tv_3:
        current_int = 2;
        line_3.setVisibility(View.VISIBLE);
        line_1.setVisibility(View.INVISIBLE);
        line_2.setVisibility(View.INVISIBLE);
        line_4.setVisibility(View.INVISIBLE);
      //  tv_allNum.setText("5");
        if(myChoose_3.size() ==0){
            getTong("vcard");
        }else{
            ahandler.sendEmptyMessage(1);
        }

        break;
    case R.id.dialog_select_tv_4:
        current_int = 3;
        line_4.setVisibility(View.VISIBLE);
        line_1.setVisibility(View.INVISIBLE);
        line_3.setVisibility(View.INVISIBLE);
        line_2.setVisibility(View.INVISIBLE);
     //   tv_allNum.setText("5");
     //   myChoose_4.clear();
        if(myChoose_4.size() ==0){
            getTong("tdcode");
        }else{
            ahandler.sendEmptyMessage(1);
        }
        break;
    }
    }


    private String myResult;
    private void getTong(final String type){
        list_dm.clear();
//        myChoose.clear();
//        myChoose_4.clear();
//        myChoose_2.clear();
//        myChoose_3.clear();
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);
        String tokenTemp = sharedPreferences.getString("user_token", "");
        params.add(new BasicNameValuePair("user_token", tokenTemp));
        params.add(new BasicNameValuePair("type", type));
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
                        myResult = jo.getString("info");
                        if(jo.getString("status").equals("1")){
                            JSONArray ja = jo.getJSONArray("data");
                            for(int i = 0;i<ja.length();i++){
                                JSONObject jo_2 = ja.getJSONObject(i);
                                Material material = new Material();
                                switch (type){
                                    case "banner":
                                        material.setIcon_url(jo_2.getString("ad_img"));
                                        material.setmUrl(jo_2.getString("ad_link"));
                                        material.setmType(0);
                                        material.setmId(jo_2.getString("id"));
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
                                        myChoose.add(material);
                                        //list_dm.add(material);
                                        break;
                                    case "imgtext":

                                        material.setmType(1);
                                        material.setmId(jo_2.getString("id"));
                                        material.setIcon_url(jo_2.getString("ad_img"));
                                        material.setmUrl(jo_2.getString("ad_link"));

                                        material.setmTitle(jo_2.getString("ad_pro_title"));
                                        material.setmInfo(jo_2.getString("ad_pro_des"));

                                        Log.i("eeee",jo_2.getString("ad_img"));
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
                                        myChoose_2.add(material);
                                        //list_dm.add(material);
                                          break;
                                    case "vcard":

                                        material.setmType(2);
                                        material.setmId(jo_2.getString("id"));
                                        material.setIcon_url(jo_2.getString("ad_img"));
                                        material.setmName(jo_2.getString("ad_name"));
                                        material.setmPhone(jo_2.getString("ad_mobile"));
                                        material.setmAdress(jo_2.getString("ad_address"));
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
                                        myChoose_3.add(material);
                                      //  list_dm.add(material);
                                        break;

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
                                        myChoose_4.add(material);
                                       // list_dm.add(material);
                                        break;
                                }
                                ahandler.sendEmptyMessage(1);
                            }
                        }else{
                            ahandler.sendEmptyMessage(2);
                        }
                    }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
            }
        }).start();
    }

    class ArticleHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //获取成功
                case 1:
                    list_dm.clear();
                    if(current_int ==0){
                        if(myChoose.size()!=0){
                         //   list_dm = myChoose;

                            for(int i = 0;i<myChoose.size();i++){
                                list_dm.add(myChoose.get(i));
                            }

//                        for(Material a:myChoose){
//                            list_dm.add(a);
//                        }
                        }

                    }else if(current_int ==1){



                        for(Material a:myChoose_2){
                            list_dm.add(a);
                        }

                    }else if(current_int ==2){
                        for(Material a:myChoose_3){
                            list_dm.add(a);
                        }

                    }else if(current_int ==3){
                        for(Material a:myChoose_4){
                            list_dm.add(a);
                        }

                    }

                    dialogAdapter.notifyDataSetChanged();
                    tvnull.setVisibility(View.GONE);
                    break;
                //获取失败
                case 2:
                    tvnull.setVisibility(View.VISIBLE);
                    tvnull.setText(myResult);
                    break;
                //编辑成功
                case 11:
                    fristOpen = true;
                    init(myUrl);
                    break;
                //编辑失败
                case 12:
                    init(myUrl);
                    // Toast.makeText(Article_info.this,compielResult,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private Dialog materialDialog;
    private View material_view;
    private List<Fragment> list_dialogF = new ArrayList<>();
    private ViewPager vp_dialog;
    private FragmentManager myFM;
    private ListView lv_dialog;
    private TextView dTv_1,dTv_2,dTv_3,dTv_4;
    private List<Material> list_dm = new ArrayList<>();
    private MaterialAdapter dialogAdapter;
    private  TextView tvnull;
    private View line_1,line_2,line_3,line_4;
    private TextView tv_allNum,tv_title;
    private void showMaterialDialog(){
        httpPost = new HttpPost(allurl.getGgList());
        myFM = this.getSupportFragmentManager();
        TextView tv_cancle,tv_ok;
        material_view = getLayoutInflater().inflate(R.layout.dialog_select_m,null);
        tv_cancle = (TextView) material_view.findViewById(R.id.dialog_select_m_tv_cancle);
        tv_ok = (TextView) material_view.findViewById(R.id.dialog_select_m_tv_ok);
        tv_title = (TextView) material_view.findViewById(R.id.dialog_select_m_tv_title);
        vp_dialog = (ViewPager) material_view.findViewById(R.id.fragment_material_vp);
        lv_dialog = (ListView) material_view.findViewById(R.id.dialog_select_m_lv);
        tvnull = (TextView) material_view.findViewById(R.id.dialog_select_m_tvnull);
        dTv_1 = (TextView) material_view.findViewById(R.id.dialog_select_tv_1);
        dTv_2 = (TextView) material_view.findViewById(R.id.dialog_select_tv_2);
        dTv_3 = (TextView) material_view.findViewById(R.id.dialog_select_tv_3);
        dTv_4 = (TextView) material_view.findViewById(R.id.dialog_select_tv_4);
        tv_allNum = (TextView) material_view.findViewById(R.id.dialog_select_m_tv_num);
        line_1 = material_view.findViewById(R.id.dialog_select_m_line_1);
        line_2 = material_view.findViewById(R.id.dialog_select_m_line_2);
        line_3 = material_view.findViewById(R.id.dialog_select_m_line_3);
        line_4 = material_view.findViewById(R.id.dialog_select_m_line_4);


        list_dm.clear();
//        if(current_int ==0){
//            list_dm = myChoose;
//        }else if(current_int ==1){
//            list_dm = myChoose_2;
//        }else if(current_int ==2){
//            list_dm = myChoose_3;
//        }else if(current_int ==3){
//            list_dm = myChoose_4;
//        }

        materialDialog = new Dialog(this,R.style.dialog);
        materialDialog.setContentView(material_view);
        materialDialog.show();
        dialogAdapter = new MaterialAdapter(list_dm);
        lv_dialog.setAdapter(dialogAdapter);

//        MaterialPagerAdapter mpa = new MaterialPagerAdapter(myFM);
//       // vp_dialog.setOffscreenPageLimit(4);
//        vp_dialog.setAdapter(mpa);

        if(addType.equals("top")){
            tv_title.setText("头部还能选择：");
        }else{
            tv_title.setText("底部还能选择：");
        }


        getTong("banner");
        tv_cancle.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
        dTv_1.setOnClickListener(this);
        dTv_2.setOnClickListener(this);
        dTv_3.setOnClickListener(this);
        dTv_4.setOnClickListener(this);

        materialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isPush = false;
                list_myChoose.clear();
                myChoose.clear();
                myChoose_2.clear();
                myChoose_3.clear();
                myChoose_4.clear();
                current_int = 0;
            }
        });
    }


    private boolean mm;
    private Dialog dialog_help;
    View view;
    TextView iv_wx,iv_pyq;
    private void showShareDialog() {
            view = getLayoutInflater().inflate(R.layout.dialog_share, null);
            iv_wx = (TextView) view.findViewById(R.id.dialog_share_iv_wx);
            iv_pyq = (TextView) view.findViewById(R.id.dialog_share_iv_pyq);
            dialog_help = new Dialog(this, R.style.dialog);
            dialog_help.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            Window window = dialog_help.getWindow();
            window.setWindowAnimations(R.style.main_menu_animstyle);
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = this.getWindowManager().getDefaultDisplay().getHeight();
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog_help.onWindowAttributesChanged(wl);
        dialog_help.setCanceledOnTouchOutside(true);
        dialog_help.show();
        iv_wx.setOnClickListener(this);
        iv_pyq.setOnClickListener(this);
    }

    class MaterialAdapter extends BaseAdapter {
        Fm1_ViewHolder FVH ;
        List<Material> my_Material;
        //AsynImageLoader asynImageLoader = new AsynImageLoader();
        int allNum = 5;
        // 用来控制CheckBox的选中状况
        private HashMap<Integer, Boolean> isSelected = new HashMap<>();

        public MaterialAdapter(List<Material> my_Material) {
            this.my_Material = my_Material;
            initDate();
        }

        // 初始化isSelected的数据
        private void initDate() {
            for (int i = 0; i < my_Material.size(); i++) {
                isSelected.put(i, false);
            }
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                FVH = new Fm1_ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_material, null);
                FVH.mIcon = (ImageView) convertView.findViewById(R.id.item_material_icon);
                FVH.mTitle = (TextView) convertView.findViewById(R.id.item_material_title);
                FVH.mInfo = (TextView) convertView.findViewById(R.id.item_material_info);
                FVH.mAll_icon = (ImageView) convertView.findViewById(R.id.item_material_daicon);
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


            if(current_int ==0) {
                FVH.ck_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isPush = true;
                        if (!myChoose.get(position).isChoose()) {
                            if (allNum > 0) {
                                allNum -= 1;
                                tv_allNum.setText(allNum + "");
                                myChoose.get(position).setIsChoose(true);

                                list_myChoose.add(my_Material.get(position));

                                dialogAdapter.notifyDataSetChanged();
                            } else {
                                tv_allNum.setText(allNum + "");
                            }
                        } else {

                            if (allNum < 5) {
                                allNum += 1;
                                tv_allNum.setText(allNum + "");
                                myChoose.get(position).setIsChoose(false);
                                list_myChoose.remove(my_Material.get(position));

                                dialogAdapter.notifyDataSetChanged();
                            } else {
                                //少了
                                tv_allNum.setText(allNum + "");
                            }
                        }
                    }
                });
            }else if(current_int == 1){
                FVH.ck_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!myChoose_2.get(position).isChoose()) {

                            if (allNum > 0) {
                                allNum -= 1;
                                tv_allNum.setText(allNum + "");
                                myChoose_2.get(position).setIsChoose(true);

                                list_myChoose.add(my_Material.get(position));
                                dialogAdapter.notifyDataSetChanged();
                            } else {
                                tv_allNum.setText(allNum + "");
                            }
                        } else {
                            if (allNum < 5) {
                                allNum += 1;
                                tv_allNum.setText(allNum + "");
                                myChoose_2.get(position).setIsChoose(false);

                                list_myChoose.remove(my_Material.get(position));

                                dialogAdapter.notifyDataSetChanged();
                            } else {
                                //少了
                                tv_allNum.setText(allNum + "");
                            }
                        }
                    }
                });
            }else if(current_int == 2){
                FVH.ck_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!myChoose_3.get(position).isChoose()) {
                            if (allNum > 0) {
                                allNum -= 1;
                                tv_allNum.setText(allNum + "");
                                myChoose_3.get(position).setIsChoose(true);

                                list_myChoose.add(my_Material.get(position));

                                dialogAdapter.notifyDataSetChanged();
                            } else {
                                tv_allNum.setText(allNum + "");

                            }
                        } else {
                            if (allNum < 5) {
                                allNum += 1;
                                tv_allNum.setText(allNum + "");
                                myChoose_3.get(position).setIsChoose(false);

                                list_myChoose.remove(my_Material.get(position));

                                dialogAdapter.notifyDataSetChanged();
                            } else {
                                //少了
                                tv_allNum.setText(allNum + "");
                            }
                        }
                    }
                });

            }else if(current_int ==3){
                FVH.ck_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!myChoose_4.get(position).isChoose()) {

                            if (allNum > 0) {
                                allNum -= 1;
                                tv_allNum.setText(allNum + "");
                                myChoose_4.get(position).setIsChoose(true);

                                list_myChoose.add(my_Material.get(position));

                                dialogAdapter.notifyDataSetChanged();
                            } else {
                                tv_allNum.setText(allNum + "");

                            }
                        } else {
                            if (allNum < 5) {
                                allNum += 1;
                                tv_allNum.setText(allNum + "");
                                myChoose_4.get(position).setIsChoose(false);
                                list_myChoose.remove(my_Material.get(position));
                                dialogAdapter.notifyDataSetChanged();
                            } else {
                                //少了
                                tv_allNum.setText(allNum + "");
                            }
                        }
                    }
                });
            }

            if(current_int ==0) {

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isPush = true;
                        if (!myChoose.get(position).isChoose()) {
                            if (allNum > 0) {
                                Log.i("qqqqq", "list_size=" + list_dm.size());
                                allNum -= 1;
                                tv_allNum.setText(allNum + "");
                                myChoose.get(position).setIsChoose(true);

                               // list_dm = myChoose;
                                Log.i("qqqqq","list_size="+list_dm.size());
                                dialogAdapter.notifyDataSetChanged();

                                list_myChoose.add(my_Material.get(position));
                            } else {
                                //多了
                                tv_allNum.setText(allNum + "");
                            }
                        } else {
                            if (allNum < 5) {
                                allNum += 1;
                                tv_allNum.setText(allNum + "");
                                myChoose.get(position).setIsChoose(false);


                                list_myChoose.remove(my_Material.get(position));
                            //    list_dm = myChoose;
                                dialogAdapter.notifyDataSetChanged();
                            } else {
                                //少了
                                tv_allNum.setText(allNum + "");
                            }
                        }
                    }
                });
            }else if(current_int ==1){
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isPush = true;
                        if (!myChoose_2.get(position).isChoose()) {
                            if (allNum > 0) {
                                allNum -= 1;
                                tv_allNum.setText(allNum + "");
                                myChoose_2.get(position).setIsChoose(true);

                                list_myChoose.add(my_Material.get(position));
                               // list_dm = myChoose_2;
                                dialogAdapter.notifyDataSetChanged();

                            } else {
                                //多了
                                tv_allNum.setText(allNum + "");
                            }
                        } else {
                            if (allNum < 5) {
                                allNum += 1;
                                tv_allNum.setText(allNum + "");
                                myChoose_2.get(position).setIsChoose(false);

                                list_myChoose.remove(my_Material.get(position));
                                list_dm = myChoose_2;
                                dialogAdapter.notifyDataSetChanged();
                            } else {
                                //少了
                                tv_allNum.setText(allNum + "");
                            }
                        }
                    }
                });
            }else if(current_int ==2){
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!myChoose_3.get(position).isChoose()) {
                            if (allNum > 0) {
                                allNum -= 1;
                                tv_allNum.setText(allNum + "");
                                my_Material.get(position).setIsChoose(true);
                                list_myChoose.add(my_Material.get(position));

                                dialogAdapter.notifyDataSetChanged();
                            } else {
                                //多了
                                tv_allNum.setText(allNum + "");
                            }
                        } else {
                            if (allNum < 5) {
                                allNum += 1;
                                tv_allNum.setText(allNum + "");
                                my_Material.get(position).setIsChoose(false);
                                list_myChoose.remove(my_Material.get(position));

                                dialogAdapter.notifyDataSetChanged();
                            } else {
                                //少了
                                tv_allNum.setText(allNum + "");
                            }
                        }
                    }
                });

            }else if(current_int ==3){
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!myChoose_4.get(position).isChoose()) {
                            if (allNum > 0) {
                                allNum -= 1;
                                tv_allNum.setText(allNum + "");
                                myChoose_4.get(position).setIsChoose(true);

                                list_myChoose.add(my_Material.get(position));

                                dialogAdapter.notifyDataSetChanged();
                            } else {
                                //多了
                                tv_allNum.setText(allNum + "");
                            }
                        } else {
                            if (allNum < 5) {
                                allNum += 1;
                                tv_allNum.setText(allNum + "");
                                myChoose_4.get(position).setIsChoose(false);
                                list_myChoose.remove(my_Material.get(position));
                                dialogAdapter.notifyDataSetChanged();
                            } else {
                                //少了
                                tv_allNum.setText(allNum + "");
                            }
                        }
                    }
                });
            }

            FVH.ck_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    dialogAdapter.notifyDataSetChanged();
                }
            });

            FVH.ck_1.setChecked(list_dm.get(position).isChoose());
            FVH.tv_tag_1.setVisibility(View.GONE);
            FVH.tv_tag_2.setVisibility(View.GONE);
            FVH.tv_tag_3.setVisibility(View.GONE);

            Log.i("aaaaaa",my_Material.get(position).getIcon_url());

            if(my_Material.get(position).getmType() == 0){ //通知栏
                FVH.ll_icon.setVisibility(View.INVISIBLE);
                FVH.ll_info.setVisibility(View.INVISIBLE);
                FVH.mAll_icon.setVisibility(View.VISIBLE);
                FVH.mAdress.setVisibility(View.GONE);
                FVH.tv_tag_1.setText(R.string.tag_tong);
                FVH.mTitle.setText(my_Material.get(position).getmTitle());
                FVH.mInfo.setText(my_Material.get(position).getmInfo());
           //     asynImageLoader.showImageAsyn(FVH.mAll_icon, my_Material.get(position).getIcon_url(), R.drawable.nopic);

                if(!isPush){
                    ImageLoader.getInstance().displayImage(my_Material.get(position).getIcon_url() ==null?"":my_Material.get(position).getIcon_url(),FVH.mAll_icon,
                            ExampleApplication.getInstance().getOptions(R.drawable.nopic));
                }



            }else if(my_Material.get(position).getmType() == 1){
                FVH.ll_icon.setVisibility(View.VISIBLE);
                FVH.ll_info.setVisibility(View.VISIBLE);
                FVH.mAll_icon.setVisibility(View.INVISIBLE);
                FVH.mAdress.setVisibility(View.GONE);
                FVH.tv_tag_1.setText(R.string.tag_tuwen);
                FVH.mTitle.setText(my_Material.get(position).getmTitle());
                FVH.mInfo.setText(my_Material.get(position).getmInfo());
             //   asynImageLoader.showImageAsyn(FVH.mIcon, my_Material.get(position).getIcon_url(), R.drawable.nopic);

                ImageLoader.getInstance().displayImage(my_Material.get(position).getIcon_url() ==null?"":my_Material.get(position).getIcon_url(),FVH.mAll_icon,
                        ExampleApplication.getInstance().getOptions(R.drawable.nopic));

            }else if(my_Material.get(position).getmType() == 2){
                FVH.ll_icon.setVisibility(View.VISIBLE);
                FVH.ll_info.setVisibility(View.VISIBLE);
                FVH.mAll_icon.setVisibility(View.INVISIBLE);
                FVH.tv_tag_1.setText(R.string.tag_mingpian);

                FVH.mTitle.setText(my_Material.get(position).getmName());
                FVH.mInfo.setText(my_Material.get(position).getmPhone());
                FVH.mAdress.setText(my_Material.get(position).getmAdress());

            //    asynImageLoader.showImageAsyn(FVH.mIcon, my_Material.get(position).getIcon_url(), R.drawable.nopic);

                ImageLoader.getInstance().displayImage(my_Material.get(position).getIcon_url() ==null?"":my_Material.get(position).getIcon_url(),FVH.mAll_icon,
                        ExampleApplication.getInstance().getOptions(R.drawable.nopic));


            }else if(my_Material.get(position).getmType() == 3){
                FVH.ll_icon.setVisibility(View.VISIBLE);
                FVH.ll_info.setVisibility(View.VISIBLE);
                FVH.mAll_icon.setVisibility(View.INVISIBLE);
                FVH.tv_tag_1.setText(R.string.tag_erweima);
                FVH.mAdress.setVisibility(View.VISIBLE);
                FVH.mAdress.setVisibility(View.GONE);
                FVH.mTitle.setText(my_Material.get(position).getmTitle());
                FVH.mInfo.setText(my_Material.get(position).getmInfo());
              //  asynImageLoader.showImageAsyn(FVH.mIcon, my_Material.get(position).getIcon_url(), R.drawable.nopic);


                ImageLoader.getInstance().displayImage(my_Material.get(position).getIcon_url() ==null?"":my_Material.get(position).getIcon_url(),FVH.mAll_icon,
                        ExampleApplication.getInstance().getOptions(R.drawable.nopic));
            }
            return convertView;
        }
    }

    class Fm1_ViewHolder{

        ImageView mIcon;
        TextView mTitle;
        TextView mInfo;
        TextView mAdress;
        ImageView mAll_icon;
        LinearLayout ll_icon;
        LinearLayout ll_info;
        TextView tv_tag_1;
        TextView tv_tag_2;
        TextView tv_tag_3;
        CheckBox ck_1;

    }

    /**
     * 分享到微信好友
     */
    Bitmap thumb_1;
    WXMediaMessage msg_1;
    private void TuiJianToFriend(String str){

        WXWebpageObject webpage_2 = new WXWebpageObject();
        webpage_2.webpageUrl = myUrl+"&share=true";
        msg_1 = new WXMediaMessage(webpage_2);

        if(str.equals("null")){
            msg_1.title  = "轻松分享,拇指营销";
        }else {
                msg_1.title = str;
        }
            msg_1.description = myContent_;

//        if(myImgUrl != null){
//
//            new LoadPicThread(myImgUrl,new Handler(){
//
//                @Override
//                public void handleMessage(Message msg) {
//                   byte[] b = (byte[]) msg.obj;
//
//                    msg_1.thumbData = Util.bmpToByteArray(Bytes2Bimap(b), false);
//                    SendMessageToWX.Req req = new SendMessageToWX.Req();
//                    req.transaction = buildTransaction("webpage");
//                    req.message = msg_1;
//                    req.scene = SendMessageToWX.Req.WXSceneSession;
//                    api.sendReq(req);
//                }
//            });
//        }else{
//
//Bitmap bb = BitmapFactory.d
//        }

     //   Log.i("qqqqqqqqqq",myImgUrl+"-------------");
        ImageLoader.getInstance().displayImage(myImgUrl == null  ? "" : myImgUrl,iv_temp,
                ExampleApplication.getInstance().getOptions(R.drawable.icon_2),
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);

                                if(imageUri.equals("null")){

                                    thumb_1 = BitmapFactory.decodeResource(Article_info.this.getResources(),R.drawable.icon_2);
                                    msg_1.thumbData = Util.bmpToByteArray(thumb_1, false);

                                }else{
                                    thumb_1 =  yaSuoImage(loadedImage);
                                    msg_1.thumbData = Util.bmpToByteArray(thumb_1, false);
                                }

                            Log.i("qqqqqqqq",imageUri+"------------");


                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = buildTransaction("webpage");
                            req.message = msg_1;
                            req.scene = SendMessageToWX.Req.WXSceneSession;
                            api.sendReq(req);
                            SharedPreferences sharedPreferences = Article_info.this.getSharedPreferences("shareid", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("sid", myUid);
                            editor.commit();
                            dialog_help.dismiss();
                        }
                    }
        );
    }


//    public Bitmap Bytes2Bimap(byte[] b) {
//        if(b.length != 0) {
//            return BitmapFactory.decodeByteArray(b, 0, b.length);
//            }else{
//            return null;
//            }
//        }


    /**
     * 压缩图片
     */
    private Bitmap yaSuoImage(Bitmap image){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if(image == null){
            Bitmap bmm = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_2);
            return bmm;
        }

        image.compress(Bitmap.CompressFormat.JPEG, 100, out);

        float zoom = (float)Math.sqrt(10 * 1024 / (float)out.toByteArray().length);

        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);

        Bitmap result = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);

        out.reset();
        result.compress(Bitmap.CompressFormat.JPEG, 100, out);

        while(out.toByteArray().length > 10 * 1024){
            System.out.println(out.toByteArray().length);
            matrix.setScale(0.9f, 0.9f);
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
            out.reset();
             result.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }
        return result;
    }

    public static final int IMAGE_SIZE=32768;//微信分享图片大小限制
    private class LoadPicThread extends Thread{
        private String url;
        private Handler handler;
        public LoadPicThread(String url,Handler handler){
            this.url=url;
            this.handler=handler;
        }

        @Override
        public void run(){
            try {

                URL picurl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection)picurl.openConnection(); // 获得连接
                conn.setConnectTimeout(6000);//设置超时
                conn.setDoInput(true);
                conn.setUseCaches(false);//不缓存
                conn.connect();
                Bitmap bmp=BitmapFactory.decodeStream(conn.getInputStream());

                ByteArrayOutputStream output = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
                int options = 100;
                while (output.toByteArray().length > IMAGE_SIZE && options != 10) {
                    output.reset(); //清空baos
                    bmp.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到baos中
                    options -= 10;
                }

                bmp.recycle();
                byte[] result = output.toByteArray();
                output.close();

                Message message=handler.obtainMessage();
                message.obj=result;
                message.sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * 分享到朋友圈
     */
    Bitmap thumb_2;
    WXMediaMessage msg_2;
    private void TuiJianToWX(String str){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = myUrl+"&share=true";
        msg_2 = new WXMediaMessage(webpage);
        if(str.equals("null")){
            msg_2.title  = "轻松分享,拇指营销";
        }else {
            msg_2.title = str;
        }


        msg_2.description = myContent_;

        ImageLoader.getInstance().displayImage(myImgUrl.equals("null") ? "" : myImgUrl,iv_temp,
                ExampleApplication.getInstance().getOptions(R.drawable.icon_2),
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);

                        if(imageUri.equals("null")){
                            thumb_2 = BitmapFactory.decodeResource(Article_info.this.getResources(),R.drawable.icon_2);
                            msg_2.thumbData = Util.bmpToByteArray(thumb_1, false);
                        }else{
                            thumb_2 = yaSuoImage(loadedImage);
                            msg_2.thumbData = Util.bmpToByteArray(thumb_2, true);
                        }

                        SendMessageToWX.Req req = new SendMessageToWX.Req();

                        req.transaction = buildTransaction("webpage");

                        req.message = msg_2;

                        req.scene = SendMessageToWX.Req.WXSceneTimeline;

                        api.sendReq(req);

                        SharedPreferences sharedPreferences = Article_info.this.getSharedPreferences("shareid", MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("sid",myUid);

                        editor.commit();

                        dialog_help.dismiss();
                    }
                }
        );



    }


    public static Bitmap getImage(String urlpath) throws Exception {
        URL url = new URL(urlpath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        Bitmap bitmap = null;
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        }
        return bitmap;
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
