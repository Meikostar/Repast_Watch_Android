package com.canplay.repast_wear.mvp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.mvp.model.Message;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qi_fu on 2017/7/27.
 */

public class MessageAdapter extends BaseAdapter {
    private List<Message> messages;
    private LayoutInflater inflater;
    private Context context;
    private int type;

    public MessageAdapter(Context context, List<Message> messages) {
        this.inflater = ((Activity) context).getLayoutInflater();
        this.messages = messages;
        this.context = context;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (type == 1) {
            return getHaveRespondView(position, convertView, parent);
        } else {
            return getNoRespondView(position, convertView, parent);
        }
    }

    private View getNoRespondView(int position, View convertView, ViewGroup parent) {
        noRespondHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.watch_adapter_item_will, parent, false);
            holder = new noRespondHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (noRespondHolder) convertView.getTag();
        }
        holder.tvName.setText(messages.get(position).getTableFrom());
        holder.clockTime.setText("一分钟");
        return convertView;
    }

    public View getHaveRespondView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.watch_adapter_item_have, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(messages.get(position).getContent());
        return convertView;
    }
    static class ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class noRespondHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.clock_time)
        TextView clockTime;
        @BindView(R.id.to_right)
        ImageView toRight;

        noRespondHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
