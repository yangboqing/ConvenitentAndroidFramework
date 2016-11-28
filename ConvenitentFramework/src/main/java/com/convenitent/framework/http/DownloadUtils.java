package com.convenitent.framework.http;

import android.content.ContentValues;

import com.convenitent.framework.app.$;
import com.convenitent.framework.utils.FileUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

/**
 * Created by yangboqing on 16/8/16.
 * 上传和下载工具类
 */
public final class DownloadUtils {

    private static ExecutorService mExecutorService = Executors.newFixedThreadPool(4);

    /**
     * 异步下载文件
     * @param downloadUrl
     * @param dest
     * @param append
     * @param callBack
     * @return
     * @throws Exception
     */
    public static long $download(String downloadUrl, File dest, boolean append, DownloadCallBack callBack) throws Exception {
        return $download(downloadUrl, dest, append, new ContentValues(), callBack);
    }

    /**
     * 异步下载文件,支持自定义Header
     * @param downloadUrl
     * @param dest
     * @param append
     * @param header
     * @param callBack
     * @return
     * @throws Exception
     */
    public static long $download(String downloadUrl, File dest, boolean append, ContentValues header, DownloadCallBack callBack) throws Exception {
        int progress = 0;
        long remoteSize = 0;
        int currentSize = 0;
        long totalSize = -1;

        if (!append && dest.exists() && dest.isFile()) {
            dest.delete();
        }

        if (append && dest.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(dest);
                currentSize = fis.available();
            } catch (IOException e) {
                throw e;
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
        }
        InputStream is = null;
        FileOutputStream os = null;
        try {
            URL u = new URL(downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setUseCaches(false);
            conn.setConnectTimeout($.sConnectTimeOut);
            conn.setReadTimeout($.sReadTimeout);
            conn.setRequestMethod("GET");
            // 设置断点续传的起始位置
            if (currentSize > 0) {
                conn.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
            }
            // 自定义header
            for (Map.Entry<String, Object> entry : header.valueSet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                conn.setRequestProperty(key, value);
            }
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = conn.getInputStream();
                remoteSize = conn.getContentLength();
                String contentEndcoding = conn.getHeaderField("Content-Encoding");
                if (contentEndcoding != null && contentEndcoding.equalsIgnoreCase("gzip")) {
                    is = new GZIPInputStream(is);
                }
                os = new FileOutputStream(dest, append);
                byte buffer[] = new byte[8192];
                int readSize = 0;
                while ((readSize = is.read(buffer)) > 0) {
                    os.write(buffer, 0, readSize);
                    os.flush();
                    totalSize += readSize;
                    // 通知回调下载进度
                    if (callBack != null) {
                        progress = (int) (totalSize * 100 / remoteSize);
                        callBack.onDownloading(progress);
                    }
                }
                if (progress != 100) {
                    callBack.onDownloading(100);
                }

                if (totalSize < 0) {
                    totalSize = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }

        if (totalSize < 0) {
            throw new Exception("Download file fail: " + downloadUrl);
        }

        // 下载完成并通知回调
        if (callBack != null) {
            callBack.onDownloaded();
        }

        return totalSize;
    }

    /**
     * 同步上传文件
     * @param url
     * @param params
     * @param files
     * @return
     */
    public static String $upload(String url, Map<String, String> params, Map<String, File> files) {
        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";

        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;

        try {
            URL uri = new URL(url);
            conn = (HttpURLConnection) uri.openConnection();
            conn.setReadTimeout($.sReadTimeout);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

            // text parameters
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);
                sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=GBK" + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append(entry.getValue());
                sb.append(LINEND);
            }
            os = new DataOutputStream(conn.getOutputStream());
            os.write(sb.toString().getBytes());
            // send file data
            if (files != null)
                for (Map.Entry<String, File> file : files.entrySet()) {
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    sb1.append("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
                            + file.getValue().getName() + "\"" + LINEND);
                    sb1.append("Content-Type: application/octet-stream; charset=GBK" + LINEND);
                    sb1.append(LINEND);
                    os.write(sb1.toString().getBytes());

                    is = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                    is.close();
                    os.write(LINEND.getBytes());
                }

            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            os.write(end_data);
            os.flush();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                return FileUtils.$read(in);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 异步上传文件
     * @param url
     * @param params
     * @param files
     * @param callBack
     */
    public static void $upload(final String url, final Map<String, String> params, final Map<String, File> files, final CallBack callBack) {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                String result = $upload(url, params, files);
                callBack.onFinish(result);
            }
        });
    }

    /**
     * Http请求回调
     * onFinish,带结果字符串
     */
    public interface CallBack {
        void onFinish(String result);
    }

    /**
     * 下载回调:下载进度和下载完成
     * onDownloading,带一个进度值:0~100
     * onDownloaded
     */
    public interface DownloadCallBack {
        void onDownloading(int progress);
        void onDownloaded();
    }
}
