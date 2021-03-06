package com.wangdao.our.spread_2.slide_widget.widget_image;

import java.io.File;

import android.util.Log;


public class FileUtils {

    /**
     * ɾ���ļ����ļ���
     * @param file
     */
    public static void deleteFile(File file) {
        if(file == null || !file.exists()) {
            return;
        }
        if(file.isFile()) {
            final File to = new File( file.getAbsolutePath() + System.currentTimeMillis());
            file.renameTo( to);
            to.delete();
        }
        else {
            File[] files = file.listFiles();
            if(files != null && files.length > 0) {
                for(File innerFile: files) {
                    deleteFile( innerFile);
                }
            }
            final File to = new File( file.getAbsolutePath() + System.currentTimeMillis());
            file.renameTo( to);
            to.delete();
        }
    }
}
