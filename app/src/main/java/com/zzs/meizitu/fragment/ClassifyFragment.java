package com.zzs.meizitu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzs.meizitu.R;
import com.zzs.meizitu.activity.ClassifyDetailActivity;
import com.zzs.meizitu.adapter.ClassifyAdapter;

import java.util.ArrayList;


/**
 * @author zzstar
 * @data 2018/2/6
 */

public class ClassifyFragment extends Fragment {
    private RecyclerView rvImageClassify;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_classify, null);
        rvImageClassify = view.findViewById(R.id.rvImageClassify);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Integer> imgs = new ArrayList<>();
        final String[] titles = {"湿身诱惑", "性感酥胸", "邻家小妹", "野性妖娆", "丝袜美腿", "香车美人"};
        imgs.add(R.drawable.class0);
        imgs.add(R.drawable.class1);
        imgs.add(R.drawable.class2);
        imgs.add(R.drawable.class3);
        imgs.add(R.drawable.class4);
        imgs.add(R.drawable.class5);
        rvImageClassify.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ClassifyAdapter classifyAdapter = new ClassifyAdapter(getActivity(), imgs);
        rvImageClassify.setAdapter(classifyAdapter);
        classifyAdapter.setOnItemClickListener(new ClassifyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                // Toast.makeText(getContext(), "钱不够演员未定剧本暂无", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), ClassifyDetailActivity.class);
                intent.putExtra("title", titles[pos]);
                getActivity().startActivity(intent);
            }
        });
    }
}
