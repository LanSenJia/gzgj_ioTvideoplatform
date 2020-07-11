package com.example.gzgj_iotvideoplatform.ui.VideoFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gzgj_iotvideoplatform.Application.APP;
import com.example.gzgj_iotvideoplatform.R;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZPlayer;
import com.videogo.realplay.RealPlayStatus;

import static android.content.ContentValues.TAG;
import static com.videogo.realplay.RealPlayMsg.MSG_REALPLAY_PLAY_SUCCESS;
import static com.videogo.realplay.RealPlayStatus.STATUS_INIT;

public class DashboardFragment extends Fragment implements SurfaceHolder.Callback, Handler.Callback, View.OnClickListener{

    private DashboardViewModel dashboardViewModel;
    private SurfaceView videoPalySV;
    private View screenshotBtn;
    private View videoStopBtn;
    private EZPlayer ezPlayer;
    private SurfaceHolder videoPalySVHolder;
    private int status = RealPlayStatus.STATUS_INIT;
    private Handler handler;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        return root;
    }

    private void initView(View view) {
        //绑定视频监控surfaceview控件
        videoPalySV = view.findViewById(R.id.video_play_sv);
        //截图按钮
        screenshotBtn = view.findViewById(R.id.video_screenshot_btn);
        //停止按钮
        videoStopBtn = view.findViewById(R.id.video_stop_btn);
    }

    //初始化数据
    private void initData(View view) {
        //萤石播放器初始化
        ezPlayer = new EZPlayer();
        //摄像头设定holder事件
        videoPalySVHolder = videoPalySV.getHolder();
        videoPalySVHolder.addCallback(this);
        handler = new Handler();
        screenshotBtn.setOnClickListener(this);
        videoStopBtn.setOnClickListener(this);

    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case MSG_REALPLAY_PLAY_SUCCESS:
                Log.i(TAG, "handleMessage:  调用成功");
                break;
            case EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL:
                //播放失败,得到失败信息
                ErrorInfo errorinfo = (ErrorInfo) msg.obj;
                //得到播放失败错误码
                int code = errorinfo.errorCode;
                //得到播放失败模块错误码
                String codeStr = errorinfo.moduleCode;
                //得到播放失败描述
                String description = errorinfo.description;
                //得到播放失败解决方方案
                description = errorinfo.sulution;
                break;
            case EZConstants.MSG_VIDEO_SIZE_CHANGED:
                //解析出视频画面分辨率回调
                try {
                    String temp = (String) msg.obj;
                    String[] strings = temp.split(":");
                    int mVideoWidth = Integer.parseInt(strings[0]);
                    int mVideoHeight = Integer.parseInt(strings[1]);
                    //解析出视频分辨率
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;

        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (videoPalySV != null) {
            ezPlayer.setSurfaceHold(holder);
        }
        videoPalySVHolder = holder;
        if (status == STATUS_INIT) {
            //绑定摄像头的序列号
            ezPlayer = APP.getOpenSDK().createPlayer("D87910395", 1);
            if (ezPlayer == null)
                return;
            //开启视频监控
            ezPlayer.setHandler(handler);
            ezPlayer.setSurfaceHold(videoPalySVHolder);
            ezPlayer.startRealPlay();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (ezPlayer != null) {
            ezPlayer.setSurfaceHold(null);
        }
        videoPalySVHolder = null;
    }

    @Override
    public void onClick(View v) {

    }
}