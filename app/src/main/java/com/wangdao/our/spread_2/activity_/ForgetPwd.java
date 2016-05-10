package com.wangdao.our.spread_2.activity_;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.wangdao.our.spread_2.R;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class ForgetPwd extends Activity{

    private EditText et_mobile,et_pwd,et_code;
    private Button bt_getCode,bt_rePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        initView();
    }


    private void initView(){
        et_mobile = (EditText) findViewById(R.id.activity_forget_pwd_et_tel);
        et_pwd = (EditText) findViewById(R.id.activity_forget_pwd_et_pwd);
        et_code = (EditText) findViewById(R.id.activity_forget_pwd_et_code);
        bt_getCode = (Button) findViewById(R.id.activity_forget_pwd_bt_getcode);
        bt_rePwd = (Button) findViewById(R.id.activity_forget_pwd_bt_repwd);
    }
}
