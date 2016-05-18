package com.wangdao.our.spread_2.activity_;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.activity_.ChangeNickName;
import com.wangdao.our.spread_2.activity_.ChangePwd;
import com.wangdao.our.spread_2.activity_.mine_activity.BindingAccount;
import com.wangdao.our.spread_2.slide_widget.AllUrl;
import com.wangdao.our.spread_2.slide_widget.CacheUtils;
import com.wangdao.our.spread_2.slide_widget.shap_imageview.CustomImageView;
import com.wangdao.our.spread_2.slide_widget.widget_image.Crop;
import com.wangdao.our.spread_2.slide_widget.widget_image.FileUtils;


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
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2016/4/22 0022.
 *
 */
public class AboutInfo extends Activity implements View.OnClickListener{

    private String dateTime;
    private ImageView iv_cancle;
    private Button bt_out_login;

    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allUrl = new AllUrl();

    private ImageView iv_icon;
    private AboutInfoHandler aihandler = new AboutInfoHandler();
    private TextView tv_mobile,tv_nickname;
    private LinearLayout ll_icon,ll_mobile,ll_nickname,ll_pwd,ll_bd;
    private String avatar64, avatar128,avatar256;
    private File mTempDir;
    private TextView tv_isbd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_info);
        initView();
        initOnClick();
        mTempDir = new File( Environment.getExternalStorageDirectory(),"Temp");
        if(!mTempDir.exists()){
            mTempDir.mkdirs();
        }
        initData();
    }

    private String getDataResult = "网络异常";
    private Bitmap myIcon;
    private String userTureName = "";
    private void initData(){
        SharedPreferences sharedPreferences = AboutInfo.this.getSharedPreferences("user", MODE_PRIVATE);
        final String url = sharedPreferences.getString("avatar256", "");
        String aMobile = sharedPreferences.getString("mobile", "");
        String aNickname = sharedPreferences.getString("nickname","");
        String mToken = sharedPreferences.getString("user_token","");

        httpPost = new HttpPost(allUrl.getUserAllInfo());
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
                getDataResult = jo.getString("info");
                if(jo.getString("status").equals("1")){

                    JSONObject jo_3 = jo.getJSONObject("data");
                    userTureName = jo_3.getString("truename");
                    aihandler.sendEmptyMessage(11);
                }else{
                    aihandler.sendEmptyMessage(12);
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

        tv_mobile.setText(aMobile);
        tv_nickname.setText(aNickname);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    myIcon = getImage(url);
                    aihandler.sendEmptyMessage(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    class AboutInfoHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    iv_icon.setImageBitmap(toRoundBitmap(myIcon));
                    break;
                //上传头像成功,保存信息
                case 3:
                    SharedPreferences sharedPreferences = AboutInfo.this.getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("avatar64",avatar64);
                    editor.putString("avatar128",avatar128);
                    editor.putString("avatar256", avatar256);
                    editor.commit();
                    break;
                //注销成功
                case 9:
                    changUser();
                    break;
                //注销失败
                case 8:
                    new AlertDialog.Builder(AboutInfo.this)
                            .setTitle("注销")
                            .setMessage(logOutResult)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                //获取用户信息成功
                case 11:
                    if(userTureName.length() == 0){
                        tv_isbd.setText("未绑定");
                    }else{
                        tv_isbd.setText("已绑定");
                    }
                    break;
                //获取用户信息失败
                case 12:
                    break;
            }
        }
    }

    // 获取指定路径的图片
    public static Bitmap getImage(String urlpath) throws IOException {
        URL url = new URL(urlpath);
        InputStream is=url.openStream();
        return  BitmapFactory.decodeStream(is);
    }
    private void initView(){
        tv_isbd = (TextView) findViewById(R.id.activity_about_info_tv_isbd);
        ll_bd = (LinearLayout) findViewById(R.id.activity_about_info_ll_bd);
        iv_cancle = (ImageView) findViewById(R.id.activity_about_info_iv_cancle);
        bt_out_login = (Button) findViewById(R.id.activity_about_info_bt_out_login);
        iv_icon = (ImageView) findViewById(R.id.activity_about_info_iv_icon);
        tv_mobile = (TextView) findViewById(R.id.activity_about_infO_tv_mobile);
        tv_nickname = (TextView) findViewById(R.id.activity_about_info_tv_nickname);
        ll_icon = (LinearLayout) findViewById(R.id.activity_about_info_ll_icon);
        ll_mobile = (LinearLayout) findViewById(R.id.activity_about_info_ll_mobile);
        ll_nickname = (LinearLayout) findViewById(R.id.activity_about_info_ll_nickname);
        ll_pwd = (LinearLayout) findViewById(R.id.activity_about_info_ll_pwd);
    }

    private void initOnClick(){
        ll_bd.setOnClickListener(this);
        iv_cancle.setOnClickListener(this);
        bt_out_login.setOnClickListener(this);
        ll_icon.setOnClickListener(this);
        ll_mobile.setOnClickListener(this);
        ll_nickname.setOnClickListener(this);
        ll_pwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_about_info_iv_cancle:
                finish();
                break;
            case R.id.activity_about_info_bt_out_login:
                showLogoutDialog();
                break;
            case R.id.activity_about_info_ll_icon:
                showPhotoDialog();
                break;
            case R.id.activity_about_info_ll_mobile:
                break;
            case R.id.activity_about_info_ll_nickname:
                Intent cnIntent = new Intent(AboutInfo.this,ChangeNickName.class);
                startActivityForResult(cnIntent,1);
                break;
            case R.id.activity_about_info_ll_pwd:
                Intent cpIntent = new Intent(AboutInfo.this,ChangePwd.class);
                startActivityForResult(cpIntent,1);
                break;
            case R.id.dialog_logout_yes:
                log_out();
                materialDialog.dismiss();
                break;
            case R.id.dialog_logout_cancle:
                materialDialog.dismiss();
                break;
            //绑定账号
            case R.id.activity_about_info_ll_bd:
                Intent bdIntent = new Intent(AboutInfo.this, BindingAccount.class);
                startActivityForResult(bdIntent,1);
                break;
        }
    }
    /**
     * 注销
     */
    private String logOutResult;
    private void log_out(){
        httpPost = new HttpPost(allUrl.getLogin_out());
        new Thread(new Runnable() {
            @Override
            public void run() {
       try {
             httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

             httpResponse = new DefaultHttpClient().execute(httpPost);
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            String result = EntityUtils.toString(httpResponse.getEntity());
            JSONObject jo = new JSONObject(result);
            logOutResult =jo.getString("info");
            if(jo.getString("status").equals("1")){
                aihandler.sendEmptyMessage(9);
            }else{
                aihandler.sendEmptyMessage(8);
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

    private void changUser(){
        SharedPreferences sharedPreferences = AboutInfo.this.getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Intent reLogin = new Intent(AboutInfo.this,LoginActivity.class);
        startActivity(reLogin);
        setResult(66);
        finish();
    }
    Dialog materialDialog;
    View material_view;
    TextView tv_cancle,tv_ok;
    private void showLogoutDialog(){
        material_view = getLayoutInflater().inflate(R.layout.dialog_logout,null);
        tv_cancle = (TextView) material_view.findViewById(R.id.dialog_logout_cancle);
        tv_ok = (TextView) material_view.findViewById(R.id.dialog_logout_yes);
        materialDialog = new Dialog(this,R.style.dialog);
        materialDialog.setContentView(material_view);
        materialDialog.show();
        tv_cancle.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
    }

    private Dialog dialog_help_2;
    View view_2;
    helpdialog_item_2 hi_2 = null;
    private void showPhotoDialog() {
        if(hi_2==null){

            hi_2= new helpdialog_item_2();
            view_2 = getLayoutInflater().inflate(R.layout.dialog_out_login, null);
            hi_2.tv_help1 = (TextView) view_2.findViewById(R.id.bt_help1);
            hi_2.tv_help2 = (TextView) view_2.findViewById(R.id.bt_help2);
            hi_2.tv_helpcancle = (TextView) view_2.findViewById(R.id.bt_helpcancle);
            dialog_help_2 = new Dialog(this, R.style.transparentFrameWindowStyle);
            dialog_help_2.setContentView(view_2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            Window window = dialog_help_2.getWindow();
            window.setWindowAnimations(R.style.main_menu_animstyle);
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = this.getWindowManager().getDefaultDisplay().getHeight();
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog_help_2.onWindowAttributesChanged(wl);
            dialog_help_2.setCanceledOnTouchOutside(true);
            view_2.setTag(hi_2);
        }else{
            hi_2 = (helpdialog_item_2) view_2.getTag();
        }
        hi_2.tv_help1.setText("相册选取");
        hi_2.tv_help2.setText("相机拍照");
        hi_2.tv_help1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date1 = new Date(System.currentTimeMillis());
                dateTime = date1.getTime() + "";
                pickImage();
                dialog_help_2.dismiss();
            }
        });
        hi_2.tv_help2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date(System.currentTimeMillis());
                dateTime = date.getTime() + "";
                takePicktrue();
                dialog_help_2.dismiss();
            }
        });
        hi_2.tv_helpcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_help_2.dismiss();
            }
        });
        dialog_help_2.show();
    }

    class helpdialog_item_2{
        TextView tv_help1;
        TextView tv_help2;
        TextView tv_helpcancle;
    }

    /**
     * 相机
     */
    public void takePicktrue() {
        getImageFromCamera();
    }

    /**
     * 相册
     */
    public void pickImage() {
        Crop.pickImage(this);
    }

    protected void getImageFromCamera() {
        // create Intent to take a picture and return control to the calling
        // application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = "Temp_camera" + String.valueOf( System.currentTimeMillis());
        File cropFile = new File( mTempDir, fileName);
        Uri fileUri = Uri.fromFile( cropFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name
        mCurrentPhotoPath = fileUri.getPath();
        // start the image capture Intent
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
    }
    private String mCurrentPhotoPath;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1458;
    private String iconUrl;
    private String icon_result;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 5:
            case 11:
                initData();
                break;
            case 66:
                setResult(66);
                finish();
                break;
        }

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == Crop.REQUEST_PICK) {
                beginCrop( data.getData());
            }
            else if(requestCode == Crop.REQUEST_CROP) {
                handleCrop( resultCode, data);

                SharedPreferences sharedPreferences = AboutInfo.this.getSharedPreferences("user", MODE_PRIVATE);
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap bitmap = extras.getParcelable("data");
                        Bitmap bmp = null;

                        iconUrl = saveToSdCard(a_tuwen);

                        params.add(new BasicNameValuePair("user_token", sharedPreferences.getString("user_token", "")));
                        String str_nickname =  ""+imgToBase64(iconUrl,a_tuwen);
                        //回收img
                        iv_icon.setDrawingCacheEnabled(false);
                        String st = sharedPreferences.getString("user_token","")+","+str_nickname;
                        params.add(new BasicNameValuePair("img", st));
                        httpPost = new HttpPost(allUrl.getIcon_update());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                                    httpResponse = new DefaultHttpClient().execute(httpPost);
                                    if (httpResponse.getStatusLine().getStatusCode() == 200) {

                                        String result = EntityUtils.toString(httpResponse.getEntity());
                                        JSONObject jo_result = new JSONObject(result);
                                        icon_result = jo_result.getString("info");
                                        if(jo_result.getString("status").equals("1")){
                                            JSONObject jo_result_2 = jo_result.getJSONObject("data");
                                            avatar64 = jo_result_2.getString("avatar64");
                                            avatar128 = jo_result_2.getString("avatar128");
                                            avatar256 = jo_result_2.getString("avatar256");
                                            aihandler.sendEmptyMessage(3);
                                            initData();
                                        }else{
                                            aihandler.sendEmptyMessage(4);
                                        }
                                        Log.i("qqqqq","上传成功"+result);
                                        JSONObject jo = new JSONObject(result);
                                        Log.i("qqqqq", jo.getString("info"));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                }
            }
            else if(requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
                if(mCurrentPhotoPath != null) {
                    beginCrop( Uri.fromFile( new File( mCurrentPhotoPath)));
                }
            }
        }

        Log.i("qqqqq",resultCode+"ioahflafa");
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    String files = CacheUtils.getCacheDirectory(this, true, "icon") + dateTime;
                    File file = new File(files);
                    if (file.exists() && file.length() > 0) {
                        Uri uri = Uri.fromFile(file);
                        startPhotoZoom(uri);
                        BitmapFactory.decodeFile(uri.toString());
                        Log.i("qqqqq", "+++" + uri.toString());
                    } else {
                    }
                    break;
                case 2:
                    if (data == null) {
                        return;
                    }

                    startPhotoZoom(data.getData());
                    break;
                case 3:

                    break;

                default:
                    break;
            }
        }
    }
    Bitmap a_tuwen;
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            iv_icon.setImageURI(Crop.getOutput(result));

            iv_icon.setDrawingCacheEnabled(true);
             a_tuwen = iv_icon.getDrawingCache();
            iv_icon.setImageBitmap(toRoundBitmap(a_tuwen));



//            mCircleView.setImageBitmap( getCircleBitmap(Crop.getOutput(result)));
        } else if (resultCode == Crop.RESULT_ERROR) {
//            Toast.makeText(getActivity(), Crop.getError(result).getMessage(),
//                    Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getCircleBitmap(Uri uri) {
        Bitmap src =  BitmapFactory.decodeFile( uri.getPath());
        Bitmap output = Bitmap.createBitmap( src.getWidth(), src.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas( output);

        Paint paint = new Paint();
        Rect rect = new Rect( 0, 0, src.getWidth(), src.getHeight());

        paint.setAntiAlias( true);
        paint.setFilterBitmap( true);
        paint.setDither( true);
        canvas.drawARGB( 0, 0, 0, 0);
        canvas.drawCircle( src.getWidth() / 2, src.getWidth() / 2, src.getWidth() / 2, paint);
        paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap( src, rect, rect, paint);
        return output;
    }

    private void beginCrop(Uri source) {

        String fileName = "Temp_" + String.valueOf( System.currentTimeMillis());
        File cropFile = new File( mTempDir, fileName);
        Uri outputUri = Uri.fromFile( cropFile);
        new Crop( source).output( outputUri).setCropType(true).start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTempDir.exists()){
            FileUtils.deleteFile(mTempDir);
        }
    }

    public static  Bitmap toRoundBitmap(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            left = 0;
            bottom = width;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);


        //鐢荤櫧鑹插渾鍦�
        paint.reset();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0);
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, width / 2, width / 2 - 4 / 2, paint);
        return output ;
    }
    public String saveToSdCard(Bitmap bitmap) {
        String files = CacheUtils.getCacheDirectory(this, true, "icon")
                + dateTime + "_12.jpg";
        File file = new File(files);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 120);
//        intent.putExtra("outputY", 120);
//        intent.putExtra("crop", "true");
//        intent.putExtra("scale", true);// 鍘婚敓鏂ゆ嫹閿熻妭鎲嬫嫹
//        intent.putExtra("scaleUpIfNeeded", true);// 鍘婚敓鏂ゆ嫹閿熻妭鎲嬫嫹
        // intent.putExtra("noFaceDetection", true);//閿熸枻鎷烽敓鏂ゆ嫹璇嗛敓鏂ゆ嫹
//        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }
    /**
     * @param bitmap
     * @return
     */
    public String imgToBase64(String imgPath,Bitmap bitmap) {
        if (imgPath !=null && imgPath.length() > 0) {
            bitmap = readBitmap(imgPath);
        }
        if(bitmap == null){
           Log.i("qqqqq","nulllllllllllllllllll");
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);


            out.flush();
            out.close();
            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private Bitmap readBitmap(String imgPath) {
        try {
            return BitmapFactory.decodeFile(imgPath);
        } catch (Exception e) {
            return null;
        }

    }


}
