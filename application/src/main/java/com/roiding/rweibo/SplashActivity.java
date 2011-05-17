package com.roiding.rweibo;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.roiding.rweibo.OAuthLoginDialog.DialogListener;
import com.roiding.rweibo.data.OAuthUser;

public class SplashActivity extends AbstractActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);


        OAuthUser oUser = provider.getVerifiedAccount();
        if (oUser == null) {
            // Button btn_login = (Button) findViewById(R.id.btn_login);

        } else {
            Intent intent = new Intent(SplashActivity.this, TimeLineActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_OAUTH_SUCCESS:
                    break;
                case Constants.MSG_OAUTH_FAIL:
                    break;
            }
        }
    };

    public void onClickHandler(View view) {
        new OAuthLoginDialog(this, provider, new DialogListener() {

            @Override
            public void onComplete(Bundle b) {
                mHandler.sendMessage(mHandler.obtainMessage(Constants.MSG_OAUTH_SUCCESS));
            }

            @Override
            public void onError(Exception e) {
                mHandler.sendMessage(mHandler.obtainMessage(Constants.MSG_OAUTH_FAIL, e.getMessage()));
            }

        }).show();
    }
}
