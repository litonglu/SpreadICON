package com.wangdao.our.spread_2.slide_widget.widget_push;

import com.android.volley.Response;

/**
 * Created by Administrator on 2016/6/13 0013.
 */
public interface ResponseListener<T> extends Response.ErrorListener,Response.Listener<T> {
}