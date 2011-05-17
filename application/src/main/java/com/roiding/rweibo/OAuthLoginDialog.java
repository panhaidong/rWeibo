package com.roiding.rweibo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.roiding.rweibo.provider.DataProvider;

public class OAuthLoginDialog extends Dialog {

    static final float[] DIMENSIONS_LANDSCAPE = { 460, 260 };
    static final float[] DIMENSIONS_PORTRAIT = { 280, 420 };

    private Context mContext;
    private DataProvider mProvider;
    private DialogListener mListener;
    private Handler mHandler;

    private WebView mWebView;

    public OAuthLoginDialog(Context context, DataProvider provider, DialogListener listener) {
        super(context);
        mContext = context;
        mProvider = provider;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mHandler = new Handler();

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        final float scale = getContext().getResources().getDisplayMetrics().density;
        float[] dimensions = display.getWidth() < display.getHeight() ? DIMENSIONS_PORTRAIT : DIMENSIONS_LANDSCAPE;


        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_oauth_login, null);
        addContentView(contentView, new FrameLayout.LayoutParams((int) (dimensions[0] * scale + 0.5f),
                (int) (dimensions[1] * scale + 0.5f)));
        mWebView = (WebView) contentView.findViewById(R.id.web_login);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new OAuthLoginWebViewClient());
        retrieveRequestToken();
    }

    private void retrieveRequestToken() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String mUrl = mProvider.getAuthUrl();
                    mWebView.loadUrl(mUrl);
                } catch (Exception e) {
                    mListener.onError(e);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void retrieveAccessToken(final String url) {
        new Thread() {
            @Override
            public void run() {
                Uri uri = Uri.parse(url);
                String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
                try {
                    mProvider.retrieveAccessToken(verifier);
                } catch (Exception e) {
                    mListener.onError(e);
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        OAuthLoginDialog.this.dismiss();
                    }
                });
            }
        }.start();
    }

    private class OAuthLoginWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(Constants.LOG_TAG, "shouldOverrideUrlLoading: " + url);
            if (url.startsWith(Private.OAUTH_CALLBACK_URL)) {
                retrieveAccessToken(url);
                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d(Constants.LOG_TAG, "onReceivedError: " + errorCode + ", description" + description);
            super.onReceivedError(view, errorCode, description, failingUrl);
            // mListener.onError(new DialogError(description, errorCode,
            // failingUrl));
            OAuthLoginDialog.this.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(Constants.LOG_TAG, "WebView loading URL: " + url);
            super.onPageStarted(view, url, favicon);
            // if (mSpinner.isShowing()) {
            // mSpinner.dismiss();
            // }
            // mSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(Constants.LOG_TAG, "onPageFinished " + url);

        }
    }

    public static interface DialogListener {
        public void onComplete(Bundle values);

        public void onError(Exception e);
    }

}
