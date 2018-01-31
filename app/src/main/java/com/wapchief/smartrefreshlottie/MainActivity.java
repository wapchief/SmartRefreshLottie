package com.wapchief.smartrefreshlottie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.header.DropboxHeader;
import com.scwang.smartrefresh.header.FunGameBattleCityHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wapchief
 * @date 2018/1/30
 */
public class MainActivity extends AppCompatActivity implements
        Toolbar.OnMenuItemClickListener,OnRefreshListener{

    Toolbar mToolbar;
    RecyclerView mRecycler;
    SmartRefreshLayout mRefresh;
    MyAdapter mAdapter;
    /**自定义header*/
    MyRefreshLottieHeader mRefreshLottieHeader;
    MyRefreshAnimHeader mRefreshAnimHeader;
    /**Smart库中的刷新样式*/
    FunGameBattleCityHeader mBattleCityHeader;
    ClassicsHeader mClassicsHeader;
    DropboxHeader mDropboxHeader;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initToolBar();
        initAdapter();
    }

    private void initAdapter() {
        //布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(this);
        //分割线
        mRecycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mAdapter = new MyAdapter(getDatas());
        mRecycler.setLayoutManager(manager);
        mRecycler.setAdapter(mAdapter);

    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(this);
    }

    List<String> mStringList = new ArrayList<>();
    private void initView() {
        mToolbar = findViewById(R.id.main_toolbar);
        mRecycler = findViewById(R.id.main_rv);
        mRefresh = findViewById(R.id.main_refresh);

        //初始化header
        mRefreshLottieHeader = new MyRefreshLottieHeader(this);
        mBattleCityHeader = new FunGameBattleCityHeader(this);
        mClassicsHeader = new ClassicsHeader(this);
        mDropboxHeader = new DropboxHeader(this);
        mRefreshAnimHeader = new MyRefreshAnimHeader(this);
        setHeader(mRefreshLottieHeader);
        mRefresh.setOnRefreshListener(this);
    }

    /**
     * 设置刷新header风格
     * @param header
     */
    private void setHeader(RefreshHeader header) {
        if (mRefresh.isRefreshing()){
            mRefresh.finishRefresh();
        }
        mRefresh.setRefreshHeader(header);
    }

    /**
     * 数据源
     * @return Data
     */
    private int dataSize = 20;
    private List<String> getDatas() {
        for (int i = 0; i < dataSize; i++) {

            mStringList.add(i + "   item");
        }
        return mStringList;
    }

    private List<String> getDatas(String header) {
        mStringList.clear();
        for (int i = 0; i < dataSize; i++) {

            mStringList.add(i + "   item    " + header);
        }
        return mStringList;
    }


    /**
     * Menu菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**监听选项*/
    private String headerName = "";
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_1:
                setHeader(mRefreshLottieHeader);
                headerName = "mHeaderLottie";
                mRefresh.autoRefresh();
                break;
            case R.id.action_2:
                setHeader(mBattleCityHeader);
                headerName = "mBattleCityHeader";
                mRefresh.autoRefresh();
                break;
            case R.id.action_3:
                setHeader(mDropboxHeader);
                headerName = "mDropboxHeader";
                mRefresh.autoRefresh();
                break;
            case R.id.action_4:
                setHeader(mRefreshAnimHeader);
                headerName = "mHeaderAnim";
                mRefresh.autoRefresh();
                break;
                default:

                    break;
        }
        return true;
    }

    /**刷新事件*/
    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        refreshlayout.getLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.setNewList(getDatas(headerName));
                mRefresh.finishRefresh();
            }
        },2000);
    }



    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        List<String> mList = new ArrayList<>();

        public MyAdapter(List<String> list) {
            mList = list;

        }

        public void setNewList(List<String> list) {
            mList = list;
            notifyDataSetChanged();
        }

        public void addList(List<String> list) {
            mList.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(v);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder,final int position) {
            String s = mList.get(position);
            holder.mTextView.setText(s);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        //切换Lottie动画
                    Toast.makeText(MainActivity.this, "已切换Lottie动画"+(position+1), Toast.LENGTH_LONG).show();
                        switch (position) {
                            case 0:
                                mRefreshLottieHeader.setAnimationViewJson("anim1.json");
                                setHeader(mRefreshLottieHeader);
                                headerName = "mHeaderLottie";
                                mRefresh.autoRefresh();
                                break;
                            case 1:
                                mRefreshLottieHeader.setAnimationViewJson("anim2.json");
                                setHeader(mRefreshLottieHeader);
                                headerName = "mHeaderLottie";
                                mRefresh.autoRefresh();
                                break;
                            case 2:
                                mRefreshLottieHeader.setAnimationViewJson("anim3.json");
                                setHeader(mRefreshLottieHeader);
                                headerName = "mHeaderLottie";
                                mRefresh.autoRefresh();
                                break;
                            case 3:
                                mRefreshLottieHeader.setAnimationViewJson("anim4.json");
                                setHeader(mRefreshLottieHeader);
                                headerName = "mHeaderLottie";
                                mRefresh.autoRefresh();
                                break;
                                default:
                                    mRefreshLottieHeader.setAnimationViewJson("anim2.json");
                                    setHeader(mRefreshLottieHeader);
                                    headerName = "mHeaderLottie";
                                    mRefresh.autoRefresh();
                                    break;
                        }
                    }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView mTextView;
            public MyViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
