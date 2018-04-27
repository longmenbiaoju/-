package com.zzs.meizitu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zzs.meizitu.R;

import java.util.ArrayList;

/**
 * @author zzstar
 * @data 2018/2/6
 */

public class ClassifyAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<Integer> imgs;

    public ClassifyAdapter(Context context, ArrayList<Integer> imgs) {
        this.context = context;
        this.imgs = imgs;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classify, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Glide.with(context).load(imgs.get(position)).into(((ViewHolder) holder).ivClassify);
        ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivClassify;

        public ViewHolder(View view) {
            super(view);
            ivClassify = view.findViewById(R.id.ivClassify);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    private ClassifyAdapter.OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(ClassifyAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
