package com.canplay.repast_wear.mvp.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseActivity;
import com.canplay.repast_wear.base.BaseApplication;
import com.canplay.repast_wear.base.manager.AppManager;
import com.canplay.repast_wear.mvp.adapter.BinderSelectAdapter;
import com.canplay.repast_wear.mvp.component.DaggerBaseComponent;
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
    private List<Map<String, Object>> mapList = new ArrayList<>();
    ;
    private long businessId;
    private List<Table> tableList = new ArrayList<>();
    private SpUtil sp;
    private String androidId;//设备号
    private String name;//设备名称

    private PopupWindow popSignOut;
    private EditText editText;


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
        androidId = sp.getString("deviceCode");
        Log.e("businessId---", businessId + "");
        tablePresenter.getBusinessTableList(businessId, androidId, this);
    }

    @Override
    public void initOther() {
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectTable();
            }
        });
    }

    private void getSelectTable() {
        List<Long> tableIds = adapter.getTableIds();
        if (tableIds.size() != 0) {
            String tableNos = "";
            for (int i = 0; i < tableIds.size(); i++) {
                if (tableNos == "") {
                    tableNos = tableIds.get(i) + "";
                } else {
                    tableNos = tableNos + "," + tableIds.get(i);
                }
            }
            Log.e("选择的数据为tableId", tableNos);
            showSignOut(tableNos);
        } else {
            showToast("您还未绑定任何桌子");
            return;
        }
    }


    private void showSignOut(final String tableNos) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_device_id, null);
        popSignOut = new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popSignOut.setContentView(contentView);

        editText = (EditText) contentView.findViewById(R.id.et_psw);
        TextView tvCancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        TextView tvSure = (TextView) contentView.findViewById(R.id.tv_sure);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popSignOut.dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editText.getText().toString();
                if (password.equals("") && password.length() == 0) {
                    toast = Toast.makeText(BinderTabeActivity.this, "请输入手表名!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    name = editText.getText().toString();
                    tablePresenter.bondBusiness(androidId, businessId, tableNos, name, BinderTabeActivity.this);
                }
            }
        });
        //获取焦点
        popSignOut.setFocusable(true);
        popSignOut.setOutsideTouchable(true);
        //背景颜色
        popSignOut.setBackgroundDrawable(new ColorDrawable(0xffffff));
        //动画效果（进入页面和退出页面时的效果）
        //window.setAnimationStyle(R.style.windows);
        //显示位置：showAtLocation(主布局所点击的按钮id, 位置, x, y);
        popSignOut.showAtLocation(BinderTabeActivity.this.findViewById(R.id.show), Gravity.CENTER, 0, 0);

    }

    @Override
    public <T> void toList(List<T> list, int type, int... refreshType) {
        tableList = (List<Table>) list;
        Log.e("后台数据为：", tableList.toString());
//        AccountManager.addTables(tableList);//数据进行临时存储
        adapter = new BinderSelectAdapter(this, tableList);
        listTableMsg.setAdapter(adapter);
    }

    @Override
    public <T> void toEntity(T entity) {
    }

    @Override
    public void toNextStep(int type) {
        if (type == 2) {
            if (popSignOut != null) {
                popSignOut.dismiss();
            }
            sp.putBoolean("hasBinder", true);
            AppManager.getInstance(this).finishAllActivity();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void showTomast(String table) {

    }
}
