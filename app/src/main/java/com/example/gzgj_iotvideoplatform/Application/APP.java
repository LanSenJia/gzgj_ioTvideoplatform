package com.example.gzgj_iotvideoplatform.Application;

import android.app.Application;

import com.hjq.toast.ToastUtils;
import com.videogo.openapi.EZOpenSDK;

/**
 * @Create by lansen
 * @data： ：
 * Describe：application初始化类
 */
public class APP extends Application {
    private static APP context;
    //appkey
    private String APP_KEY = "8e29633e2e584dceab013edd56686179";
    //用到的时候需要生成
    private String ACCESSTOKEN = "at.1fgovme18sv4fp760ssb0in3cdkh89aq-6iikfat73z-04iqftb-mrhsh9kat";


    public static APP getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //sdk日志开关，正式发布需要去掉

        EZOpenSDK.showSDKLog(true);
        //设置是否支持P2P取流,详见api
        EZOpenSDK.enableP2P(false);
        //初始化APP_KEY
        EZOpenSDK.initLib(this, APP_KEY);
        //获取AccessToken
        EZOpenSDK.getInstance().setAccessToken(ACCESSTOKEN);

        ToastUtils.init(this);


    }

    public static EZOpenSDK getOpenSDK() {
        EZOpenSDK ezOpenSDK = EZOpenSDK.getInstance();
        return ezOpenSDK;
    }

}

