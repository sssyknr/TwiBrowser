package ss.ss.sss.twibrowser.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

import ss.ss.sss.twibrowser.R;
import ss.ss.sss.twibrowser.utils.PreferenceUtils;

/**
 * テーマ設定画面
 * TODO: レイアウト考えなきゃ
 */
public class SettingThemeActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    // ////////////////////////////////////////////////////////
    // Private Static Final Field
    // ////////////////////////////////////////////////////////

    /** テーマ */
    private static final Map<String, Integer> THEME_MAP = new HashMap<String, Integer>() {
        {
            put("Red", R.style.RedTheme);
            put("Pink", R.style.PinkTheme);
            put("Purple", R.style.PurpleTheme);
            put("DeepPurple", R.style.DeepPurpleTheme);
            put("Indigo", R.style.IndigoTheme);
            put("Blue", R.style.BlueTheme);
            put("LightBlue", R.style.LightBlueTheme);
            put("Cyan", R.style.CyanTheme);
            put("Teal", R.style.TealTheme);
            put("Green", R.style.GreenTheme);
            put("LightGreen", R.style.LightGreenTheme);
            put("Lime", R.style.LimeTheme);
            put("Yellow", R.style.YellowTheme);
            put("Amber", R.style.AmberTheme);
            put("Orange", R.style.OrangeTheme);
            put("DeepOrange", R.style.DeepOrangeTheme);
            put("Brown", R.style.BrownTheme);
            put("Grey", R.style.GreyTheme);
            put("BlueGrey", R.style.BlueGreyTheme);
        }
    };

    // ////////////////////////////////////////////////////////
    // Override Method
    // ////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_theme);

        ListView listView = (ListView) findViewById(R.id.activity_settingtheme_listview);
        listView.setOnItemClickListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice);
        adapter.addAll(THEME_MAP.keySet());
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String value = (String) adapterView.getItemAtPosition(i);
        int themeId = THEME_MAP.get(value);

        // プリファレンスに保存
        PreferenceUtils.writeInteger(this, PreferenceUtils.Key.THEME, themeId);

        // テーマ適用
//        setTheme(themeId);

        finish();
    }
}
