# SmartRefreshLottie
SmartRefreshLayout下拉刷新+Lottie动画+自定义帧动画实现一键切换

下载APK：

[![Crates.io](https://img.shields.io/badge/downloads-APK-blue.svg)](https://fir.im/a6t5)

![](https://github.com/wapchief/SmartRefreshLottie/blob/master/screenshots/lottie.gif?raw=true)
![](https://github.com/wapchief/SmartRefreshLottie/blob/master/screenshots/refreshHeader.gif?raw=true)

[自定义LottieHeader](https://github.com/wapchief/SmartRefreshLottie/blob/master/app/src/main/java/com/wapchief/smartrefreshlottie/MyRefreshLottieHeader.java)

[自定义AnimHeader](https://github.com/wapchief/SmartRefreshLottie/blob/master/app/src/main/java/com/wapchief/smartrefreshlottie/MyRefreshAnimHeader.java)


##一、介绍

####SmartRefreshLayout
是目前使用比较广泛的一款下拉刷新和上拉加载库。实现起来非常方便，可以一键修改全局的刷新样式。而且该库已经提供了大量的刷新效果，其中包括默认的 SwipeRefresh 经典风格，以及一些高级的 比如游戏刷新、适用于聊天项目的上拉加载更多等等。
![](http://upload-images.jianshu.io/upload_images/2858691-23c181bc6cf5fe6a.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


####Lottie
是 Airbnb 开源的一款动画库，该库的优势是不需要程序员自己写 Anim 而是将这些工作交给设计师处理，最后拿到到只是一个 Json 文件，通过 Lottie 加载 Json 文件就能展示出动画效果。
[Lottie社区](https://www.lottiefiles.com/)已经提供了上千套刷新的 Json 文件，如果对定制要求不是太高，完全可以拿来用，小到点击效果，大到刷新，启动页等等。

![](http://upload-images.jianshu.io/upload_images/2858691-9de2ed8fce7121b7.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##二、基本使用

####SmartRefreshLayout

* 引入依赖
```
//默认提供三种加载效果
compile 'com.scwang.smartrefresh:SmartRefreshLayout:1.0.4-7'
//提供了多种现成的刷新Header库，可以直接用，如不需要可以不集成
compile 'com.scwang.smartrefresh:SmartRefreshHeader:1.0.4-7'
```
__注意：同时需要检查是否集成了 appcompat ，如果没集成，需要添加，版本跟随当前
 构建的版本号__

```
compile 'com.android.support:appcompat-v7:26.1.0'
```
* 布局实现

```
<com.scwang.smartrefresh.layout.SmartRefreshLayout
        android:id="@+id/main_refresh"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/main_rv"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>
    </com.scwang.smartrefresh.layout.SmartRefreshLayout>
```

* 刷新和加载
  如果需要更换刷新的 Header 只需调用``refreshLayout.setRefreshHeader(new MyRefreshAnimHeader);``
```
refreshLayout.setOnRefreshListener(new OnRefreshListener() {
    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if(refreshLayout.isLoading()){
              //关闭加载
              refreshLayout.finishLoadmore()
          }
    }
});
refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
         if(refreshLayout.isRefreshing()){
              //关闭刷新
              refreshLayout.finishRefresh();
          }
    }
});

```

* 全局加载
需要自定义 Application ，
```
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

```

####Lottie

* 引入依赖
```
compile 'com.airbnb.android:lottie:2.2.0'
```

* 布局 XML
动画 Json 文件需要放在 main 目录的 assets 中，如果没有该文件，就手动创建一个，再将动画文件拷贝进去，然后就可以在布局或者代码中直接使用。
```
<com.airbnb.lottie.LottieAnimationView
        android:id="@+id/loading_lottie"
        android:layout_width="wrap_content"
        android:layout_height="100dp"
        android:layout_gravity="center_horizontal"
        app:lottie_fileName="anim2.json"/>
```
* 动画开始、结束
动画的播放和结束都是只需要一行代码就可以完成。如果需要在代码中修改动画文件，只需要调用``mAnimationView.setAnimation(animName);``
```
//开始播放动画
mAnimationView.playAnimation();
//取消播放
mAnimationView.cancelAnimation();
```

## 三、结合Lottie使用

虽然 SmartRefreshLayout 已经提供十多种加载的样式，并且支持对样式的颜色等效果的基修改。
但是如果需要实现其它的加载动画效果，就需要自己来自定义 RefreshHeader。

最简单的方法是使用帧动画。但是还需要我们去拿到帧图片，然后再去设置。
这里我们已经引入了 Lottie ，发现其实可以利用 Lottie 实现随时一行代码切换上千种效果岂不是美哉。

![lottie动画切换](http://upload-images.jianshu.io/upload_images/2858691-3d8fffaaa2cb189b.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

* 自定义RefreshHeader
 必须要实现RefreshHeader类，然后针对提供的方法，来加入我们自定义的元素
```
//初始化View
 private void initView(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.loading_lottie, this);
        mAnimationView = (LottieAnimationView) view.findViewById(R.id.loading_lottie);

    }
//开始刷新
 @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {
        mAnimationView.playAnimation();
    }
//结束刷新
    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        mAnimationView.cancelAnimation();
        return 0;
    }

```

* 在自定义 SmartRefresh 中提供 Lottie 动画切换的方法

```
  public void setAnimationViewJson(String animName){
        mAnimationView.setAnimation(animName);
    }

    public void setAnimationViewJson(Animation anim){
        mAnimationView.setAnimation(anim);
    }
```

## 四、一行代码切换刷新动画

```
mRefreshLottieHeader.setAnimationViewJson("anim2.json");
```


------

> Demo地址：https://github.com/wapchief/SmartRefreshLottie
已经添加了自定义 Anim 帧动画的切换。[Demo下载](https://fir.im/a6t5)
>
> 关于 SmartRefreshLayout 更详细的使用请参考官方文档
https://github.com/scwang90/SmartRefreshLayout
>
> Lottie基本介绍：[一款非常好用的动画库Lottie](https://www.jianshu.com/p/86b1103db051)
>
> [Lottie社区](https://www.lottiefiles.com/) 可以自行下载动画文件
