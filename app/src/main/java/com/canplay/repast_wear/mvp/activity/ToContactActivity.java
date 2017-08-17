package com.canplay.repast_wear.mvp.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseActivity;
import com.canplay.repast_wear.base.BaseApplication;
import com.canplay.repast_wear.mvp.component.DaggerBaseComponent;
import com.canplay.repast_wear.mvp.model.AccountManager;
import com.canplay.repast_wear.mvp.model.Table;
import com.canplay.repast_wear.mvp.present.MessageContract;
import com.canplay.repast_wear.mvp.present.MessagePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToContactActivity extends BaseActivity implements MessageContract.View {
    @Inject
    MessagePresenter presenter;
    @BindView(R.id.list_to_other)
    GridView listToOther;
    private List<Table> nameList = new ArrayList<>();
    private ListAdapter adapter;
    private TextView name;
    private long pushId;

    @Override
    public void initInjector() {
        DaggerBaseComponent.builder().appComponent(((BaseApplication) getApplication()).getAppComponent()).build().inject(this);
        presenter.attachView(this);
        initUI(R.layout.activity_change);
        ButterKnife.bind(this);
    }

    @Override
    public void initCustomerUI() {
        adapter = new ArrayAdapter(this, R.layout.adapter_select_man, R.id.tv_name, nameList) {


            @Override
            public View getView(int position,
                                View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                name = (TextView) view.findViewById(R.id.tv_name);
                name.setText(nameList.get(position).getTableNo());
                return view;
            }
        };
        nameList = AccountManager.getTableList();
        listToOther.setAdapter(adapter);
    }

    @Override
    public void initOther() {
        pushId = getIntent().getLongExtra("pushId", 0);
        listToOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                name.setBackground(getResources().getDrawable(R.drawable.org_cycle));
//                presenter.pushMessage(pushId,nameList.get(position).getTableId(),ToContactActivity.this);
            }
        });
    }

    @Override
    public void showTomast(String msg) {
        if (msg.equals("true") || msg.equals("转移成功")) {
            finish();
        }
        showTomast(msg);
    }
}
