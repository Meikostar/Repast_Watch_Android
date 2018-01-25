package com.canplay.repast_wear.mvp.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.SwipmenuListView.SwipeListLayout;
import com.canplay.repast_wear.mvp.model.Message;
import com.canplay.repast_wear.util.DateUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RespondAdapter extends BaseAdapter {

    private List<Message> messageList;
    private Context context;
    private LayoutInflater layoutInflater;
    private int type;
    private CountDownTimer removetimer;
    private ImageViewClickListener clickListener;
    private Set<SwipeListLayout> sets = new HashSet();
    public void setDatas( List<Message> messageList){
        this.messageList = messageList;
    }
    public RespondAdapter(Context context, List<Message> messageList, ListView lv_content) {
        this.messageList = messageList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        lv_content.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (sets.size() > 0) {
                            for (SwipeListLayout s : sets) {
                                s.setStatus(SwipeListLayout.Status.Close, true);
                                sets.remove(s);
                            }
                        }
                        break;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
    }

    public void setClickListener(ImageViewClickListener clickListener) {
        this.clickListener = clickListener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_item_respond, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else holder =(ViewHolder) convertView.getTag();
        final Message message = messageList.get(position);
        holder.tvContext.setText(message.getContent());
        holder.tableNumber.setText(message.getTableNo());
        holder.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.delete(message,1,position);
            }
        });
        final String timeDistance = DateUtil.getTimeDistance(DateUtil.getTimeLong(), message.getTime());


        holder.ll_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickListener != null){
                    clickListener.ImageClicks(position);
                }
            }
        });
        holder.ll_bg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if(clickListener != null){
                    clickListener.ImageClick(position);
                }
                return true;
            }
        });
//        holder.finished.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(clickListener != null){
//                    clickListener.ImageClick(position);
//                }
//            }
//        });
        return convertView;
    }
    public interface selectItemListener{
        void delete(Message message, int type, int poistion);
    }
    public void setDeletListener(selectItemListener listener){
        this.listener=listener;
    }
    private selectItemListener listener;
    protected class ViewHolder {
        private TextView tvContext;

        private TextView complete;
        private LinearLayout ll_bg;

        private TextView tvTime;
        private TextView tableNumber;

        public ViewHolder(View view) {
            tvContext = (TextView) view.findViewById(R.id.tv_context);
            complete = (TextView) view.findViewById(R.id.complain);

            ll_bg = (LinearLayout) view.findViewById(R.id.ll_bg);

            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tableNumber = (TextView) view.findViewById(R.id.table_number);
        }
    }

    public interface ImageViewClickListener {
        void ImageClick(int position);
        void ImageClicks(int position);
    }
    class MyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public MyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        sets.remove(s);
                    }
                }
                sets.add(slipListLayout);
            } else {
                if (sets.contains(slipListLayout))
                    sets.remove(slipListLayout);
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }

    }
}

