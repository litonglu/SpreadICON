package com.wangdao.our.spread_2.wxapi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wangdao.our.spread_2.R;
import com.wangdao.our.spread_2.activity_.LoginActivity;
import com.wangdao.our.spread_2.slide_widget.AllUrl;

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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/7 0007.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler
{
    private IWXAPI api;
    private final String APP_ID = "wxf9612b61458aff13";

    private HttpPost httpPost;
    private HttpResponse httpResponse = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    private AllUrl allurl = new AllUrl();
    private wxHandler whandler  = new wxHandler();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, APP_ID, false);
        api.registerApp(APP_ID);
        api.handleIntent(getIntent(), this);

    }

    @Override
    public void onReq(BaseReq req)
    {
    }

    @Override
    public void onResp(BaseResp resp)
    {
        int result = 0;
        switch (resp.errCode)
        {
            case BaseResp.ErrCode.ERR_OK:
                shareOk();
                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }




      //  Toast.makeText(this, result, Toast.LENGTH_LONG).show();

        finish();

        overridePendingTransition(R.anim.change_in, R.anim.change_out);

    }
private String shareResult;
    private void shareOk(){
        httpPost = new HttpPost(allurl.getShareOk());
        SharedPreferences sharedPreferences = WXEntryActivity.this.getSharedPreferences("user", MODE_PRIVATE);
        String userToken = sharedPreferences.getString("user_token", "");
        SharedPreferences sharedPreferences_id = WXEntryActivity.this.getSharedPreferences("shareid", MODE_PRIVATE);
        String uid = sharedPreferences_id.getString("sid", "");
        params.add(new BasicNameValuePair("user_token", userToken));
        params.add(new BasicNameValuePair("writing_id", uid));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpResponse = new DefaultHttpClient().execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(httpResponse.getEntity());
                        JSONObject jo = new JSONObject(result);
                        shareResult = jo.getString("info");
                        if(jo.getString("status").equals("1")){
                            whandler.sendEmptyMessage(1);
                        }else{
                            whandler.sendEmptyMessage(2);
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

    class wxHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //分享成功
                case 1:
                    Log.i("qqqqqq",shareResult);
                    Toast.makeText(WXEntryActivity.this, shareResult, Toast.LENGTH_LONG).show();
                    break;
                //分享失败
                case 2:
                    Log.i("qqqqqq",shareResult);
                    Toast.makeText(WXEntryActivity.this, shareResult, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

}
