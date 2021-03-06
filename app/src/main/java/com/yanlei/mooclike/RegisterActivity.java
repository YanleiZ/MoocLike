package com.yanlei.mooclike;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.yanlei.Utils.HttpUtil;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText rUserName;
    private EditText rPassword1;
    private EditText rPassword2;
    private EditText rName;
    private Spinner rSex;
    private EditText rPhone;
    private EditText rLocal;
    private EditText rEmail;

    private Button register;
    private Button cancel;

    public static String loginCode = "";

    private UserRegisterTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rUserName = (EditText) findViewById(R.id.rUsername);
        rPassword1 = (EditText) findViewById(R.id.rPassword1);
        rPassword2 = (EditText) findViewById(R.id.rPassword2);
        rName = (EditText) findViewById(R.id.rName);
        rSex = (Spinner) findViewById(R.id.rSex);
        rPhone = (EditText) findViewById(R.id.rPhone);
        rLocal = (EditText) findViewById(R.id.rLocal);
        rEmail = (EditText) findViewById(R.id.rEmail);

        register = (Button) findViewById(R.id.register_button);
        cancel = (Button) findViewById(R.id.cancel_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = rUserName.getText().toString().trim();
                String password1 = rPassword1.getText().toString().trim();
                String password2 = rPassword2.getText().toString().trim();
                String name = rName.getText().toString().trim();
                String sex = (String) rSex.getSelectedItem();
                String phone = rPhone.getText().toString().trim();
                String local = rLocal.getText().toString().trim();
                String email = rEmail.getText().toString().trim();
                if (userName.trim().equals("") || password1.trim().equals("") || name.trim().equals("") ||
                        sex.trim().equals("") || phone.trim().equals("") || local.trim().equals("") ||
                        email.trim().equals("")) {
                     Toast.makeText(RegisterActivity.this, "请完善个人信息!", Toast.LENGTH_SHORT).show();
                } else if(password1.length()<6){
                     Toast.makeText(RegisterActivity.this, "密码长度必须大于6!", Toast.LENGTH_SHORT).show();
                }else if (!password1.equals(password2)) {
                    Toast.makeText(RegisterActivity.this, "两次输入密码不一致!", Toast.LENGTH_SHORT).show();
                } else{
                    mAuthTask = new UserRegisterTask(userName, password1, name, sex, phone, local, email);
                    mAuthTask.execute((Void) null);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(nextIntent);
                // 结束该Activity
                finish();
            }
        });
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String rsuserName;
        private final String rspassword;
        private final String rsname;
        private final String rssex;
        private final String rsphone;
        private final String rslocal;
        private final String rsemail;

        UserRegisterTask(String username, String password, String name, String sex, String phone, String local, String email) {
            rsuserName = username;
            rspassword = password;
            rsname = name;
            rssex = sex;
            rsphone = phone;
            rslocal = local;
            rsemail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // network access.
                String strFlag = "";
                String loginFlag = "";
                // 使用Map封装请求参数
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("username", rsuserName);
                map.put("password", rspassword);
                map.put("confirm_password", rspassword);
                map.put("name", rsname);
                map.put("sex", rssex);
                map.put("age", null);
                map.put("tel", rsphone);
                map.put("address", rslocal);
                map.put("email", rsemail);
                // 定义发送请求的URL
                String url = HttpUtil.BASE_URL + "AppRegisterServlet?method=add";
                Log.d("url", url);
                Log.d("username", rsuserName);
                Log.d("password1", rspassword);
                Log.d("name", rsname);
                Log.d("sex", rssex);
                Log.d("phone", rsphone);
                Log.d("local", rslocal);
                Log.d("email", rsemail);
                try {
                    // 发送请求
                    strFlag = HttpUtil.postRequest(url, map);  //POST方式

                    Log.d("服务器返回值", strFlag);


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                  //
                String[] reArray = strFlag.split("\\|###\\|", 2);
                if(reArray.length<2){

                }else {
                    loginFlag = reArray[0];
                    loginCode = reArray[1];
                }

                //
                if (loginFlag.trim().equals("true")) {
                    // 如果注册成功
                    return true;
                } else {
                    //注册失败
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {


            if (success) {
                Toast.makeText(RegisterActivity.this, "注册成功，请登录!", Toast.LENGTH_SHORT).show();
                // 跳转到 HomeActivity
                Intent nextIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(nextIntent);
                // 结束该Activity
                finish();
            } else {
                if(loginCode.equals("exist")){
                    Toast.makeText(RegisterActivity.this, "此用户名已存在，请更换用户名再试!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(RegisterActivity.this, "注册失败，请稍后再试!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                // 利用handler延迟发送更改状态信息
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                //System.exit(0);
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
