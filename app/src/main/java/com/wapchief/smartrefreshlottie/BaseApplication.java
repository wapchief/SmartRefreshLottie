package com.wapchief.smartrefreshlottie;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

/**
 * Created by wapchief on 2018/1/29.
 */

public class BaseApplication extends Application{
    public static BaseApplication mContent;
    @Override
    public void onCreate() {
        super.onCreate();
        mContent = this;

    }

    /*设置全局的下拉刷新样式*/
    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout refreshLayout) {
                return new MyRefreshLottieHeader(mContent);
            }
        });
    }

}
