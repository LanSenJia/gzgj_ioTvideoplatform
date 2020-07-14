package com.example.gzgj_iotvideoplatform;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.gzgj_iotvideoplatform.Application.APP;
import com.example.gzgj_iotvideoplatform.ui.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.toast.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.videogo.exception.BaseException;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
      //底部导航栏
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }



    /**
     * 通过调用服务接口判断AppKey和AccessToken且有效
     * @return 是否依旧有效
     */
    private boolean checkAppKeyAndAccessToken() {
        boolean isValid = false;
        try {
            APP.getOpenSDK().getDeviceList(0, 1);
            isValid = true;
        } catch (BaseException e) {
            e.printStackTrace();
            Log.e("Main", "Error code is " + e.getErrorCode());
            int errCode = e.getErrorCode();
            String errMsg;
            switch (errCode){
                case 400031:
                    errMsg = getApplicationContext().getString(R.string.tip_of_bad_net);
                    break;
                default:
                    errMsg = getApplicationContext().getString(R.string.login_expire);
                    break;
            }
            ToastUtils.show(errMsg);
        }
        return isValid;
    }



}