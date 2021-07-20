package edu.cuhk.csci3310.project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MapWebView extends Fragment {

    String location = "Go24";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_web_view, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            location = getArguments().getString("location");

        } catch (Exception e) {
            location = "Yuen Long Plaza";
        }


        WebView myWebView = (WebView) getActivity().findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewController());
        myWebView.getSettings().setJavaScriptEnabled(true);

        String locationWithoutSpace=location.replace(' ','+');

        myWebView.loadUrl("https://www.google.com/maps/search/?api=1&query=" +locationWithoutSpace );

    }

    public class WebViewController extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}