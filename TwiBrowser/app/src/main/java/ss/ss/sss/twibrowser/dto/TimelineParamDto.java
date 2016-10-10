package ss.ss.sss.twibrowser.dto;

import twitter4j.Paging;

/**
 * タイムライン取得パラメータDto
 */
public class TimelineParamDto {

    // ////////////////////////////////////////////////////////
    // Private Field
    // ////////////////////////////////////////////////////////

    /** 読み込みページ */
    public Paging page;

    // ////////////////////////////////////////////////////////
    // Constructor
    // ////////////////////////////////////////////////////////

    /**
     * コンストラクタ
     */
    public TimelineParamDto() {
        this.page = new Paging();
        this.page.setCount(50);
    }
}
