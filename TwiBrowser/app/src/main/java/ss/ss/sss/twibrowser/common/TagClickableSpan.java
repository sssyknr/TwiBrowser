package ss.ss.sss.twibrowser.common;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * TextView内のタグ(#~)をクリック可能にします。
 */
public class TagClickableSpan extends ClickableSpan {

    // ////////////////////////////////////////////////////////
    // Private Field
    // ////////////////////////////////////////////////////////

    /** リスナー */
    private OnTagClickListener mListener;

    /** タグ */
    private String mTag;

    // ////////////////////////////////////////////////////////
    // Constructor
    // ////////////////////////////////////////////////////////

    /**
     * コンストラクタ
     *
     * @param tag tag
     */
    public TagClickableSpan(final String tag, final OnTagClickListener listener) {
        super();
        mTag = tag;
        mListener = listener;
    }

    // ////////////////////////////////////////////////////////
    // Override Method
    // ////////////////////////////////////////////////////////

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onTagClick(mTag);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    // ////////////////////////////////////////////////////////
    // Interface
    // ////////////////////////////////////////////////////////

    /**
     * タグタップリスナー
     */
    public interface OnTagClickListener {
        /**
         * タグタップ
         *
         * @param tag タグ
         */
        void onTagClick(final String tag);
    }
}
