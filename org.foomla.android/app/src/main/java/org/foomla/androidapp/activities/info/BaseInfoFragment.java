package org.foomla.androidapp.activities.info;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.foomla.androidapp.R;
import org.foomla.androidapp.utils.ProgressVisualizationUtil;

public abstract class BaseInfoFragment extends Fragment {

    private final class WebViewClientWithProgressBar extends WebViewClient {

        private final Activity activity;
        private final WebView webView;

        private WebViewClientWithProgressBar(WebView webView, Activity activity) {
            this.webView = webView;
            this.activity = activity;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.setVisibility(View.VISIBLE);
            ProgressVisualizationUtil.hideProgressbar(activity);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            webView.setVisibility(View.GONE);
            ProgressVisualizationUtil.showProgressbar(activity);
        }
    }

    private WebView webView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_info_web_view, container, false);

        webView = (WebView) fragmentView.findViewById(R.id.web_view);

        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebSettings settings = webView.getSettings();
        settings.setPluginState(PluginState.ON);
        webView.setWebViewClient(new WebViewClientWithProgressBar(webView, getActivity()));

        webView.setVisibility(View.GONE);
        webView.loadUrl(getUrl());
    }

    protected abstract String getUrl();

}
