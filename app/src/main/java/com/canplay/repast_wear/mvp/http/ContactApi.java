package com.canplay.repast_wear.mvp.http;

import com.canplay.repast_wear.mvp.model.Contact;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;


public interface ContactApi {
    /**
     * 获得联系人
     * @param options
     * @return
     */
    @GET("")
    Observable<Contact> getContacts(@QueryMap Map<String, String> options);
}
