package com.canplay.repast_wear.mvp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.mvp.model.Table;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderSelectAdapter extends BaseAdapter {

    private List<Table> tableList;
    private Context context;
    private LayoutInflater layoutInflater;
    private boolean isSelect = true;
    private boolean canSelect;
    private List<Long> tableIds = new ArrayList<>();


    public BinderSelectAdapter(Context context, List<Table> tableList) {
        this.tableList = tableList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public List<Long> getTableIds() {
        return tableIds;
    }

    @Override
    public int getCount() {
        return tableList.size();
    }

    @Override
    public Table getItem(int position) {
        return tableList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_item_table, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Table table = tableList.get(position);
        holder.tvName.setText(String.valueOf(table.getTableNo()));
        final long type = table.getBound();
        final long tableId = tableList.get(position).getTableId();
        holder.toRight.setClickable(false);
        if (type == 1) {
            tableIds.add(tableId);
            Log.e("tableIds自动   +  ",tableIds.toString());
            holder.toRight.setChecked(true);
        }
        if (type == 0) {
            holder.toRight.setChecked(false);
        }
        holder.contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.toRight.isChecked()){
                    table.setBound(0);
                    holder.toRight.setChecked(false);
                    if(tableIds.contains(tableId)){
                        tableIds.remove(tableId);
                        Log.e("点击了tableIds   -  ",tableIds.toString());
                    }
                }else {
                    table.setBound(1);
                    holder.toRight.setChecked(true);
                    if(!tableIds.contains(tableId)){
                        tableIds.add(tableId);
                        Log.e("点击了tableIds   +  ",tableIds.toString());
                    }
                }

//                if (type == 1) {
//                    if(isSelect){
//                        holder.toRight.setChecked(false);
//                        table.setBound(0);
//                        if(tableIds.contains(tableId)){
//                            tableIds.remove(tableId);
//                            Log.e("点击了tableIds   -  ",tableIds.toString());
//                        }
//                        isSelect=false;
//                    }else {
//                        holder.toRight.setChecked(true);
//                        table.setBound(1);
//                        if(!tableIds.contains(tableId)){
//                            tableIds.add(tableId);
//                            Log.e("点击了tableIds   +  ",tableIds.toString());
//                        }
//                        isSelect=true;
//                    }
//                }
//                if (type == 0) {
//                    if(!isSelect){
//                        holder.toRight.setChecked(true);
//                        table.setBound(1);
//                        if(!tableIds.contains(tableId)){
//                            tableIds.add(tableId);
//                            Log.e("点击了tableIds   +  ",tableIds.toString());
//                        }
//                        isSelect=true;
//                    }else {
//                        holder.toRight.setChecked(false);
//                        table.setBound(0);
//                        if(tableIds.contains(tableId)){
//                            tableIds.remove(tableId);
//                            Log.e("点击了tableIds   -  ",tableIds.toString());
//                        }
//                        isSelect=false;
//                    }
//                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.to_right)
        CheckBox toRight;
        @BindView(R.id.rl_contain)
        RelativeLayout contain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    interface setContainClikeListener extends View.OnClickListener {

    }
}
