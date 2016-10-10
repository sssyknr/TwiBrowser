package ss.ss.sss.twibrowser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ss.ss.sss.twibrowser.R;
import ss.ss.sss.twibrowser.utils.TwitterUtils;

/**
 * スプラッシュ画面
 */
public class SplashActivity extends BaseActivity {

    // ////////////////////////////////////////////////////////
    // Override Method
    // ////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler hdl = new Handler();

        // 500ms遅延させてHandlerを実行します。
        if (TwitterUtils.hasAccessToken(this)) {    // 認証済み
            hdl.postDelayed(new mainHandler(), 500);
        } else {    // 未認証
            hdl.postDelayed(new authHandler(), 500);
        }
    }

    // ////////////////////////////////////////////////////////
    // Inner Class
    // ////////////////////////////////////////////////////////

    /**
     * スプラッシュ終了後に実行する処理を記述します。
     * <pre>
     *     メイン画面に遷移します。
     * </pre>
     */
    private class mainHandler implements Runnable {

        public void run() {
            // スプラッシュ完了後に実行するActivityを指定
            Intent intent = new Intent(getApplication(), TopListActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            // SplashActivityを終了
            SplashActivity.this.finish();
        }
    }

    /**
     * スプラッシュ終了後に実行する処理を記述します。
     * <pre>
     *     認証画面に遷移します。
     * </pre>
     */
    private class authHandler implements Runnable {

        public void run() {
            // スプラッシュ完了後に実行するActivityを指定
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
            // SplashActivityを終了
            SplashActivity.this.finish();
        }
    }
}
