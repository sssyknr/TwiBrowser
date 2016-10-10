package ss.ss.sss.twibrowser.common;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * TextView内のid(@~)をクリック可能にします。
 */
public class IdClickableSpan extends ClickableSpan {

    // ////////////////////////////////////////////////////////
    // Private Field
    // ////////////////////////////////////////////////////////

    /** リスナー */
    private OnIdClickListener mListener;
    /** ID */
    private String mId;

    // ////////////////////////////////////////////////////////
    // Constructor
    // ////////////////////////////////////////////////////////

    /**
     * コンストラクタ
     *
     * @param id id
     * @param listener リスナー
     */
    public IdClickableSpan(final String id, final OnIdClickListener listener) {
        super();
        mId = id;
        mListener = listener;
    }

    // ////////////////////////////////////////////////////////
    // Override Method
    // ////////////////////////////////////////////////////////

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onIdClick(mId);
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
     * IDタップリスナー
     */
    public interface OnIdClickListener {
        /**
         * IDタップ
         *
         * @param id ID
         */
        void onIdClick(final String id);
    }
}
