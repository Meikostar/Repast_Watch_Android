package com.canplay.repast_wear.mvp.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseActivity;
import com.canplay.repast_wear.base.BaseApplication;
import com.canplay.repast_wear.mvp.component.DaggerBaseComponent;
import com.canplay.repast_wear.mvp.present.TableContract;
import com.canplay.repast_wear.mvp.present.TablePresenter;
import com.canplay.repast_wear.util.SpUtil;
import com.canplay.repast_wear.view.TitleBarLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BinderActivity extends BaseActivity implements TableContract.View {
    @Inject
    TablePresenter tablePresenter;
    @BindView(R.id.rl_shen)
    RelativeLayout rlShen;
    @BindView(R.id.rl_shi)
    RelativeLayout rlShi;
    @BindView(R.id.rl_qu)
    RelativeLayout rlQu;
    @BindView(R.id.rl_dian)
    RelativeLayout rlDian;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.fragment_contain)
    LinearLayout fragmentContain;
    @BindView(R.id.tv_shen)
    TextView tvShen;
    @BindView(R.id.tv_shi)
    TextView tvShi;
    @BindView(R.id.tv_qu)
    TextView tvQu;
    @BindView(R.id.tv_dian)
    TextView tvDian;
    private SpUtil sp;
    private List<String> dianList = new ArrayList<>();
    private int PROVINCE = 101;
    private int CITY = 110;
    private int AREA = 111;
    private int BUINS = 112;
    private int poistion = -1;
    private int poistions = -1;
    private int provinceCode = 0;
    private int cityCode = 0;
    private int areaCode = 0;
    private Intent intent;
    private ListAdapter adapter;
    private long businessId;

    @Override
    public void initInjector() {
        DaggerBaseComponent.builder().appComponent(((BaseApplication) getApplication()).getAppComponent()).build().inject(this);
        tablePresenter.attachView(this);
    }

    @Override
    public void initCustomerUI() {
        initUI(R.layout.activity_binder);
        ButterKnife.bind(this);
        TitleBarLayout titleBarView = getTitleBarView();
        titleBarView.setLeftArrowShow();
        titleBarView.setTvBackColor(R.color.orange_f);
        sp = SpUtil.getInstance();

    }

    @Override
    public void initOther() {
    }


    @OnClick({R.id.rl_shen, R.id.rl_shi, R.id.rl_qu, R.id.rl_dian, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_shen:
                intent = new Intent(this, BaseAdressActivity.class);
                intent.putExtra("title", "省");
                startActivityForResult(intent, PROVINCE);
                break;
            case R.id.rl_shi:
                if (poistion != -1) {
                    intent = new Intent(this, BaseAdressActivity.class);
                    intent.putExtra("title", "市");
                    intent.putExtra("poistion", poistion);
                    startActivityForResult(intent, CITY);
                } else {
                    showToast("请选择相关省份");
                }
                break;
            case R.id.rl_qu:
                if (poistions != -1) {
                    intent = new Intent(this, BaseAdressActivity.class);
                    intent.putExtra("title", "县");
                    intent.putExtra("poistions", poistions);
                    intent.putExtra("poistion", poistion);
                    startActivityForResult(intent, AREA);
                } else {
                    showToast("请选择相关市");
                }
                break;
            case R.id.rl_dian:
                if (areaCode != 0) {
                    Intent intent = new Intent(BinderActivity.this, BaseAdressActivity.class);
                    intent.putExtra("title", "店铺");
                    intent.putExtra("areaCode", areaCode + "");
                    Log.e("areaCode---", areaCode + "");
                    startActivityForResult(intent, BUINS);
                } else {
                    showToast("请选择相关县/区");
                }
                break;
            case R.id.btn_next:
                if (tvDian.getText().length() == 0) {
                    showToast("请选择省／市／区");
                    break;
                }
                Intent intent = getIntent();
                intent.setClass(BinderActivity.this, BinderTabeActivity.class);
                intent.putExtra("businessId", businessId);
                startActivity(intent);
                sp.putBoolean("hasBinder", false);
                break;
        }
    }

    public void setTextNull(int textPosition) {
        switch (textPosition) {
            case 4:
                tvDian.setText("");
                break;
            case 2:
                tvShi.setText("");
                tvQu.setText("");
                tvDian.setText("");
                break;
            case 3:
                tvQu.setText("");
                tvDian.setText("");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            int position = data.getIntExtra("poistion", 0);
            int code = data.getIntExtra("code", 0);
            if (requestCode == PROVINCE) {
                this.poistion = position;
                provinceCode = code;
                poistions = -1;
                cityCode = 0;
                areaCode = 0;
                businessId = 0;
                tvShen.setText(name);
                this.poistion = position;
                provinceCode = code;
                setTextNull(2);
            } else if (requestCode == CITY) {
                this.poistions = position;
                tvShi.setText(name);
                cityCode = code;
                setTextNull(3);
            } else if (requestCode == AREA) {
                tvQu.setText(name);
                areaCode = code;
                setTextNull(4);
            } else {
                tvDian.setText(name);
                businessId = code;
                sp.putString("businessId",code+"");
            }
        }
    }

    @Override
    public void onBackClick(View v) {
//        finishAffinity();
        super.onBackClick(v);
    }

    @Override
    public <T> void toList(List<T> list, int type, int... refreshType) {

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
