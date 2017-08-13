package com.canplay.repast_wear.mvp.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseActivity;
import com.canplay.repast_wear.base.BaseApplication;
import com.canplay.repast_wear.mvp.adapter.BinderSelectAdapter;
import com.canplay.repast_wear.mvp.component.DaggerBaseComponent;
import com.canplay.repast_wear.mvp.model.Table;
import com.canplay.repast_wear.mvp.present.TableContract;
import com.canplay.repast_wear.mvp.present.TablePresenter;
import com.canplay.repast_wear.util.SpUtil;
import com.canplay.repast_wear.view.TitleBarLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
public class BinderTabeActivity extends BaseActivity implements TableContract.View ,View.OnClickListener {

    @Inject
    TablePresenter tablePresenter;
    private ListView listTableMsg;
    private BinderSelectAdapter adapter;
    private List<Map<String, Object>> mapList = new ArrayList<>();;
    private Map<String, Object> data;
    private List<String> selectTables=new ArrayList<>();
    private long businessId;
    private List<Table> tableList=new ArrayList<>();
    private SpUtil sp;


    @Override
    public void initInjector() {
        DaggerBaseComponent.builder().appComponent(((BaseApplication) getApplication()).getAppComponent()).build().inject(this);
        tablePresenter.attachView(this);
        initUI(R.layout.activity_binder_table);
        TitleBarLayout titleBarView = getTitleBarView();
        titleBarView.setLeftArrowShow();
        titleBarView.setTvBackColor(R.color.orange_f);
        sp = SpUtil.getInstance();
    }

    @Override
    public void initCustomerUI() {
        listTableMsg = (ListView) findViewById(R.id.list_table_msg);
        businessId = getIntent().getLongExtra("businessId", 0);
        Log.e("businessId---",businessId+"");
        tablePresenter.getBusinessTableList(businessId,this);
    }

    @Override
    public void initOther() {
        findViewById(R.id.btn_next).setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                next();
                break;
        }
    }
    private void next() {
        getSelectTable();
        Log.e("----select----",selectTables.toString());
        sp.putBoolean("hasBinder",true);
        startActivity(new Intent(this, MainActivity.class));
    }
    private void getSelectTable(){
        if(mapList.size() != 0){
            for (int i = 0; i < mapList.size(); i++) {
                Map<String, Object> data = mapList.get(i);
                if ((int)data.get("type") == 1)
                    selectTables.add(String.valueOf(data.get("tableNumber")));
            }
        }else{
            showToast("您还未绑定");
            return;
        }
    }

    @Override
    public <T> void toList(List<T> list, int type, int... refreshType) {
        showToast("后台数据为："+list.toString());
        tableList=(List<Table>)list;
        for (int i = 0; i < tableList.size(); i++) {
            data = new HashMap<>();
            data.put("tableNumber", tableList.get(i).getTableNo());
            data.put("type",tableList.get(i).getBound());
            mapList.add(data);
        }
        adapter = new BinderSelectAdapter(this, mapList);
        listTableMsg.setAdapter(adapter);

    }

    @Override
    public <T> void toEntity(T entity) {
    }
    @Override
    public void toNextStep(int type) {

    }
    @Override
    public void showTomast(String table) {

    }
}
