package com.canplay.repast_wear.mvp.activity;

import android.util.Log;
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

public class ToContactActivity extends BaseActivity implements MessageContract.View {
    @Inject
    MessagePresenter presenter;
    @BindView(R.id.list_to_other)
    GridView listToOther;
    private List<Table> nameList = new ArrayList<>();
    private ListAdapter adapter;
    private TextView name;
    private long pushId;
    private SpUtil sp;
    private String deviceCode;
    private String businessId;

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
        presenter.getWatchList(deviceCode, businessId, this);

    }

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
                presenter.watchPushMessage(pushId, nameList.get(position).getTableId());
            }
        });
    }

    @Override
    public <T> void toList(List<T> list, int type, int... refreshType) {
        if (type == 1) {
            nameList = (List<Table>) list;
            adapter = new ArrayAdapter(this, R.layout.adapter_select_man, R.id.tv_name, nameList) {


                @Override
                public View getView(int position,
                                    View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    name = (TextView) view.findViewById(R.id.tv_name);
                    name.setText(nameList.get(position).getTableNo());
                    TextView checkbox = (TextView) view.findViewById(R.id.checkbox);
                    if (nameList.get(position).getState().equals("1")) {
                        checkbox.setBackground(getResources().getDrawable(R.drawable.org_cycle));
                    }
                    return view;
                }
            };
            listToOther.setAdapter(adapter);
        }
        Log.e("table", nameList.toString());
    }

    @Override
    public <T> void toEntity(T entity) {

    }

    @Override
    public void toNextStep(int type) {
        if (type==1) {
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
