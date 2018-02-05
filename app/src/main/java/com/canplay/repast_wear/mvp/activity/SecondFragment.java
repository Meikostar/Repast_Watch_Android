package com.canplay.repast_wear.mvp.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseApplication;
import com.canplay.repast_wear.base.BaseFragment;
import com.canplay.repast_wear.base.RxBus;
import com.canplay.repast_wear.base.SubscriptionBean;
import com.canplay.repast_wear.mvp.adapter.RespondAdapter;
import com.canplay.repast_wear.mvp.component.DaggerBaseComponent;
import com.canplay.repast_wear.mvp.model.Message;
import com.canplay.repast_wear.mvp.model.Resps;
import com.canplay.repast_wear.mvp.present.MessageContract;
import com.canplay.repast_wear.mvp.present.MessagePresenter;
import com.canplay.repast_wear.util.DateUtil;
import com.canplay.repast_wear.util.SpUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.guhy.swiperefresh.SwipeRefreshMode;
import me.guhy.swiperefresh.SwipeRefreshPlus;

//未应答
public class SecondFragment extends BaseFragment {


    @BindView(R.id.show)
    LinearLayout show;
    @BindView(R.id.message)
    LinearLayout llmessage;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private Unbinder unbinder;
    private boolean isDownLoad = true;
    private boolean isFlash;
    private boolean isLoadMore;
    private boolean canClick = true;
    private boolean isFirst = true;
    View noMoreView;

    public static FisrstFragment newInstance() {
        FisrstFragment fragment = new FisrstFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        super.onCreateView(inflater, container, bundle);
        View view = inflater.inflate(R.layout.fragment_second, null);

        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    private void initView() {
        llmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.SIGNOUT,""));
            }
        });
    }



    @Override
    public void onDestroyView() {

        unbinder.unbind();
        super.onDestroyView();
    }



}
