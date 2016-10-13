package ss.ss.sss.twibrowser.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * プリファレンス管理クラス
 */
public class PreferenceUtils {

    // ////////////////////////////////////////////////////////
    // Public Static Method
    // ////////////////////////////////////////////////////////

    /**
     * Int型を書き込む
     *
     * @param context コンテキスト
     * @param key キー
     * @param value バリュー
     */
    public static void writeInteger(final Context context, final String key, final int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(key, value).apply();
    }

    /**
     * Int型を読み込む
     *
     * @param context コンテキスト
     * @param key キー
     * @param defValue デフォルトバリュー
     * @return keyに紐づく値
     */
    public static int readInteger(final Context context, final String key, final int defValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, defValue);
    }


    // ////////////////////////////////////////////////////////
    // Public Static Final Field
    // ////////////////////////////////////////////////////////

    /** プリファレンスキー */
    public static final class Key {
        /** アプリテーマ */
        public static final String THEME = "appTheme";
        /** 前回閉じたカセットの位置 */
        public static final String PREV_CASSETTE_POSITION = "prevCassettePosition";
        /** 前回閉じたカッセトの位置（オフセット） */
        public static final String PREV_CASSETTE_OFFSET = "prevCassetteOffset";
    }
}
