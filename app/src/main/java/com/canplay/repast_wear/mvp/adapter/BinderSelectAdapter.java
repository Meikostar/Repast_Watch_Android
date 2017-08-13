package com.canplay.repast_wear.mvp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canplay.repast_wear.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BinderSelectAdapter extends BaseAdapter {

    private List<Map<String,Object>> objects;
    private Context context;
    private LayoutInflater layoutInflater;
    private boolean isSelect;
    private boolean canSelect;


    public BinderSelectAdapter(Context context,List<Map<String,Object>> objects) {
        this.objects=objects;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Map<String,Object> getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_item_table, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Map<String, Object> map = objects.get(position);
        holder.tvName.setText(String.valueOf(map.get("tableNumber")));
        final int type = (int) map.get("type");
        if(type == 3) {
            holder.toRight.setButtonDrawable(context.getResources().getDrawable(R.mipmap.no_select));
            holder.toRight.setEnabled(false);
        }
        else if(type == 1)
            holder.toRight.setChecked(true);
        holder.contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 3){
                    return;
                }
                map.remove("type");
                if(holder.toRight.isChecked()){
                    map.put("type",0);
                    holder.toRight.setChecked(false);
                }else {
                    map.put("type",1);
                    holder.toRight.setChecked(true);
                }
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
    interface setContainClikeListener extends View.OnClickListener{

    }
}
