package com.zzs.meizitu.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.zzs.meizitu.Contances;
import com.zzs.meizitu.R;
import com.zzs.meizitu.adapter.ViewPagerAdapter;
import com.zzs.meizitu.bean.CheckBean;
import com.zzs.meizitu.fragment.ClassifyFragment;
import com.zzs.meizitu.fragment.NewestFragment;
import com.zzs.meizitu.utils.APKVersionCodeUtils;
import com.zzs.meizitu.utils.SpUtils;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tab;
    private ViewPager home_viewpager;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.INTERNET}, 0);
            }
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        home_viewpager = findViewById(R.id.home_viewpager);
        NavigationView nav_view = findViewById(R.id.nav_view);
        tab = findViewById(R.id.tab);
        nav_view.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        initData();
    }

    private void initData() {

        NewestFragment newestFragment = new NewestFragment();
        ClassifyFragment classifyFragment = new ClassifyFragment();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<Fragment> mFragments = new ArrayList<>();
        mFragments.add(newestFragment);
        mFragments.add(classifyFragment);
        titles.add("最新");
        titles.add("分类");
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments, titles);
        home_viewpager.setAdapter(viewPagerAdapter);
        tab.setupWithViewPager(home_viewpager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navigation_item_collection) {
            startActivity(new Intent(this, CollectionActivity.class));
        } else if (id == R.id.navigation_item_focus) {
            getVersion();
        } else if (id == R.id.navigation_item_clear_cache) {
            SpUtils.clear();
            Snackbar sb = Snackbar.make(drawer, "缓存已经清除", Snackbar.LENGTH_SHORT);
            View view = sb.getView();
            view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(0xffffffff);
            sb.show();
        } else if (id == R.id.navigation_item_about) {
            showAboutDialog();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View
                .inflate(this, R.layout.about_dialog, null);
        builder.setView(view);
        TextView tvVersionType = view.findViewById(R.id.tvVersionType);
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager()
                    .getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //获取APP版本versionName
        String versionName = packageInfo.versionName;
        tvVersionType.setText("版本:" + versionName);
        builder.setCancelable(true);
        builder.create().show();

    }

    private void getVersion() {

        OkGo.<String>get(Contances.check).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                paraeJson(response.body());
            }
        });


    }

    private void paraeJson(String json) {
        Gson gson = new Gson();
        CheckBean checkBean = gson.fromJson(json, CheckBean.class);
        int versionCodeNow = APKVersionCodeUtils.getVersionCode(this);
        int versionCodeNewest = checkBean.getData().get(0).getVersioncode();
        int versionCodeIgnore = SpUtils.getInt("versionCode");
        if (versionCodeNow < versionCodeNewest && versionCodeNewest != versionCodeIgnore) {
            showUpdataDialog(checkBean);
        } else {
            Snackbar sb = Snackbar.make(drawer, "已经是最新版", Snackbar.LENGTH_SHORT);
            View view = sb.getView();
            view.setBackgroundColor(getResources().getColor(R.color.colorAccent));//修改view的背景色
            ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(0xffffffff);//获取Snackbar的message控件，修改字体颜色
            sb.show();

        }
    }

    protected void showUpdataDialog(final CheckBean checkBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View
                .inflate(this, R.layout.update_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView tvVersionType = view.findViewById(R.id.tvVersionType);
        TextView tvFileSize = view.findViewById(R.id.tvFileSize);
        TextView tvUpdateContent = view.findViewById(R.id.tvUpdateContent);
        final CheckBox cbIgnoreUpdate = view.findViewById(R.id.cbIgnoreUpdate);
        TextView tvTalkLater = view.findViewById(R.id.tvTalkLater);
        TextView tvUpdateNow = view.findViewById(R.id.tvUpdateNow);
        tvVersionType.setText("版本:" + checkBean.getData().get(0).getVersions());
        tvFileSize.setText("更新包体积:" + checkBean.getData().get(0).getVolume());
        tvUpdateContent.setText(checkBean.getData().get(0).getContent().replace("\\n","\n"));
        final AlertDialog dialog = builder.create();
        dialog.show();
        tvTalkLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbIgnoreUpdate.isChecked()) {
                    SpUtils.putInt("versionCode", checkBean.getData().get(0).getVersioncode());
                }
                dialog.dismiss();
            }
        });
        tvUpdateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                downLoadApk();
            }
        });


    }

    protected void downLoadApk() {
        //进度条
        final ProgressDialog pd;
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        OkGo.<File>get(Contances.down).execute(new FileCallback() {
            @Override
            public void onSuccess(Response<File> response) {
                pd.dismiss();
                installApk(response.body());

            }

            @Override
            public void downloadProgress(Progress progress) {
                pd.setProgress((int) progress.fraction * 100);
            }
        });
    }

    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
