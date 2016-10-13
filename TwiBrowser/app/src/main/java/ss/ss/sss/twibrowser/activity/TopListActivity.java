package ss.ss.sss.twibrowser.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.List;

import ss.ss.sss.twibrowser.R;
import ss.ss.sss.twibrowser.activity.adapter.TopListAdapter;
import ss.ss.sss.twibrowser.dto.TimelineParamDto;
import ss.ss.sss.twibrowser.task.TimelineTask;
import ss.ss.sss.twibrowser.utils.PreferenceUtils;
import ss.ss.sss.twibrowser.utils.SerializableUtils;
import twitter4j.ExtendedMediaEntity;
import twitter4j.ResponseList;
import twitter4j.Status;

/**
 * トップ画面
 * <pre>
 *     つぶやきをリスト表示します。
 * </pre>
 */
public class TopListActivity extends BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener, TopListAdapter.OnTopListClickListener {

    // ////////////////////////////////////////////////////////
    // Private Field
    // ////////////////////////////////////////////////////////

    /** ドロワー */
    private DrawerLayout mDrawerLayout;
    /** スワイプリフレッシュレイアウト */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    /** リスト */
    private ListView mListView;
    /** - フッター */
    private View mFooterView;
    /** アダプター */
    private TopListAdapter mAdapter;

    // ////////////////////////////////////////////////////////
    // Override Method
    // ////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        // ナビゲーションドロワー
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_top_drawerlayout);
        ((NavigationView)findViewById(R.id.activity_top_navigationview)).setNavigationItemSelectedListener(this);

        // ツールバー
        findViewById(R.id.activity_top_menu_button).setOnClickListener(this);

        // スワイプリフレッシュレイアウト
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_top_swiperefreshlayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // リスト
        mAdapter = new TopListAdapter(this, this);
        mFooterView = View.inflate(this, R.layout.item_toplist_additionalroading, null);
        mListView = (ListView) findViewById(R.id.activity_top_listview);
        mListView.setOnScrollListener(this);
        mListView.setAdapter(mAdapter);

        // 前回取得済みのタイムラインと位置を取得
        int id = TimelineTaskCallback.ID.INIT;
        int position = PreferenceUtils.readInteger(this, PreferenceUtils.Key.PREV_CASSETTE_POSITION, 0);
        int offset = PreferenceUtils.readInteger(this, PreferenceUtils.Key.PREV_CASSETTE_OFFSET, 0);
        List<Status> items = SerializableUtils.load(this);
        if (items != null) {
            mAdapter.addAll(items);
            mListView.setSelectionFromTop(position, offset);
            id = TimelineTaskCallback.ID.NEW;
        }

        // ローダー初期化
        getSupportLoaderManager().initLoader(id, null, new TimelineTaskCallback());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // ドロワー
        mDrawerLayout.closeDrawer(GravityCompat.START);
        // 取得したタイムラインを保持しておく。
        int position = mListView.getFirstVisiblePosition();
        int offset = mListView.getChildAt(0).getTop();
        PreferenceUtils.writeInteger(this, PreferenceUtils.Key.PREV_CASSETTE_POSITION, position);
        PreferenceUtils.writeInteger(this, PreferenceUtils.Key.PREV_CASSETTE_OFFSET, offset);
        SerializableUtils.save(mAdapter.getItemsForCache(position), this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.activity_top_menu_button) {  // メニュー
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onRefresh() {
        updateReading();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        // isEmpty
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (mAdapter.getItems().size() != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {    // 最後のカセット
            additionalReading();
        }
    }

    @Override
    public void onProfileClick() {
        // TODO: プロフィール画面へ
        Log.d("[onProfileClick]", "onProfileClick");
    }

    @Override
    public void onPhotoClick(String url, ExtendedMediaEntity[] entity) {
        // TODO: 写真画面へ
        Log.d("[onPhotoClick]", "url = "+url);
    }

    @Override
    public void onVideoClick(String url, ExtendedMediaEntity[] entity) {
        // TODO: ビデオ画面へ
        Log.d("[onVideoClick]", "url = "+url);
    }

    @Override
    public void onHashTagClick(String tag) {
        // TODO: 検索画面へ
        Log.d("[onHashTagClick]", "tag = "+tag);
    }

    @Override
    public void onIdClick(String id) {
        // TODO: プロフィール画面へ
        Log.d("[onIdClick]", "id = "+id);
    }

    @Override
    public void onUrlClick(String url) {
        // TODO: WebView画面へ
        Log.d("[onUrlClick]", "url = "+url);
    }

    @Override
    public void onReplayClick() {
        // TODO: 返信画面へ
        Log.d("[onReplayClick]", "onReplayClick");
    }

    @Override
    public void onRetweetClick() {
        // TODO: リツイート画面へ
        Log.d("[onRetweetClick]", "onRetweetClick");
    }

    @Override
    public void onFavoriteClick() {
        // TODO: お気に入り登録
        Log.d("[onFavoriteClick]", "onFavoriteClick");
    }

    // ////////////////////////////////////////////////////////
    // Private Method
    // ////////////////////////////////////////////////////////

    /**
     * さらよみ
     */
    private void additionalReading() {

        if (mListView.getFooterViewsCount() != 0) { // 通信中
            return;
        }

        // 読み込みダイアログ
        mListView.addFooterView(mFooterView);

        // 読み込み
        getSupportLoaderManager().initLoader(TimelineTaskCallback.ID.OLD, null, new TimelineTaskCallback());
    }

    /**
     * 更新
     */
    private void updateReading() {
        // 読み込み
        getSupportLoaderManager().initLoader(TimelineTaskCallback.ID.NEW, null, new TimelineTaskCallback());
    }

    // ////////////////////////////////////////////////////////
    // Inner Class
    // ////////////////////////////////////////////////////////

    /**
     * タイムライン取得コールバック
     */
    private class TimelineTaskCallback implements LoaderManager.LoaderCallbacks<ResponseList<Status>> {

        /** 読み込み種別 */
        private int id;

        /** 読み込み種別 */
        public final class ID {
            /** 初回 */
            public static final int INIT = 0;
            /** 過去 */
            public static final int OLD = 1;
            /** 未来 */
            public static final int NEW = 2;
        }

        @Override
        public Loader<ResponseList<Status>> onCreateLoader(int id, Bundle args) {
            this.id = id;
            TimelineParamDto dto = new TimelineParamDto();
            if (ID.NEW == id) {    // 更新
                long sinceId = mAdapter.getSinceId();
                if (sinceId > 0) {
                    dto.page.setSinceId(sinceId);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    return null;
                }
            } else if (ID.OLD == id) {  // 過去分
                long maxId = mAdapter.getMaxId();
                if (maxId > 0) {
                    dto.page.setMaxId(maxId);
                } else {
                    return null;
                }
            }
            return new TimelineTask(getApplicationContext(), dto);
        }

        @Override
        public void onLoadFinished(Loader<ResponseList<Status>> loader, ResponseList<Status> data) {
            if (data == null || data.size() == 0) {
                // ダイアログ削除
                mListView.removeFooterView(mFooterView);
                return;
            }

            // 表示位置記憶
            int position = 0;
            int offset = 0;
            if (mListView.getChildAt(0) != null) {
                position = mListView.getFirstVisiblePosition();
                offset = mListView.getChildAt(0).getTop();
            }

            // 読み込みカセット追加
            if (ID.INIT == id || ID.OLD == id) {    // 初回・過去
                mAdapter.addAll(data);
            } else if (ID.NEW == id){    // 未来
                position += data.size();
                mAdapter.addAllTop(data);
            }

            // 表示位置設定
            mListView.setSelectionFromTop(position, offset);

            // ダイアログ削除
            mListView.removeFooterView(mFooterView);
            // 引っ張って更新終了
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onLoaderReset(Loader<ResponseList<Status>> loader) {

            // ダイアログ削除
            mListView.removeFooterView(mFooterView);
            // 引っ張って更新終了
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
