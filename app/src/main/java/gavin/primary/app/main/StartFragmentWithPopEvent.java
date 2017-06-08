package gavin.primary.app.main;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 开启下一页面 并把当前页面关闭
 *
 * @author gavin.xiong 2016/12/10
 */
public class StartFragmentWithPopEvent {

    public SupportFragment targetFragment;

    public StartFragmentWithPopEvent(SupportFragment targetFragment) {
        this.targetFragment = targetFragment;
    }
}
