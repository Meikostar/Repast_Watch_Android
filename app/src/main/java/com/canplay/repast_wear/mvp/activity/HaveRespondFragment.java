package com.canplay.repast_wear.mvp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.guhy.swiperefresh.SwipeRefreshMode;
import me.guhy.swiperefresh.SwipeRefreshPlus;

//已应答(已完成)
public class HaveRespondFragment extends BaseFragment implements MessageContract.View {

    @Inject
    MessagePresenter messagePresenter;
    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.list_have_respond)
    ListView listHaveRespond;
    @BindView(R.id.mSwipeRefresh)
    SwipeRefreshPlus mSwipeRefresh;
    private Unbinder unbinder;
    private List<Message> messages = new ArrayList<>();
    private RespondAdapter adapter;
    private RespondActivity activity;
    private Resps resps;
    private SpUtil spUtil;
    private String deviceCode;
    private int pageSize = 10;//每页数
    private int pageNo = 1;//当前页 首页传1
    private int state = 2;//1：忽略的消息，2已完成
    private boolean isDownLoad = true;
    private boolean isFlash;
    private boolean isLoadMore;
    View noMoreView;

    public static HaveRespondFragment newInstance() {
        HaveRespondFragment fragment = new HaveRespondFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        super.onCreateView(inflater, container, bundle);
        View view = inflater.inflate(R.layout.fragment_have_respond, null);
        unbinder = ButterKnife.bind(this, view);
        spUtil = SpUtil.getInstance();
        deviceCode = spUtil.getString("deviceCode");
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
        adapter.setType(1);
        listHaveRespond.setAdapter(adapter);
        messagePresenter.getWatchMessageList(deviceCode, pageSize, pageNo, state, getActivity());
        mSwipeRefresh.setScrollMode(SwipeRefreshMode.MODE_BOTH);
    }
    Handler handler=new Handler();
    Runnable runnable=new Runnable(){
        @Override
        public void run() {
            pageNo++;
            messagePresenter.getWatchMessageList(deviceCode, pageSize, pageNo, state, getActivity());
            mSwipeRefresh.setLoadMore(false);
        }
    };
    Runnable run=new Runnable(){
        @Override
        public void run() {
            messages.clear();//重新加载，清空
            pageNo = 1;
            messagePresenter.getWatchMessageList(deviceCode, pageSize, pageNo, state, getActivity());
            mSwipeRefresh.setRefresh(false);
            mSwipeRefresh.showNoMore(false);
        }
    };
    private void initData() {
        mSwipeRefresh.setRefreshColorResources(new int[]{R.color.colorPrimary,R.color.red, R.color.green});
        mSwipeRefresh.setLoadMoreColorResources(new int[]{R.color.colorPrimary,R.color.red, R.color.green});
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshPlus.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                handler.postDelayed(run, 1000);
//                mSwipeRefresh.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        messages.clear();//重新加载，清空
//                        pageNo = 1;
//                        messagePresenter.getWatchMessageList(deviceCode, pageSize, pageNo, state, getActivity());
//                        mSwipeRefresh.setRefresh(false);
//                        mSwipeRefresh.showNoMore(false);
//                    }
//                }, 1000);
            }

            @Override
            public void onPullUpToRefresh() {
                if (!isDownLoad) {
                    mSwipeRefresh.showNoMore(true);
                } else {
                    handler.postDelayed(runnable, 1000);
//                    mSwipeRefresh.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            pageNo++;
//                            messagePresenter.getWatchMessageList(deviceCode, pageSize, pageNo, state, getActivity());
//                            mSwipeRefresh.setLoadMore(false);
//                        }
//                    }, 1000);
                }
                mSwipeRefresh.setRefresh(false);
            }
        });
        noMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.item_no_more, null, false);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        noMoreView.setPadding(10, 10, 10, 10);
        mSwipeRefresh.setNoMoreView(noMoreView, layoutParams);
    }


    @Override
    public void onDestroyView() {
        handler.removeCallbacks(runnable);
        handler.removeCallbacks(run);
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public <T> void toList(List<T> list, int type, int... refreshType) {
    }

    @Override
    public <T> void toEntity(T entity) {
        if (isFlash) {
            messages.clear();
            isFlash = false;
        }
        Resps resps = (Resps) entity;
        Log.e("HaveRespondFragment---", resps.toString());
        List<Message> pushListResps = resps.getPushListResps();

        if (pushListResps == null || pushListResps.size() == 0) {
            alert("已全部加载");
            if (messages.size() == 0) {
                tvNull.setVisibility(View.VISIBLE);
            }
            if (isLoadMore) {
                isDownLoad = true;
            }
        } else {
            isLoadMore=true;
            tvNull.setVisibility(View.GONE);
            for (int i = 0; i < pushListResps.size(); i++) {
                Message message = pushListResps.get(i);
                messages.add(message);
            }
            adapter.notifyDataSetChanged();
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

    //    @Override
//    public void onRefresh() {
//        pageNo = 1;
//        isFlash = true;
//        messagePresenter.getWatchMessageList(deviceCode, pageSize, pageNo, state, getActivity());
//        if (isFlash) {
//            listHaveRespond.setRefreshTime("刚刚");
//        }
//        isDownLoad = true;
//    }
//
//    @Override
//    public void onLoadMore() {
//        pageNo = pageNo + 1;
//        messagePresenter.getWatchMessageList(deviceCode, pageSize, pageNo, state, getActivity());
//        isDownLoad = true;
//    }
//
//    private void stop() {
//        listHaveRespond.stopRefresh();
//        listHaveRespond.stopLoadMore();
//        isDownLoad = false;
//    }
}
