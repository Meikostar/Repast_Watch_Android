package com.canplay.repast_wear.mvp.activity;

import android.content.Intent;
import android.widget.ListView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseActivity;
import com.canplay.repast_wear.base.BaseApplication;
import com.canplay.repast_wear.mvp.adapter.AddressAdapter;
import com.canplay.repast_wear.mvp.component.DaggerBaseComponent;
import com.canplay.repast_wear.mvp.model.AREA;
import com.canplay.repast_wear.mvp.model.CITY;
import com.canplay.repast_wear.mvp.model.PROVINCE;
import com.canplay.repast_wear.mvp.present.TableContract;
import com.canplay.repast_wear.mvp.present.TablePresenter;
import com.canplay.repast_wear.util.TextUtil;
import com.canplay.repast_wear.view.NavigationBar;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BaseAdressActivity extends BaseActivity implements TableContract.View,NavigationBar.NavigationBarListener{

    @Inject
    TablePresenter tablePresenter;
    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.list)
    ListView list;
    private AddressAdapter adapter;
    private String title = "";
    private String  areaCode ;
    private int poistion;//选择是哪个省
    private int poistions;//选择是哪个市

    private boolean haveCity = true;
    private List<CITY> city;
    private List<PROVINCE> plist;
    private List<AREA> area;
    private PROVINCE prov;
    private String privoce;
    private int status = 0;
    @Override
    public void initInjector() {
        DaggerBaseComponent.builder().appComponent(((BaseApplication) getApplication()).getAppComponent()).build().inject(this);
        tablePresenter.attachView(this);
    }

    @Override
    public void initCustomerUI() {
        setContentView(R.layout.activity_base_adress);
        ButterKnife.bind(this);
        navigationBar.setNavigationBarListener(this);
        if (getIntent() != null) {
            areaCode = getIntent().getStringExtra("areaCode");
            title = getIntent().getStringExtra("title");
            poistion = getIntent().getIntExtra("poistion", 0);
            poistions = getIntent().getIntExtra("poistions", 0);
        }

        if(title.equals("省")){
            navigationBar.setNaviTitle("省/市");
        }else if(title.equals("市")){
            navigationBar.setNaviTitle("市/区");
        }else if(title.equals("县")) {
            navigationBar.setNaviTitle("县/区");
        }else {
            navigationBar.setNaviTitle(title);
            tablePresenter.getBusinessNameList(areaCode);
        }
        if(TextUtil.isNotEmpty(areaCode)){
            adapter = new AddressAdapter(this, 1);//城市适配器
        }else {
            adapter = new AddressAdapter(this, 0);//店铺适配器
        }
        list.setAdapter(adapter);
    }

    @Override
    public void initOther() {
        if(TextUtil.isNotEmpty(areaCode)){

        }else{
            Observable.create(new Observable.OnSubscribe<JSONObject>() {

                @Override
                public void call(Subscriber<? super JSONObject> subscriber) {
                    JSONObject json = TextUtil.getJson("city.json",BaseAdressActivity.this);
                    subscriber.onNext(json);
                    subscriber.onCompleted();//结束异步任务

                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JSONObject>() {
                        @Override
                        public void call(JSONObject json) {
                            if (title.equals("省") ) {

                                JSONObject dataJson = json.optJSONObject("data");
                                prov = new Gson().fromJson(dataJson.toString(), PROVINCE.class);
                                adapter.setData(prov.getProvinceCityList(), null, null);
                            } else if (title.equals("市")) {
                                JSONObject dataJson = json.optJSONObject("data");
                                prov = new Gson().fromJson(dataJson.toString(), PROVINCE.class);
                                if (prov.getProvinceCityList().get(poistion).getCityList() != null) {
                                    haveCity = true;
                                    city = prov.getProvinceCityList().get(poistion).getCityList();
                                    adapter.setData(null, city, null);
                                }

                            }else {
                                JSONObject dataJson = json.optJSONObject("data");
                                prov = new Gson().fromJson(dataJson.toString(), PROVINCE.class);
                                if (prov.getProvinceCityList().get(poistion).getCityList().get(poistions).getAreaList()!= null) {
                                    haveCity = true;
                                    area = prov.getProvinceCityList().get(poistion).getCityList().get(poistions).areaList;
                                    adapter.setData(null, null, area);
                                }
                            }
                        }
                    });
        }
        adapter.getItems(new AddressAdapter.ItemCliks() {
            @Override
            public void getItem(int poistions, String name, int id) {
                if (title.equals("省") ) {
                    setDatas(poistions,prov.getProvinceCityList().get(poistions).provinceName,prov.getProvinceCityList().get(poistions).provinceCode);
                } else if(title.equals("市")) {
                    setDatas(poistions,city.get(poistions).cityName,city.get(poistions).cityCode);
                }else if(title.equals("县")) {
                    setDatas(poistions,area.get(poistions).areaName,area.get(poistions).areaCode);
                }else {
                    setDatas(poistions,plist.get(poistions).businessName,plist.get(poistions).businessId);
                }
            }
        });
    }
    public void setDatas(int poistion, String name, int code) {
        Intent intent = new Intent();
        intent.putExtra("poistion", poistion);
        intent.putExtra("name", name);
        intent.putExtra("code", code);
        setResult(RESULT_OK, intent);
        finish();
    }
    @Override
    public <T> void toList(List<T> list, int type, int... refreshType) {
        plist= (List<PROVINCE>) list;
        if(plist.size() == 0){
            showToast("该区域不存在店铺，请重新选择地区");
        }
        adapter.setData(plist,null,null);
    }

    @Override
    public <T> void toEntity(T entity) {
//        Log.e("toEntity",entity.toString());
//        plist= (List<PROVINCE>) entity;
//        if(plist.size() == 0){
//            showToast("该区域不存在店铺，请重新选择地区");
//        }
//        adapter.setData(plist,null,null);

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {}

    @Override
    public void navigationLeft() {
        this.finish();
    }

    @Override
    public void navigationRight() {

    }

    @Override
    public void navigationimg() {

    }
}
