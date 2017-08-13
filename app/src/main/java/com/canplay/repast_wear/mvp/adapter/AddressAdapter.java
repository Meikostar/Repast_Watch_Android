package com.canplay.repast_wear.mvp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.mvp.model.AREA;
import com.canplay.repast_wear.mvp.model.CITY;
import com.canplay.repast_wear.mvp.model.CONTURY;
import com.canplay.repast_wear.mvp.model.PROVINCE;

import java.util.List;


public class AddressAdapter extends BaseAdapter {
    private Context mContext;
    private List<CONTURY> mCities;
    private int type=-1;//
    private int status=0;//
    private int id=0;//
    public List<PROVINCE> list;
    public List<CITY> city;
    public List<AREA> states;
    public ItemCliks cliks;
    private String name;
    public AddressAdapter(Context mContext, int status) {
        this.status=status;
        this.mContext = mContext;
    }
    public void getItems(ItemCliks cliks){
        this.cliks=cliks;
    }
    public interface ItemCliks{
        void getItem(int poistion, String name, int id);
    }
    public void setData(List<PROVINCE> list, List<CITY> city, List<AREA> states){
        if(this.city!=null){
            this.city.clear();
        }
        if(this.states!=null){
            this.states.clear();
        }
        if(this.list!=null){
            this.list.clear();
        }
        this.city=city;
        this.list=list;
        this.states=states;
        if(list!=null){
            type=0;
        }else if(city!=null){
            type=1;
        }else {
            type=2;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return type!=-1?(type==0?(list.size()):(type==1?city.size():states.size())):0;
    }

    @Override
    public Object getItem(int position) {
        return mCities == null ? null : mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ResultViewHolder holder;
        if (view == null){
            holder = new ResultViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.address_item_view, parent, false);
            holder.name= (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        }else{
            holder = (ResultViewHolder) view.getTag();
        }
        if(type==0){
            if(status!=0){
                name=list.get(position).businessName;
            }else{
                name=list.get(position).getProvinceName();
            }


        }else if(type==1){
            name=city.get(position).getCityName();
        }else {
            name=states.get(position).getAreaName();

        }
        holder.name.setText(name);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==0){
                    name=list.get(position).provinceName;
                    id=list.get(position).provinceCode;

                }else if(type==1){
                    name=city.get(position).cityName;
                    id=city.get(position).cityCode;
                }else {
                    name=states.get(position).areaName;
                    id=states.get(position).areaCode;

                }
                cliks.getItem(position,name,id);
            }
        });
        return view;


    }

    public static class ResultViewHolder{

        TextView name;

    }
}
