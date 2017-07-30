package com.canplay.repast_wear.mvp.component;



import com.canplay.repast_wear.base.AppComponent;
import com.canplay.repast_wear.mvp.ActivityScope;

import dagger.Component;

/**
 * Created by leo on 2016/9/27.
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface BaseComponent{

}
