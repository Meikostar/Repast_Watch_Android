package com.canplay.repast_wear.mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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


public class HaveRespondFragment extends BaseFragment {
    @BindView(R.id.list_have_respond)
    ListView listHaveRespond;
    private Unbinder unbinder;
    private List<Message> messages = new ArrayList<>();
    private RespondAdapter adapter;

    public static HaveRespondFragment newInstance() {
        HaveRespondFragment fragment = new HaveRespondFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_have_respond, null);
        unbinder = ButterKnife.bind(this, view);
        inject();
        initView();
        initData();
        return view;
    }

    private void inject() {
        listHaveRespond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),BinderActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        adapter = new RespondAdapter(getActivity(), messages);
        adapter.setType(1);
        listHaveRespond.setAdapter(adapter);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            Message message = new Message();
            message.setNumber(i + "");
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
