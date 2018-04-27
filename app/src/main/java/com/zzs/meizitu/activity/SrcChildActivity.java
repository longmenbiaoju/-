package com.zzs.meizitu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zzs.meizitu.Contances;
import com.zzs.meizitu.R;
import com.zzs.meizitu.adapter.ChildAdapter;
import com.zzs.meizitu.bean.ChildBean;
import com.zzs.meizitu.view.GridSpacingItemDecoration;

import java.util.ArrayList;

public class SrcChildActivity extends Activity {

    private RecyclerView rvImage;
    private ChildBean childBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_src_child);
        rvImage=findViewById(R.id.rvImage);
       rvImage.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        initData();
    }

    private void initData() {
        int childId = getIntent().getIntExtra("childId", -1);
        OkGo.<String>get(Contances.childsrc+"srcchildid="+childId).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                paraeJson(response.body());

            }
        });

    }

    private void paraeJson(String json) {
        Gson gson = new Gson();
        childBean = gson.fromJson(json, ChildBean.class);
        ChildAdapter childAdapter = new ChildAdapter(childBean,this);
        rvImage.addItemDecoration(new GridSpacingItemDecoration(2, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
        rvImage.setAdapter(childAdapter);
        childAdapter.setOnRVitemClickListener(new ChildAdapter.OnRVitemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(SrcChildActivity.this, PhotoBrowserActivity.class);
                String srclist = childBean.getData().get(pos).getSrclist();
                intent.putExtra("curImageUrl", Contances.qiniuimage+srclist.substring(srclist.lastIndexOf("/")));
                ArrayList<String> arrayList = new ArrayList<>();
                for (int i = 0; i < childBean.getData().size(); i++) {
                    String srclist1 = childBean.getData().get(i).getSrclist();
                    arrayList.add(Contances.qiniuimage+srclist1.substring(srclist1.lastIndexOf("/")));
                }
                intent.putStringArrayListExtra("imageUrls", arrayList);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();
    }
}
