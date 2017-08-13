package com.canplay.repast_wear.mvp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseFragment;
import com.canplay.repast_wear.mvp.adapter.RespondAdapter;
import com.canplay.repast_wear.mvp.model.Message;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NoRespondFragment extends BaseFragment {
    @BindView(R.id.list_no_respond)
    ListView listNoRespond;
    private Unbinder unbinder;
    private List<Message> messages = new ArrayList<>();
    private RespondAdapter adapter;

    public static NoRespondFragment newInstance() {
        NoRespondFragment fragment = new NoRespondFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_norespond, null);
        unbinder=ButterKnife.bind(this, view);
        inject();
        initView();
        initData();
        return view;
    }

    private void inject() {

    }

    private void initView() {
        adapter = new RespondAdapter(getActivity(), messages);
        adapter.setType(2);
        listNoRespond.setAdapter(adapter);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            Message message = new Message();
            message.setTableFrom(i + "号桌");
            message.setNumber(i+"");
            message.setContent("转移给" + i);
            messages.add(message);
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
