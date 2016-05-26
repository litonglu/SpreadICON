package com.wangdao.our.spread_2.activity_.mine_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;
import com.wangdao.our.spread_2.ExampleApplication;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.activity_.AboutInfo;
import com.wangdao.our.spread_2.activity_.Article_info;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/9 0009.
 *  推广
 */
public class Popularize extends Activity implements View.OnClickListener{

    private ImageView iv_cancle;
    private TextView tv_share;
    private IWXAPI api;
    private final String APP_ID = "wx24f1f8198ba7b121";
    private ImageView iv_temp;
    private PopularizeHandler popHandler = new PopularizeHandler();
    private HttpPost httpPost;
    private HttpPost httpPost_2;
    private HttpPost httpPost_3;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();
    private CircleImageView pIv_icon;
    private Bitmap myIcon;
    private ImageView iv_erweima;
    private String userIconUrl;
    private TextView tv_shareName;
    private ProgressBar popularize_pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popularize);

        api = WXAPIFactory.createWXAPI(Popularize.this, APP_ID);

        initView();
        initClick();
        initData();
        initErWeiMa();
        initName();
    }
    private void initView(){
        iv_cancle = (ImageView) findViewById(R.id.activity_popularize_iv_cancle);
        tv_share = (TextView) findViewById(R.id.activity_popularize_tv_share);
        iv_temp = (ImageView) findViewById(R.id.popularize_iv_temp);
        pIv_icon = (CircleImageView) findViewById(R.id.activity_popularize_iv_icon);
        iv_erweima = (ImageView) findViewById(R.id.activity_popularize_iv_erweima);
        tv_shareName = (TextView) findViewById(R.id.activity_popularize_tv_name);
        popularize_pb = (ProgressBar) findViewById(R.id.activity_popularize_pb);
    }

    private void initClick(){
        iv_cancle.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        iv_erweima.setOnClickListener(this);
    }

    /**
     * 初始化头像
     */
    private void initData(){
        SharedPreferences sharedPreferences = Popularize.this.getSharedPreferences("user", MODE_PRIVATE);
        userIconUrl = sharedPreferences.getString("avatar256", "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    myIcon = getImage(userIconUrl);
                    popHandler.sendEmptyMessage(21);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    // 获取指定路径的图片
    public static Bitmap getImage(String urlpath) throws IOException {
        URL url = new URL(urlpath);
        InputStream is=url.openStream();
        return  BitmapFactory.decodeStream(is);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回
            case R.id.activity_popularize_iv_cancle:
                finish();
                break;
            //分享
            case R.id.activity_popularize_tv_share:
                showShareDialog();
                break;
            //分享到微信
            case R.id.dialog_share_iv_wx:
                getShareInfo(1);
                break;
            //分享到朋友圈
            case R.id.dialog_share_iv_pyq:
                getShareInfo(2);
                break;
            //点击二维码
            case R.id.activity_popularize_iv_erweima:
                showIsSaveDialog();
                break;
            //确定保存二维码到本地
            case R.id.dialog_logout_cancle:

            //        saveBitmap(bmp_icon);

                try {
                    startSave(bmp_icon);
                } catch (IOException e) {
                    Log.i("qqqqqq","保存错误"+e);
                    e.printStackTrace();
                }

                materialDialog.dismiss();
                break;
            //取消保存二维码
            case R.id.dialog_logout_yes:
                materialDialog.dismiss();
                break;
        }
    }


    /**
     * 是否保存dialog
     */
    private Dialog materialDialog;
    private void showIsSaveDialog(){
        View material_view;
        TextView tv_cancle,tv_ok,tv_dialog_info;

            material_view = getLayoutInflater().inflate(R.layout.dialog_logout,null);
            tv_cancle = (TextView) material_view.findViewById(R.id.dialog_logout_cancle);
        tv_dialog_info = (TextView) material_view.findViewById(R.id.dialog_logout_tv_info);
            tv_ok = (TextView) material_view.findViewById(R.id.dialog_logout_yes);
            materialDialog = new Dialog(this,R.style.dialog);
            materialDialog.setContentView(material_view);
        tv_dialog_info.setText("保存到本地?");
        tv_cancle.setText("是");
        tv_ok.setText("否");

            materialDialog.show();

            tv_cancle.setOnClickListener(this);
            tv_ok.setOnClickListener(this);

    }

    private void startSave(Bitmap bmp) throws IOException {
        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOut = null;
        File file = new File(path, "FitnessGirl" + "myShare" + ".jpg");
        fOut = new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        fOut.flush();
        fOut.close();
        MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        Toast.makeText(Popularize.this,"成功保存到根目录",Toast.LENGTH_SHORT).show();
    }

    class PopularizeHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //分享到微信
                case 1:
                    TuiJianToFriend();
                    break;
                //获取分享信息失败
                case 2:

                    new AlertDialog.Builder(Popularize.this)
                            .setTitle("自动登录失败")
                            .setMessage(shareInfoResult)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                    break;
                //分享到朋友圈
                case 11:
                    TuiJianToWX();
                    break;
                //初始化头像
                case 21:
                    pIv_icon.setImageBitmap(myIcon);
                    break;
                //初始化二维码
                case 31:
                    popularize_pb.setVisibility(View.GONE);
                    iv_erweima.setImageBitmap(bmp_icon);
                    break;
                //初始化二维码失败
                case 32:
                    popularize_pb.setVisibility(View.GONE);
                    Toast.makeText(Popularize.this,"二维码生成失败，请重试",Toast.LENGTH_SHORT).show();
                    break;
                //初始化姓名
                case 41:
                    tv_shareName.setText(mNickName);
                    break;
            }
        }
    }

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

    /**
     * 获得分享出去的信息
     */
    private String shareIcon,shareTitle,shareContent;
    private String shareInfoResult = "网络异常";
    private void getShareInfo(final int ty){
        httpPost = new HttpPost(allurl.getShareInfo());
        SharedPreferences sharedPreferences = Popularize.this.getSharedPreferences("user", MODE_PRIVATE);
        String mUid = sharedPreferences.getString("uid", "");
        params.add(new BasicNameValuePair("link", mUid));
new Thread(new Runnable() {
    @Override
    public void run() {
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            httpResponse = new DefaultHttpClient().execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jo = new JSONObject(result);
                shareInfoResult = jo.getString("info");
                if(jo.getString("status").equals("1")){
                    JSONObject jo_2 = jo.getJSONObject("data");
                    shareIcon = jo_2.getString("logo");
                    shareTitle = jo_2.getString("title");
                    shareContent = jo_2.getString("des");
                    if(ty ==1){
                        popHandler.sendEmptyMessage(1);
                    }else{
                        popHandler.sendEmptyMessage(11);
                    }

                }else{
                        popHandler.sendEmptyMessage(2);
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
     * 分享到微信好友
     */
    private Bitmap thumb_1;
    private WXMediaMessage msg_1;
    private String mUid;
    private void TuiJianToFriend(){
        SharedPreferences sharedPreferences = Popularize.this.getSharedPreferences("user", MODE_PRIVATE);
         mUid = sharedPreferences.getString("uid", "");

        WXWebpageObject webpage_2 = new WXWebpageObject();
        webpage_2.webpageUrl = allurl.getShare_Url()+"?link="+mUid;
        msg_1 = new WXMediaMessage(webpage_2);

        msg_1.title  = shareTitle;
        msg_1.description = shareContent;

        ImageLoader.getInstance().displayImage(shareIcon == null ? "" : shareIcon,iv_temp,
                ExampleApplication.getInstance().getOptions(R.drawable.icon),
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        thumb_1 =  yaSuoImage(loadedImage);
                        msg_1.thumbData = Util.bmpToByteArray(thumb_1, false);
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("webpage");
                        req.message = msg_1;
                        req.scene = SendMessageToWX.Req.WXSceneSession;
                        api.sendReq(req);
                        dialog_help.dismiss();
                    }
                }
        );
    }
    /**
     * 分享到朋友圈
     */
    Bitmap thumb_2;
    WXMediaMessage msg_2;
    private void TuiJianToWX(){
        SharedPreferences sharedPreferences = Popularize.this.getSharedPreferences("user", MODE_PRIVATE);
        String mUid = sharedPreferences.getString("uid", "");

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = allurl.getShare_Url()+"?link="+mUid;
        msg_2 = new WXMediaMessage(webpage);

        msg_2.title  = shareTitle;
        msg_2.description = shareContent;

        ImageLoader.getInstance().displayImage(shareIcon == null ? "" : shareIcon,iv_temp,
                ExampleApplication.getInstance().getOptions(R.drawable.icon),
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        thumb_2 = yaSuoImage(loadedImage);
                        msg_2.thumbData = Util.bmpToByteArray(thumb_2, true);
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("webpage");
                        req.message = msg_2;
                        req.scene = SendMessageToWX.Req.WXSceneTimeline;
                        api.sendReq(req);
                        dialog_help.dismiss();
                    }
                }
        );
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    /**
     * 压缩图片
     */
    private Bitmap yaSuoImage(Bitmap image){

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 85, out);
        float zoom = (float)Math.sqrt(10 * 1024 / (float)out.toByteArray().length);

        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);

        Bitmap result = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);

        out.reset();
        result.compress(Bitmap.CompressFormat.JPEG, 85, out);
        while(out.toByteArray().length > 10 * 1024){
            System.out.println(out.toByteArray().length);
            matrix.setScale(0.9f, 0.9f);
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
            out.reset();
            result.compress(Bitmap.CompressFormat.JPEG, 85, out);
        }
        return result;
    }

    /**
     * 初始化用户名
     */
    private String mNickName = "";
    private void initName(){
        httpPost_3 = new HttpPost(allurl.getUserAllInfo_all());
        SharedPreferences sharedPreferences = Popularize.this.getSharedPreferences("user", MODE_PRIVATE);
        String mToken = sharedPreferences.getString("user_token", "");
        params.add(new BasicNameValuePair("user_token", mToken));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost_3.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost_3);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        if(jo.getString("status").equals("1")){
                            JSONObject jo_2 = jo.getJSONObject("data");
                            mNickName = jo_2.getString("nickname");
                            popHandler.sendEmptyMessage(41);
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

    /**
     * 初始化二维码
     */
    private String urlcontext;
    private Bitmap bmp_icon;
    private void initErWeiMa(){
        popularize_pb.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = Popularize.this.getSharedPreferences("user", MODE_PRIVATE);
        mUid = sharedPreferences.getString("uid", "");
//        urlcontext = "http://qr.liantu.com/api.php?text="+allurl.getShare_Url()+"?link="+mUid+
//                "&w=140&bg=FFFFFF&fg=000000&logo="+userIconUrl;

        urlcontext = "http://qr.liantu.com/api.php?text=拇指营销"+mUid+
                "&w=240&bg=FFFFFF&fg=000000&logo="+userIconUrl;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bmp_icon = getHttpBitmap(urlcontext);
                    URL url = new URL(urlcontext);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5*1000);
                    connection.setDoInput(true);
                    connection.connect();
                    //得到输入流
                    InputStream is = connection.getInputStream();
                    bmp_icon = BitmapFactory.decodeStream(is);
                    //关闭输入流
                    popHandler.sendEmptyMessage(31);
                    is.close();
                    //关闭连接
                    connection.disconnect();
                } catch (Exception e) {
                    popHandler.sendEmptyMessage(32);
                    Log.i("qqqqq","异常"+e);
                    e.printStackTrace();
                }


            }
        }).start();



        //网络访问兼容
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

    /**
     *
     * @param data
     * @return
     * 解析图片的代码
     */
    public Bitmap getHttpBitmap(String data)
    {
        Bitmap bitmap = null;
        try
        {
            //初始化一个URL对象
            URL url = new URL(data);
            //获得HTTPConnection网络连接对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5*1000);
            connection.setDoInput(true);
            connection.connect();
            //得到输入流
            InputStream is = connection.getInputStream();
            Log.i("TAG", "*********inputstream**"+is);
            bitmap = BitmapFactory.decodeStream(is);
            Log.i("TAG", "*********bitmap****"+bitmap);
            //关闭输入流
            is.close();
            //关闭连接
            connection.disconnect();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    /** 保存方法 */
    public void saveBitmap(Bitmap bmp) {

        File f = new File("/sdcard/namecard/", "wzyx");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

            Toast.makeText(Popularize.this,"保存成功",Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
