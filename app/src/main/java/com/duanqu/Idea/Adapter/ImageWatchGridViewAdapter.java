package com.duanqu.Idea.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.duanqu.Idea.CustomView.CircleNumber;
import com.duanqu.Idea.R;
import com.duanqu.Idea.activity.ImageWatchActivity;
import com.duanqu.Idea.utils.ImageLoader;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/10/20.
 */
public class ImageWatchGridViewAdapter extends ArrayAdapter<ImageWatchActivity.ImageFloder> {
    ArrayList<ImageWatchActivity.ImageFloder> floders;
    ArrayList<String> mSelected = new ArrayList<>();
    HashMap<String,String[]> images;
    int[] floderSize;
    String[] floderNames;
    ImageLoader imageLoader;
    int resource;
    int count;
    int tag;
    Context context;

    public ImageWatchGridViewAdapter(Context context, int resource, List<ImageWatchActivity.ImageFloder> objects) {
        super(context, resource, objects);

        floders = (ArrayList<ImageWatchActivity.ImageFloder>) objects;
        InitArray();
        imageLoader = ImageLoader.getInstance(8, ImageLoader.Type.LIFO);

        this.resource = resource;
        this.context = context;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    private void InitArray() {
        floderSize = new int[floders.size()];
        floderNames = new String[floders.size()];
        images = new HashMap<>();
        //images
        for(int i=0;i<floders.size();i++)
        {
            String[] strings = floders.get(i).getDirpath().list(new FilenameFilter() {
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
            });

            images.put(floders.get(i)
                    .getDirpath().getAbsolutePath(),strings);
            count += strings.length;
            //为2个辅助数组赋值
            floderSize[i] = strings.length;
            floderNames[i] = floders.get(i).getDirpath().getAbsolutePath();
        }
    }


    @Override
    public int getCount() {
        return count+1;
    }

    public void setFloders(ArrayList<ImageWatchActivity.ImageFloder> floders) {
        count = 0;
        this.floders = floders;
        InitArray();
        notifyDataSetChanged();
    }

    public ArrayList<String> getmSelected() {
        return mSelected;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int innerPosition = position;
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.image_watch__grid_item, null);
            viewHolder = new ViewHolder();

            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.id_item_image);
            viewHolder.id_item_select = (CircleNumber) convertView.findViewById(R.id.id_item_select);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(position==0){
            viewHolder.imageView.setImageResource(R.drawable.icon_choosephoto_camera);
        }else{
            viewHolder.imageView.setImageResource(R.drawable.pictures_no);

        }
        viewHolder.id_item_select.setVisibility(View.INVISIBLE);
        int[] tuples = positionInMap(position-1);
        String temUri = null;
        if(position!=0){
            temUri = floderNames[tuples[0]]
                    + "/"+images.get(floderNames[tuples[0]])[tuples[1]];

            imageLoader.loadImage(temUri,
                    viewHolder.imageView);

        }
        final String currentImageUri = temUri;
        viewHolder.imageView.setColorFilter(null);
        //viewHolder.id_item_select.setImageResource(R.drawable.picture_unselected);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener()
        {
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v)
            {
                if(position==0){

                    takePhoto();
                    return;
                }

                // 已经选择过该图片
                if (mSelected.contains(currentImageUri))
                {
                    mSelected.remove(currentImageUri);
                    viewHolder.id_item_select.setVisibility(View.INVISIBLE);
                    viewHolder.imageView.setColorFilter(null);
                } else
                // 未选择该图片
                {
                    if(mSelected.size()>=9){
                        Toast.makeText(getContext(), "你最多选择9张图片", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    viewHolder.id_item_select.setVisibility(View.VISIBLE);
                    System.out.println(currentImageUri);
                    mSelected.add(currentImageUri);
                    viewHolder.id_item_select.setCar(String.valueOf(mSelected.size()));
                    viewHolder.number = mSelected.size();
                    viewHolder.imageView.setColorFilter(Color.parseColor("#77000000"));
                }
                if(tag == 0)
                {
                    ImageWatchActivity.mImageWatchActivity.Done();
                }
            }
        });


        if (mSelected.contains(currentImageUri))
        {
            viewHolder.id_item_select.setVisibility(View.VISIBLE);
            System.out.println(currentImageUri);
            viewHolder.id_item_select.setCar(String.valueOf(viewHolder.number));
            viewHolder.imageView.setColorFilter(Color.parseColor("#77000000"));
        }

        return convertView;
    }

    private int[] positionInMap(int positon)
    {
        int[] tuples = new int[2];
        int temp = 0;
        for(int i=0;i<floderSize.length;i++)
        {
            temp += floderSize[i];
            if(positon<temp){
                tuples[0] = i;
                tuples[1] = positon - temp + floderSize[i];
                break;
            }
        }
        return tuples;
    }

    private void takePhoto() {
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent localIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            ImageWatchActivity.mImageWatchActivity.lastName =
                    new StringBuilder().append(System.currentTimeMillis()).append("").toString().hashCode() + ".jpg";
            localIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                            ImageWatchActivity.mImageWatchActivity.lastName)));
            ImageWatchActivity.mImageWatchActivity.startActivityForResult(localIntent, 2);
        } else {
            Toast.makeText(context, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }




    class ViewHolder {
        ImageView imageView;
        CircleNumber id_item_select;
        int number;
    }
}
