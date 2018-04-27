package com.zzs.meizitu;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.zzs.meizitu.utils.SpUtils;

/**
 * @author zzstar
 * @data 2018/1/25
 */

public class MeiZiTuApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogAdapter(new AndroidLogAdapter());
        SpUtils.init(this);

    }


}
