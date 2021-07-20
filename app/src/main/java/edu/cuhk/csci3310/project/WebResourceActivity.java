package edu.cuhk.csci3310.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebResourceActivity extends AppCompatActivity {


    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_resource);

        myWebView = findViewById(R.id.webViewTwo);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();


        String web_link = extras.getString("web_link");
        if(!web_link.startsWith("https://") && !web_link.startsWith("https://"))
            web_link = "https://" + web_link;


        myWebView.setWebViewClient(new WebResourceActivity.WebViewController());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(web_link);
//        myWebView.loadUrl("https://www.google.com" );
    }

    public class WebViewController extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}