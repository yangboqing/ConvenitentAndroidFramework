package com.convenitent.framework.image.universal;

import com.convenitent.framework.app.$;
import com.convenitent.framework.image.BasicInitImageLoader;
import com.convenitent.framework.utils.AppUtils;
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
 * universal-image-loader框架初始化
 * Created by yangboqing on 2016/11/28.
 */

public class UniversalImageLoader implements BasicInitImageLoader {

    @Override
    public void init() {
        File cacheDir = StorageUtils.getCacheDirectory($.sAppContext); // 缓存文件夹路径
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .displayer(new FadeInBitmapDisplayer(500))
                .imageScaleType(ImageScaleType.EXACTLY).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                $.sAppContext)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(3)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(5 * 1024 * 1024))
                // 可以通过自己的内存缓存实现
                .memoryCacheSize(AppUtils.getMemorySize() / 8)
                // 内存缓存的最大值
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // .writeDebugLogs() // Remove for release app
                .defaultDisplayImageOptions(options).build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
