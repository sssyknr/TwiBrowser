package ss.ss.sss.twibrowser.activity;

import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

/**
 * 基盤アクティビティ
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトル非表示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 縦画面固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // アクティビティ名ログ
        Log.d("さささ", this.getClass().getCanonicalName());
    }
}
