LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := root_check
LOCAL_SRC_FILES := root_check.c
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)