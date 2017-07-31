

#include "jni.h"
#include "string.h"
#include "android/log.h"
#include "stdio.h"
#include "stdlib.h"
#define LOGE(TAG,FORMAT,...) __android_log_print(ANDROID_LOG_ERROR,TAG,FORMAT,__VA_ARGS__)


/**
 * 这两个函数参数类型jobject与jclass有什么区别呢 ？ 这两个类型表示 ， Java的native函数 ， 是成员方法还是类方法
 */
