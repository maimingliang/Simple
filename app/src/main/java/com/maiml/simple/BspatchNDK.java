package com.maiml.simple;

/**
 * author   maimingliang
 */
public class BspatchNDK {


    /**
     *
     * @param oldFilePath 旧apk文件路径
     * @param newFilePath 新apk文件路径
     * @param patchFilePath 差分包apk文件路径
     */
    public static native void bspatch(String oldFilePath, String newFilePath, String patchFilePath);


    static {
        System.loadLibrary("native-lib");
    }
}
