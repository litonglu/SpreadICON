package com.wangdao.our.spread_2;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * For developer startup JPush SDK
 * 
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 */
public class ExampleApplication extends Application {
    private static final String TAG = "JPush";

    private static ExampleApplication exampleApplication = new ExampleApplication();
    public static ExampleApplication getInstance(){
        return exampleApplication;
    }

    @Override
    public void onCreate() {    	     
    	 Log.d(TAG, "[ExampleApplication] onCreate");
         super.onCreate();
        initImageLoader();
         JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
         JPushInterface.init(this);     		// 初始化 JPush
    }

    /**
     * 初始化ImageLoader
     */
    public void initImageLoader(){
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCache(new LruMemoryCache(5*1024*1024))
                .memoryCacheSize(10*1024*1024)
                .discCache(new UnlimitedDiscCache(cacheDir))
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        ImageLoader.getInstance().init(config);
    }

    public DisplayImageOptions getOptions(int drawableId){
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(drawableId)
                .showImageForEmptyUri(drawableId)
                .showImageOnFail(drawableId)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .bitmapConfig(Bitmap.Config.RGB_565)
//                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .build();
    }
//    public DisplayImageOptions options2(){
//        return  new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.nopilc_2)
//                .showImageForEmptyUri(R.drawable.nopilc_2)
//                .showImageOnFail(R.drawable.nopilc_2)
//                .resetViewBeforeLoading(true)
//                .cacheInMemory(true)
//                .cacheOnDisc(true)
//                .imageScaleType(ImageScaleType.EXACTLY)
//                        // .bitmapConfig(Bitmap.Config.RGB_565)
//                .bitmapConfig(Bitmap.Config.ARGB_8888)
//                .displayer(new RoundedBitmapDisplayer(5))
//                .build();
//    }

}
