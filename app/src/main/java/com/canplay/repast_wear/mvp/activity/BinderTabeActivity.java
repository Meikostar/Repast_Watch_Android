package com.canplay.repast_wear.mvp.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseActivity;
import com.canplay.repast_wear.base.BaseApplication;
import com.canplay.repast_wear.base.manager.AppManager;
import com.canplay.repast_wear.mvp.adapter.BinderSelectAdapter;
import com.canplay.repast_wear.mvp.component.DaggerBaseComponent;
import com.canplay.repast_wear.mvp.model.AccountManager;
import com.canplay.repast_wear.mvp.model.Table;
import com.canplay.repast_wear.mvp.present.TableContract;
import com.canplay.repast_wear.mvp.present.TablePresenter;
import com.canplay.repast_wear.util.SpUtil;
import com.canplay.repast_wear.view.TitleBarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
public class BinderTabeActivity extends BaseActivity implements TableContract.View {

    @Inject
    TablePresenter tablePresenter;
    private ListView listTableMsg;
    private BinderSelectAdapter adapter;
    private List<Map<String, Object>> mapList = new ArrayList<>();;
//    private List<String> selectTables=new ArrayList<>();
    private long businessId;
    private List<Table> tableList=new ArrayList<>();
    private SpUtil sp;
    private String androidId;//设备号
    private boolean isClike=true;


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
        androidId =sp.getString("deviceCode");
        Log.e("businessId---",businessId+"");
        tablePresenter.getBusinessTableList(businessId,androidId,this);
    }

    @Override
    public void initOther() {
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClike){
                    getSelectTable();
                    isClike=false;
                }
            }
        });
//        listTableMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (type == 3) {
//                    return;
//                }
//                if (holder.toRight.isChecked()) {
//                    holder.toRight.setChecked(false);
//                    table.setBound(0);
//                } else {
//                    holder.toRight.setChecked(true);
//                    table.setBound(1);
//                }
//            }
//        });
    }
    private void getSelectTable(){
        List<Long> tableIds = adapter.getTableIds();
        if(tableIds.size() != 0){
            String tableNos="";
            for (int i = 0; i < tableIds.size(); i++) {
                if(tableNos == ""){
                        tableNos=tableIds.get(i)+"";
                    }else {
                    tableNos=tableNos+","+tableIds.get(i);
                }
            }
            Log.e("选择的数据为tableId",tableNos);
            tablePresenter.bondBusiness(androidId,businessId,tableNos,this);
        }else{
            showToast("您还未绑定任何桌子");
            return;
        }
    }

    @Override
    public <T> void toList(List<T> list, int type, int... refreshType) {
        tableList=(List<Table>)list;
        Log.e("后台数据为：",tableList.toString());
        AccountManager.addTables(tableList);//数据进行临时存储
        adapter = new BinderSelectAdapter(this, tableList);
        listTableMsg.setAdapter(adapter);
    }

    @Override
    public <T> void toEntity(T entity) {
    }
    @Override
    public void toNextStep(int type) {
        if(type == 2){
            sp.putBoolean("hasBinder",true);
//            setResult(RESULT_OK,getIntent());
            AppManager.getInstance(this).finishAllActivity();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
    @Override
    public void showTomast(String table) {

    }
}
