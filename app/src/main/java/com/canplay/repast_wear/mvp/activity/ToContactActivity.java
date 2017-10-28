package com.canplay.repast_wear.mvp.activity;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseActivity;
import com.canplay.repast_wear.base.BaseApplication;
import com.canplay.repast_wear.mvp.component.DaggerBaseComponent;
import com.canplay.repast_wear.mvp.model.Table;
import com.canplay.repast_wear.mvp.present.MessageContract;
import com.canplay.repast_wear.mvp.present.MessagePresenter;
import com.canplay.repast_wear.util.SpUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.guhy.swiperefresh.SwipeRefreshMode;
import me.guhy.swiperefresh.SwipeRefreshPlus;

public class ToContactActivity extends BaseActivity implements MessageContract.View {
    @Inject
    MessagePresenter presenter;
    @BindView(R.id.list_to_other)
    GridView listToOther;
    @BindView(R.id.mSwipeRefresh)
    SwipeRefreshPlus mSwipeRefresh;
    private List<Table> nameList = new ArrayList<>();
    private ListAdapter adapter;
    private TextView name;
    private long pushId;
    private SpUtil sp;
    private String deviceCode="";
    private String businessId="";

    private int pageSize = 20;//每页数
    private int pageIndex =1;//当前页 首页传1

    private boolean isDownLoad = true;
    private boolean isFlash;
    private boolean isLoadMore;
    View noMoreView;

    @Override
    public void initInjector() {
        DaggerBaseComponent.builder().appComponent(((BaseApplication) getApplication()).getAppComponent()).build().inject(this);
        presenter.attachView(this);
        initUI(R.layout.activity_change);
        ButterKnife.bind(this);
        sp = SpUtil.getInstance();
        deviceCode = sp.getString("deviceCode");
        businessId = sp.getString("businessId");
    }

    @Override
    public void initCustomerUI() {
        presenter.getWatchList(deviceCode, businessId,pageSize,pageIndex,this);
        mSwipeRefresh.setScrollMode(SwipeRefreshMode.MODE_LOADMODE);
    }
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            pageIndex++;
            presenter.getWatchList(deviceCode, businessId,pageSize,pageIndex,ToContactActivity.this);
            mSwipeRefresh.setLoadMore(false);
        }
    };
    Runnable run = new Runnable() {
        @Override
        public void run() {
            isFlash=true;
            pageIndex = 1;
            presenter.getWatchList(deviceCode, businessId,pageSize,pageIndex,ToContactActivity.this);
            mSwipeRefresh.setRefresh(false);
            mSwipeRefresh.showNoMore(false);
        }
    };

    @Override
    public void initOther() {
        pushId = getIntent().getLongExtra("pushId", 0);
        Log.e("pushId", pushId + "");
        listToOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (nameList.get(position).getState().equals("1")) {
                    return;
                }
                presenter.watchPushMessage(pushId, nameList.get(position).getWatchCode());
            }
        });
        mSwipeRefresh.setRefreshColorResources(new int[]{R.color.colorPrimary, R.color.red, R.color.green});
        mSwipeRefresh.setLoadMoreColorResources(new int[]{R.color.colorPrimary, R.color.red, R.color.green});
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshPlus.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
//                handler.postDelayed(run, 1000);
            }

            @Override
            public void onPullUpToRefresh() {
                if (!isDownLoad) {
                    toast = Toast.makeText(ToContactActivity.this, "已全部加载!", Toast.LENGTH_SHORT);
                    mSwipeRefresh.showNoMore(true);
                } else {
                    handler.postDelayed(runnable, 500);
                }
                mSwipeRefresh.setRefresh(false);
            }
        });
        noMoreView = LayoutInflater.from(this).inflate(R.layout.item_no_more, null, false);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        noMoreView.setPadding(10, 10, 10, 10);
        mSwipeRefresh.setNoMoreView(noMoreView, layoutParams);
    }

    @Override
    public <T> void toList(List<T> list, int type, int... refreshType) {
        List<Table> tables = (List<Table>) list;
        if(tables.size() == 0){
            isDownLoad =false;
            return;
        }
        nameList.addAll(tables);
        adapter = new ArrayAdapter(this, R.layout.adapter_select_man, R.id.tv_name, nameList) {
            @Override
            public View getView(int position,
                                View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                name = (TextView) view.findViewById(R.id.tv_name);
                name.setText(nameList.get(position).getName());
                TextView checkbox = (TextView) view.findViewById(R.id.checkbox);
                if (nameList.get(position).getState().equals("1")) {
                    checkbox.setBackground(getResources().getDrawable(R.drawable.org_cycle));
                }
                return view;
            }
        };
        listToOther.setAdapter(adapter);
        if (type == 1) {//有下一页
            isDownLoad =true;
        }else if (type == 0) {
            isDownLoad =false;
        }
        Log.e("table", nameList.toString());
    }

    @Override
    public <T> void toEntity(T entity,int type) {
    }

    @Override
    public void toNextStep(int type) {
        if (type==403) {
            toast = Toast.makeText(this, "转移成功", Toast.LENGTH_SHORT);
            toast.show();
            setResult(RESULT_OK,getIntent());
            finish();
        }

    }

    @Override
    public void showTomast(String msg) {

    }
}
