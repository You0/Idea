package com.duanqu.Idea;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.duanqu.qupai.engine.session.MovieExportOptions;
import com.duanqu.qupai.engine.session.ProjectOptions;
import com.duanqu.qupai.engine.session.ThumbnailExportOptions;
import com.duanqu.qupai.engine.session.UISettings;
import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
import com.duanqu.qupai.sdk.android.QupaiService;
import com.duanqu.Idea.common.Contant;
import com.duanqu.Idea.common.RequestCode;
import com.duanqu.Idea.result.RecordResult;
import com.duanqu.Idea.utils.AppConfig;
import com.duanqu.Idea.utils.AppGlobalSetting;

public class MainActivity extends Activity {
    private static final String TAG = "Upload";
    Button btn_record;
    EditText edit_time;
    EditText edit_min_time;
    EditText edit_rate;
    EditText edit_watermark;
    EditText beauty_skin_progress;
    TextView tv_result;

    private float mDurationLimit;
    private float mMinDurationLimit;
    private int mVideoBitrate;
    private int mWaterMark = -1;
    private int beautySkinProgress;

    private String waterMarkPath ;
    private Switch st_more_music;
    private Switch st_lead_in;
    private Switch has_edit_page;
    private Switch camera_font_on;
    private Switch beauty_skin_on;
    private Switch beauty_skinview_on;

    private NumberPicker np_video_width;
    private NumberPicker np_video_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);
        btn_record = (Button) findViewById(R.id.record);
        edit_rate = (EditText) findViewById(R.id.edit_rate);
        edit_time = (EditText) findViewById(R.id.edit_time);
        edit_min_time = (EditText) findViewById(R.id.edit_min_time);
        st_more_music = (Switch) findViewById(R.id.st_more_music);
        st_lead_in = (Switch) findViewById(R.id.st_lead_in);
        has_edit_page = (Switch) findViewById(R.id.has_edit_page);
        edit_watermark = (EditText) findViewById(R.id.edit_watermark);
        tv_result = (TextView) findViewById(R.id.tv_result);
        beauty_skin_progress =(EditText)findViewById(R.id.beauty_skin_progress);

        camera_font_on= (Switch) findViewById(R.id.camera_font_on);
        beauty_skin_on  = (Switch) findViewById(R.id.beauty_skin_on);
        beauty_skinview_on =(Switch) findViewById(R.id.beauty_skinview_on);

        np_video_width = (NumberPicker) findViewById(R.id.np_output_video_width);
        np_video_width.setMinValue(240);
        np_video_width.setMaxValue(1280);
        np_video_height = (NumberPicker) findViewById(R.id.np_output_video_height);
        np_video_height.setMinValue(240);
        np_video_height.setMaxValue(1280);

        np_video_width.setValue(480);
        np_video_height.setValue(480);

        btn_record.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                QupaiService qupaiService = AlibabaSDK
                        .getService(QupaiService.class);

                if (qupaiService == null) {
                    Toast.makeText(MainActivity.this, "插件没有初始化，无法获取 QupaiService",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                //视频时长
                if (!TextUtils.isEmpty(edit_time.getText())) {
                    mDurationLimit = Float.valueOf(edit_time.getText().toString());
                } else {
                    mDurationLimit = Contant.DEFAULT_DURATION_LIMIT;
                }

                //默认最小时长
                if (!TextUtils.isEmpty(edit_min_time.getText())) {
                    mMinDurationLimit = Float.valueOf(edit_min_time.getText().toString());
                } else {
                    mMinDurationLimit = Contant.DEFAULT_MIN_DURATION_LIMIT;
                }

                //视频码率
                if (!TextUtils.isEmpty(edit_rate.getText())) {
                    mVideoBitrate = Integer.valueOf(edit_rate.getText().toString());
                } else {
                    mVideoBitrate = Contant.DEFAULT_BITRATE;
                }

                //是否需要水印
                mWaterMark = Integer.valueOf(TextUtils.isEmpty(edit_watermark.getText().toString()) ? "1" : edit_watermark.getText().toString());
                //水印存储的目录
                waterMarkPath = Contant.WATER_MARK_PATH ;

                //美颜参数:1-100.这里不设指定为80,这个值只在第一次设置，之后在录制界面滑动美颜参数之后系统会记住上一次滑动的状态
                beautySkinProgress= Integer.valueOf(TextUtils.isEmpty(beauty_skin_progress.getText())?"80":beauty_skin_progress.getText().toString());

                UISettings _UISettings = new UISettings() {

                    @Override
                    public boolean hasEditor() {
                        return has_edit_page.isChecked();
                    }

                    @Override
                    public boolean hasImporter() {
                        return st_lead_in.isChecked();
                    }

                    @Override
                    public boolean hasGuide() {
                        return false;
                    }

                    @Override
                    public boolean hasSkinBeautifer() {
                        return beauty_skinview_on.isChecked();
                    }
                };

                MovieExportOptions movie_options = new MovieExportOptions.Builder()
                        .setVideoBitrate(mVideoBitrate)
                        .configureMuxer("movflags", "+faststart")
                        .build();

                ProjectOptions projectOptions = new ProjectOptions.Builder()
                        .setVideoSize(np_video_width.getValue(), np_video_height.getValue())
                        .setVideoFrameRate(30)
                        .setDurationRange(mMinDurationLimit,mDurationLimit)
                        .get();

                ThumbnailExportOptions thumbnailExportOptions =new ThumbnailExportOptions.Builder()
                        .setCount(1).get();

                VideoSessionCreateInfo info =new VideoSessionCreateInfo.Builder()
                            .setWaterMarkPath(waterMarkPath)
                            .setWaterMarkPosition(mWaterMark)
                            .setCameraFacing(camera_font_on.isChecked()?Camera.CameraInfo.CAMERA_FACING_FRONT:
                                    Camera.CameraInfo.CAMERA_FACING_BACK)
                            .setBeautyProgress(beautySkinProgress)
                            .setBeautySkinOn(beauty_skin_on.isChecked())
                            .setMovieExportOptions(movie_options)
                            .setThumbnailExportOptions(thumbnailExportOptions)
                            .build();

                //初始化，建议在application里面做初始化，这里做是为了方便开发者认识参数的意义
                qupaiService.initRecord(info,projectOptions,_UISettings);

                //是否需要更多音乐页面--如果不需要更多音乐可以干掉
                Intent moreMusic = new Intent();
                if (st_more_music.isChecked()) {
                    moreMusic.setClass(MainActivity.this, MoreMusicActivity.class);
                } else {
                    moreMusic = null;
                }
                qupaiService.hasMroeMusic(moreMusic);

                //引导，只显示一次，这里用SharedPreferences记录
                final AppGlobalSetting sp = new AppGlobalSetting(getApplicationContext());
                Boolean isGuideShow = sp.getBooleanGlobalItem(
                        AppConfig.PREF_VIDEO_EXIST_USER, true);

                /**
                 * 建议上面的initRecord只在application里面调用一次。这里为了能够开发者直观看到改变所以可以调用多次
                 */
                qupaiService.showRecordPage(MainActivity.this, RequestCode.RECORDE_SHOW, isGuideShow);

                sp.saveGlobalConfigItem(
                        AppConfig.PREF_VIDEO_EXIST_USER, false);

            }
        });

    }

    String videoFile;
    String [] thum;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            RecordResult result =new RecordResult(data);
            //得到视频地址，和缩略图地址的数组，返回十张缩略图
            videoFile = result.getPath();
            thum = result.getThumbnail();
            result.getDuration();

            tv_result.setText("视频路径:" + videoFile + "图片路径:" + thum[0]);

            /**
             * 清除草稿,草稿文件将会删除。所以在这之前请执行move操作。
             * move操作请自行实现，第一版本的copyVideoFile接口不再使用
             */
            QupaiService qupaiService = AlibabaSDK
                    .getService(QupaiService.class);
            qupaiService.deleteDraft(getApplicationContext(),data);

        } else {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "RESULT_CANCELED", Toast.LENGTH_LONG).show();
            }
        }
    }


}
