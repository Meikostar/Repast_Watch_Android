package com.canplay.repast_wear.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.util.CanplayUtils;
import com.canplay.repast_wear.util.DateUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 标题栏
 * Created by leo on 2016/8/19.
 * version 1.0.0
 */
public class TitleBarLayout extends FrameLayout {

    @BindView(R.id.title_layout)
    RelativeLayout title_layout;

    @BindView(R.id.title_text)
    /** 标题 */
            TextView titleName;

    @BindView(R.id.title_back_tv)
    /** 返回的文字 */
            TextView title_back_tv;
    @BindView(R.id.clock_time)
    TextView clockTime;
    @BindView(R.id.title_r)
    TextView titleR;

    private TextView textView;

    @BindView(R.id.title_left_layout)
    /** 标题栏左边的布局 ,显示正在加载的布局,身驱的布局*/
            View title_left_layout;

    @BindView(R.id.title_right_layout)
    /** 标题栏右边的布局, 身躯 */
            LinearLayout title_right_layout;

    Context context;

    public final static int right_img_id = CanplayUtils.generateViewId();
    private OnBackBtnClickListener backBtnClickListener;
    private OnRightBtnClickListener rightBtnClickListener;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            clockTime.setText(DateUtil.getSystemTime());
        }
    };

    public TitleBarLayout(Context context) {
        this(context, null);
        this.context = context;
    }

    public TitleBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public TitleBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.base_title_layout, this, true);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化标题栏
     */
    protected void initView() {
        // 左边按钮的事件
        title_left_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (backBtnClickListener != null) {
                    backBtnClickListener.onBackClick(v);
                }
            }
        });

       Timer timer=new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                handler.sendMessage(new Message());
            }
        }, 0,1000);
    }

    /**
     * 设置标题名称
     *
     * @param title
     */
    public void setTitleText(String title) {
        titleName.setText(TextUtils.isEmpty(title) ? "" : title);
    }
    public void setClickable(boolean clickable){
        title_back_tv.setClickable(clickable);
    }

    /**
     * 隐藏标题名称
     */
    public void setTitleDisable() {
        titleName.setVisibility(GONE);
    }

    /**
     * 设置左边文字颜色
     */
    public void setTvBackColor(int color) {
        title_back_tv.setTextColor(getResources().getColor(color));
    }

    /**
     * 显示标题名称
     */
    public void setTitleEnable() {
        titleName.setVisibility(VISIBLE);
    }

    /**
     * 设置背景颜色
     *
     * @param resId
     */
    public void setTitleLayoutBackGround(int resId) {
        title_layout.setBackgroundResource(resId);
    }

    /**
     * 设置标题名称
     *
     * @param resId
     */
    public void setTitleText(int resId) {
        titleName.setText(resId);
    }

    /**
     * 设置返回的文本
     *
     * @param resId
     */
    public void setBackText(int resId) {
        title_back_tv.setText(resId);
    }

    /**
     * 隐藏左边的箭头
     */
    public void setLeftArrowDisable() {
        title_back_tv.setCompoundDrawables(null, null, null, null);
    }

    /**
     * 显示左边的箭头
     */
    public void setLeftArrowShow() {
        Drawable leftDrawable = getResources().getDrawable(R.mipmap.back);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        title_back_tv.setCompoundDrawables(leftDrawable, null, null, null);
    }

    /**
     * 隐藏左边的返回
     */
    public void setLeftDisable() {
        title_left_layout.setVisibility(View.GONE);
    }

    /**
     * 显示左边的返回
     */
    public void setLeftShow() {
        title_left_layout.setVisibility(View.VISIBLE);
    }

    /**
     * @Title: resetTitleRightMenu
     * @Description: TODO(重设标题栏右边的菜单, 包含影藏操作)
     * @param:
     */
    public void resetTitleRightMenu(int... resID) {
        int dp_5 = (int) getResources().getDimension(R.dimen.dp_5);
        int size = resID == null ? 0 : resID.length;
        boolean isAllStr = true;
        boolean isAllDra = true;
        for (int i = 0; i < size; i++) {
            //判断它是不是图片资源
            if (resID[i] >= 0x7f020000 && resID[i] <= 0x7f02ffff || resID[i] >= 0x7f030000 && resID[i] <= 0x7f03ffff) {
                isAllStr = false;
            } else {
                isAllDra = false;
            }
        }
        if (isAllStr) {
            String[] res = new String[size];
            //全是文字
            for (int i = 0; i < size; i++) {
                res[i] = getResources().getString(resID[i]);
            }
            resetTitleRightMenu(res);
        } else if (isAllDra) {
            //全是图片
            if (title_right_layout != null) {
                title_right_layout.removeAllViews();
            }
            title_right_layout.setVisibility(View.VISIBLE);
            int dp_10 = (int) getResources().getDimension(R.dimen.dp_10);
            for (int i = 0; i < size; i++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout relativeLayout = new RelativeLayout(getContext());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                relativeLayout.setLayoutParams(params);
                relativeLayout.setTag(i);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                ImageView imageView = new ImageView(getContext());
                imageView.setId(right_img_id);
                if (i == 0) {
                    if (size == 1) {
                        imageView.setPadding(dp_10, dp_10, dp_10, dp_10);
                    } else {
                        imageView.setPadding(dp_10, dp_10, dp_10, dp_10);
                    }
                } else {
                    imageView.setPadding(dp_10, dp_10, dp_10, dp_10);
                }
                imageView.setImageResource(resID[i]);
                imageView.setBackgroundResource(R.drawable.title_color_select);
                relativeLayout.addView(imageView);
                relativeLayout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (rightBtnClickListener != null) {
                            rightBtnClickListener.rightClick((Integer) v.getTag());
                        }
                    }
                });
                title_right_layout.addView(relativeLayout, layoutParams);
            }
        } else {
            //又有文字又有图片
            if (title_right_layout != null) {
                title_right_layout.removeAllViews();
            }
            title_right_layout.setVisibility(View.VISIBLE);
            int dp_10 = (int) getResources().getDimension(R.dimen.dp_10);
            for (int i = 0; i < size; i++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (resID[i] >= 0x7f020000 && resID[i] <= 0x7f02ffff) {
                    RelativeLayout relativeLayout = new RelativeLayout(getContext());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    relativeLayout.setLayoutParams(params);
                    relativeLayout.setTag(i);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    ImageView imageView = new ImageView(getContext());
                    imageView.setId(android.R.id.icon);
                    imageView.setPadding(dp_10, dp_10, dp_10, dp_10);
                    imageView.setImageResource(resID[i]);
                    imageView.setBackgroundResource(R.drawable.title_color_select);
                    relativeLayout.addView(imageView);
                    relativeLayout.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (rightBtnClickListener != null) {
                                rightBtnClickListener.rightClick((Integer) v.getTag());
                            }
                        }
                    });
                    title_right_layout.addView(relativeLayout, layoutParams);
                } else {
                    textView = new TextView(getContext());
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(dp_10, 0, dp_10, 0);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_21px));
                    textView.setText(resID[i]);
                    textView.setTextColor(getResources().getColor(R.color.text_default));
                    textView.setBackgroundResource(R.drawable.title_color_select);
                    textView.setTag(i);
                    textView.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (rightBtnClickListener != null) {
                                rightBtnClickListener.rightClick((Integer) v.getTag());
                            }
                        }
                    });
                    title_right_layout.addView(textView, layoutParams);
                }
            }
        }
    }

    public void resetTitleRightMenu(Drawable drawable) {
        if (title_right_layout != null) {
            title_right_layout.removeAllViews();
        }
        title_right_layout.setVisibility(View.VISIBLE);
        int dp_10 = (int) getResources().getDimension(R.dimen.dp_10);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        relativeLayout.setLayoutParams(params);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        ImageView imageView = new ImageView(getContext());
        imageView.setId(right_img_id);
        relativeLayout.setTag(0);
        imageView.setPadding(dp_10, dp_10, dp_10, dp_10);
        imageView.setImageDrawable(drawable);
        imageView.setBackgroundResource(R.drawable.title_color_select);
        relativeLayout.addView(imageView);
        relativeLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (rightBtnClickListener != null) {
                    rightBtnClickListener.rightClick((Integer) v.getTag());
                }
            }
        });
        title_right_layout.addView(relativeLayout, layoutParams);
    }

    /**
     * 设置右菜单栏的文字是否可以点击
     *
     * @param clickable 是否可以点击
     */
    public void setRightTextviewClickable(boolean clickable) {
        if (null != textView) {
            textView.setBackgroundResource(R.color.text_default);
            textView.setClickable(clickable);
        }
    }

    public void resetTitleRightMenu(String[] res) {
        int size = res == null ? 0 : res.length;
        if (title_right_layout != null) {
            title_right_layout.removeAllViews();
        }
        if (size > 0) {
            title_right_layout.setVisibility(View.VISIBLE);
            int dp_10 = (int) getResources().getDimension(R.dimen.dp_10);
            for (int i = 0; i < size; i++) {
                TextView textView = new TextView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_28px));
                textView.setText(res[i]);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackgroundResource(R.drawable.title_color_select);
                textView.setPadding(dp_10, 0, dp_10, 0);
                title_right_layout.addView(textView, layoutParams);
                textView.setTag(i);
                textView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (rightBtnClickListener != null) {
                            rightBtnClickListener.rightClick((Integer) v.getTag());
                        }
                    }
                });
            }
        }
    }

    /**
     * @Title: resetTitleRightMenu
     * @Description: TODO(重设标题栏右边的菜单, 包含隐藏操作，设置按钮背景，设置按钮字体颜色)
     * @param:
     */
    public void resetTitleRightMenu(int backSrc, int textColor, String titleName) {
        if (title_right_layout != null) {
            title_right_layout.removeAllViews();
        }
        title_right_layout.setVisibility(View.VISIBLE);
        int dp_10 = (int) getResources().getDimension(R.dimen.dp_10);
        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_21px));
        if (textColor != -1) {
            textView.setTextColor(getResources().getColor(textColor));
        }
        if (backSrc == -1) {
            textView.setBackgroundResource(R.drawable.title_color_select);
        } else {
            textView.setBackgroundResource(backSrc);
        }
        textView.setPadding(dp_10, 0, dp_10, 0);
        if (titleName != null) {
            textView.setText(titleName);
        }
        title_right_layout.setGravity(Gravity.CENTER_VERTICAL);
        title_right_layout.addView(textView, layoutParams);
        textView.setTag(0);
        textView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (rightBtnClickListener != null) {
                    rightBtnClickListener.rightClick((Integer) v.getTag());
                }
            }
        });
    }

    /**
     * 设置返回按钮点击监听器
     *
     * @param listener
     */
    public void setOnBackBtnClickListener(OnBackBtnClickListener listener) {
        this.backBtnClickListener = listener;
    }

    /**
     * 设置右侧图标按钮点击监听器
     *
     * @param listener
     */
    public void setOnRightBtnClickListener(OnRightBtnClickListener listener) {
        this.rightBtnClickListener = listener;
    }

    /**
     * 点击监听器
     */
    public interface OnBackBtnClickListener {
        void onBackClick(View v);
    }

    /**
     * 选择车辆点击监听器
     */
    public interface OnRightBtnClickListener {
        void rightClick(int id);
    }

    public LinearLayout getTitleRightLayout() {
        return title_right_layout;
    }
}