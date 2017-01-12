package com.convenitent.framework.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;

import com.convenitent.framework.app.$;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by yangboqing on 16/8/16.
 */
public final class AppUtils {

    /**
     * 获取app的版本数versionCode,比如38
     * @return
     */
    public static int $vercode() {
        int result = 0;
        String packageName = $.sAppContext.getPackageName();
        PackageInfo packageInfo;
        try {
            packageInfo = $.sAppContext.getPackageManager().getPackageInfo(packageName, 0);
            result = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError(e);
        }
        return result;
    }

    /**
     * 获取app的版本名versionName,比如0.6.9
     * @return
     */
    public static String $vername() {
        String result = null;
        String packageName = $.sAppContext.getPackageName();
        PackageInfo packageInfo;
        try {
            packageInfo = $.sAppContext.getPackageManager().getPackageInfo(packageName, 0);
            result = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError(e);
        }
        return result;
    }

    /**
     * 获取app的名称
     * @return
     */
    public static String $appname() {
        String result = null;
        String packageName = $.sAppContext.getPackageName();
        ApplicationInfo applicationInfo;
        try {
            PackageManager packageManager = $.sAppContext.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            result = packageManager.getApplicationLabel(applicationInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断一个app是否在运行
     * @param packageName app的包名
     * @return 在运行则返回true,否则false
     */
    public static boolean $running(String packageName) {
        if (packageName == null) {
            packageName = $.sAppContext.getPackageName();
        }
        ActivityManager am = (ActivityManager) $.sAppContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo rapi : infos){
            if(rapi.processName.equals(packageName))
                return true;
        }
        return false;
    }

    /**
     * 判断一个activity是否在前台运行
     * @param activityName activity的全路径名称
     * @return 在前台则返回true,否则返回false
     */
    public static boolean isTopActivy(String activityName) {
        String cmpNameTemp = null;
        //这里是对android 5.0系统get_task权限进行兼容
//        if (Build.VERSION.SDK_INT < 21) { // 如果版本低于22
            ActivityManager manager = (ActivityManager) $.sAppContext.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
            if (runningTaskInfos != null) {
                cmpNameTemp = runningTaskInfos.get(0).topActivity.getShortClassName();
            }
//        }else{
//            final int PROCESS_STATE_TOP = 2;
//            try {
//                // 获取正在运行的进程应用的信息实体中的一个字段,通过反射获取出来
//                Field processStateField = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
//                // 获取所有的正在运行的进程应用信息实体对象
//                List<ActivityManager.RunningAppProcessInfo> processes = ((ActivityManager) $.sAppContext
//                        .getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
//                // 循环所有的进程,检测某一个进程的状态是最上面,也是就最近运行的一个应用的状态的时候,就返回这个应用的包名
//                for (ActivityManager.RunningAppProcessInfo process : processes) {
//                    if (process.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
//                            && process.importanceReasonCode == 0) {
//                        int state = processStateField.getInt(process);
//                        if (state == PROCESS_STATE_TOP) { // 如果这个实体对象的状态为最近的运行应用
//                            cmpNameTemp = process.importanceReasonComponent.getShortClassName();
//                        }
//                    }
//                }
//            } catch (Exception e) {
//            }
//        }
        if (cmpNameTemp == null) {
            return false;
        }
        return cmpNameTemp.endsWith(activityName);
    }

    /**
     * 获取当前显示的app的包名
     * @return
     */
    public static String $getTopProcessPackageName(){
        String cmpNameTemp = null;
        //这里是对android 5.0系统get_task权限进行兼容
        if (Build.VERSION.SDK_INT < 21) { // 如果版本低于22
            ActivityManager manager = (ActivityManager) $.sAppContext.getSystemService(Context.ACTIVITY_SERVICE);

            List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
            if (runningTaskInfos != null) {
                cmpNameTemp = runningTaskInfos.get(0).topActivity.getPackageName();
            }
        }else{
            final int PROCESS_STATE_TOP = 2;
            try {
                // 获取正在运行的进程应用的信息实体中的一个字段,通过反射获取出来
                Field processStateField = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
                // 获取所有的正在运行的进程应用信息实体对象
                List<ActivityManager.RunningAppProcessInfo> processes = ((ActivityManager) $.sAppContext
                        .getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
                // 循环所有的进程,检测某一个进程的状态是最上面,也是就最近运行的一个应用的状态的时候,就返回这个应用的包名
                for (ActivityManager.RunningAppProcessInfo process : processes) {
                    if (process.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                            && process.importanceReasonCode == 0) {
                        int state = processStateField.getInt(process);
                        if (state == PROCESS_STATE_TOP) { // 如果这个实体对象的状态为最近的运行应用
                            cmpNameTemp = process.processName;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return cmpNameTemp;
    }

    /**
     * 获取应用公钥签名
     * @param context
     * @return
     */
    public static Signature $sign(Context context) {
        PackageInfo pi;
        Signature sign = null;
        try {
            pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            sign = pi.signatures[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

    /**
     * 比较当前签名HashCode和预设的HashCode
     * @param context
     * @param presetHashCode
     * @return
     */
    public static boolean $signCheckWithHashCode(Context context, int presetHashCode) {
        Signature signature = $sign(context);
        return signature.hashCode() == presetHashCode;
    }


    /**
     * 获取dalvik给应用所分配的堆大小
     * @return
     */
    public static int $getMemorySize() {
        ActivityManager activityManager = (ActivityManager) $.sAppContext.getSystemService(Context
                .ACTIVITY_SERVICE);
        int memClass = activityManager.getMemoryClass();
        LogUtils.$i("获取dalvik虚拟机分配给app的内存大小:"+memClass);
        return memClass;
    }
    /**
     * 删除应用数据： cache, file, share prefs, databases
     * @param context
     */
    public static void $clear(Context context) {
        $clearCache(context);
        $clearFiles(context);
        $clearSharedPreference(context);
        $clearDatabase(context);
    }



    /**
     * 删除应用缓存目录
     * @param context
     */
    public static void $clearCache(Context context) {
        FileUtils.$del(context.getCacheDir(), true);
        FileUtils.$del(context.getExternalCacheDir(), true);
    }

    /**
     * 删除应用文件目录
     * @param context
     */
    public static void $clearFiles(Context context) {
        FileUtils.$del(context.getFilesDir(), true);
    }

    /**
     * 删除应用Shared Prefrence目录
     * @param context
     */
    public static void $clearSharedPreference(Context context) {
        FileUtils.$del(new File("/data/data/" + context.getPackageName() + "/shared_prefs"), true);
    }

    /**
     * 删除应用数据库目录
     * @param context
     */
    public static void $clearDatabase(Context context) {
        FileUtils.$del(new File("/data/data/" + context.getPackageName() + "/databases"), true);
    }

}
