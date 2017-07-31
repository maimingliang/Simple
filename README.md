## 工具：

下载 [bsdiff ](http://www.daemonology.net/bsdiff/bsdiff-4.3.tar.gz) 

下载[bzip2](http://www.bzip.org/downloads.html),

NDK版本 ：r12


### 解压bsdiff，copy bspatch.c文件到android studio 的cpp目录下。

### 解压bzip2：

![001.png](http://upload-images.jianshu.io/upload_images/1157085-4953f7e9d055ede3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

 将解压出来的文件copy 到Android Studio的cpp目录下：

![002.png](http://upload-images.jianshu.io/upload_images/1157085-f5c74a6b366f696d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 在bzip2目录下，创建一个CMakeLists.txt文件：

![003.png](http://upload-images.jianshu.io/upload_images/1157085-ffcd72a0633986bd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 在cpp目录下，创建一个CMakeLists.txt文件：

![004.png](http://upload-images.jianshu.io/upload_images/1157085-558c8229121619c0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

内容为：

```code
# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds it for you.
# Gradle automatically packages shared libraries with your APK.


set(CMAKE_VERBOSE_MAKEFILE on) #输出详细的编译和链接信息

set(bzip2_src_DIR ${CMAKE_SOURCE_DIR}) #表示CMake的资源目录

add_subdirectory(${bzip2_src_DIR}/bzip2)


add_library( # Sets the name of the library.
             patch-box

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # Associated headers in the same location as their source
             # file are automatically included.
             patch-box.c
             bspatch.c
             )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because system libraries are included in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.


#CMAKE_CXX_FLAGS "-Wall" c++编译器参数

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11 -Wall -DGLM_FORCE_SIZE_T_LENGTH")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -DGLM_FORCE_RADIANS")


find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in the
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       patch-box

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )

```


### 配置build.gradle

```xml

   defaultConfig {
  
        ......

        externalNativeBuild {
            cmake {
                cppFlags "-std=c++11"
//                 指定只用clang编译器
//                 Clang是一个C语言、Objective-C、C++语言的轻量级编译器。
                arguments "-DANDROID_TOOLCHAIN=clang"
//                 生成.so库的目标平台
                abiFilters "armeabi-v7a"
            }
        }
    }

 externalNativeBuild {
        cmake {
            path "src/main/cpp/CMakeLists.txt"
        }
    }

```

### 编写调用函数

 创建类：
```code

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
    public static native void bspatch(String oldFilePath,String newFilePath,String patchFilePath);


    static {
        System.loadLibrary("patch-box");
    }
}

```

打开bspatch.c文件后面，添加函数：

```code

/*合并APK*/
JNIEXPORT void JNICALL
Java_com_maiml_bspatchlibrary_BspatchNDK_bspatch(JNIEnv *env, jclass type, jstring oldFilePath_,
												 jstring newFilePath_, jstring patchFilePath_) {

	const char *oldFilePath = (*env)->GetStringUTFChars(env, oldFilePath_, 0);
	const char *newFilePath = (*env)->GetStringUTFChars(env, newFilePath_, 0);
	const char *patchFilePath = (*env)->GetStringUTFChars(env, patchFilePath_, 0);


	// if(argc!=4) errx(1,"usage: %s oldfile newfile patchfile\n",argv[0]);

	int argc = 4 ;
	char* argv[4] ;
	argv[0] = "bspatch";
	argv[1] = oldFilePath;
	argv[2] = newFilePath;
	argv[3] = patchFilePath;

	bspatch_main(argc,argv);

	LOGE("MainActivity","%s","合并APK完成");

	(*env)->ReleaseStringUTFChars(env, oldFilePath_, oldFilePath);
	(*env)->ReleaseStringUTFChars(env, newFilePath_, newFilePath);
	(*env)->ReleaseStringUTFChars(env, patchFilePath_, patchFilePath);
}


```

调用：
```code
BspatchNDK.bspatch(oldFile, newApkPath, patchFileAbsolutePath);
```
END。
