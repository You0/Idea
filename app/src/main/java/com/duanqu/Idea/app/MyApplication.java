package com.duanqu.Idea.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.duanqu.Idea.Config;
import com.duanqu.Idea.R;
import com.duanqu.qupai.engine.session.MovieExportOptions;
import com.duanqu.qupai.engine.session.ProjectOptions;
import com.duanqu.qupai.engine.session.ThumbnailExportOptions;
import com.duanqu.qupai.engine.session.UISettings;
import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
import com.duanqu.qupai.sdk.android.QupaiService;
import com.duanqu.Idea.config.Contant;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 初始化操作，更多音乐为可配置选项。
 * Created by Mulberry on 2015/7/7.
 */
public class MyApplication extends Application{
    private static Context context;
    private static SharedPreferences sharedPreferences;

    private static HashMap<String,Handler> handlers = new HashMap<>();

    public static void setHandlers(String key,Handler handler) {
        handlers.put(key,handler);
    }

    public static Handler getHandlers(String key) {
        return handlers.get(key);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences("userInfo",MODE_PRIVATE);
        //初始化用户参数
        //这样写不好，应该建一个Bean
        LoadPreferences();


        Fresco.initialize(context);
        AlibabaSDK.asyncInit(this, new InitResultCallback() {
            @Override
            public void onSuccess() {
                QupaiService qupaiService = AlibabaSDK
                        .getService(QupaiService.class);

                if (qupaiService == null) {
                    Toast.makeText(MyApplication.this, "插件没有初始化，无法获取 QupaiService",
                            Toast.LENGTH_LONG).show();
                    return;
                }else {
//                    Toast.makeText(MyApplication.this, "初始化成功",
//                            Toast.LENGTH_LONG).show();
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
                //.setWaterMarkPath(Contant.WATER_MARK_PATH)
                //.setWaterMarkPosition(1)
                VideoSessionCreateInfo info =new VideoSessionCreateInfo.Builder()
                        .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_BACK)
                        .setBeautyProgress(0)
                        .setBeautySkinOn(true)
                        .setMovieExportOptions(movie_options)
                        .setThumbnailExportOptions(thumbnailExportOptions)
                        .build();


                qupaiService.initRecord(info,projectOptions,_UISettings);

                qupaiService.addMusic(0, "Athena", "assets://Qupai/music/Athena");
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(MyApplication.this, "插件没有初始化，无法获取 QupaiService"+ "code:" + i + "s" + s ,
                        Toast.LENGTH_LONG).show();
                Log.e("onFailure", "code:" + i + "s" + s );
            }
        });
    }

    public static void LoadPreferences() {
        Config.Token = sharedPreferences.getString("Token","");
        Config.username = sharedPreferences.getString("username","");
        Config.imageurl = sharedPreferences.getString("imageurl","");
        Config.sign = sharedPreferences.getString("sign","");
        Config.headurl = sharedPreferences.getString("headurl","");
        Config.nickname = sharedPreferences.getString("nickname","");
        Config.sex = sharedPreferences.getString("sex","");
        Config.sign = sharedPreferences.getString("sign","");
        Config.grades = sharedPreferences.getString("grades","");
        Config.major = sharedPreferences.getString("major","");
        Config.school = sharedPreferences.getString("school","");
        Config.email = sharedPreferences.getString("email","");
        Config.userid = sharedPreferences.getString("userid","");
        Config.defaultHeader = "res://"+context.getPackageName()+"/"+ R.drawable.default_head_1;
        if(Config.headurl.equals("")){
            Config.headurl = "res://"+context.getPackageName()+"/"+ R.drawable.default_head_1;
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static Context getContext()
    {
        return context;
    }

    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static int dip2px( float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static void setSharedPreferences(String key,String param)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo",
                context.MODE_PRIVATE);

        sharedPreferences.edit().putString(key,param).commit();

    }

    public static void setSharedPreferences(String key,Boolean param)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo",
                context.MODE_PRIVATE);

        sharedPreferences.edit().putBoolean(key,param).commit();

    }

    public static void LoadImageBySize(Uri uri, SimpleDraweeView draweeView, int width, int height){
        ImageRequest request =
                ImageRequestBuilder.newBuilderWithSource(uri)
                        .setResizeOptions(new ResizeOptions(width,height))
                        //缩放,在解码前修改内存中的图片大小, 配合Downsampling可以处理所有图片,否则只能处理jpg,
                        // 开启Downsampling:在初始化时设置.setDownsampleEnabled(true)
                        .setProgressiveRenderingEnabled(true)//支持图片渐进式加载
                        .setAutoRotateEnabled(true) //如果图片是侧着,可以自动旋转
                        .build();

        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(draweeView.getController())
                        .setAutoPlayAnimations(true) //自动播放gif动画
                        .build();

        draweeView.setController(controller);
    }


    public static File GetFrescoFile(String uri)
    {
        FileBinaryResource resource = (FileBinaryResource)Fresco.getImagePipelineFactory()
                .getMainDiskStorageCache()
                .getResource(new SimpleCacheKey(uri.toString()));

        File file = resource.getFile();
        return file;
    }

    public static String Encode(String str)
    {
        try {
            str = URLEncoder.encode(str,"UTF-8");
            str =  URLEncoder.encode(str,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String Decode(String str){
        try {
            str = URLDecoder.decode(str,"UTF-8");
            str =  URLDecoder.decode(str,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }





    private static final String AUTHTAG = "QupaiAuth";

}
