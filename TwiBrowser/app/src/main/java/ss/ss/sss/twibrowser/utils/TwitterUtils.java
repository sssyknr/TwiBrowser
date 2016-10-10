package ss.ss.sss.twibrowser.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ss.ss.sss.twibrowser.R;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Twitter関連のユーティリティクラス
 */
public class TwitterUtils {

    // ////////////////////////////////////////////////////////
    // Private Static Final Field
    // ////////////////////////////////////////////////////////

    // プリファレンス
    /** Twitter関連プリファレンス名 */
    private static final String PREF_NAME = "twitter_access_token";
    /** キー：アクセストークン */
    private static final String TOKEN = "token";
    /** キー：秘密鍵 */
    private static final String TOKEN_SECRET = "token_secret";

    // ////////////////////////////////////////////////////////
    // Public Static Method
    // ////////////////////////////////////////////////////////

    /**
     * Twitterインスタンスを取得します。アクセストークンが保存されていれば自動的にセットします。
     *
     * @param context コンテキスト
     * @return Twitterインスタンス
     */
    public static Twitter getTwitterInstance(Context context) {
        String consumerKey = context.getString(R.string.twitter_consumer_key);
        String consumerSecret = context.getString(R.string.twitter_consumer_secret);

        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);

        if (hasAccessToken(context)) {
            twitter.setOAuthAccessToken(loadAccessToken(context));
        }
        return twitter;
    }

    /**
     * アクセストークンをプリファレンスに保存します。
     *
     * @param context コンテキスト
     * @param accessToken アクセストークン
     */
    public static void storeAccessToken(Context context, AccessToken accessToken) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, accessToken.getToken());
        editor.putString(TOKEN_SECRET, accessToken.getTokenSecret());
        editor.apply();
    }

    /**
     * アクセストークンをプリファレンスから読み込みます。
     *
     * @param context コンテキスト
     * @return アクセストークン
     */
    public static AccessToken loadAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String token = preferences.getString(TOKEN, null);
        String tokenSecret = preferences.getString(TOKEN_SECRET, null);
        if (token != null && tokenSecret != null) {
            return new AccessToken(token, tokenSecret);
        } else {
            return null;
        }
    }

    /**
     * アクセストークンが存在する場合はtrueを返します。
     *
     * @return true : アクセストークンあり / false : アクセストークンなし
     */
    public static boolean hasAccessToken(Context context) {
        return loadAccessToken(context) != null;
    }

}
