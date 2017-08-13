package com.canplay.repast_wear.mvp.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseActivity;
import com.canplay.repast_wear.mvp.adapter.ViewPagerAdapter;
import com.canplay.repast_wear.view.TitleBarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RespondActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private FragmentManager fm;
    private int mCurrentIndex = 0;//当前小圆点的位置
    /**
     * 屏幕的宽度
     */
    private int screenWidth;

    @Override
    public void initInjector() {
        initUI(R.layout.activity_respond);
        ButterKnife.bind(this);
        TitleBarLayout titleBarView = getTitleBarView();
        titleBarView.setTvBackColor(R.color.green_cyc);
    }

    @Override
    public void initCustomerUI() {
        fm = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        fragmentList.add(NoRespondFragment.newInstance());
        fragmentList.add(HaveRespondFragment.newInstance());
//
        adapter = new ViewPagerAdapter(fm, fragmentList);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(new TabOnPageChangeListener(this, viewpager, llContainer, 2));
//
    }

    @Override
    public void initOther() {
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
        private Context context;
        private ViewPager viewPager;
        private LinearLayout dotLayout;
        private int size;//需要点的个数
        private int img1 = R.drawable.cycle_shade, img2 = R.drawable.white_cycle;
        private int imgSize = 8;
        private List<ImageView> dotViewLists = new ArrayList<>();

        public TabOnPageChangeListener(Context context, ViewPager viewPager, LinearLayout dotLayout, int size) {
            this.context = context;
            this.viewPager = viewPager;
            this.dotLayout = dotLayout;
            this.size = size;

            for (int i = 0; i < size; i++) {
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                //为小圆点左右添加间距
                params.leftMargin = 5;
                params.rightMargin = 5;
                //手动给小圆点一个大小
                params.height = imgSize;
                params.width = imgSize;
                if (i == 0) {
                    imageView.setBackgroundResource(img2);
                } else {
                    imageView.setBackgroundResource(img1);
                }
                //为LinearLayout添加ImageView
                dotLayout.addView(imageView, params);
                dotViewLists.add(imageView);
            }

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < size; i++) {
                //选中的页面改变小圆点为选中状态，反之为未选中
                if ((position % size) == i) {
                    ((View) dotViewLists.get(i)).setBackgroundResource(img2);
                } else {
                    ((View) dotViewLists.get(i)).setBackgroundResource(img1);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
