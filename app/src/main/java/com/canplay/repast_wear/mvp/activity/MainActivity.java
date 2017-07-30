package com.canplay.repast_wear.mvp.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseActivity;
import com.canplay.repast_wear.base.RxBus;
import com.canplay.repast_wear.base.SubscriptionBean;
import com.canplay.repast_wear.mvp.model.Message;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements View.OnClickListener{


    @BindView(R.id.former)
    TextView former;
    @BindView(R.id.call_list)
    TextView callList;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.to_other)
    TextView toOther;
    @BindView(R.id.complain)
    TextView complain;
    @BindView(R.id.clock_time)
    TextView clockTime;
    private Subscription mSubscription;
    private CountDownTimer timer = new CountDownTimer(30000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            clockTime.setText((millisUntilFinished / 1000) + "");
        }

        @Override
        public void onFinish() {
        }
    };
    @Override
    public void initInjector() {
        initUI(R.layout.activity_watch);
        ButterKnife.bind(this);
    }

    @Override
    public void initCustomerUI() {
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.CHOOSE) {
                    Message message=(Message)bean.content;
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
        timer.start();
    }

    @Override
    public void initOther() {
        toOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ChangeActivity.class);
                startActivity(intent);
            }
        });
    }
   private void showPopWindown(Message message){
       LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
       //加载子布局
       View view = inflater.inflate(R.layout.popwindow, null);
       //绑定id
//       ImageView img1 = (ImageView) view.findViewById(R.id.imageView1);
//       ImageView img2 = (ImageView) view.findViewById(R.id.imageView2);
//       ImageView img3 = (ImageView) view.findViewById(R.id.imageView3);
//       ImageView img4 = (ImageView) view.findViewById(R.id.imageView4);
       //设置监听
//       img1.setOnClickListener(this);
//       img2.setOnClickListener(this);
//       img3.setOnClickListener(this);
//       img4.setOnClickListener(this);
       //设置弹窗宽高：PopupWindow(子布局, 弹窗的宽度, 弹窗的高度);
       PopupWindow window = new PopupWindow(view, 400, WindowManager.LayoutParams.WRAP_CONTENT);
       //获取焦点
       window.setFocusable(true);
       window.setOutsideTouchable(true);
       //背景颜色
       window.setBackgroundDrawable(new ColorDrawable(0xb000000));
       //动画效果（进入页面和退出页面时的效果）
//       window.setAnimationStyle(R.style.windows);
//       //显示位置：showAtLocation(主布局所点击的按钮id, 位置, x, y);
//       window.showAtLocation(MainActivity.this.findViewById(R.id.button1), Gravity.BOTTOM, 10, 10);
//       //弹窗消失监听
       window.setOnDismissListener(new PopupWindow.OnDismissListener() {
           @Override
           public void onDismiss() {
           }
       });
   }
    /**
     * 取消倒计时
     * @param v
     */
    public void oncancel(View v) {
        timer.cancel();
    }

    /**
     * 开始倒计时
     * @param v
     */
    public void restart(View v) {
        timer.start();
    }

    @Override
    public void onClick(View v) {

    }
}
