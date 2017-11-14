package me.gavin.primary.app.main;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 左侧菜单开关事件
 *
 * @author gavin.xiong 2016/12/5  2016/12/5
 */
public class StartFragmentEvent {

    public SupportFragment supportFragment;

    public StartFragmentEvent(SupportFragment supportFragment) {
        this.supportFragment = supportFragment;
    }
}
