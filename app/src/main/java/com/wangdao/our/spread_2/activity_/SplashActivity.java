package com.wangdao.our.spread_2.activity_;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.wangdao.our.spread_2.MainActivity;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.slide_widget.AllUrl;
import com.wangdao.our.spread_2.slide_widget.widget_push.ExampleUtil;
import com.wangdao.our.spread_2.slide_widget.widget_push.PushSetActivity;

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
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class SplashActivity extends Activity {
	private static final long DELAY_TIME = 4000L;
	private HttpPost httpPost;
	private HttpResponse httpResponse = null;
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
	private AllUrl allurl = new AllUrl();
	private Boolean isLogin = false;
	private MyHandler_S myHandler_s = new MyHandler_S();

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

	private String userIdTag;
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
							JSONObject jo_2 = jo.getJSONObject("data");
							userIdTag = jo_2.getString("uid");
							isLogin = true;
							myHandler_s.sendEmptyMessage(1);
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


		class MyHandler_S extends Handler{

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what){
					case 1:
						SharedPreferences sharedPreferences = SplashActivity.this.getSharedPreferences("setTag", MODE_PRIVATE);
						String stat = sharedPreferences.getString("status", "");
						setAlias(userIdTag);
//						if(stat.equals("1")){
//
//						}else {
//							setAlias(userIdTag);
//						}
						break;
					case 2:

						break;
				}
			}
		}

	private void setAlias(String alias) {
		//EditText aliasEdit = (EditText) findViewById(R.id.et_alias);
		//String alias = aliasEdit.getText().toString().trim();
		if (TextUtils.isEmpty(alias)) {
			Log.i("qqqqq","标签为空");
			return;
		}
		if (!ExampleUtil.isValidTagAndAlias(alias)) {
			Log.i("qqqqq","标签无效");
			return;
		}

		// 调用 Handler 来异步设置别名
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));

	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
				case 0:

					logs = "Set tag and alias success";
					Log.i("qqqqq", logs);
					// 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。

					SharedPreferences sharedPreferences = SplashActivity.this.getSharedPreferences("setTag", MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("status", "1");
					editor.commit();

					break;
				case 6002:
					logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
					Log.i("qqqqq", logs);
					// 延迟 60 秒来调用 Handler 设置别名
					mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
					break;
				default:
					logs = "Failed with errorCode = " + code;
					Log.e("qqqqq", logs);
			}
		//	ExampleUtil.showToast(logs, getApplicationContext());
		}
	};


	private static final int MSG_SET_ALIAS = 1001;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG_SET_ALIAS:
					Log.d("qqqqq", "Set alias in handler.");
					// 调用 JPush 接口来设置别名。
					JPushInterface.setAliasAndTags(getApplicationContext(),
							(String) msg.obj,
							null,
							mAliasCallback);
					break;
				default:
					Log.i("qqqqq", "Unhandled msg - " + msg.what);
			}
		}
	};

}
