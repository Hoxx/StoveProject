package com.hxx.filelib;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Houxingxiu
 * Date : 2018/12/15
 */
public class AssetFileManager {

    private Context      mContext;
    private AssetManager mAssetManager;
    private String       dataDataPath;

    public AssetFileManager(Context context) {
        mContext = context;
        init();
    }


    private void init() {
        mAssetManager = mContext.getAssets();
        dataDataPath = "/data/data/";
    }


    public void copy(String rootPath) {
        if (mAssetManager != null) {
            try {
                String[] result = mAssetManager.list(rootPath);
                for (String rootItemPath : result) {
                    Log.e("HXX-TAG---", "copy-rootItemPath：" + rootItemPath);
                    list(rootPath + "/" + rootItemPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("HXX-TAG---", "拷贝失败：：：：：：：：：：：：：：：：" + e.toString());
            }
        }
    }


    private void list(String rootPath) throws IOException {
        String[] result = mAssetManager.list(rootPath);
        for (String rootItemPath : result) {
            Log.e("HXX-TAG---", "rootItemPath：" + rootItemPath);
            if (isDir(rootPath + "/" + rootItemPath)) {
                String dataPath = dataDataPath + rootItemPath;
                createDir(dataPath);
                list(rootPath + "/" + rootItemPath);
            } else {
                String childPath = rootPath + "/" + rootItemPath;
                String targetPath = dataDataPath + childPath;
                copyFormAsset(childPath, targetPath);
            }
        }
    }

    private boolean isDir(String filePath) throws IOException {
        return mAssetManager.list(filePath).length > 0;
    }

    private void createDir(String filePath) {
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
    }


    private void copyFormAsset(String sourceFileFullPath, String targetFilePath) throws IOException {
        Log.e("HXX-TAG---", "开始拷贝---sourceFileFullPath:" + sourceFileFullPath);
        Log.e("HXX-TAG---", "开始拷贝---targetFilePath:" + targetFilePath);
        File file = new File(targetFilePath);
        if (file.exists()) {
            file.delete();
        }
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        InputStream inputStream = mAssetManager.open(sourceFileFullPath);
        OutputStream outputStream = new FileOutputStream(targetFilePath);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        Log.e("HXX-TAG---", "拷贝成功---sourceFileFullPath:" + sourceFileFullPath);
        Log.e("HXX-TAG---", "拷贝成功---targetFilePath:" + targetFilePath);
    }


}
