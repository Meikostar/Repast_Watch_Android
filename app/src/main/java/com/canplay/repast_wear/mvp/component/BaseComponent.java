package com.canplay.repast_wear.mvp.component;


import com.canplay.repast_wear.base.AppComponent;
import com.canplay.repast_wear.mvp.ActivityScope;
import com.canplay.repast_wear.mvp.activity.BaseAdressActivity;
import com.canplay.repast_wear.mvp.activity.BinderActivity;
import com.canplay.repast_wear.mvp.activity.BinderTabeActivity;
import com.canplay.repast_wear.mvp.activity.DetailActivity;
import com.canplay.repast_wear.mvp.activity.HaveRespondFragment;
import com.canplay.repast_wear.mvp.activity.MainActivity;
import com.canplay.repast_wear.mvp.activity.NoRespondFragment;
import com.canplay.repast_wear.mvp.activity.RespondActivity;
import com.canplay.repast_wear.mvp.activity.ToContactActivity;
import com.canplay.repast_wear.mvp.activity.orderFragment;

import dagger.Component;

/**
 * Created by leo on 2016/9/27.
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface BaseComponent{

    void inject(BinderActivity binderActivity);

    void inject(BinderTabeActivity binderTabeActivity);

    void inject(BaseAdressActivity baseAdressActivity);

    void inject(MainActivity mainActivity);

    void inject(ToContactActivity toContactActivity);

    void inject(RespondActivity respondActivity);

    void inject(NoRespondFragment noRespondFragment);

    void inject(HaveRespondFragment haveRespondFragment);
    void inject(orderFragment haveRespondFragment);
    void inject(DetailActivity haveRespondFragment);

}
