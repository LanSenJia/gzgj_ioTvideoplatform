package com.example.gzgj_iotvideoplatform.ui.homeFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.gzgj_iotvideoplatform.Application.APP;
import com.example.gzgj_iotvideoplatform.R;
import com.example.gzgj_iotvideoplatform.ui.activity.VideoSettingActivity;
import com.hjq.toast.ToastUtils;
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
    private Button settingBtn;
    private Button callingBtn;
    private Button sreenshotBtn;
    private Button returnvisitBtn;

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
        settingBtn = view.findViewById(R.id.play_setting_btn);
        callingBtn = view.findViewById(R.id.calling_btn);
        sreenshotBtn = view.findViewById(R.id.screenshot_btn);
        returnvisitBtn = view.findViewById(R.id.returnvisit_btn);

    }


    private void initData(View root) {
        ezPlayer = new EZPlayer();
        //摄像头设定holder事件
        videoPalySVHolder = videoPlaySv.getHolder();
        videoPalySVHolder.addCallback(this);
        handler = new Handler();
        settingBtn.setOnClickListener(this);
        callingBtn.setOnClickListener(this);
        sreenshotBtn.setOnClickListener(this);
        returnvisitBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_setting_btn:
                Intent intent = new Intent(getActivity(), VideoSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.returnvisit_btn:
            case R.id.calling_btn:
            case R.id.screenshot_btn:
                ToastUtils.show("功能正在开发中哦~(。・∀・)ノ");
                break;

            default:
                break;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (ezPlayer != null) {
            ezPlayer.stopRealPlay();
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        if (ezPlayer != null) {
//            //开启视频监控
////            ezPlayer.setHandler(handler);
////            ezPlayer.setSurfaceHold(videoPalySVHolder);
//            ezPlayer.startRealPlay();
//        }
//    }

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
        if (ezPlayer != null) {
            ezPlayer.stopRealPlay();
            ezPlayer.startRealPlay();
        }
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
                Log.e(TAG, "handleMessage: " + "调用失败，失败信息" +
                        errorinfo + "  失败错误码=" + codeStr + "失败描述：" + description);
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