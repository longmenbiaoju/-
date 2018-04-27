package com.zzs.meizitu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zzs.meizitu.Contances;
import com.zzs.meizitu.R;
import com.zzs.meizitu.adapter.CoverAdapter;
import com.zzs.meizitu.bean.CoverBean;
import com.zzs.meizitu.view.GridSpacingItemDecoration;

public class ClassifyDetailActivity extends Activity {

    private RecyclerView rvClassify;
    private CoverAdapter coverAdapter;
    private int ITEMCOUNT = 30;
    private int LOADING = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvClassifyTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        tvClassifyTitle = findViewById(R.id.tvClassifyTitle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rvClassify = findViewById(R.id.rvClassify);
        swipeRefreshLayout.setColorSchemeColors(0xffff00ff, 0xff6699FF, 0xff99cc);
        rvClassify.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        initData();
    }

    private void initData() {
        String title = getIntent().getStringExtra("title");
        tvClassifyTitle.setText(title);
        OkGo.<String>get(Contances.refresh).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                paraeJson(response.body());
            }
        });

    }

    private void paraeJson(String json) {
        Gson gson = new Gson();
        final CoverBean coverBean = gson.fromJson(json, CoverBean.class);
        coverAdapter = new CoverAdapter(coverBean.getData(), this);
        rvClassify.addItemDecoration(new GridSpacingItemDecoration(2, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
        rvClassify.setAdapter(coverAdapter);
        coverAdapter.setOnRVitemClickListener(new CoverAdapter.OnRVitemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(ClassifyDetailActivity.this, SrcChildActivity.class);
                intent.putExtra("childId", coverBean.getData().get(pos).getSrcid());
                startActivity(intent);

            }
        });
        final int[] lastPositions = new int[2];
        rvClassify.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = -1;
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                lastPosition = findMax(lastPositions);
                if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    if (LOADING == 0) {
                        LoadMoreFromNet(lastPosition);
                        LOADING = 1;
                    }
                }

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkGo.<String>get(Contances.refresh).execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        refreshListHead(response.body());

                    }
                });
            }
        });
    }

    //列表头部数据更新
    private void refreshListHead(String json) {
        swipeRefreshLayout.setRefreshing(false);
        Gson gson = new Gson();
        CoverBean coverBean = gson.fromJson(json, CoverBean.class);
        coverAdapter.addHeadData(coverBean.getData());
        coverAdapter.notifyItemRangeChanged(0, ITEMCOUNT);
    }

    //每次返回30条
    private void LoadMoreFromNet(final int lastPosition) {
        OkGo.<String>get(Contances.index + "page=" + ((lastPosition + 1) / ITEMCOUNT)).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LoadMoreParseJson(response.body(), lastPosition);
                LOADING = 0;
            }
        });

    }

    private void LoadMoreParseJson(String json, int lastPosition) {
        Gson gson = new Gson();
        CoverBean coverBean = gson.fromJson(json, CoverBean.class);
        coverAdapter.addData(coverBean.getData());
        coverAdapter.notifyItemRangeChanged(lastPosition + 1, ITEMCOUNT);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
