package com.convenitent.framework.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.convenitent.framework.utils.LogUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * 初始化Application一些工作
 * 例如:Imageloader
 * Created by yangboqing on 16/8/24.
 */
public class SupportApplication extends Application {

    public Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = getApplicationContext();
        initImageLoader();

    }

    /**
     * 初始化配置ImageLoader
     */
    private void initImageLoader(){
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext()); // 缓存文件夹路径
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .displayer(new FadeInBitmapDisplayer(500))
                .imageScaleType(ImageScaleType.EXACTLY).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(3)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(5 * 1024 * 1024))
                // 可以通过自己的内存缓存实现
                .memoryCacheSize(getMemorySize() / 8)
                // 内存缓存的最大值
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // .writeDebugLogs() // Remove for release app
                .defaultDisplayImageOptions(options).build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    private int getMemorySize() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context
                .ACTIVITY_SERVICE);
        int memClass = activityManager.getMemoryClass();
        LogUtils.$i("获取dalvik虚拟机分配给app的内存大小:"+memClass);
        return memClass;
    }
}
