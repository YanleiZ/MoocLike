package com.yanlei.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.yanlei.Utils.HttpUtil;

import java.util.HashMap;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ConnectIntentService extends IntentService {
    public static String loginCode;
    public ConnectIntentService() {
        super("TestIntentService");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        /**
         * 经测试，IntentService里面是可以进行耗时的操作的
         * IntentService使用队列的方式将请求的Intent加入队列，
         * 然后开启一个worker thread(线程)来处理队列中的Intent
         * 对于异步的startService请求，IntentService会处理完成一个之后再处理第二个
         */
        Boolean flag = false;
        //通过intent获取主线程传来的用户名和密码字符串
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        flag = doLogin(username, password);
        Log.d("登录结果", flag.toString());

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("ACTION_RECV_MSG");
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra("result", flag.toString());
        broadcastIntent.putExtra("resultCode", loginCode);
        sendBroadcast(broadcastIntent);

    }

    // 定义发送请求的方法
    private Boolean doLogin(String username, String password)
    {
        String strFlag = "";
        String loginFlag = "";
        // 使用Map封装请求参数
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("un", username);
        map.put("pw", password);
        // 定义发送请求的URL
        String url = HttpUtil.BASE_URL + "?un=" + username + "&pw=" + password;  //GET方式
        // String url = HttpUtil.BASE_URL + "LoginServlet"; //POST方式
        Log.d("url", url);
        Log.d("username", username);
        Log.d("password", password);
        try {
            // 发送请求
            strFlag = HttpUtil.postRequest(url, map);  //POST方式
//		  strFlag = HttpUtil.getRequest(url);  //GET方式
            Log.d("服务器返回值", strFlag);
            String [] reArray =  strFlag.split("\\|###\\|",2);
            if (reArray.length<2){

            }else {
                loginFlag = reArray[0];
                loginCode = reArray[1];
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(loginFlag.trim().equals("true")){
            return true;
        }else{
            return false;
        }

    }
}
