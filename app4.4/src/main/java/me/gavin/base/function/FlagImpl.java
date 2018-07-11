package me.gavin.base.function;

/**
 * FlagImpl
 *
 * @author gavin.xiong 2017/8/21
 */
public interface FlagImpl {

    /**
     * this.flag |= flag
     *
     * @param flag flag
     */
    void add(int flag);

    /**
     * this.flag ^= this.flag & flag
     *
     * @param flag flag
     */
    void remove(int flag);

    /**
     * (this.flag & flag) == flag
     *
     * @param flag flag
     * @return (this.flag & flag) == flag
     */
    boolean contain(int flag);

    /**
     * this.flag = 0;
     */
    void clear();

}
