package me.gavin.primary.app.common.bm;

/**
 * 大图查看更多
 *
 * @author gavin.xiong 2017/5/22
 */
public class BigImageLoadMoreEvent {

    public int requestCode;
    public Integer position;

    /**
     * 大图查看更多
     *
     * @param requestCode 来源 - hashCode
     * @param position    目标下标 - null ? next : pop
     */
    BigImageLoadMoreEvent(int requestCode, Integer position) {
        this.requestCode = requestCode;
        this.position = position;
    }

}
