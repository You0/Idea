package com.duanqu.Idea.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

    /**
     * 保存路径的文件夹名称
     */
    public static  String DIR_NAME = "qupai_video_test";

    /**
     * 给指定的文件名按照时间命名
     */
    private static  SimpleDateFormat OUTGOING_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");

    /**
     * 得到指定的Video保存路径
     * @return
     */
    public static File getDoneVideoPath() {
        File dir = new File(Environment.getExternalStorageDirectory()
                + File.separator + DIR_NAME);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    /**
     * 得到输出的Video保存路径
     * @return
     */
    public static String newOutgoingFilePath() {
        String str = OUTGOING_DATE_FORMAT.format(new Date());
        String fileName = getDoneVideoPath() + File.separator
                + "video_" + str + ".mp4";
        return fileName;
    }



    public static String saveFile(Context c, String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        return saveFile(c, filePath, fileName, bytes);
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static String saveFile(Context c, String filePath, String fileName, byte[] bytes) {
        String fileFullName = "";
        FileOutputStream fos = null;
        try {
            String suffix = "";

            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName + suffix);
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, fileName + suffix));
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        return fileFullName;
    }

}
