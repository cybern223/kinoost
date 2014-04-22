package com.cyber.kinoost;

import com.cyber.kinoost.api.vk.sources.Auth;
import com.cyber.kinoost.api.*;
import com.cyber.kinoost.db.repositories.UserRepository;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.cyber.kinoost.R;

public class LoginActivity extends Activity {
	private static final String TAG = "Kate.LoginActivity";

	WebView webview;
	Account account;
	Context context;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UserRepository userRepo = new UserRepository(this);
		if(userRepo.getUser().getId() == 0) this.finish();
		
		setContentView(R.layout.login);
		
		context = this;

		webview = (WebView) findViewById(R.id.vkontakteview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.clearCache(true);

		// Чтобы получать уведомления об окончании загрузки страницы
		webview.setWebViewClient(new VkontakteWebViewClient());

		// otherwise CookieManager will fall with
		// java.lang.IllegalStateException: CookieSyncManager::createInstance()
		// needs to be called before CookieSyncManager::getInstance()
		CookieSyncManager.createInstance(this);

		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();

		String url = Auth.getUrl(Constants.API_ID, Auth.getSettings());
		webview.loadUrl(url);
	}

	class VkontakteWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			parseUrl(url);
		}
	}

	private void parseUrl(String url) {
		try {
			if (url == null)
				return;
			Log.i(TAG, "url=" + url);
			if (url.startsWith(Auth.redirect_url)) {
				if (!url.contains("error=")) {
					String[] auth = Auth.parseRedirectUrl(url);
					Intent intent = new Intent();
					intent.putExtra("token", auth[0]);
					intent.putExtra("user_id", Long.parseLong(auth[1]));
					
					account = new Account(context, auth[0], Long.parseLong(auth[1]));
					account.save();
					
					setResult(Activity.RESULT_OK, intent);
				}
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}