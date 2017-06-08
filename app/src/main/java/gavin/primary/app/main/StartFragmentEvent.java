package gavin.primary.app.main;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 开启下一个页面
 *
 * @author gavin.xiong 2016/12/10
 */
public class StartFragmentEvent {

    public SupportFragment targetFragment;

    public StartFragmentEvent(SupportFragment targetFragment) {
        this.targetFragment = targetFragment;
    }
}
