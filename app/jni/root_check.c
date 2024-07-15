#include <jni.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <android/log.h>

#define TAG "RootCheckNative"

static const char *blacklistedMountPaths[] = {
        "magisk",
        "core/mirror",
        "core/img"
};

static const char *suPaths[] = {
        "/data/local/su",
        "/data/local/bin/su",
        "/data/local/xbin/su",
        "/sbin/su",
        "/su/bin/su",
        "/system/bin/su",
        "/system/bin/.ext/su",
        "/system/bin/failsafe/su",
        "/system/sd/xbin/su",
        "/system/usr/we-need-root/su",
        "/system/xbin/su",
        "/cache/su",
        "/data/su",
        "/dev/su"
};

static inline int is_mountpaths_detected() {
    FILE *fp = fopen("/proc/self/mounts", "r");
    if (fp == NULL) return 0;

    char *line = NULL;
    size_t len = 0;

    while (getline(&line, &len, fp) != -1) {
        for (int i = 0; i < sizeof(blacklistedMountPaths) / sizeof(blacklistedMountPaths[0]); i++) {
            if (strstr(line, blacklistedMountPaths[i]) != NULL) {
                __android_log_print(ANDROID_LOG_INFO, TAG, "Detected mount path: %s", blacklistedMountPaths[i]);
                free(line);
                fclose(fp);
                return 1;
            }
        }
    }

    free(line);
    fclose(fp);
    return 0;
}

static inline int is_supath_detected() {
    for (int i = 0; i < sizeof(suPaths) / sizeof(suPaths[0]); i++) {
        if (access(suPaths[i], F_OK) != -1) {
            __android_log_print(ANDROID_LOG_INFO, TAG, "Detected SU path: %s", suPaths[i]);
            return 1;
        }
    }
    return 0;
}

JNIEXPORT jboolean JNICALL
Java_com_example_rootdetectorapp_MainActivity_checkRootNative(JNIEnv *env, jobject thiz) {
    return (is_mountpaths_detected() || is_supath_detected()) ? JNI_TRUE : JNI_FALSE;
}
