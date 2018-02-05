package com.canplay.repast_wear.mvp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseActivity;
import com.canplay.repast_wear.base.BaseApplication;
import com.canplay.repast_wear.mvp.adapter.DetailAdapter;
import com.canplay.repast_wear.mvp.component.DaggerBaseComponent;
import com.canplay.repast_wear.mvp.model.Message;
import com.canplay.repast_wear.mvp.present.MessageContract;
import com.canplay.repast_wear.mvp.present.MessagePresenter;
import com.canplay.repast_wear.view.TitleBarLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity implements MessageContract.View {

    @Inject
    MessagePresenter messagePresenter;
    @BindView(R.id.listview)
    ListView listview;
    private String detailNo;
    private int mCurrentIndex = 0;//当前小圆点的位置
    /**
     * 屏幕的宽度
     */
    private int screenWidth;

    @Override
    public void initInjector() {
        DaggerBaseComponent.builder().appComponent(((BaseApplication) getApplication()).getAppComponent()).build().inject(this);
        messagePresenter.attachView(this);
    }

    @Override
    public void onBackClick(View v) {
//        v.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        finish();
//                    }
//                }, 1000);
        super.onBackClick(v);
    }
  private DetailAdapter adapter;
    @Override
    public void initCustomerUI() {
        initUI(R.layout.orer_detail_activity);
        ButterKnife.bind(this);
        detailNo=getIntent().getStringExtra("detailNo");
        messagePresenter.watchOrderInfo(detailNo,this);
        TitleBarLayout titleBarView = getTitleBarView();
        titleBarView.setTvBackColor(R.color.green_cyc);
        adapter=new DetailAdapter(this);
        listview.setAdapter(adapter);
    }

    @Override
    public void notifyBattery(int level, int scale, int status) {

    }

    @Override
    public void initOther() {
    }

    @Override
    public <T> void toList(List<T> list, int type, int... refreshType) {
        List<Message> pushListResps = (List<Message>) list;
        adapter.setData(pushListResps);
        adapter.notifyDataSetChanged();
    }

    @Override
    public <T> void toEntity(T entity, int type) {

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }


}
