package com.zzs.meizitu.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zzs.meizitu.R;
import com.zzs.meizitu.adapter.CoverAdapter;
import com.zzs.meizitu.bean.CoverBean;
import com.zzs.meizitu.db.DatabaseHelper;
import com.zzs.meizitu.view.GridSpacingItemDecoration;

import java.util.ArrayList;

public class CollectionActivity extends Activity {
    private RecyclerView rvCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rvCollection = findViewById(R.id.rvCollection);
        rvCollection.setLayoutManager(new GridLayoutManager(this, 2));
        rvCollection.addItemDecoration(new GridSpacingItemDecoration(2, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase readableDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from collections", null);
        final ArrayList<CoverBean.DataBean> dataBeans = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0); //获取第一列的值,第一列的索引从0开始
            String srctitle = cursor.getString(1);//获取第二列的值
            String coversrc = cursor.getString(2);//获取第二列的值
            int srcid = cursor.getInt(3);//获取第二列的值
            CoverBean.DataBean dataBean = new CoverBean.DataBean();
            dataBean.setChecked(true);
            dataBean.setId(id);
            dataBean.setSrctitle(srctitle);
            dataBean.setCoversrc(coversrc);
            dataBean.setSrcid(srcid);
            dataBeans.add(dataBean);
        }
        cursor.close();
        databaseHelper.close();
        CoverAdapter coverAdapter = new CoverAdapter(dataBeans, this);
        rvCollection.setAdapter(coverAdapter);
        coverAdapter.setOnRVitemClickListener(new CoverAdapter.OnRVitemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(CollectionActivity.this, SrcChildActivity.class);
                intent.putExtra("childId", dataBeans.get(pos).getSrcid());
                startActivity(intent);
            }
        });


    }
}
