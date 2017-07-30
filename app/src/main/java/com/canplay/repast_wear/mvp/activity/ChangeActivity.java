package com.canplay.repast_wear.mvp.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeActivity extends BaseActivity {
    @BindView(R.id.list_to_other)
    ListView listToOther;
    private List<String> nameList = new ArrayList<>();
    private ListAdapter adapter;

    @Override
    public void initInjector() {
        initUI(R.layout.activity_change);
        ButterKnife.bind(this);
    }

    @Override
    public void initCustomerUI() {
        adapter = new ArrayAdapter(this, R.layout.watch_adapter_item, R.id.tv_name, nameList) {
            @Override
            public View getView(int position,
                                View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(R.id.tv_name);
                text.setText(nameList.get(position));
                return view;
            }
        };
        for (int i = 0; i < 10; i++) {
            nameList.add("服务员"+i);
        }
        listToOther.setAdapter(adapter);
    }

    @Override
    public void initOther() {
        listToOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(ChangeActivity.this,RespondActivity.class);
                startActivity(intent);
            }
        });
    }
}
