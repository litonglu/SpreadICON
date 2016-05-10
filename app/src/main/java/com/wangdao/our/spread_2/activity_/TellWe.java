package com.wangdao.our.spread_2.activity_;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wangdao.our.spread_2.R;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class TellWe extends Activity {
    private ImageView iv_cancle;


   // private Button bt_tellW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tell_we);
      //  bt_tellW = (Button) findViewById(R.id.activity_tell_we_bt_tellwe);
        iv_cancle = (ImageView) findViewById(R.id.activity_tell_we_iv_cancle);
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        bt_tellW.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:66663324"));
//                if (ActivityCompat.checkSelfPermission(TellWe.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                startActivity(intent);
//            }
//        });
    }
}
