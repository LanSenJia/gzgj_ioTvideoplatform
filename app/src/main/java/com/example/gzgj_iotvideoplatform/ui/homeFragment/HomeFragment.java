package com.example.gzgj_iotvideoplatform.ui.homeFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class HomeFragment extends Fragment implements View.OnClickListener, SurfaceHolder.Callback, Handler.Callback {

    private HomeViewModel homeViewModel;
    private SurfaceView videoPlaySv;
    private EZPlayer ezPlayer;
    private SurfaceHolder videoPalySVHolder;
    private Handler handler;
    private int status = RealPlayStatus.STATUS_INIT;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initView(root);
        initData(root);
        return root;
    }

    private void initView(View view) {
        videoPlaySv = view.findViewById(R.id.video_play_sv);
    }


    private void initData(View root) {
        ezPlayer = new EZPlayer();
        //摄像头设定holder事件
        videoPalySVHolder = videoPlaySv.getHolder();
        videoPalySVHolder.addCallback(this);
        handler = new Handler();

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (videoPlaySv != null) {
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
}