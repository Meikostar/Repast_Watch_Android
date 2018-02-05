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
import com.canplay.repast_wear.mvp.model.Message;
import com.canplay.repast_wear.mvp.model.Table;
import com.canplay.repast_wear.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailAdapter extends BaseAdapter {

    private List<Message> tableList;
    private Context context;
    private LayoutInflater layoutInflater;
    private boolean isSelect = true;
    private boolean canSelect;
    private List<Long> tableIds = new ArrayList<>();


    public DetailAdapter(Context context) {

        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
public void setData(List<Message> tableList){
    this.tableList = tableList;
}
    @Override
    public int getCount() {
        return tableList==null?0:tableList.size();
    }

    @Override
    public Message getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.adapter_item_detail, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Message table = tableList.get(position);
        if(TextUtil.isNotEmpty(table.cnName)){
            holder.tvName.setText(table.cnName);
        }


        holder.toRight.setText(table.count+"");

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.to_right)
        TextView toRight;
        @BindView(R.id.rl_contain)
        RelativeLayout contain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    interface setContainClikeListener extends View.OnClickListener {

    }
}
