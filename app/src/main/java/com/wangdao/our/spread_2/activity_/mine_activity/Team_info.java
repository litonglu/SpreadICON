package com.wangdao.our.spread_2.activity_.mine_activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wangdao.our.spread_2.ExampleApplication;
import com.wangdao.our.spread_2.R;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/28 0028.
 */
public class Team_info extends Activity{


    private TextView tv_myname;
    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();

    private Team_Handler team_handler = new Team_Handler();

    private String str_nickname,str_true_name,str_tell,str_addTime,str_loginTime,str_login_num,str_ps,str_icon,str_invite_user;


    private TextView tv_nickname,tv_tell,tv_addTime,tv_login_time,tv_login_num,tv_invite_user;
    private CircleImageView iv_icon;
    private ImageView iv_backround;
    private ImageView iv_cancle;
    private String team_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);
        initView();
        Intent gIntent = getIntent();
        team_id = gIntent.getExtras().getString("id");

        initData();

    }

    /**
     *
     */
    private void initView(){

        tv_nickname = (TextView) findViewById(R.id.team_info_tv_nickname);
        tv_tell = (TextView) findViewById(R.id.team_info_tv_tell);
        tv_addTime = (TextView) findViewById(R.id.team_info_tv_addtime);
        tv_login_time = (TextView) findViewById(R.id.team_info_tv_logintime);
        tv_login_num = (TextView) findViewById(R.id.team_info_tv_loginnum);
        tv_invite_user = (TextView) findViewById(R.id.team_info_tv_ps);
        iv_cancle = (ImageView) findViewById(R.id.activity_team_info_iv_cancle);
        iv_icon = (CircleImageView) findViewById(R.id.activity_team_info_icon);
        tv_myname = (TextView) findViewById(R.id.activity_team_info_tv_name_);
        iv_backround = (ImageView) findViewById(R.id.activity_team_info_iv_backround);

        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData(){
        httpPost = new HttpPost(allurl.getTeamPersonInfo());
        SharedPreferences sharedPreferences = Team_info.this.getSharedPreferences("user", MODE_PRIVATE);
        String mToken = sharedPreferences.getString("user_token", "");

        params.add(new BasicNameValuePair("uid", team_id));
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
                        if(jo.getString("status").equals("1")){

                            JSONObject jo_2 = jo.getJSONObject("data");
                            str_nickname = jo_2.getString("nickname");
                            str_login_num = jo_2.getString("login");


                            str_addTime = jo_2.getString("reg_time");
                            str_loginTime = jo_2.getString("last_login_time");
                            str_true_name = jo_2.getString("truename");

                            str_tell = jo_2.getString("mobile");
                            str_icon = jo_2.getString("avatar256");

                            JSONObject ho_3 = jo_2.getJSONObject("userlink");
                            str_ps = ho_3.getString("remark");

                            JSONObject ho_4 = jo_2.getJSONObject("invite_user");
                            str_invite_user = ho_4.getString("nickname");

                            team_handler.sendEmptyMessage(1);
                        }else{

                            team_handler.sendEmptyMessage(2);
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


    class Team_Handler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:

                    tv_nickname.setText(str_nickname);

                    tv_addTime.setText(str_addTime);

                    tv_tell.setText(str_tell);
                    tv_login_num.setText(str_login_num);
                    tv_login_time.setText(str_loginTime);
                    tv_myname.setText(str_nickname);
                    tv_invite_user.setText(str_invite_user);

                    ImageLoader.getInstance().displayImage(str_icon == null ? "" : str_icon, iv_icon,
                            ExampleApplication.getInstance().getOptions(R.drawable.default_photo));


                    ImageLoader.getInstance().displayImage(str_icon == null ? "" : str_icon, iv_backround,
                            ExampleApplication.getInstance().getOptions(R.drawable.default_photo),


                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            iv_backround.setImageBitmap(doBlur(loadedImage,10,false));
                        }
                    }
                    );

                    break;
                case 2:
                    Toast.makeText(Team_info.this,"网络异常",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
    /**
     * 模糊操作
     * @param bitmap_main
     * @param bitmap_over
     * @param canvas
     * @param blur （0=<blur<=255）值越大越不清
     */
    private  void blurImage(Bitmap bitmap_main, Bitmap bitmap_over, Canvas canvas, int blur) {

        int width =  bitmap_main.getWidth();
        int height = bitmap_main.getHeight();

        //设置覆盖图的图片的宽和高与模糊图片相同
        bitmap_over = setOverImage(bitmap_over, width,height );
        Paint paint = new Paint();
        //消除锯齿
        paint.setAntiAlias(true);
        //先画要模糊的图片
        canvas.drawBitmap(bitmap_main, 0, 0, paint);
        //设置画笔透明度(透明度越低，越模糊)
        paint.setAlpha(blur);
        //画上覆盖图片
        canvas.drawBitmap(bitmap_over, 0, 0, paint);
        //到这里图片模糊已经完成

        //下面设置以图片中心为原点，宽的四分之一的圆内不模糊
        canvas.save();
        //设置画笔不透明
        paint.setAlpha(255);
        //设置路径
        Path mPath = new Path();

        //添加圆
        mPath.addCircle(width/2, height/2, width/4, Path.Direction.CCW);
        //设置绘图部分
        canvas.clipPath(mPath, Region.Op.REPLACE);
        //再绘画上模糊的图片
        canvas.drawBitmap(bitmap_main, 0, 0, paint);
        canvas.restore();
    }
    /**
     * 以最省内存的方式读取本地资源的图片
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }
    /**
     * 设置覆盖图片的大小
     * @param bmp
     * @param new_width
     * @param new_height
     * @return
     */
    private Bitmap setOverImage(Bitmap bmp, int new_width, int new_height) {
        Bitmap bitmap = null;
        try {
            int width = bmp.getWidth();
            int height = bmp.getHeight();

            float scale_w = ((float)new_width)/width;
            float scale_h = ((float)new_height)/height;
            // 创建操作图片用的matrix对象
            Matrix matrix = new Matrix();
            // 缩放图片动作
            matrix.postScale(scale_w, scale_h);
            // 创建新的图片
            bitmap = Bitmap.createBitmap(bmp, 0, 0, width, height,matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }



}
