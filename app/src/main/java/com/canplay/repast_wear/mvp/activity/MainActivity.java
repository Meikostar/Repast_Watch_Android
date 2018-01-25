package com.canplay.repast_wear.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.BatteryManager;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseActivity;
import com.canplay.repast_wear.base.BaseApplication;
import com.canplay.repast_wear.base.RxBus;
import com.canplay.repast_wear.base.SubscriptionBean;
import com.canplay.repast_wear.mvp.component.DaggerBaseComponent;
import com.canplay.repast_wear.mvp.model.ApkUrl;
import com.canplay.repast_wear.mvp.model.DEVICE;
import com.canplay.repast_wear.mvp.model.Message;
import com.canplay.repast_wear.mvp.model.Version;
import com.canplay.repast_wear.mvp.present.MessageContract;
import com.canplay.repast_wear.mvp.present.MessagePresenter;
import com.canplay.repast_wear.util.DownloadApk;
import com.canplay.repast_wear.util.NetUtil;
import com.canplay.repast_wear.util.SpUtil;
import com.canplay.repast_wear.util.TextUtil;
import com.canplay.repast_wear.view.TitleBarLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;


public class MainActivity extends BaseActivity implements MessageContract.View {

    @Inject
    MessagePresenter messagePresenter;
    @BindView(R.id.show)
    LinearLayout show;
    @BindView(R.id.message)
    LinearLayout llmessage;


    private Subscription mSubscription;
    private long showtime;//覆盖的时间
    private SpUtil sp;
    private long tableId;//桌子id （30s后自动转移情况传0，手动转移传桌子id）
    private long pushId;//推送记录id
    private boolean isShow = false;
    private Message passMessage;
    private String androidId;
    private String tableNo;
    private PopupWindow popSignOut;
    private EditText editText;
    private PowerManager.WakeLock wakeLock;

    @Override
    public void initInjector() {
        DaggerBaseComponent.builder().appComponent(((BaseApplication) getApplication()).getAppComponent()).build().inject(this);
        messagePresenter.attachView(this);
    }
    private int times;
    private int status;
    private TitleBarLayout titleBarView;
    @Override
    public void initCustomerUI() {
        initUI(R.layout.activity_main);
        ButterKnife.bind(this);
         titleBarView = getTitleBarView();
        titleBarView.setLeftArrowDisable();
        titleBarView.setBackText(R.string.main_name,null);
        star= new SoundPool(10, AudioManager.STREAM_SYSTEM, 0);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = star.load(this, R.raw.star, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级

        sp = SpUtil.getInstance();
        androidId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        sp.putString("deviceCode", androidId);
        if (!sp.getBoolean("hasBinder")) {
            startActivity(new Intent(this, BinderActivity.class));
            finish();
        } else {
//            messagePresenter.deviceInfo(androidId);
        }
        if(NetUtil.isNetworkAccessiable(MainActivity.this)){
            messagePresenter.getInit(androidId);
        }else {
            timel = new CountDownTimer(1000*180, 3000) {
                @Override
                public void onTick(long millisUntilFinished) {

                        if(NetUtil.isNetworkAccessiable(MainActivity.this)){
                            //网络请求
                                messagePresenter.getInit(androidId);
                        }



                }
                @Override
                public void onFinish() {

                }
            }.start();
        }


    }

    @Override
    public void notifyBattery(int level, int scale, int status) {
           titleBarView.notifyBattery(level, scale, status);
    }
    private CountDownTimer timel;
    @Override
    public void initOther() {
        PowerManager pm = (PowerManager) MainActivity.this.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "SimpleTimer");
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.CHOOSE) {
                    Message message = (Message) bean.content;
                    pushId = message.getPushId();
                    if (wakeLock != null) {
                        wakeLock.acquire();
                    }
                    showJPushData(message);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPop();
            }
        });
        llmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RespondActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackClick(View v) {
        if (tableNo != null) {
            Log.e("backclick  ", "点击了显示桌号");
            toast = Toast.makeText(this, "当前绑定的桌号为：" + tableNo, Toast.LENGTH_SHORT);
            toast.show();
        }
//        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
//        alertDialogBuilder.setTitle("切换店面").setMessage("是否改变店面绑定").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                startActivity(new Intent(MainActivity.this, BinderActivity.class));
//                sp.putBoolean("hasBinder", false);
//                finish();
//            }
//        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        }).show();
    }

    public void showPop() {
        final View contentView = LayoutInflater.from(this).inflate(R.layout.pop_account, null);
        final PopupWindow dialog = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(contentView);
        TextView cancel = (TextView) contentView.findViewById(R.id.pop_cancel);
        TextView toLogin = (TextView) contentView.findViewById(R.id.pop_to_login);
        TextView loginOut = (TextView) contentView.findViewById(R.id.pop_login_out);
        //获取焦点
        dialog.setFocusable(true);
        dialog.setOutsideTouchable(true);
        //背景颜色
        dialog.setBackgroundDrawable(new ColorDrawable(0xffffff));
        //动画效果（进入页面和退出页面时的效果）
        //window.setAnimationStyle(R.style.windows);
        //显示位置：showAtLocation(主布局所点击的按钮id, 位置, x, y);
        dialog.showAtLocation(MainActivity.this.findViewById(R.id.show), Gravity.BOTTOM, 0, 0);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showSignOut();
//                finishAffinity();
            }
        });
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, BinderActivity.class));
            }
        });
    }

    private CountDownTimer timer;
    private PopupWindow window;
    private Vibrator vibrator;
    private TextView toOther;
    private TextView form;
    private TextView complain;
    private TextView callList;
    private TextView menuName;
    private TextView clockTime;

    private SoundPool star;//声明一个SoundPool
    private int music;//定义一个整型用load（）；来设置suondID
    public void showJPushData(final Message message) {
        if (wakeLock != null) {
            wakeLock.release();
        }
//        if (isShow) {                       //注释：取消30秒后自动调用转移接口
//            AccountManager.send(this, showtime, passMessage);//进行消息过度处理
//        } else {
//            passMessage = message;
//        }
        // 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        star.play(music, 1, 1, 0, 1, 1);

        long[] pattern = {100, 400, 100, 400}; // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //加载子布局
        View view = inflater.inflate(R.layout.popwindow, null);
        if (window == null) {
            window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            toOther = (TextView) view.findViewById(R.id.to_other);
            form = (TextView) view.findViewById(R.id.former);
            callList = (TextView) view.findViewById(R.id.call_list);
            complain = (TextView) view.findViewById(R.id.complain);
            menuName = (TextView) view.findViewById(R.id.menuName);
            clockTime = (TextView) view.findViewById(R.id.clock_time);
        }
        menuName.setText(message.getMenuName());
        form.setText(message.getTableNo());
        //获取焦点
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        //背景颜色
        window.setBackgroundDrawable(new ColorDrawable(0xffffff));
        //动画效果（进入页面和退出页面时的效果）
        window.setAnimationStyle(R.style.PopupAnimation);
        //显示位置：showAtLocation(主布局所点击的按钮id, 位置, x, y);
        window.showAtLocation(MainActivity.this.findViewById(R.id.show), Gravity.CENTER, 0, 0);
        isShow = true;
        //弹窗消失监听
        if (timer == null) {
            timer = new CountDownTimer(30000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    clockTime.setText((millisUntilFinished / 1000) + "");
                    showtime = millisUntilFinished;
                }

                @Override
                public void onFinish() {//自动转移
                    if (window != null) {
                        isShow = false;
                        if (window.isShowing()) {
                            window.dismiss();
                        }
                        window = null;
//                        sendMessage(0, pushId);   //注释：取消30秒后自动调用转移接口
                    }
                }
            }.start();
        } else timer.start();
        setPopListen(message);
    }


    private void setPopListen(final Message message) {
        toOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//转移
                Intent intent = new Intent(MainActivity.this, ToContactActivity.class);
                intent.putExtra("pushId", message.getPushId());
                startActivityForResult(intent, 2);
            }
        });
        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//完成
                Intent intent = new Intent(MainActivity.this, RespondActivity.class);
                startActivity(intent);
//                messagePresenter.finishPushMessage(pushId, MainActivity.this);
                window.dismiss();
                isShow = false;
                timer.cancel();
                window = null;
            }
        });
        callList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RespondActivity.class));
            }
        });
    }


    @Override
    public <T> void toList(List<T> list, int type, int... refreshType) {

    }

    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 1) {//获取版本信息
            Version version = (Version) entity;
            String apkVersion = version.getApkVersion();
            if (apkVersion.compareTo(getVersion(this)) > 0) {
                messagePresenter.getApkInfo();
            }
            if(TextUtil.isNotEmpty(version.getWatchName())){
                timel.cancel();
                titleBarView.setBackText(R.string.main_name,"－"+version.getWatchName());
            }else {
                messagePresenter.getInit(androidId);
            }

        } else if (type == 2) {//进行版本的自动下载更新
            ApkUrl apkUrl = (ApkUrl) entity;
            String uploadUrl = apkUrl.getUploadUrl();
            new Thread(new DownloadApk(uploadUrl,this)).start();
        } else if (type == 3) {
            DEVICE device = (DEVICE) entity;
            Log.e("device", device.toString());
            if (device.getBound() == 0) startActivity(new Intent(this, BinderActivity.class));
            tableNo = device.getTableNo();
        }
    }

    @Override
    public void toNextStep(int type) {
        if (type == 1) {
            toast = Toast.makeText(this, "转移成功", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (type == 2) {
            startActivity(new Intent(MainActivity.this, BinderActivity.class));
            sp.putBoolean("hasBinder", false);
            finish();
        }
        if (type == 3) {
            popSignOut.dismiss();
            Intent intent =  new Intent(Settings.ACTION_SETTINGS);

            startActivity(intent);
            finishAffinity();
        }
        if (type == 4) {
            toast = Toast.makeText(this, "密码错误!", Toast.LENGTH_SHORT);
            toast.show();
            editText.setText("");
        }
    }

    @Override
    public void showTomast(String msg) {

    }

//    public void sendMessage(long tableId, long pushId) {
//        Log.e("pushId", pushId + "");
//        messagePresenter.watchPushMessage(pushId, tableId);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (window != null) {
                window.dismiss();
                isShow = false;
                timer.cancel();
                window = null;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showSignOut() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_signout, null);
        popSignOut = new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popSignOut.setContentView(contentView);
        editText = (EditText) contentView.findViewById(R.id.et_psw);
        TextView tvCancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        TextView tvSure = (TextView) contentView.findViewById(R.id.tv_sure);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popSignOut.dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editText.getText().toString();
                if (password.equals("") && password.length() == 0) {
                    toast = Toast.makeText(MainActivity.this, "输入密码不能为空!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    messagePresenter.deviceSignOut(androidId, password, 3);
                }
            }
        });
        //获取焦点
        popSignOut.setFocusable(true);
        popSignOut.setOutsideTouchable(true);
        //背景颜色
        popSignOut.setBackgroundDrawable(new ColorDrawable(0xffffff));
        //动画效果（进入页面和退出页面时的效果）
        //window.setAnimationStyle(R.style.windows);
        //显示位置：showAtLocation(主布局所点击的按钮id, 位置, x, y);
        popSignOut.showAtLocation(MainActivity.this.findViewById(R.id.show), Gravity.CENTER, 0, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            showSignOut();
            return true;
//            messagePresenter.deviceSignOut();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static String getVersion(Context context) {

        try {

            PackageManager manager = context.getPackageManager();

            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);

            String version = info.versionName;

            return version;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
