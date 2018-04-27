package com.zzs.meizitu.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.zzs.meizitu.Contances;
import com.zzs.meizitu.R;
import com.zzs.meizitu.bean.CoverBean;
import com.zzs.meizitu.db.DatabaseHelper;

import java.util.List;


/**
 * @author zzstar
 * @data 2018/1/25
 */

public class CoverAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<CoverBean.DataBean> data;
    private final LazyHeaders.Builder referer;
    private DatabaseHelper databaseHelper;

    public CoverAdapter(List<CoverBean.DataBean> data, Context context) {
        this.context = context;
        this.data = data;
        databaseHelper = new DatabaseHelper(context);
        referer = new LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)")
                .addHeader("Referer", "http://i.meizitu.net");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cover, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).tvtitle.setText(data.get(position).getSrctitle());
        final String coversrc = data.get(position).getCoversrc();
        ((ViewHolder) holder).collection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    data.get(position).setChecked(true);
                    SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    //一一对应填充数据
                    values.put("id", data.get(position).getId());
                    values.put("srctitle", data.get(position).getSrctitle());
                    values.put("coversrc",Contances.qiniuimage+coversrc.substring(coversrc.lastIndexOf("/")));
                    values.put("srcid", data.get(position).getSrcid());
                    writableDatabase.replace("collections", null, values);
                } else {
                    data.get(position).setChecked(false);
                    SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
                    //删除的条件
                    String whereClause = "id=?";
                    //删除的条件参数
                    String[] whereArgs = {data.get(position).getId() + ""};
                    //执行删除
                    writableDatabase.delete("collections", whereClause, whereArgs);
                }

            }
        });

        GlideUrl glideUrl = new GlideUrl(Contances.qiniuimage+coversrc.substring(coversrc.lastIndexOf("/")), referer.build());
        Glide.with(context).load(glideUrl).into(((ViewHolder) holder).ivCover);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRVitemClickListener != null) {
                    onRVitemClickListener.onItemClick(position);

                }
            }
        });

        ((ViewHolder) holder).collection.setChecked(data.get(position).isChecked());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(List<CoverBean.DataBean> data) {
        this.data.addAll(data);
    }

    public void addHeadData(List<CoverBean.DataBean> data) {
        this.data.addAll(0, data);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCover;
        private TextView tvtitle;
        private CheckBox collection;

        public ViewHolder(View view) {
            super(view);
            ivCover = view.findViewById(R.id.ivCover);
            tvtitle = view.findViewById(R.id.tvtitle);
            collection = view.findViewById(R.id.collection);
        }
    }

    public interface OnRVitemClickListener {
        void onItemClick(int pos);
    }

    private OnRVitemClickListener onRVitemClickListener;

    //        提供一个方法
    public void setOnRVitemClickListener(OnRVitemClickListener onRVitemClickListener) {
        this.onRVitemClickListener = onRVitemClickListener;
    }
}
