package me.gavin.app.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import me.gavin.R;
import me.gavin.base.BindingActivity;
import me.gavin.databinding.MainActivityMainBinding;
import me.gavin.test.TestFragment;
import me.gavin.util.DisplayUtil;
import me.gavin.util.L;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends BindingActivity<MainActivityMainBinding>
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final SupportFragment[] mFragments = new SupportFragment[4];
    private View mNoticeTipView;

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity_main;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mFragments[0] = findFragment(HomeFragment.class);
        if (mFragments[0] == null) {
            mFragments[0] = HomeFragment.newInstance();
            mFragments[1] = TestFragment.newInstance();
            mFragments[2] = TestFragment.newInstance();
            mFragments[3] = MineFragment.newInstance();
            loadMultipleRootFragment(R.id.holder, 0, mFragments);
        } else {
            mFragments[1] = findFragment(TestFragment.class);
            mFragments[2] = findFragment(TestFragment.class);
            mFragments[3] = findFragment(MineFragment.class);
        }
        fixBottomNav();
        // mBinding.bottomNav.setItemIconTintList(null);
        mBinding.bottomNav.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                showHideFragment(mFragments[0]);
                break;
            case R.id.nav_quotation:
                showHideFragment(mFragments[1]);
                break;
            case R.id.nav_news:
                showHideFragment(mFragments[2]);
                break;
            case R.id.nav_mine:
                showHideFragment(mFragments[3]);
                break;
        }
        return true;
    }

    /**
     * 利用反射修改底部导航栏 - 去除滑动动画 & 增加角标
     */
    private void fixBottomNav() {
        try {
            Field mMenuView = mBinding.bottomNav.getClass().getDeclaredField("mMenuView");
            mMenuView.setAccessible(true);
            Object bottomNavigationMenuView = mMenuView.get(mBinding.bottomNav);
            Class<?> bottomNavClass = bottomNavigationMenuView.getClass();
            // 设置 整体 无滑动动画
            Field mShiftingMode = bottomNavClass.getDeclaredField("mShiftingMode");
            mShiftingMode.setAccessible(true);
            mShiftingMode.set(bottomNavigationMenuView, false);
            // 获取 item
            Field mButtons = bottomNavClass.getDeclaredField("mButtons");
            mButtons.setAccessible(true);
            Object itemViewArray = mButtons.get(bottomNavigationMenuView);
            int padding = DisplayUtil.dp2px(1);
            int padding2 = DisplayUtil.dp2px(1);
            for (int i = 0; i < 4; i++) {
                // 设置 item 无滑动动画
                BottomNavigationItemView itemView = (BottomNavigationItemView) Array.get(itemViewArray, i);
                itemView.setPadding(0, padding2, 0, padding2);
                Class<?> itemViewClass = itemView.getClass();
                Field mShiftingMode2 = itemViewClass.getDeclaredField("mShiftingMode");
                mShiftingMode2.setAccessible(true);
                mShiftingMode2.set(itemView, false);

                Field mIcon = itemView.getClass().getDeclaredField("mIcon");
                mIcon.setAccessible(true);
                ImageView imageView = (ImageView) mIcon.get(itemView);
                imageView.setPadding(padding, padding, padding, padding);
            }
            // 更新视图
            Method updateMenuView = bottomNavClass.getDeclaredMethod("updateMenuView");
            updateMenuView.invoke(bottomNavigationMenuView);
            // 添加红点
            BottomNavigationItemView bottomNavigationItemView = (BottomNavigationItemView) Array.get(itemViewArray, 4);
            bottomNavigationItemView.addView(createTip());
        } catch (Exception e) {
            L.e(e);
        }
    }

    /**
     * 设置角标
     */
    private View createTip() {
        mNoticeTipView = new View(this);
        int size = DisplayUtil.dp2px(6);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, size, Gravity.CENTER);
        int marginLeft = DisplayUtil.dp2px(12);
        int marginBottom = DisplayUtil.dp2px(16);
        lp.setMargins(marginLeft, 0, 0, marginBottom);
        mNoticeTipView.setLayoutParams(lp);
        mNoticeTipView.setBackgroundResource(R.drawable.bg_circle_tip);
        // mNoticeTipView.setVisibility(View.GONE);
        return mNoticeTipView;
    }

}
