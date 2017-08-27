package com.canplay.repast_wear.mvp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseApplication;
import com.canplay.repast_wear.base.BaseFragment;
import com.canplay.repast_wear.mvp.adapter.RespondAdapter;
import com.canplay.repast_wear.mvp.component.DaggerBaseComponent;
import com.canplay.repast_wear.mvp.model.Message;
import com.canplay.repast_wear.mvp.model.Resps;
import com.canplay.repast_wear.mvp.present.MessageContract;
import com.canplay.repast_wear.mvp.present.MessagePresenter;
import com.canplay.repast_wear.util.SpUtil;
import com.canplay.repast_wear.view.loadmore.XListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NoRespondFragment extends BaseFragment implements MessageContract.View, XListView.IXListViewListener {

    @Inject
    MessagePresenter messagePresenter;
    @BindView(R.id.list_no_respond)
    XListView listNoRespond;
    @BindView(R.id.tv_null)
    TextView tvNull;
    //    @BindView(R.id.mSwipeRefresh)
//    MaterialRefreshLayout mSwipeRefresh;
    private Unbinder unbinder;
    private List<Message> messages = new ArrayList<>();
    private RespondAdapter adapter;
    private RespondActivity activity;
    private Resps resps;
    private SpUtil spUtil;
    private String deviceCode;
    private int pageSize = 3;//每页数
    private int pageNo = 1;//当前页 首页传1
    private int state = 1;//1：忽略的消息，2已完成
    private boolean isDownLoad;
    private boolean isFlash;

    public static NoRespondFragment newInstance() {
        NoRespondFragment fragment = new NoRespondFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        super.onCreateView(inflater, container, bundle);
        View view = inflater.inflate(R.layout.fragment_norespond, null);
        unbinder = ButterKnife.bind(this, view);
        spUtil = SpUtil.getInstance();
        deviceCode = spUtil.getString("deviceCode");//f0e33224b705bb16
        inject();
        initView();
        initData();
        return view;
    }

    private void inject() {
        DaggerBaseComponent.builder().appComponent(((BaseApplication) getActivity().getApplication()).getAppComponent()).build().inject(this);
        messagePresenter.attachView(this);
        activity = (RespondActivity) getActivity();
    }

    private void initView() {
        adapter = new RespondAdapter(getActivity(), messages);
        adapter.setType(2);
        listNoRespond.setAdapter(adapter);
        messagePresenter.getWatchMessageList(deviceCode, pageSize, pageNo, state, getActivity());
    }

    private void initData() {
        listNoRespond.setPullLoadEnable(true);
        listNoRespond.setXListViewListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public <T> void toList(List<T> list, int type, int... refreshType) {
//
    }

    @Override
    public <T> void toEntity(T entity) {
        if(isFlash){
            isFlash=false;
            messages.clear();
        }
        Resps resps = (Resps) entity;
        List<Message> pushListResps = resps.getPushListResps();
        Log.e("NoRespondFragment", resps.toString());
        //1：忽略的消息，2已完成
        if (pushListResps == null || pushListResps.size() == 0) {
            if (messages.size() == 0) {
                tvNull.setVisibility(View.VISIBLE);
            }
        } else {
            tvNull.setVisibility(View.GONE);
            for (int i = 0; i < pushListResps.size(); i++) {
                Message message = pushListResps.get(i);
                messages.add(message);
            }
            adapter.notifyDataSetChanged();

        }
        if (isDownLoad) {
            stop();
        }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }

    public void alert(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        isFlash=true;
        pageNo =  1;
        messagePresenter.getWatchMessageList(deviceCode, pageSize, pageNo, state, getActivity());
        if(isFlash){
            listNoRespond.setRefreshTime("刚刚");
        }
        isDownLoad = true;
    }

    @Override
    public void onLoadMore() {
        pageNo = pageNo + 1;
        messagePresenter.getWatchMessageList(deviceCode, pageSize, pageNo, state, getActivity());
        isDownLoad = true;
    }

    private void stop() {
        listNoRespond.stopRefresh();
        listNoRespond.stopLoadMore();
        isDownLoad=false;
    }

}
