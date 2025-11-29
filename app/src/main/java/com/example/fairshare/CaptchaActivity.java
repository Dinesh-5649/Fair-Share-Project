package com.example.fairshare;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;

public class CaptchaActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new CaptchaInterface(), "Android");

        // Load local captcha.html
        webView.loadUrl("https://GunaBharathi-cmd.github.io/Fairshare.Captcha/captcha.html");
    }

    private class CaptchaInterface {
        @JavascriptInterface
        public void onCaptchaSuccess(String token) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("captcha_token", token);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
