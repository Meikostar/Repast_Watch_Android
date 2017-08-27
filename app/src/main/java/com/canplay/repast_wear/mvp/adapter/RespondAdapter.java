package com.canplay.repast_wear.mvp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.mvp.model.Message;
import com.canplay.repast_wear.util.DateUtil;

import java.util.List;

public class RespondAdapter extends BaseAdapter {

    private List<Message> messageList;
    private Context context;
    private LayoutInflater layoutInflater;
    private int type;

    public RespondAdapter(Context context, List<Message> messageList) {
        this.messageList = messageList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Message getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_item_respond, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else holder =(ViewHolder) convertView.getTag();
        Message message = messageList.get(position);
        holder.tvContext.setText(message.getContent());
        holder.tableNumber.setText(message.getTableNo());
        if (type == 1) {//已完成
            holder.tvTime.setText(DateUtil.getTimeDistance(DateUtil.getTimeLong(),message.getTime())+"前");
            holder.finished.setVisibility(View.VISIBLE);
            holder.imageNext.setVisibility(View.GONE);
        }else {
            holder.tvTime.setText(DateUtil.getTimeDistance(DateUtil.getTimeLong(),message.getTime())+"");
        }
        return convertView;
    }


    protected class ViewHolder {
        private TextView tvContext;
        private ImageView imageNext;
        private TextView finished;
        private TextView tvTime;
        private TextView tableNumber;

        public ViewHolder(View view) {
            tvContext = (TextView) view.findViewById(R.id.tv_context);
            imageNext = (ImageView) view.findViewById(R.id.image_next);
            finished = (TextView) view.findViewById(R.id.tv_finish);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tableNumber = (TextView) view.findViewById(R.id.table_number);
        }
    }
}

