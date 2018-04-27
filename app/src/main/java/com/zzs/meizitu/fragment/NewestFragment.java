package com.zzs.meizitu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zzs.meizitu.Contances;
import com.zzs.meizitu.R;
import com.zzs.meizitu.activity.SrcChildActivity;
import com.zzs.meizitu.adapter.CoverAdapter;
import com.zzs.meizitu.bean.CoverBean;
import com.zzs.meizitu.view.GridSpacingItemDecoration;

/**
 * @author zzstar
 * @data 2018/2/6
 */

public class NewestFragment extends Fragment {
    private RecyclerView rvImage;
    private int LOADING = 0;//1加载中，0没加载
    private CoverAdapter coverAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int ITEMCOUNT = 30;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_newest, null);
        rvImage = view.findViewById(R.id.rvImage);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(0xffff00ff, 0xff6699FF, 0xff99cc);
        rvImage.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    private void initData() {

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
        coverAdapter = new CoverAdapter(coverBean.getData(), getContext());
        rvImage.addItemDecoration(new GridSpacingItemDecoration(2, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
        rvImage.setAdapter(coverAdapter);
        coverAdapter.setOnRVitemClickListener(new CoverAdapter.OnRVitemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(getContext(), SrcChildActivity.class);
                intent.putExtra("childId", coverBean.getData().get(pos).getSrcid());
                startActivity(intent);

            }
        });
        final int[] lastPositions = new int[2];

        rvImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
