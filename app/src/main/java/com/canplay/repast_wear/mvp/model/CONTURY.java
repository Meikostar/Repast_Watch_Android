package com.canplay.repast_wear.mvp.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mykar on 17/3/17.
 */
public class CONTURY implements Serializable {
    public String id;

    public int type;

    public String name;

    public String provinceid;
    public String first_letter;


    public String full_name;//存放完整地址 例：吉林省-长春市-市辖区

    public String full_id;//存放完整id，省id-市id-区id

    public ArrayList<CONTURY> child_list = new ArrayList<CONTURY>();
    public String citySelectId;

    public CONTURY() {
        super();
    }

    public static CONTURY formJsons(JSONObject jo) {
        CONTURY area = new CONTURY();
        area.id = jo.optString("id");
        area.name = jo.optString("name");
        area.type = jo.optInt("type");
        area.first_letter = jo.optString("first_letter");

        JSONArray array_next = jo.optJSONArray("child_list");
        if (array_next != null && array_next.length() > 0) {
            for (int i = 0; i < array_next.length(); i++) {
                area.child_list.add(CONTURY.formJsons(array_next.optJSONObject(i)));
            }
        }
        return area;

    }

    @Override
    public boolean equals (Object o){
        if (o instanceof CONTURY) {
            CONTURY item = (CONTURY) o;
            return item.full_name.equals(full_name);
        }

        return false;
    }
}
