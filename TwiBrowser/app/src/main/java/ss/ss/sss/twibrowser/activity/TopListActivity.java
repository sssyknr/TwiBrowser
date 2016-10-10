package ss.ss.sss.twibrowser.activity;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import ss.ss.sss.twibrowser.R;
import ss.ss.sss.twibrowser.activity.adapter.TopListAdapter;
import ss.ss.sss.twibrowser.dto.TimelineParamDto;
import ss.ss.sss.twibrowser.task.TimelineTask;
import twitter4j.ExtendedMediaEntity;
import twitter4j.ResponseList;
import twitter4j.Status;

/**
 * トップ画面
 * <pre>
 *     つぶやきをリスト表示します。
 * </pre>
 */
public class TopListActivity extends BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener, TopListAdapter.OnTopListClickListener {

    // ////////////////////////////////////////////////////////
    // Private Field
    // ////////////////////////////////////////////////////////

    /** ドロワー */
    private DrawerLayout mDrawerLayout;
    /** 更新ボタン */
    private Button mUpdateButton;
    /** リスト */
    private ListView mListView;
    /** - フッター */
    private View mFooterView;
    /** アダプター */
    private TopListAdapter mAdapter;

    /** リクエストパラメータDto */
    private TimelineParamDto mParamDto;

    /** 更新ボタンアニメーション */
    private RotateAnimation animation;

    // ////////////////////////////////////////////////////////
    // Override Method
    // ////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        // 初期化
        mParamDto = new TimelineParamDto();

        // ナビゲーションドロワー
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_top_drawerlayout);

        // ツールバー
        findViewById(R.id.activity_top_menu_button).setOnClickListener(this);
        mUpdateButton = (Button) findViewById(R.id.activity_top_update_button);
        mUpdateButton.setOnClickListener(this);

        // リスト
        mAdapter = new TopListAdapter(this, this);
        mFooterView = View.inflate(this, R.layout.item_toplist_additionalroading, null);
        mListView = (ListView) findViewById(R.id.activity_top_listview);
        mListView.setOnScrollListener(this);
        mListView.setAdapter(mAdapter);

        // ローダー初期化
        getSupportLoaderManager().initLoader(TimelineTaskCallback.ID.INIT, null, new TimelineTaskCallback());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // ドロワー
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.activity_top_menu_button) {  // メニュー
            mDrawerLayout.openDrawer(GravityCompat.START);
        } else if (id == R.id.activity_top_update_button) { // 更新
            updateReading();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (totalItemCount == firstVisibleItem + visibleItemCount) {    // 最後のカセット
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

        long id = mAdapter.getAdditionalRedingId();
        if (id < 0) {
            return;
        }
        /*
         * さらよみの基準とするIDが返却値に含まれないように -1 して設定する。
         */
        mParamDto.page.setMaxId(id - 1);

        // 読み込みダイアログ
        mListView.addFooterView(mFooterView);

        // 読み込み
        getSupportLoaderManager().initLoader(TimelineTaskCallback.ID.OLD, null, new TimelineTaskCallback());
    }

    /**
     * 更新
     */
    private void updateReading() {

        if (animation != null) {    // 更新中
            return;
        }

        long id = mAdapter.getUpdateReadingId();
        if (id < 0) {
            return;
        }
        /*
         * 更新の基準とするIDが返却値に含まれないように +1 して設定する。
         */
        mParamDto.page.setSinceId(id);

        // 更新中はアニメーション
        animation = new RotateAnimation(0, 360, mUpdateButton.getWidth() / 2, mUpdateButton.getHeight() / 2);
        animation.setDuration(1000);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        mUpdateButton.startAnimation(animation);

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
            return new TimelineTask(getApplicationContext(), mParamDto);
        }

        @Override
        public void onLoadFinished(Loader<ResponseList<Status>> loader, ResponseList<Status> data) {
            if (data == null || data.size() == 0) {
                // ダイアログ削除
                mListView.removeFooterView(mFooterView);
                // 更新アニメーション終了
                if (animation != null) {
                    animation.cancel();
                    animation = null;
                }
                return;
            }

            // 読み込みカセット追加
            if (ID.INIT == id || ID.OLD == id) {    // 過去
                mAdapter.addAll(data);
            } else if (ID.NEW == id){    // 初回・未来
                mAdapter.addAllTop(data);
            }

            // ダイアログ削除
            mListView.removeFooterView(mFooterView);
            // 更新アニメーション終了
            if (animation != null) {
                animation.cancel();
                animation = null;
            }
        }

        @Override
        public void onLoaderReset(Loader<ResponseList<Status>> loader) {

            // ダイアログ削除
            mListView.removeFooterView(mFooterView);
            // 更新アニメーション終了
            if (animation != null) {
                animation.cancel();
                animation = null;
            }
        }
    }

}
