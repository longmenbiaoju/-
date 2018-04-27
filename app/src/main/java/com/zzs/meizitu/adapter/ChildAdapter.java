package com.zzs.meizitu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.zzs.meizitu.Contances;
import com.zzs.meizitu.R;
import com.zzs.meizitu.bean.ChildBean;

import java.util.List;


/**
 * @author zzstar
 * @data 2018/1/25
 */

public class ChildAdapter extends RecyclerView.Adapter {
    private Context context;

    private final LazyHeaders.Builder referer;
    private final List<ChildBean.DataBean> data;


    public ChildAdapter(ChildBean childBean, Context context) {
        this.context = context;
        data = childBean.getData();
        referer = new LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)")
                .addHeader("Referer", "http://i.meizitu.net");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_child_cover, null);
        return new ChildAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        String srclist = data.get(position).getSrclist();
        GlideUrl glideUrl = new GlideUrl(Contances.qiniuimage+srclist.substring(srclist.lastIndexOf("/")), referer.build());
        Glide.with(context).load(glideUrl).into(((ChildAdapter.ViewHolder) holder).ivCover);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRVitemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCover;

        public ViewHolder(View view) {
            super(view);
            ivCover = view.findViewById(R.id.ivCover);
        }
    }

  public   interface OnRVitemClickListener {
        void onItemClick(int pos);
    }

    private ChildAdapter.OnRVitemClickListener onRVitemClickListener;


    public void setOnRVitemClickListener(ChildAdapter.OnRVitemClickListener onRVitemClickListener) {
        this.onRVitemClickListener = onRVitemClickListener;
    }
}
