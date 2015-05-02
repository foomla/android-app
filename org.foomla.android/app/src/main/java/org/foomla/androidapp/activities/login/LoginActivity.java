package org.foomla.androidapp.activities.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginActivity extends BaseActivity {

    private class OAuthWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.clearCache(true);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            logger.debug("OAuthActivity on page started for URL {}", url);

            if (url.startsWith(getFoomlaApplication().getOAuthRedirectUrl())) {
                String authCode = parseAuthCode(url);
                if (authCode != null) {
                    setAuthCodeResult(authCode);
                } else {
                    String error = parseError(url);
                    setErrorResult(error);
                }
                finish();
            } else {
                super.onPageStarted(view, url, favicon);
            }
        }

        private String parseAuthCode(String url) {

            Matcher matcher = AUTH_CODE_PATTERN.matcher(url);

            if (matcher.matches()) {
                String authCode = matcher.group(1);
                return authCode;
            }

            return null;
        }

        private String parseError(String url) {

            Matcher matcher = ERROR_PATTERN.matcher(url);

            if (matcher.matches()) {
                String error = matcher.group(1);
                return error;
            }

            return null;
        }

    }

    public static final String CODE = "CODE";

    public static final String ERROR = "ERROR";

    private static final Pattern AUTH_CODE_PATTERN = Pattern.compile(".*code=(\\w+).*");
    private static final Pattern ERROR_PATTERN = Pattern.compile(".*error=(\\w+).*");
    private final Logger logger = LoggerFactory.getLogger(LoginActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        boolean register = getIntent().getExtras().getBoolean("register");
        initOAuthWebView(register);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private WebView initOAuthWebView(boolean register) {

        WebView webView = (WebView) this.findViewById(R.id.web_view);
        webView.setWebViewClient(new OAuthWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        String authorizeUrl = getFoomlaApplication().getOAuthAuthorizeUrl();
        if (register) {
            authorizeUrl = authorizeUrl + "&scope=register";
        }
        webView.loadUrl(authorizeUrl);

        return webView;
    }

    private void setAuthCodeResult(String authCode) {
        Intent intent = new Intent();
        intent.putExtra(CODE, authCode);
        if (getParent() == null) {
            setResult(RESULT_OK, intent);
        } else {
            getParent().setResult(RESULT_OK, intent);
        }
    }

    private void setErrorResult(String error) {
        Intent intent = new Intent();
        if (error != null) {
            intent.putExtra(ERROR, error);
        }
        if (getParent() == null) {
            setResult(RESULT_CANCELED, intent);
        } else {
            getParent().setResult(RESULT_CANCELED, intent);
        }
    }
}
