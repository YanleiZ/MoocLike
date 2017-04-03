package com.yanlei.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Entity;
import android.util.Log;

/**
 * Created by Yanlei on 2017/3/29.
 */

public class HttpUtil {
    // 创建HttpClient对象
    public static HttpClient httpClient = new DefaultHttpClient();
    public static final String BASE_URL = "http://192.168.1.109:8080/";
    public static final String REAL_NAME = "REALNAME";

    /**
     * @param url 发送请求的URL
     * @return 服务器响应字符串
     * @throws Exception
     */
    public static String getRequest(String url) throws Exception {
        // 创建HttpGet对象。
        HttpGet get = new HttpGet(url);
        // 发送GET请求
        HttpResponse httpResponse = httpClient.execute(get);
        // 如果服务器成功地返回响应
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            // 获取服务器响应字符串
            String result = EntityUtils.toString(httpResponse.getEntity());
            return result;
        } else {
            Log.d("服务器响应代码", (new Integer(httpResponse.getStatusLine()
                    .getStatusCode())).toString());
            return null;
        }
    }

    /**
     * @param url 发送请求的URL
     *            params
     *            请求参数
     * @return 服务器响应字符串
     * @throws Exception
     */
    public static String postRequest(String url, Map<String, String> rawParams)
            throws Exception {
        // 创建HttpPost对象。
        HttpPost post = new HttpPost(url);
        // 如果传递参数个数比较多的话可以对传递的参数进行封装
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String key : rawParams.keySet()) {
            // 封装请求参数
            params.add(new BasicNameValuePair(key, rawParams.get(key)));
        }
        // 设置请求参数
        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        // 发送POST请求
        HttpResponse httpResponse = httpClient.execute(post);
        // 如果服务器成功地返回响应
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            // 获取服务器响应字符串
            String result = EntityUtils.toString(httpResponse.getEntity());
            return result;
        }
        return null;
    }

    /**
     * 获取网络图片
     */
    public static byte[] getImage(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");   //设置请求方法为GET
        conn.setReadTimeout(5 * 1000);    //设置请求过时时间为5秒
        InputStream inputStream = conn.getInputStream();   //通过输入流获得图片数据

        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        byte[] data = bos.toByteArray();     //获得图片的二进制数据
        return data;
    }
}