package com.wangdao.our.spread_2.activity_;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.wangdao.our.spread_2.activity_.mine_activity.Popularize;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class TellWe extends Activity {
    private ImageView iv_cancle;
    private WebView wb_;
    private Dialog dia_wait;
    private ImageView dialog_iv;
    private TextView tv_share;
    // private Button bt_tellW;
    private IWXAPI api;
    private final String APP_ID = "wx24f1f8198ba7b121";
    private String myUrl = "http://mp.weixin.qq.com/s?__biz=MzAxODQwMTYxMA==&mid=502752513&idx=1&sn=e6212db8bb98fbda13cff66fafdcb520&scene=1&srcid=0607KB0xxnsxbvR8S3FBpcpx&from=groupmessage&isappinstalled=0#wechat_redirect";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tell_we);
      //  bt_tellW = (Button) findViewById(R.id.activity_tell_we_bt_tellwe);
        api = WXAPIFactory.createWXAPI(TellWe.this, APP_ID);
        iv_cancle = (ImageView) findViewById(R.id.activity_tell_we_iv_cancle);
        tv_share = (TextView) findViewById(R.id.activity_tell_we_tv_share);

        wb_ = (WebView) findViewById(R.id.activity_tell_we_wb);
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        bt_tellW.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:66663324"));
//                if (ActivityCompat.checkSelfPermission(TellWe.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                startActivity(intent);
//            }
//        });
        startDialog();
        init(myUrl);
      //  wb_.loadUrl(myUrl);

        wb_.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dia_wait.dismiss();

            }
        });

        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog();
            }
        });
    }


    private void startDialog(){

        View dialog_view = getLayoutInflater().inflate(R.layout.dialog_wait_2,null);
        dia_wait = new Dialog(this,R.style.dialog);
        dia_wait.setContentView(dialog_view);
        dialog_iv  = (ImageView) dialog_view.findViewById(R.id.dialog_wait_2_iv);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.dialog_zhuang);

        LinearInterpolator lir = new LinearInterpolator();
        anim.setInterpolator(lir);

        dialog_iv.startAnimation(anim);

        dia_wait.show();
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
        iv_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TuiJianToWX();
                dialog_help.dismiss();
            }
        });

        iv_pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TuiJianToFriend();
                dialog_help.dismiss();
            }
        });
    }


    private void init(String url){

        wb_.getSettings().setJavaScriptEnabled(true);
        wb_.loadUrl(url);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wb_.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                        + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }
        });
    }

    /**
     * 分享到微信好友
     */
    private Bitmap thumb_1;
    private WXMediaMessage msg_1;
    private String mUid;
    private void TuiJianToFriend(){

        WXWebpageObject webpage_2 = new WXWebpageObject();
        webpage_2.webpageUrl = myUrl;
        msg_1 = new WXMediaMessage(webpage_2);

        msg_1.title  = "边广告边赚钱！三分钟讲解“拇指营销”如何收入轻松月入过万模式";
        msg_1.description = "了解拇指营销--人人可为，终身仅仅投资298元、却能轻松实现月入过万的好项目！";

        thumb_1 = BitmapFactory.decodeResource(getResources(),R.drawable.fenxiangaaa);
        msg_1.thumbData = Util.bmpToByteArray(thumb_1, false);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg_1;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);

    }

    /**
     * 分享到朋友圈
     */
    Bitmap thumb_2;
    WXMediaMessage msg_2;
    private void TuiJianToWX(){

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = myUrl;
        msg_2 = new WXMediaMessage(webpage);

        msg_2.title  = "边广告边赚钱！三分钟讲解“拇指营销”如何收入轻松月入过万模式";
        msg_2.description = "了解拇指营销--人人可为，终身仅仅投资298元、却能轻松实现月入过万的好项目！";
        thumb_1 = BitmapFactory.decodeResource(getResources(),R.drawable.fenxiangaaa);
        msg_2.thumbData = Util.bmpToByteArray(thumb_1, false);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg_2;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
