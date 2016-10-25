package com.duanqu.Idea.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duanqu.Idea.Adapter.ImageWatchGridViewAdapter;
import com.duanqu.Idea.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */
public class ImageWatchActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private ImageView mImageView;
    private ArrayList<ImageFloder> imageFloders;
    private ListImageDirPopupWindow mListImageDirPopupWindow;
    private int mScreenHeight;
    private RelativeLayout mBottomLy;
    private TextView mChooseDir;
    private TextView mImageCount;
    public static ImageWatchActivity mImageWatchActivity;
    private ImageWatchGridViewAdapter gridViewAdapter;
    public static int tag = 1;
    public String lastName;
    private TextView ok;
    private ArrayList<String> Al_Selected;

    //储存文件夹中的图片数量
    private int MpicSize;

    //图片数量最多的文件夹
    private File maxSizeDir;

    //所有的图片(图片数量最多的文件夹里的所有图片)
    private List<String> Images;

    private GridView gridView;
    private ListAdapter adapter;

    //临时的辅助类，防止同一文件夹被多次扫描
    private HashSet<String> dirPaths = new HashSet<String>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();

            switch (msg.what) {
                case 0: {
                    mImageCount.setText(MpicSize + "张");
                    if (imageFloders.size() == 0) {
                        Toast.makeText(ImageWatchActivity.this, "一张图片也没扫到╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
                    } else {
                        gridViewAdapter = new ImageWatchGridViewAdapter
                                (ImageWatchActivity.this, R.layout.image_watch__grid_item, imageFloders);
                        gridViewAdapter.setTag(tag);
                        if(Al_Selected!=null){
                            gridViewAdapter.getmSelected().addAll(Al_Selected);
                        }
                        gridView.setAdapter(gridViewAdapter);

                    }
                    initListDirPopupWindw();

                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageWatchActivity = this;
        setContentView(R.layout.image_watch_layout);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        imageFloders = new ArrayList<ImageFloder>();
        gridView = (GridView) findViewById(R.id.id_gridView);
        Intent intent = getIntent();
        tag = intent.getIntExtra("tag", 1);
        Al_Selected = intent.getStringArrayListExtra("selected");
        initView();
        getImages();
        initEvent();

    }


    private void initView() {
        gridView = (GridView) findViewById(R.id.id_gridView);
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total_count);
        ok = (TextView) findViewById(R.id.ok);
        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
    }


    /*
        * 通过内容提供者扫描图片
        */
    public void getImages() {
        if (Environment.getExternalStorageState().
                equals(Environment.MEDIA_REMOVED)) {
            Toast.makeText(ImageWatchActivity.this, "暂无外部存储", Toast.LENGTH_SHORT).show();
        }
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri ImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = ImageWatchActivity.this.getContentResolver();

//                query()方法参数           对应SQL部分                                                   描述
//                uri                   from table_name                      指定查询某个应用程序下的某一张表
//                projection            select column1, column2             指定查询的列名
//                selection             where column = value                指定 where 的约束条件
//                selectionArgs              -                              为 where 中的占位符提供具体的值
//                orderBy               order by column1, column2           指定查询结果的排序方式
                // 只查询jpeg和png的图片

                Cursor cursor = contentResolver.query(ImageUri, null,
                        null, null, MediaStore.Images.Media.DATE_MODIFIED);

                while (cursor.moveToNext()) {
                    ImageFloder imageFloder;
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    //获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    String dirPath = parentFile.getAbsolutePath();

                    if (dirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        dirPaths.add(dirPath);
                        imageFloder = new ImageFloder().setDirpath(parentFile)
                                .setFirstImagepath(path);
                    }
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg") || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg")|| filename.endsWith(".JPG")
                                    || filename.endsWith(".JPGE")|| filename.endsWith(".PNG")
                                    || filename.equals(".gif")||filename.endsWith(".GIF")
                                    ) {
                                return true;
                            }
                            return false;
                        }
                    }).length;
                    imageFloder.setPicSize(picSize);
                    imageFloders.add(imageFloder);
                    MpicSize += picSize;
                }
                cursor.close();
                dirPaths = null;
                handler.sendEmptyMessage(0);

            }
        }).start();
    }


    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(this, imageFloders);

        mListImageDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(new ListImageDirPopupWindow.OnImageDirSelected() {
            @Override
            public void selected(ImageFloder floder) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(floder);
                gridViewAdapter.getmSelected().clear();
                gridViewAdapter.setFloders(arrayList);
                mImageCount.setText(floder.getPicSize() + "张");
                mChooseDir.setText(floder.getDirpath().getName());
                mListImageDirPopupWindow.dismiss();
            }
        });
    }


    private void initEvent() {
        /**
         * 为底部的布局设置点击事件，弹出popupWindow
         */
        mBottomLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListImageDirPopupWindow
                        .setAnimationStyle(R.style.PopupAnimation);

                mListImageDirPopupWindow.showAsDropDown(mBottomLy);
//                mListImageDirPopupWindow.showAtLocation(mBottomLy,
//                        Gravity.NO_GRAVITY,0,mBottomLy.getHeight());
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);
            }
        });

        //选择完毕后返回
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ArrayList SendBackArray = new ArrayList();
                SendBackArray.addAll(gridViewAdapter.getmSelected());
                intent.putCharSequenceArrayListExtra("data_return", SendBackArray);
                setResult(RESULT_OK, intent);
                //gridViewAdapter.getmSelected().clear();
                finish();
            }
        });


    }

    //是单选图片时返回
    public void Done() {
        Intent intent = new Intent();
        intent.putExtra("data_return", gridViewAdapter.getmSelected().get(0));
        //intent.putCharSequenceArrayListExtra("data_return", GridViewAdapter.mSelectedImage);
        setResult(RESULT_OK, intent);
        gridViewAdapter.getmSelected().clear();
        finish();
    }


    //拍照时返回
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 点击取消按钮
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case 2:// 如果是调用相机拍照时
//                Toast.makeText(ImageWatchActivity.this,
//                        doPhoto(requestCode, data), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("data_return", doPhoto(requestCode,data));
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String doPhoto(int requestCode, Intent data) {
        String filePath = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DCIM), this.lastName).getAbsolutePath();
        return filePath;


    }


    public class ImageFloder {
        private int picSize;
        private File Dirpath;
        private String FirstImagepath;


        public ImageFloder setPicSize(int picSize) {
            this.picSize = picSize;
            return this;
        }

        public int getPicSize() {
            return picSize;
        }

        public ImageFloder setDirpath(File dirpath) {
            Dirpath = dirpath;
            return this;
        }

        public ImageFloder setFirstImagepath(String firstImagepath) {
            FirstImagepath = firstImagepath;
            return this;
        }

        public File getDirpath() {
            return Dirpath;
        }

        public String getFirstImagepath() {
            return FirstImagepath;
        }
    }


}
