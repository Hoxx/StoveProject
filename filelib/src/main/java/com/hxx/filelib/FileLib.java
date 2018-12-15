package com.hxx.filelib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author Houxingxiu
 * Date : 2018/12/15
 */
public class FileLib {


    /**
     * 拷贝文件
     *
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     */
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        if (sourceFile == null || targetFile == null) {
            throw new NullPointerException();
        }
        if (!sourceFile.exists()) {
            throw new FileNotFoundException();
        }
        FileChannel input = new FileInputStream(sourceFile).getChannel();
        FileChannel output = new FileOutputStream(targetFile).getChannel();
        output.transferFrom(input, 0, input.size());
        input.close();
        output.close();
    }
}
