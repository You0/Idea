package com.duanqu.Idea.app;

import android.app.Application;
import android.hardware.Camera;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.duanqu.qupai.engine.session.MovieExportOptions;
import com.duanqu.qupai.engine.session.ProjectOptions;
import com.duanqu.qupai.engine.session.ThumbnailExportOptions;
import com.duanqu.qupai.engine.session.UISettings;
import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
import com.duanqu.qupai.sdk.android.QupaiService;
import com.duanqu.Idea.common.Contant;

/**
 * 初始化操作，更多音乐为可配置选项。
 * Created by Mulberry on 2015/7/7.
 */
public class TestApplicaticon extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        AlibabaSDK.asyncInit(this, new InitResultCallback() {
            @Override
            public void onSuccess() {
                QupaiService qupaiService = AlibabaSDK
                        .getService(QupaiService.class);

                if (qupaiService == null) {
                    Toast.makeText(TestApplicaticon.this, "插件没有初始化，无法获取 QupaiService",
                            Toast.LENGTH_LONG).show();
                    return;
                }else {
                    Toast.makeText(TestApplicaticon.this, "初始化成功",
                            Toast.LENGTH_LONG).show();
                }

                UISettings _UISettings = new UISettings() {

                    @Override
                    public boolean hasEditor() {
                        return true;
                    }

                    @Override
                    public boolean hasImporter() {
                        return super.hasImporter();
                    }

                    @Override
                    public boolean hasGuide() {
                        return true;
                    }
                    @Override
                    public boolean hasSkinBeautifer() {
                        return true;
                    }

                };

                MovieExportOptions movie_options = new MovieExportOptions.Builder()
                        .setVideoBitrate(Contant.DEFAULT_BITRATE)
                        .configureMuxer("movflags", "+faststart")
                        .build();

                ProjectOptions projectOptions = new ProjectOptions.Builder()
                        .setVideoSize(480, 480)
                        .setVideoFrameRate(30)
                        .setDurationRange(Contant.DEFAULT_MIN_DURATION_LIMIT,Contant.DEFAULT_DURATION_LIMIT)
                        .get();

                ThumbnailExportOptions thumbnailExportOptions =new ThumbnailExportOptions.Builder()
                        .setCount(1).get();

                VideoSessionCreateInfo info =new VideoSessionCreateInfo.Builder()
                        .setWaterMarkPath(Contant.WATER_MARK_PATH)
                        .setWaterMarkPosition(1)
                        .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_BACK)
                        .setBeautyProgress(80)
                        .setBeautySkinOn(true)
                        .setMovieExportOptions(movie_options)
                        .setThumbnailExportOptions(thumbnailExportOptions)
                        .build();


                qupaiService.initRecord(info,projectOptions,_UISettings);

                qupaiService.addMusic(0, "Athena", "assets://Qupai/music/Athena");
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(TestApplicaticon.this, "插件没有初始化，无法获取 QupaiService"+ "code:" + i + "s" + s ,
                        Toast.LENGTH_LONG).show();
                Log.e("onFailure", "code:" + i + "s" + s );
            }
        });

    }

    private static final String AUTHTAG = "QupaiAuth";

}
