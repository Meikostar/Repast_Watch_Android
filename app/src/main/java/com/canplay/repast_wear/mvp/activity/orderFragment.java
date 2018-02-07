package com.canplay.repast_wear.mvp.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import com.canplay.repast_wear.mvp.adapter.RespondAdapters;
import com.canplay.repast_wear.mvp.component.DaggerBaseComponent;
import com.canplay.repast_wear.mvp.model.Message;
import com.canplay.repast_wear.mvp.model.Resps;
import com.canplay.repast_wear.mvp.present.MessageContract;
import com.canplay.repast_wear.mvp.present.MessagePresenter;
import com.canplay.repast_wear.util.DateUtil;
import com.canplay.repast_wear.util.SpUtil;
import com.canplay.repast_wear.view.MPopupWindow;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.guhy.swiperefresh.SwipeRefreshMode;
import me.guhy.swiperefresh.SwipeRefreshPlus;

//已应答(已完成)
public class orderFragment extends BaseFragment implements MessageContract.View {

    @Inject
    MessagePresenter messagePresenter;
    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.list_have_respond)
    ListView listHaveRespond;
    @BindView(R.id.mSwipeRefresh)
    SwipeRefreshPlus mSwipeRefresh;
    @BindView(R.id.line)
    View line;
    private Unbinder unbinder;
    private List<Message> messages = new ArrayList<>();
    private RespondAdapters adapter;
    private OrderListActivity activity;
    private Resps resps;
    private SpUtil spUtil;
    private String deviceCode;
    private int pageSize = 10;//每页数
    private int pageNo = 1;//当前页 首页传1
    private int state = 2;//1：忽略的消息，2已完成
    private boolean isDownLoad = true;
    private boolean isFlash;
    private boolean isFirst = true;
    View noMoreView;

    public static orderFragment newInstance() {
        orderFragment fragment = new orderFragment();
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
        activity = (OrderListActivity) getActivity();
    }

    private void initView() {
        adapter = new RespondAdapters(getActivity(), messages,listHaveRespond);
        adapter.setType(1);
        listHaveRespond.setAdapter(adapter);
        messagePresenter.getOrderList( pageNo, getActivity());
        mSwipeRefresh.setScrollMode(SwipeRefreshMode.MODE_BOTH);

    }
    Handler handler=new Handler();
    Runnable runnable=new Runnable(){
        @Override
        public void run() {
            pageNo++;
            messagePresenter.getOrderList(pageNo, getActivity());
            mSwipeRefresh.setLoadMore(false);
        }
    };
    Runnable run=new Runnable(){
        @Override
        public void run() {
//            messages.clear();//重新加载，清空
            isFlash=true;
            pageNo = 1;
            messagePresenter.getOrderList(pageNo,  getActivity());
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
                    alert("已全部加载");
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
        adapter.setDeletListener(new RespondAdapters.selectItemListener() {
            @Override
            public void delete(Message message, int type, int poistion) {
                poistions=poistion;
                showPopWindow();
            }
        });
        adapter.setClickListener(new RespondAdapters.ImageViewClickListener() {
            @Override
            public void ImageClick(int position) {
                if(messages.get(position) == null){
                    return;
                }
//               showPopWindow(position);
            }

            @Override
            public void ImageClicks(int position) {
                Message message = messages.get(position);
                if (message == null || !canClick) {
                    return;
                }
                long messageTime = message.getTime();
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra("detailNo", message.detailNo);
                startActivityForResult(intent, 2);
                if (DateUtil.isLittle(messageTime)) {

                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == activity.RESULT_OK) {
            Log.e("haha", "-----重新刷新了！---------");
            canClick = false;
            isFlash = true;
            messages.clear();//重新加载，清空
            adapter.notifyDataSetChanged();
            pageNo = 1;
            messagePresenter.getOrderList( pageNo,  getActivity());//转移消息之后刷新
        }
    }
    private boolean canClick = true;

    @Override
    public void onDestroyView() {
        handler.removeCallbacks(runnable);
        handler.removeCallbacks(run);
        unbinder.unbind();
        super.onDestroyView();
    }
    private MPopupWindow mPopupWindow1;
    private View mView2;
    private TextView tvSure;
    private TextView tvCancel;
    public void showPopWindow(){
        if (mView2 == null) {
            mView2 = View.inflate(getActivity(), R.layout.pop_exit_view, null);
             tvCancel = (TextView) mView2.findViewById(R.id.tv_cancel);
             tvSure = (TextView) mView2.findViewById(R.id.tv_sure);
             tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow1.dismiss();
                }
            });
            tvSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messagePresenter.finishPush(messages.get(poistions).getPushId()+"",activity);

                    mPopupWindow1.dismiss();
                    if (messages.size() == 0) {
                        tvNull.setVisibility(View.VISIBLE);
                    }
                }
            });
            mPopupWindow1 = new MPopupWindow(mView2, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow1.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow1.setFocusable(true);
            mPopupWindow1.setOutsideTouchable(true);

            if (mPopupWindow1.isShowing()) {
                mPopupWindow1.dismiss();
            } else {
                mPopupWindow1.showAsDropDown(line, 0, 0);
            }
            mPopupWindow1.setShowListener(new MPopupWindow.IShowListener() {
                @Override
                public void onShow() {
                }

                @Override
                public void onDismiss() {
                }
            });
        } else {
            if (mPopupWindow1.isShowing()) {
                mPopupWindow1.dismiss();
            } else {
                mPopupWindow1.showAsDropDown(line, 0, 0);
            }
        }
    }
    @Override
    public <T> void toList(List<T> list, int type, int... refreshType) {
        canClick = true;
        if (isFlash) {
            messages.clear();
            isFlash = false;
        }
        List<Message> pushListResps = (List<Message>) list;
        tvNull.setVisibility(View.GONE);
        messages.addAll(pushListResps);
        adapter.notifyDataSetChanged();
        if (messages.size() == 0) {
            tvNull.setVisibility(View.VISIBLE);
        }
        if (type == 1) {//有下一页
            isDownLoad=true;
        } else if (type == 0) {
            isDownLoad=false;
//            if(!isFirst){
//                alert("已全部加载");
//                isFirst = false;
//            }
//            if (isLoadMore) {
//                isDownLoad = true;
//            }
        }
    }

    @Override
    public <T> void toEntity(T entity, int type) {
        canClick = true;
//        if (isFlash) {
//            messages.clear();
//            isFlash = false;
//        }
//        Resps resps = (Resps) entity;
//        Log.e("HaveRespondFragment---", resps.toString());
//        List<Message> pushListResps = resps.getPushListResps();
//
//        if (pushListResps == null || pushListResps.size() == 0) {
//            alert("已全部加载");
//            if (messages.size() == 0) {
//                tvNull.setVisibility(View.VISIBLE);
//            }
//            if (isLoadMore) {
//                isDownLoad = true;
//            }
//        } else {
//            isLoadMore=true;
//            tvNull.setVisibility(View.GONE);
//            for (int i = 0; i < pushListResps.size(); i++) {
//                Message message = pushListResps.get(i);
//                messages.add(message);
//            }
//            adapter.notifyDataSetChanged();
//        }
    }
    private int poistions=0;
    @Override
    public void toNextStep(int type) {
        messages.remove(poistions);
        adapter.setDatas(messages);
        adapter.notifyDataSetChanged();
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
