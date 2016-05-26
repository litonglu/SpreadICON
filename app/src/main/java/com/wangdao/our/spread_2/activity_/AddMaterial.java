package com.wangdao.our.spread_2.activity_;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangdao.our.spread_2.ExampleApplication;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.bean.Material;
import com.wangdao.our.spread_2.slide_widget.AllUrl;
import com.wangdao.our.spread_2.slide_widget.CacheUtils;
import com.wangdao.our.spread_2.slide_widget.widget_image.AsynImageLoader;
import com.wangdao.our.spread_2.slide_widget.widget_image.Crop;
import com.wangdao.our.spread_2.slide_widget.widget_image.FileUtils;
import com.wangdao.our.spread_2.slide_widget.widget_image.RoundedImageView;

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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/23 0023.
 */
public class AddMaterial extends Activity implements View.OnClickListener {
    private RadioButton radioButton_1, radioButton_2, radioButton_3, radioButton_4;
    private boolean b_rb1 = false;
    private boolean b_rb2 = false;
    private boolean b_rb3 = false;
    private boolean b_rb4 = false;
    private LinearLayout ll_icon, ll_er, ll_yi, ll_erx, ll_tel;
    private RoundedImageView iv_tong_icon;
    private TextView tv_1, tv_2, tv_3, tv_4;
    private TextView tv_explain;
    private EditText et_er1, et_er2, et_url;
    private EditText et_san_1, et_san_2, et_san_3;
    private TextView tv_actionbar;
    private ImageView iv_xIcon;
    private Material intent_m;
    private int myType = 0;
    private ImageView iv_cancle;
    private TextView tv_add;
    //当前选择类型
    private int currentType = 0;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1458;
    private File mTempDir;
    private String mCurrentPhotoPath;
    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();
    private AddMaterialHandler amh = new AddMaterialHandler();
   // private Dialog dialog_wait;
   // private View dialog_view;
 //   private TextView tv_dialog_cancle;
    private Button bt_delete;
    private boolean isCompile = false;//是否是编辑
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_m);
        initView();
        initOnClick();
        initRb();
        Intent intentData = getIntent();
        myType = intentData.getExtras().getInt("type");
        if (myType == 0) {//添加素材
            isCompile = false;
            tv_actionbar.setText(R.string.actionbar_add_m);
            bt_delete.setVisibility(View.GONE);
            isCompile = false;

        } else {//编辑素材
            isCompile = true;
            bt_delete.setVisibility(View.VISIBLE);
            tv_actionbar.setText(R.string.actionbar_compile_m);
            intent_m = (Material) intentData.getExtras().getSerializable("compile_m");
            if (intent_m.ismTop()) {
                b_rb1 = true;
                radioButton_1.setChecked(true);
            } else {
                b_rb1 = false;
                radioButton_1.setChecked(false);
            }
            if (intent_m.ismBottom()) {
                b_rb2 = true;
                radioButton_2.setChecked(true);
            } else {
                b_rb2 = false;
                radioButton_2.setChecked(false);
            }
            if (intent_m.ismTailor()) {
                b_rb3 = true;
                radioButton_3.setChecked(true);
            } else {
                b_rb3 = false;
                radioButton_3.setChecked(false);
            }

            switch (intent_m.getmType()) {
                case 0:
                    tongLan();
                    currentType = 0;
                    break;
                case 1:
                    tuWen();
                    currentType = 1;
                    break;
                case 2:
                    currentType = 2;
                    mingPian();
                    break;
                case 3:
                    currentType = 3;
                    erWeiMa();
                    break;
            }

            bt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除广告
                    new AlertDialog.Builder(AddMaterial.this)
                            .setMessage("确定删除？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteGg();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initRb() {
        radioButton_1.setChecked(true);
        radioButton_2.setChecked(true);
        radioButton_3.setChecked(true);
        radioButton_4.setChecked(true);
        b_rb1 = true;
        b_rb2 = true;
        b_rb3 = true;
        b_rb4 = true;

    }

    private void initView() {
        bt_delete = (Button) findViewById(R.id.activity_add_m_bt_delete);
        radioButton_1 = (RadioButton) findViewById(R.id.activity_add_m_rb_1);
        radioButton_2 = (RadioButton) findViewById(R.id.activity_add_m_rb_2);
        radioButton_3 = (RadioButton) findViewById(R.id.activity_add_m_rb_3);
        radioButton_4 = (RadioButton) findViewById(R.id.activity_add_m_rb_4);

        tv_1 = (TextView) findViewById(R.id.activity_add_m_click_1);
        tv_2 = (TextView) findViewById(R.id.activity_add_m_click_2);
        tv_3 = (TextView) findViewById(R.id.activity_add_m_click_3);
        tv_4 = (TextView) findViewById(R.id.activity_add_m_click_4);

        ll_icon = (LinearLayout) findViewById(R.id.activity_add_m_ll_icon);
        ll_er = (LinearLayout) findViewById(R.id.activity_add_m_ll_er);
        ll_erx = (LinearLayout) findViewById(R.id.activity_add_m_ll_erx);
        ll_yi = (LinearLayout) findViewById(R.id.activity_add_m_ll_yi);
        ll_tel = (LinearLayout) findViewById(R.id.activity_add_m_ll_bohao);
        et_er1 = (EditText) findViewById(R.id.activity_add_m_et_er1);
        et_er2 = (EditText) findViewById(R.id.activity_add_m_et_er2);
        et_url = (EditText) findViewById(R.id.activity_add_m_et_url);
        et_san_1 = (EditText) findViewById(R.id.activity_add_m_et_san1);
        et_san_2 = (EditText) findViewById(R.id.activity_add_m_et_san2);
        et_san_3 = (EditText) findViewById(R.id.activity_add_m_et_san3);
        iv_xIcon = (ImageView) findViewById(R.id.activity_add_m_iv_xicon);
        //通栏
        iv_tong_icon = (RoundedImageView) findViewById(R.id.activity_add_m_tongiv);
        tv_explain = (TextView) findViewById(R.id.activity_add_m_tv_explain);

        tv_actionbar = (TextView) findViewById(R.id.activity_add_m_tv_actionbar);
        iv_cancle = (ImageView) findViewById(R.id.activity_add_m_iv_cancle);
        tv_add = (TextView) findViewById(R.id.activity_add_m_tv_add);

      //  dialog_view = getLayoutInflater().inflate(R.layout.dialog_wait_2, null);
      //  tv_dialog_cancle = (TextView) dialog_view.findViewById(R.id.dialog_wait_tv_cancle);

        //dialog_wait = new Dialog(this, R.style.dialog);
       // dialog_wait.setContentView(dialog_view);

    }


    private Dialog dia_wait;
    private ImageView dialog_iv;
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

    private void initOnClick() {
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        tv_4.setOnClickListener(this);
        iv_cancle.setOnClickListener(this);
        tv_add.setOnClickListener(this);
        iv_tong_icon.setOnClickListener(this);
        iv_xIcon.setOnClickListener(this);
        radioButton_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b_rb1) {
                    radioButton_1.setChecked(false);
                    b_rb1 = false;
                } else {
                    radioButton_1.setChecked(true);
                    b_rb1 = true;
                }
            }
        });
        radioButton_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b_rb2) {
                    radioButton_2.setChecked(false);
                    b_rb2 = false;
                } else {
                    radioButton_2.setChecked(true);
                    b_rb2 = true;
                }
            }
        });
        radioButton_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b_rb3) {
                    radioButton_3.setChecked(false);
                    b_rb3 = false;
                } else {
                    radioButton_3.setChecked(true);
                    b_rb3 = true;
                }
            }
        });
        radioButton_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b_rb4) {
                    radioButton_4.setChecked(false);
                    b_rb4 = false;
                } else {
                    radioButton_4.setChecked(true);
                    b_rb4 = true;
                }
            }
        });

        mTempDir = new File(Environment.getExternalStorageDirectory(), "Temp");
        if (!mTempDir.exists()) {
            mTempDir.mkdirs();
        }
    }

    /**
     * 删除广告
     */
    private String delectResult;

    private void deleteGg() {
        showWaitDialog();
        httpPost = new HttpPost(allurl.getDeleteGg());
        SharedPreferences sharedPreferences = AddMaterial.this.getSharedPreferences("user", MODE_PRIVATE);
        String aToken = sharedPreferences.getString("user_token", "");
        params.add(new BasicNameValuePair("user_token", aToken));
        params.add(new BasicNameValuePair("ads_id", intent_m.getmId()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());

                        JSONObject jo = new JSONObject(result);
                        delectResult = jo.getString("info");
                        if (jo.getString("status").equals("1")) {
                            amh.sendEmptyMessage(22);
                        } else {
                            amh.sendEmptyMessage(21);
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
     * 加载dialog
     */
    private void showWaitDialog() {
        startDialog();
    }

    /**
     * 关闭dialog
     */
    private void dimssDialog() {
        dia_wait.dismiss();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_add_m_click_1:
                currentType = 0;
                tongLan();
                break;
            case R.id.activity_add_m_click_2:
                currentType = 1;
                tuWen();
                break;
            case R.id.activity_add_m_click_3:
                currentType = 2;
                mingPian();
                break;
            case R.id.activity_add_m_click_4:
                currentType = 3;
                erWeiMa();
                break;
            case R.id.activity_add_m_iv_cancle:
                finish();
                break;
            //保存
            case R.id.activity_add_m_tv_add:
                showWaitDialog();
                uploadingImg();
                //addM();
                break;

            //选图片
            case R.id.activity_add_m_iv_xicon:
            case R.id.activity_add_m_tongiv:
                showPhotoDialog();
                break;
        }
    }

    /**
     * 上传图片
     */
    private String imgUrl;
    private Bitmap abc;

    private void uploadingImg() {
        if (currentType == 0) {
            iv_tong_icon.setDrawingCacheEnabled(true);
            abc = iv_tong_icon.getDrawingCache();
        } else {
            iv_xIcon.setDrawingCacheEnabled(true);
            abc = iv_xIcon.getDrawingCache();
        }
        imgUrl = saveToSdCard(abc);
        httpPost = new HttpPost(allurl.getUploadingImg());
        SharedPreferences sharedPreferences = AddMaterial.this.getSharedPreferences("user", MODE_PRIVATE);
        String aToken = sharedPreferences.getString("user_token", "");
        params.add(new BasicNameValuePair("img", aToken + "," + imgToBase64(imgUrl, abc)));
        params.add(new BasicNameValuePair("user_token", aToken));

        if (currentType == 0) {
            iv_tong_icon.setDrawingCacheEnabled(false);
        } else {
            iv_xIcon.setDrawingCacheEnabled(false);
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
                        addResult = jo.getString("info");
                        if (jo.getString("status").equals("1")) {
                            JSONObject jo_2 = jo.getJSONObject("data");
                            addM(jo_2.getString("id"));

                        } else {
                            amh.sendEmptyMessage(2);
                        }
                    }
                } catch (Exception e) {
                    amh.sendEmptyMessage(2);
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private int top, bottom, cut;
    private String gType;//广告类型
    private String addResult = "网络异常";

    private void addM(final String img_url) {
        httpPost = new HttpPost(allurl.getAddOrCompile());
        SharedPreferences sharedPreferences = AddMaterial.this.getSharedPreferences("user", MODE_PRIVATE);
        String aToken = sharedPreferences.getString("user_token", "");
        if (isCompile) {
            params.add(new BasicNameValuePair("ads_id", intent_m.getmId()));
        }
        if (b_rb1) {
            top = 1;
        } else {
            top = 0;
        }
        if (b_rb2) {
            bottom = 1;
        } else {
            bottom = 0;
        }
        if (b_rb3) {
            cut = 1;
        } else {
            cut = 0;
        }
        switch (currentType) {

            //通栏
            case 0:

                gType = "banner";
                if (isCompile) {
                    params.add(new BasicNameValuePair("ads_id", intent_m.getmId()));
                }
                params.add(new BasicNameValuePair("user_token", aToken));
                params.add(new BasicNameValuePair("type", gType));
                params.add(new BasicNameValuePair("ad_link", et_url.getText().toString()));
                params.add(new BasicNameValuePair("ad_img", img_url));
                params.add(new BasicNameValuePair("ad_top", top + ""));
                params.add(new BasicNameValuePair("ad_bottom", bottom + ""));
                params.add(new BasicNameValuePair("ad_cut", cut + ""));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                            httpResponse = new DefaultHttpClient().execute(httpPost);
                            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                String result = EntityUtils.toString(httpResponse.getEntity());

                                JSONObject jo = new JSONObject(result);
                                addResult = jo.getString("info");

                                if (jo.getString("status").equals("1")) {
                                    amh.sendEmptyMessage(1);
                                } else {
                                    amh.sendEmptyMessage(2);
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
                break;

            //图文
            case 1:
                gType = "imgtext";
                if (isCompile) {
                    params.add(new BasicNameValuePair("ads_id", intent_m.getmId()));
                }
                params.add(new BasicNameValuePair("user_token", aToken));
                params.add(new BasicNameValuePair("type", gType));
                params.add(new BasicNameValuePair("ad_link", et_url.getText().toString()));
                params.add(new BasicNameValuePair("ad_img", img_url));

                params.add(new BasicNameValuePair("ad_top", top + ""));
                params.add(new BasicNameValuePair("ad_bottom", bottom + ""));
                params.add(new BasicNameValuePair("ad_cut", cut + ""));


                params.add(new BasicNameValuePair("ad_pro_title", et_er1.getText().toString()));
                params.add(new BasicNameValuePair("ad_pro_des", et_er2.getText().toString()));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                            httpResponse = new DefaultHttpClient().execute(httpPost);
                            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                String result = EntityUtils.toString(httpResponse.getEntity());

                                JSONObject jo = new JSONObject(result);
                                addResult = jo.getString("info");
                                if (jo.getString("status").equals("1")) {
                                    amh.sendEmptyMessage(1);
                                } else {
                                    amh.sendEmptyMessage(1);
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
                break;

            //名片
            case 2:

                gType = "vcard";
                if (isCompile) {
                    params.add(new BasicNameValuePair("ads_id", intent_m.getmId()));
                }
                params.add(new BasicNameValuePair("user_token", aToken));
                params.add(new BasicNameValuePair("type", gType));
                params.add(new BasicNameValuePair("ad_head", img_url));

                params.add(new BasicNameValuePair("ad_top", top + ""));
                params.add(new BasicNameValuePair("ad_bottom", bottom + ""));
                params.add(new BasicNameValuePair("ad_cut", cut + ""));

                params.add(new BasicNameValuePair("ad_name", et_san_1.getText().toString()));
                params.add(new BasicNameValuePair("ad_mobile", et_san_2.getText().toString()));
                params.add(new BasicNameValuePair("ad_address", et_san_3.getText().toString()));


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                            httpResponse = new DefaultHttpClient().execute(httpPost);
                            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                String result = EntityUtils.toString(httpResponse.getEntity());

                                JSONObject jo = new JSONObject(result);
                                addResult = jo.getString("info");
                                if (jo.getString("status").equals("1")) {
                                    amh.sendEmptyMessage(1);
                                } else {
                                    amh.sendEmptyMessage(1);
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
                break;
            //二维码
            case 3:
//                iv_xIcon.setDrawingCacheEnabled(true);
//                Bitmap a_erm = iv_tong_icon.getDrawingCache();
//                iv_xIcon.setDrawingCacheEnabled(false);
                gType = "tdcode";
                if (isCompile) {
                    params.add(new BasicNameValuePair("ads_id", intent_m.getmId()));
                }

                params.add(new BasicNameValuePair("user_token", aToken));
                params.add(new BasicNameValuePair("type", gType));
                params.add(new BasicNameValuePair("ad_code_img", img_url));

                params.add(new BasicNameValuePair("ad_top", top + ""));
                params.add(new BasicNameValuePair("ad_bottom", bottom + ""));
                params.add(new BasicNameValuePair("ad_cut", cut + ""));

                params.add(new BasicNameValuePair("ad_code_title", et_er1.getText().toString()));
                params.add(new BasicNameValuePair("ad_code_des", et_er2.getText().toString()));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                            httpResponse = new DefaultHttpClient().execute(httpPost);
                            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                String result = EntityUtils.toString(httpResponse.getEntity());

                                JSONObject jo = new JSONObject(result);
                                addResult = jo.getString("info");
                                if (jo.getString("status").equals("1")) {
                                    amh.sendEmptyMessage(1);
                                } else {
                                    amh.sendEmptyMessage(1);
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
                break;
        }
    }

    class AddMaterialHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //保存成功
                case 1:
                    new AlertDialog.Builder(AddMaterial.this)
                            .setTitle("RESULT")
                            .setMessage(addResult)
                            .setNegativeButton("完成", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setResult(5);
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    dimssDialog();
                    break;
                //保存失败
                case 2:
                    new AlertDialog.Builder(AddMaterial.this)
                            .setTitle("RESULT")
                            .setMessage(addResult)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    dimssDialog();
                    break;
                //删除成功
                case 22:
                    new AlertDialog.Builder(AddMaterial.this)
                            .setTitle("删除结果")
                            .setMessage(delectResult)
                            .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setResult(5);
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    dimssDialog();
                    break;
                //删除失败
                case 21:
                    new AlertDialog.Builder(AddMaterial.this)
                            .setTitle("删除结果")
                            .setMessage(delectResult)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    dimssDialog();
                    break;
            }
        }
    }

    /**
     * 在相册获取
     */
    public void takePicktrue() {
        getImageFromCamera();
    }

    /**
     * 相机
     */
    public void pickImage() {
        Crop.pickImage(this);
    }

    protected void getImageFromCamera() {
        // create Intent to take a picture and return control to the calling
        // application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = "Temp_camera" + String.valueOf(System.currentTimeMillis());
        File cropFile = new File(mTempDir, fileName);
        Uri fileUri = Uri.fromFile(cropFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name
        mCurrentPhotoPath = fileUri.getPath();
        // start the image capture Intent
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
    }

    private String dateTime;

    public String saveToSdCard(Bitmap bitmap) {
        Date date = new Date(System.currentTimeMillis());
        dateTime = date.getTime() + "";
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private String iconUrl;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Crop.REQUEST_PICK) {
                if (b_rb3) {
                    beginCrop(result.getData());
                } else {
                    Uri uri = result.getData();
                    ContentResolver cr = this.getContentResolver();
                    try {
                        Bitmap bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        if (currentType == 0) {
                            iv_tong_icon.setImageBitmap(bmp);
                        } else {
                            iv_xIcon.setImageBitmap(bmp);
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, result);
            } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
                if (mCurrentPhotoPath != null) {
                    if (b_rb3) {
                        beginCrop(Uri.fromFile(new File(mCurrentPhotoPath)));
                    } else {
                        //handleCrop(resultCode, result);
                        // iv_tong_icon.setImageURI(Crop.getOutput(("file://"+mCurrentPhotoPath)));
                        Log.i("qqqqq", mCurrentPhotoPath);

                    }
                }
            }
        }
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            switch (currentType) {
                case 0:
                    iv_tong_icon.setImageURI(Crop.getOutput(result));
                    Log.i("qqqqq", String.valueOf(Crop.getOutput(result)));
                    break;
                default:
                    iv_xIcon.setImageURI(Crop.getOutput(result));
                    break;
            }
//            mCircleView.setImageBitmap( getCircleBitmap(Crop.getOutput(result)));
        } else if (resultCode == Crop.RESULT_ERROR) {
//            Toast.makeText(getActivity(), Crop.getError(result).getMessage(),
//                    Toast.LENGTH_SHORT).show();
        }
    }


    private void beginCrop(Uri source) {
        boolean isCircleCrop = false;
        String fileName = "Temp_" + String.valueOf(System.currentTimeMillis());
        File cropFile = new File(mTempDir, fileName);
        Uri outputUri = Uri.fromFile(cropFile);

        new Crop(source).output(outputUri).setCropType(isCircleCrop).start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTempDir.exists()) {
            FileUtils.deleteFile(mTempDir);
        }
    }

    private Bitmap readBitmap(String imgPath) {
        try {
            return BitmapFactory.decodeFile(imgPath);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通知栏
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void tongLan() {
        if (myType == 1) {
            //设置图片。有数据后打开
            //iv_tong_icon.setImageBitmap(intent_m.getmIcon());
            et_url.setText(intent_m.getmUrl());
            initRB();
            tv_2.setVisibility(View.GONE);
            tv_3.setVisibility(View.GONE);
            tv_4.setVisibility(View.GONE);
        }

        tv_1.setTextColor(getResources().getColor(R.color.text_color_white));
        tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
        tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
        tv_4.setTextColor(getResources().getColor(R.color.textcolor_hui));
        tv_1.setBackground(getResources().getDrawable(R.drawable.shape_sure_btn_blue));
        tv_2.setBackground(null);
        tv_3.setBackground(null);
        tv_4.setBackground(null);

        ll_yi.setVisibility(View.VISIBLE);
        iv_tong_icon.setVisibility(View.VISIBLE);
        Log.i("qqqqq", intent_m.getIcon_url());
        if (isCompile == true) {
            Log.i("qqqqq", "url==" + intent_m.getIcon_url());
            ImageLoader.getInstance().displayImage(intent_m.getIcon_url() == null ? "" : intent_m.getIcon_url(), iv_tong_icon,
                    ExampleApplication.getInstance().getOptions(R.drawable.nopic)

            );
        }
        ll_icon.setVisibility(View.GONE);
        ll_er.setVisibility(View.GONE);
        ll_erx.setVisibility(View.GONE);
        ll_tel.setVisibility(View.GONE);
        tv_explain.setText(R.string.explain_tong);
    }

    /**
     * 图文
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void tuWen() {
        if (myType == 1) {
            //设置图片,接数据后打开
            //iv_xIcon.setImageBitmap(intent_m.getmIcon());
            et_er1.setText(intent_m.getmTitle());
            et_er2.setText(intent_m.getmInfo());
            et_url.setText(intent_m.getmUrl());
            initRB();
            tv_1.setVisibility(View.GONE);
            tv_3.setVisibility(View.GONE);
            tv_4.setVisibility(View.GONE);
        }

        tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
        tv_2.setTextColor(getResources().getColor(R.color.text_color_white));
        tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
        tv_4.setTextColor(getResources().getColor(R.color.textcolor_hui));
        tv_1.setBackground(null);
        tv_2.setBackground(getResources().getDrawable(R.drawable.shape_sure_btn_blue));
        tv_3.setBackground(null);
        tv_4.setBackground(null);
        iv_xIcon.setVisibility(View.VISIBLE);
        ll_icon.setVisibility(View.VISIBLE);
        if (isCompile) {
            AsynImageLoader asynImageLoader = new AsynImageLoader();
            asynImageLoader.showImageAsyn(iv_xIcon, intent_m.getIcon_url(), R.drawable.nopic);
        }
        ll_yi.setVisibility(View.VISIBLE);
        ll_er.setVisibility(View.VISIBLE);
        ll_erx.setVisibility(View.GONE);
        ll_tel.setVisibility(View.GONE);
        iv_tong_icon.setVisibility(View.GONE);
        tv_explain.setText(R.string.explain_tuwen);
        et_er1.setHint(R.string.et_er1_tuwen);
        et_er2.setHint(R.string.et_er2_tuwen);
    }

    /**
     * 名片
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void mingPian() {
        if (myType == 1) {
            //设置图片,接数据后打开
            //iv_xIcon.setImageBitmap(intent_m.getmIcon());
            et_san_1.setText(intent_m.getmName());
            et_san_2.setText(intent_m.getmPhone());
            et_san_3.setText(intent_m.getmAdress());
            initRB();
            tv_1.setVisibility(View.GONE);
            tv_2.setVisibility(View.GONE);
            tv_4.setVisibility(View.GONE);
        }

        tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
        tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
        tv_3.setTextColor(getResources().getColor(R.color.text_color_white));
        tv_4.setTextColor(getResources().getColor(R.color.textcolor_hui));
        tv_1.setBackground(null);
        tv_2.setBackground(null);
        tv_3.setBackground(getResources().getDrawable(R.drawable.shape_sure_btn_blue));
        tv_4.setBackground(null);
        if (isCompile) {
            AsynImageLoader asynImageLoader = new AsynImageLoader();
            asynImageLoader.showImageAsyn(iv_xIcon, intent_m.getIcon_url(), R.drawable.nopic);
        }
        ll_icon.setVisibility(View.VISIBLE);
        ll_erx.setVisibility(View.VISIBLE);
        ll_tel.setVisibility(View.VISIBLE);

        ll_yi.setVisibility(View.GONE);
        ll_er.setVisibility(View.GONE);
        iv_tong_icon.setVisibility(View.GONE);
        tv_explain.setText(R.string.explain_mingpian);
    }

    /**
     * 二维码
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void erWeiMa() {

        if (myType == 1) {

            //设置图片,接数据后打开
            //iv_xIcon.setImageBitmap(intent_m.getmIcon());
            et_er1.setText(intent_m.getmTitle());
            et_er2.setText(intent_m.getmInfo());
            initRB();

            tv_1.setVisibility(View.GONE);
            tv_2.setVisibility(View.GONE);
            tv_3.setVisibility(View.GONE);
        }

        tv_1.setTextColor(getResources().getColor(R.color.textcolor_hui));
        tv_2.setTextColor(getResources().getColor(R.color.textcolor_hui));
        tv_3.setTextColor(getResources().getColor(R.color.textcolor_hui));
        tv_4.setTextColor(getResources().getColor(R.color.text_color_white));
        tv_1.setBackground(null);
        tv_2.setBackground(null);
        tv_3.setBackground(null);
        tv_4.setBackground(getResources().getDrawable(R.drawable.shape_sure_btn_blue));

        if (isCompile) {
            AsynImageLoader asynImageLoader = new AsynImageLoader();
            asynImageLoader.showImageAsyn(iv_xIcon, intent_m.getIcon_url(), R.drawable.nopic);
        }


        ll_er.setVisibility(View.VISIBLE);
        ll_icon.setVisibility(View.VISIBLE);
        iv_tong_icon.setVisibility(View.GONE);
        ll_tel.setVisibility(View.GONE);
        ll_erx.setVisibility(View.GONE);
        ll_yi.setVisibility(View.GONE);
        tv_explain.setText(R.string.explain_erweima);
        et_er1.setHint(R.string.et_er1_erweima);
        et_er2.setHint(R.string.et_er2_erweima);
    }

    private void initRB() {
        if (intent_m.ismTop()) {
            radioButton_1.setChecked(true);
        } else {
            radioButton_1.setChecked(false);
        }
        if (intent_m.ismBottom()) {
            radioButton_2.setChecked(true);
        } else {
            radioButton_2.setChecked(false);
        }
        if (intent_m.ismTailor()) {
            radioButton_3.setChecked(true);
        } else {
            radioButton_3.setChecked(false);
        }
    }

    private Dialog dialog_help_2;
    View view_2;
    helpdialog_item_2 hi_2 = null;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showPhotoDialog() {
        if (hi_2 == null) {
            hi_2 = new helpdialog_item_2();
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
        } else {
            hi_2 = (helpdialog_item_2) view_2.getTag();
        }
        hi_2.tv_help1.setText("相册选取");
        hi_2.tv_help2.setText("相机拍照");
        if(b_rb3){
            hi_2.tv_help2.setBackground(getResources().getDrawable(R.drawable.photo_camera_selector1));
        }else{
            hi_2.tv_help2.setBackground(getResources().getDrawable(R.drawable.photo_camera_pressed));
        }

        hi_2.tv_help1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date1 = new Date(System.currentTimeMillis());
                pickImage();
                dialog_help_2.dismiss();
            }
        });
        hi_2.tv_help2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Date date = new Date(System.currentTimeMillis());
                if(b_rb3) {
                    takePicktrue();
                }else{

                }

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

    class helpdialog_item_2 {
        TextView tv_help1;
        TextView tv_help2;
        TextView tv_helpcancle;
    }

    public String imgToBase64(String imgPath, Bitmap bitmap) {
        if (imgPath != null && imgPath.length() > 0) {
            bitmap = readBitmap(imgPath);
        }
        if (bitmap == null) {
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
}
