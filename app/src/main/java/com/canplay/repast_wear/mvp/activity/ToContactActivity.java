package com.canplay.repast_wear.mvp.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.canplay.repast_wear.R;
import com.canplay.repast_wear.base.BaseActivity;
import com.canplay.repast_wear.mvp.model.Contact;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToContactActivity extends BaseActivity {
    @BindView(R.id.list_to_other)
    GridView listToOther;
    private List<Contact> nameList = new ArrayList<>();
    private ListAdapter adapter;
    private TextView name;
    @Override
    public void initInjector() {

        initUI(R.layout.activity_change);
        ButterKnife.bind(this);
    }

    @Override
    public void initCustomerUI() {
        adapter = new ArrayAdapter(this, R.layout.adapter_select_man, R.id.tv_name, nameList) {


            @Override
            public View getView(int position,
                                View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                name = (TextView) view.findViewById(R.id.tv_name);
                name.setText(nameList.get(position).getName());
                return view;
            }
        };
        for (int i = 0; i < 10; i++) {
            Contact contact=new Contact();
            contact.setName("服务员"+i);
            nameList.add(contact);
        }
        listToOther.setAdapter(adapter);
    }

    @Override
    public void initOther() {
        listToOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                name.setBackground(getResources().getDrawable(R.drawable.org_cycle));
                showToast("转移成功");
                finish();
//                Intent intent=new Intent(ToContactActivity.this,RespondActivity.class);
//                startActivity(intent);
            }
        });
    }
}
