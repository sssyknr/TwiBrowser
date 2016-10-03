package ss.ss.sss.twibrowser;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * トップ画面
 * <pre>
 *     つぶやきをリスト表示します。
 * </pre>
 */
public class TopActivity extends BaseActivity {

    // ////////////////////////////////////////////////////////
    // Private Field
    // ////////////////////////////////////////////////////////

    /** つぶやきリストビュー */
    private ListView mListView;

    // ////////////////////////////////////////////////////////
    // Override Method
    // ////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
    }

    // ////////////////////////////////////////////////////////
    // Inner Class
    // ////////////////////////////////////////////////////////


}
