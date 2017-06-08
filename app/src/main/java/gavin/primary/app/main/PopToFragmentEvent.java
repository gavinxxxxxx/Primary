package gavin.primary.app.main;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 回退到指定fragment
 *
 * @author gavin.xiong 2016/12/10
 */
public class PopToFragmentEvent {

    public Class<? extends SupportFragment> clazz;
    public boolean includeTargetFragment;

    public PopToFragmentEvent(Class<? extends SupportFragment> clazz, boolean includeTargetFragment) {
        this.clazz = clazz;
        this.includeTargetFragment = includeTargetFragment;
    }
}
