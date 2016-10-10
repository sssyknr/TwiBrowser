package ss.ss.sss.twibrowser.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import ss.ss.sss.twibrowser.R;
import ss.ss.sss.twibrowser.utils.TwitterUtils;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Twitterログイン画面
 */
public class LoginActivity extends BaseActivity {

    // ////////////////////////////////////////////////////////
    // Private Field
    // ////////////////////////////////////////////////////////

    /** ツイッターインスタンス */
    private Twitter mTwitter;
    /** コールバックURL */
    private String mCallbackUrl;
    /** リクエストトークン */
    private RequestToken mRequestToken;

    // ////////////////////////////////////////////////////////
    // Override Method
    // ////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTwitter = TwitterUtils.getTwitterInstance(this);
        mCallbackUrl = getString(R.string.twitter_auth_callback);

        // 認証
        TwitterAuthorizeTask task = new TwitterAuthorizeTask();
        task.execute();
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (intent == null
                || intent.getData() == null
                || !intent.getData().toString().startsWith(mCallbackUrl)) {
            return;
        }
        String verifier = intent.getData().getQueryParameter("oauth_verifier");

        // アクセストークン取得
        GetAccessTokenTask task = new GetAccessTokenTask();
        task.execute(verifier);
    }

    // ////////////////////////////////////////////////////////
    // Private Method
    // ////////////////////////////////////////////////////////

    /**
     * 認証成功時、トップ画面に遷移します。
     *
     * @param accessToken アクセストークン
     */
    private void successOAuth(AccessToken accessToken) {
        TwitterUtils.storeAccessToken(this, accessToken);
        Intent intent = new Intent(this, TopListActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * トースト表示
     *
     * @param text テキスト
     */
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    // ////////////////////////////////////////////////////////
    // Inner Class
    // ////////////////////////////////////////////////////////

    /**
     * Twitter認証クラス
     */
    private class TwitterAuthorizeTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                mRequestToken = mTwitter.getOAuthRequestToken(mCallbackUrl);
                return mRequestToken.getAuthorizationURL();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String url) {
            if (url != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } else {
                // 認証失敗
                Log.d(getClass().toString(), "getAuthorizationURL = null");
            }
        }
    }

    /**
     * Twitterリクエストトークン取得クラス
     */
    private class GetAccessTokenTask extends AsyncTask<String, Void, AccessToken> {

        @Override
        protected AccessToken doInBackground(String... strings) {
            try {
                return mTwitter.getOAuthAccessToken(mRequestToken, strings[0]);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(AccessToken accessToken) {
            if (accessToken != null) {
                // 認証成功！
                showToast("認証成功！");
                successOAuth(accessToken);
            } else {
                // 認証失敗。。。
                showToast("認証失敗！");
            }
        }
    }
}
