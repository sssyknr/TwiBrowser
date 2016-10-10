package ss.ss.sss.twibrowser.task;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import ss.ss.sss.twibrowser.dto.TimelineParamDto;
import ss.ss.sss.twibrowser.utils.TwitterUtils;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * タイムライン取得クラス
 */
public class TimelineTask extends AsyncTaskLoader<ResponseList<Status>> {

    // ////////////////////////////////////////////////////////
    // Private Field
    // ////////////////////////////////////////////////////////

    /** コンテキスト */
    private Context mContext;
    /** 通信パラメータ */
    private TimelineParamDto mParamDto;

    // ////////////////////////////////////////////////////////
    // Constructor
    // ////////////////////////////////////////////////////////

    /**
     * コンストラクタ
     *
     * @param context  コンテキスト
     * @param paramDto 通信パラメータ
     */
    public TimelineTask(Context context, TimelineParamDto paramDto) {
        super(context);

        mContext = context;
        mParamDto = paramDto;
    }

    // ////////////////////////////////////////////////////////
    // Override Method
    // ////////////////////////////////////////////////////////

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public ResponseList<Status> loadInBackground() {
        ResponseList<Status> homeTimeline = null;
        try {
            //TLの取得
            Twitter twitter = TwitterUtils.getTwitterInstance(mContext);
            homeTimeline = twitter.getHomeTimeline(mParamDto.page);

        } catch (TwitterException e) {
            if (e.isCausedByNetworkIssue()) {
                // TODO: 通信エラーダイアログ
//                DialogUtil.showErrorDialog(activity, "ネットワークエラー", "通信状態の良い環境で再度お試しください。", 0, null, "OK", null);
            } else {
                // TODO: エラーダイアログ
//                DialogUtil.showErrorDialog(activity, "エラー", e.getMessage(), 0, null, "OK", null);
            }
        }
        return homeTimeline;
    }
}