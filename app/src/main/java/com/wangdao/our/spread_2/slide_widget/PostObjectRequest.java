package com.wangdao.our.spread_2.slide_widget;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wangdao.our.spread_2.slide_widget.widget_push.ResponseListener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/13 0013.
 */

public class PostObjectRequest<T> extends Request<T> {

    /**
     * 正确数据的时候回掉用
     */
    private ResponseListener mListener ;
    /*用来解析 json 用的*/
    private Gson mGson ;
    /*在用 gson 解析 json 数据的时候，需要用到这个参数*/
    private Type mClazz ;
    /*请求 数据通过参数的形式传入*/
    private Map<String,String> mParams;
    //需要传入参数，并且请求方式不能再为 get，改为 post
    public PostObjectRequest(String url, Map<String,String> params,Type type, ResponseListener listener) {
        super(Method.POST, url, listener);
        this.mListener = listener ;
        mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create() ;
        mClazz = type ;
        setShouldCache(false);
        mParams = params ;
    }

    /**
     * 这里开始解析数据
     * @param response Response from the network
     * @return
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            T result ;
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.v("zgy", "====jsonString===" + jsonString);
            result = mGson.fromJson(jsonString,mClazz) ;
            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    /**
     * 回调正确的数据
     * @param response The parsed response returned by
     */
    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    //关键代码就在这里，在 Volley 的网络操作中，如果判断请求方式为 Post 则会通过此方法来获取 param，所以在这里返回我们需要的参数，
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }
}