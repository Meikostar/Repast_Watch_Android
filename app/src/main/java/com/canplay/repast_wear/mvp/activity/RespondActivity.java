package com.canplay.repast_wear.mvp.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseActivity;
import com.canplay.repast_wear.mvp.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RespondActivity extends BaseActivity {

    @BindView(R.id.no_respond)
    TextView noRespond;
    @BindView(R.id.have_respond)
    TextView haveRespond;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.id_tab_line)
    ImageView tabLine;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private FragmentManager fm;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;

    @Override
    public void initInjector() {
        initUI(R.layout.activity_respond);
        ButterKnife.bind(this);
    }

    @Override
    public void initCustomerUI() {
        fm = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        fragmentList.add(NoRespondFragment.newInstance());
        fragmentList.add(HaveRespondFragment.newInstance());
        adapter = new ViewPagerAdapter(fm, fragmentList);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(new TabOnPageChangeListener());
        initTabLine();
    }

    @Override
    public void initOther() {
        noRespond.setOnClickListener(new TabOnClickListener(0));
        haveRespond.setOnClickListener(new TabOnClickListener(1));
    }

    /**
     * 根据屏幕的宽度，初始化引导线的宽度
     */
    private void initTabLine() {

        //获取屏幕的宽度
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;

        //获取控件的LayoutParams参数(注意：一定要用父控件的LayoutParams写LinearLayout.LayoutParams)
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabLine.getLayoutParams();
        lp.width = screenWidth / 2;//设置该控件的layoutParams参数
        tabLine.setLayoutParams(lp);//将修改好的layoutParams设置为该控件的layoutParams
    }

    /**
     * 功能：点击主页TAB事件
     */
    public class TabOnClickListener implements View.OnClickListener {
        private int index = 0;

        public TabOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            viewpager.setCurrentItem(index);//选择某一页
        }

    }

    /**
     * 功能：Fragment页面改变事件
     */
    public class TabOnPageChangeListener implements ViewPager.OnPageChangeListener {

        //当滑动状态改变时调用
        public void onPageScrollStateChanged(int state) {

        }

        //当前页面被滑动时调用
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabLine.getLayoutParams();
            //返回组件距离左侧组件的距离
            lp.leftMargin = (int) ((positionOffset + position) * screenWidth / 2);
            tabLine.setLayoutParams(lp);
        }

        @Override
        public void onPageSelected(int position) {

        }

    }
}
