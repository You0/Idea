package com.duanqu.Idea.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.duanqu.Idea.R;
import com.duanqu.Idea.app.MyApplication;
import com.duanqu.Idea.fragment.ImagePostFragment;
import com.duanqu.Idea.utils.MD5;

import java.io.FileOutputStream;
import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/10/25.
 */
public class ImagePostFragmentImageItem implements View.OnClickListener{
    private String imagepath;
    private View view;
    private Context context;
    private ImageView image;
    private ImageButton delete;

    public ImagePostFragmentImageItem(Context context)
    {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate( R.layout.image_post_fragment_image_item,null);
        image = (ImageView) view.findViewById(R.id.image);
        delete = (ImageButton) view.findViewById(R.id.delete);
        delete.setOnClickListener(this);
    }

    public View getView() {
        view.setPadding(15,1,0,1);
        return view;
    }

    //设置图像地址
    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
        Bitmap bitmap = ImageResize(imagepath);
        //ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(bitmap.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
        if(image!=null){
            ViewGroup.LayoutParams params = image.getLayoutParams();
            if(bitmap.getWidth()>bitmap.getHeight()){
                params.width = bitmap.getWidth();
            }else{
                params.width =  MyApplication.dip2px(250);
            }
            System.out.println("height" + bitmap.getHeight());
            params.height = MyApplication.dip2px(250);


           //image.setLayoutParams(params);
            image.setImageBitmap(bitmap);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

    }

    private Bitmap ImageResize(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int radio = options.outHeight/ MyApplication.dip2px(250);

        System.out.println("the radio"+radio);
        options.inJustDecodeBounds = false;
        options.inSampleSize = radio+1;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        return bitmap;

    }




    @Override
    public void onClick(View v) {
        Log.e("imagePostItem","click");
        switch (v.getId())
        {
            case R.id.delete:{
                Log.e("imagePostItem","delete");
                ImagePostFragment.imageHorizon.removeView(
                        ImagePostFragment.views.get(imagepath));
                ImagePostFragment.views.remove(imagepath);
                if(ImagePostFragment.path!=null){
                    ImagePostFragment.path.remove(imagepath);
                }
            }

        }

    }
}
