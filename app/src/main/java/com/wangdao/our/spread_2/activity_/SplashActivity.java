package com.wangdao.our.spread_2.activity_;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.wangdao.our.spread_2.MainActivity;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.slide_widget.AllUrl;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
public class SplashActivity extends Activity {
	private static final long DELAY_TIME = 4000L;
	private HttpPost httpPost;
	private HttpResponse httpResponse = null;
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
	private AllUrl allurl = new AllUrl();
	private Boolean isLogin = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		httpPost = new HttpPost(allurl.getLogin_url());
		autoLogin();
		redirectByTime();
		init();

	}

	private void redirectByTime() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(isLogin){
					Intent intent = new Intent(SplashActivity.this, MainActivity.class);
					startActivity(intent);
				}else{
					Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(intent);
				}

				finish();
			}
		}, DELAY_TIME);
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
	private void init() {
		JPushInterface.init(getApplicationContext());
	}

	private void autoLogin() {
		SharedPreferences sharedPreferences = SplashActivity.this.getSharedPreferences("user", MODE_PRIVATE);
		String aMobile = sharedPreferences.getString("mobile", "");
		String aPwd = sharedPreferences.getString("pwd", "");

		params.add(new BasicNameValuePair("mobile", aMobile));
		params.add(new BasicNameValuePair("password", aPwd));
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					try {
						httpResponse = new DefaultHttpClient().execute(httpPost);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(httpResponse.getEntity());
						JSONObject jo = new JSONObject(result);
						if (jo.getString("status").equals("1")) {
							isLogin = true;
						} else {
							isLogin = false;
						}
					}
				} catch (Exception e) {
					isLogin = false;
					e.printStackTrace();
				}
			}
		}).start();
	}


}
