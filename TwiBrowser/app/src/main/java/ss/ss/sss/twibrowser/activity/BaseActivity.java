package ss.ss.sss.twibrowser.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import ss.ss.sss.twibrowser.R;
import ss.ss.sss.twibrowser.utils.PreferenceUtils;

/**
 * 基盤アクティビティ
 */
public class BaseActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {

    // ////////////////////////////////////////////////////////
    // Override Method
    // ////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // テーマカラー設定
        int themeId = PreferenceUtils.readInteger(this, PreferenceUtils.Key.THEME, R.style.RedTheme);
        setTheme(themeId);

        super.onCreate(savedInstanceState);
        // タイトル非表示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 縦画面固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // アクティビティ名ログ
        Log.d("さささ", this.getClass().getCanonicalName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) { // カラーテーマ設定
            int themeId = PreferenceUtils.readInteger(this, PreferenceUtils.Key.THEME, R.style.RedTheme);
            setTheme(themeId);
            finish();
            startActivity(new Intent(this, this.getClass()));
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_common_drawer_color_theme) {  // カラーテーマ
            startActivityForResult(new Intent(this, SettingThemeActivity.class), 0);
            return true;
        }
        return false;
    }
}
