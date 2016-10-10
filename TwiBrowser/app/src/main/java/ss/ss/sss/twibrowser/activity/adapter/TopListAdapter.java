package ss.ss.sss.twibrowser.activity.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ss.ss.sss.twibrowser.R;
import ss.ss.sss.twibrowser.common.Constants;
import ss.ss.sss.twibrowser.common.IdClickableSpan;
import ss.ss.sss.twibrowser.common.TagClickableSpan;
import ss.ss.sss.twibrowser.common.URLLinkMovementMethod;
import ss.ss.sss.twibrowser.view.RoundImageView;
import twitter4j.ExtendedMediaEntity;
import twitter4j.Status;
import twitter4j.URLEntity;

/**
 * トップ画面のadapter
 * @see ss.ss.sss.twibrowser.activity.TopListActivity
 */
public class TopListAdapter extends BaseAdapter implements View.OnClickListener, URLLinkMovementMethod.OnUrlClickListener, IdClickableSpan.OnIdClickListener, TagClickableSpan.OnTagClickListener {

    // ////////////////////////////////////////////////////////
    // Private Field
    // ////////////////////////////////////////////////////////

    /** コンテキスト */
    private Context mContext;
    /** リスナー */
    private OnTopListClickListener mListener;

    /** 内部データ */
    private List<Status> mDataList = new ArrayList<>();

    // ////////////////////////////////////////////////////////
    // Constructor
    // ////////////////////////////////////////////////////////

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     * @param listener リスナー
     */
    public TopListAdapter(Context context, OnTopListClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    // ////////////////////////////////////////////////////////
    // Override Method
    // ////////////////////////////////////////////////////////

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Status getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mDataList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_toplist, null);
            viewHolder = new ViewHolder();
            viewHolder.retweetInfoTextView = (TextView) view.findViewById(R.id.adapter_toplist_retweet_info_textview);
            viewHolder.thumbnailImageView = (RoundImageView) view.findViewById(R.id.adapter_toplist_thumbnail_imageview);
            viewHolder.thumbnailImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onProfileClick();
                    }
                }
            });
            viewHolder.usernameTextView = (TextView) view.findViewById(R.id.adapter_toplist_username_textview);
            viewHolder.screennameTextView = (TextView) view.findViewById(R.id.adapter_toplist_screenname_textview);
            viewHolder.messageTextView = (TextView) view.findViewById(R.id.adapter_toplist_message_textview);
            viewHolder.imageLayout = (HorizontalScrollView) view.findViewById(R.id.adapter_toplist_imagelayout);
            viewHolder.photoContainer = (LinearLayout) view.findViewById(R.id.adapter_toplist_photo_container);
            viewHolder.replayButton = (Button) view.findViewById(R.id.adapter_toplist_replay_button);
            viewHolder.replayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onReplayClick();
                    }
                }
            });
            viewHolder.retweetButton = (Button) view.findViewById(R.id.adapter_toplist_retweet_button);
            viewHolder.retweetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onRetweetClick();
                    }
                }
            });
            viewHolder.retweetTextView = (TextView) view.findViewById(R.id.adapter_toplist_retweet_textview);
            viewHolder.favoriteButton = (Button) view.findViewById(R.id.adapter_toplist_favorite_button);
            viewHolder.favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onFavoriteClick();
                    }
                }
            });
            viewHolder.favoriteTextView = (TextView) view.findViewById(R.id.adapter_toplist_favorite_textview);
            viewHolder.timeTextView = (TextView) view.findViewById(R.id.adapter_toplist_time_textview);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Status item = getItem(position);

        // リツイート判定
        viewHolder.retweetInfoTextView.setVisibility(View.GONE);
        if (item.getRetweetedStatus() != null) {    // リツイート
            viewHolder.retweetInfoTextView.setVisibility(View.VISIBLE);
            viewHolder.retweetInfoTextView.setText(mContext.getString(R.string.activity_toplist_retweet_info_text, item.getUser().getName()));
            item = item.getRetweetedStatus();
        }

        // サムネイル画像
        Picasso.with(mContext).load(item.getUser().getProfileImageURL()).into(viewHolder.thumbnailImageView);

        // ユーザ名
        viewHolder.usernameTextView.setText(item.getUser().getName());
        // スクリーン名
        viewHolder.screennameTextView.setText(mContext.getString(R.string.activity_toplist_screenname_text, item.getUser().getScreenName()));
        // 時間
        SimpleDateFormat format = new SimpleDateFormat("yyyy年 MM/dd HH:mm");
        viewHolder.timeTextView.setText(format.format(item.getCreatedAt()));

        // 画像レイアウト
        String message = initImageLayoutReturnMessage(position, viewHolder, item);

        // URLを表示用URLに置き換え
        URLEntity[] urlEntity = item.getURLEntities();
        for (URLEntity entity : urlEntity) {
            message = message.replace(entity.getText(), entity.getDisplayURL());
        }

        // メッセージ
        initMessageTextView(viewHolder.messageTextView, message);

        // フッター
        viewHolder.retweetTextView.setText(String.valueOf(item.getRetweetCount()));
        viewHolder.favoriteTextView.setText(String.valueOf(item.getFavoriteCount()));

        return view;
    }

    @Override
    public void onClick(View view) {
        // タグからリストインデックスと画像インデックスを取得
        String[] index = ((String)view.getTag()).split(",");
        int position = Integer.parseInt(index[0]);
        int imgPosition = Integer.parseInt(index[1]);

        // Dto取得
        Status status = getItem(position);
        // Dtoからメディアファイル取得
        ExtendedMediaEntity[] mediaEntities = status.getExtendedMediaEntities();
        // タップされたメディアファイルを取得
        ExtendedMediaEntity entity = mediaEntities[imgPosition];

        if (Constants.MediaType.PHOTO.equals(entity.getType())) {   // 写真
            if (mListener != null) {
                mListener.onPhotoClick(entity.getMediaURL(), mediaEntities);
            }
        } else if (Constants.MediaType.VIDEO.equals(entity.getType())) {    // 動画
            if (mListener != null) {
                mListener.onVideoClick(entity.getMediaURL(), mediaEntities);
            }
        }
    }

    @Override
    public void onUrlClick(TextView widget, Uri uri) {
        if (mListener != null) {
            mListener.onUrlClick(String.valueOf(uri));
        }
    }

    @Override
    public void onIdClick(String id) {
        if (mListener != null) {
            mListener.onIdClick(id);
        }
    }

    @Override
    public void onTagClick(String tag) {
        if (mListener != null) {
            mListener.onHashTagClick(tag);
        }
    }

    // ////////////////////////////////////////////////////////
    // Public Method
    // ////////////////////////////////////////////////////////

    // TODO: いらなくなったら消す
    public void testLog() {
//        Log.d("【さささ】【テストログ】", mDataList.get(mDataList.size() - 1).getText());
    }

    /**
     * リストに追加します。
     *
     * @param items items
     */
    public void addAll(final List<Status> items) {
        mDataList.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * リストの先頭に追加します。
     *
     * @param items items
     */
    public void addAllTop(final List<Status> items) {
        List<Status> tmpList = new ArrayList<>(items);
        tmpList.addAll(mDataList);
        mDataList = new ArrayList<>(tmpList);
        notifyDataSetChanged();
    }

    /**
     * さらよみで使用するIDを返却します。
     *
     * @return MaxID
     */
    public long getAdditionalRedingId() {
        if (mDataList == null || mDataList.size() == 0) {
            return -1;
        }

        return getItemId(mDataList.size() - 1);
    }

    /**
     * 更新で使用するIDを返却します。
     *
     * @return SinceID
     */
    public long getUpdateReadingId() {
        if (mDataList == null || mDataList.size() == 0) {
            return -1;
        }

        return getItemId(0);
    }

    // ////////////////////////////////////////////////////////
    // Private Method
    // ////////////////////////////////////////////////////////

    /**
     * メッセージを表示するTextViewの初期化をします。
     * <p>
     *     ID検索、タグ検索をハンドリングする設定を行う。
     * </p>
     *
     * @param textView テキストビュー
     * @param message メッセージ
     */
    private void initMessageTextView(final TextView textView, final String message) {
        Spannable spannable = Spannable.Factory.getInstance().newSpannable(message);

        // ID検索（ @~ ）
        Pattern patternId = Pattern.compile("(@[^\\p{InHiragana}\\p{InKatakana}\\p{InCJKUnifiedIdeographs}\\s:;()@]+)");
        Matcher matcherId = patternId.matcher(message);
        while(matcherId.find()){
            spannable.setSpan(new IdClickableSpan(matcherId.group(1), this), matcherId.start(1), matcherId.end(1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // タグ検索（ #~ ）
        Pattern patternHashtag = Pattern.compile("(#[\\S]+)");
        Matcher matcherHashtag = patternHashtag.matcher(message);

        while(matcherHashtag.find()){
            spannable.setSpan(new TagClickableSpan(matcherHashtag.group(1), this), matcherHashtag.start(1), matcherHashtag.end(1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textView.setText(spannable);
        URLLinkMovementMethod linkMovementMethod = new URLLinkMovementMethod();
        linkMovementMethod.setOnUrlClickListener(this);
        textView.setMovementMethod(linkMovementMethod);
    }

    /**
     * フッターの初期化を行います。
     *
     * @param position   ポジション
     * @param viewHolder ViewHolder
     * @param status     アイテム
     * @return メッセージからメディアファイルの表記を抜いたもの
     */
    private String initImageLayoutReturnMessage(final int position, final ViewHolder viewHolder, final Status status) {
        String message = status.getText();
        viewHolder.photoContainer.removeAllViews();
        ExtendedMediaEntity[] mediaEntities = status.getExtendedMediaEntities();
        // 画像・動画（添付ファイル）
        if (mediaEntities.length == 0) {    // 添付ファイルなし
            viewHolder.imageLayout.setVisibility(View.GONE);
        } else {
            viewHolder.imageLayout.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < mediaEntities.length; i++) {
            ExtendedMediaEntity entity = mediaEntities[i];
            FrameLayout layout = new FrameLayout(mContext);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int)mContext.getResources().getDimension(R.dimen.toplist_image_size), (int)mContext.getResources().getDimension(R.dimen.toplist_image_size));
            layout.setLayoutParams(params);
            layout.setPadding(0, 0, 2, 0);
            viewHolder.photoContainer.addView(layout);
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(params);
            /*
             * タグにリスト番号と何番目の画像かを登録します。
             * (position,imgPosition)形式
             */
            imageView.setTag(position + "," + i);
            imageView.setOnClickListener(this);
            layout.addView(imageView);
            Picasso.with(mContext).load(entity.getMediaURL()).into(imageView);
            if (Constants.MediaType.VIDEO.equals(entity.getType())
                    || Constants.MediaType.GIF.equals(entity.getType())) {   // 動画・GIF
                View.inflate(mContext, R.layout.item_toplist_thumbnail_play, layout);
            }
            message = message.replace(entity.getText(), "");
        }
        return message;
    }

    // ////////////////////////////////////////////////////////
    // Inner Class
    // ////////////////////////////////////////////////////////

    /**
     * ViewHolder
     */
    private class ViewHolder {
        /** リツイート情報 */
        TextView retweetInfoTextView;
        /** サムネイル */
        RoundImageView thumbnailImageView;
        /** ユーザ名 */
        TextView usernameTextView;
        /** スクリーン名 */
        TextView screennameTextView;
        /** メッセージ */
        TextView messageTextView;
        /** 写真レイアウト */
        HorizontalScrollView imageLayout;
        /** 写真コンテナー */
        LinearLayout photoContainer;
        /** 返信ボタン */
        Button replayButton;
        /** リツイートボタン */
        Button retweetButton;
        /** リツイート数 */
        TextView retweetTextView;
        /** お気に入りボタン */
        Button favoriteButton;
        /** お気に入り数 */
        TextView favoriteTextView;
        /** 時間 */
        TextView timeTextView;
    }

    // ////////////////////////////////////////////////////////
    // Interface
    // ////////////////////////////////////////////////////////

    /**
     * リスナー
     */
    public interface OnTopListClickListener {
        /**
         * プロフィールタップリスナー
         */
        void onProfileClick();

        /**
         * 写真タップリスナー
         */
        void onPhotoClick(final String url, final ExtendedMediaEntity[] entity);

        /**
         * 動画タップリスナー
         */
        void onVideoClick(final String url, final ExtendedMediaEntity[] entity);

        /**
         * ハッシュタグタップリスナー
         */
        void onHashTagClick(final String tag);

        /**
         * IDタップリスナー
         */
        void onIdClick(final String id);

        /**
         * URLタップリスナー
         */
        void onUrlClick(final String url);

        /**
         * 返信タップリスナー
         */
        void onReplayClick();

        /**
         * リツイートタップリスナー
         */
        void onRetweetClick();

        /**
         * お気に入りタップリスナー
         */
        void onFavoriteClick();
    }
}
