package com.liucr.gotocode;

import com.intellij.ide.util.PropertiesComponent;

import java.util.ArrayList;
import java.util.List;

public class Config {

    public static final String OPEN_FILE_BY_CLICK = "open_file_by_click";
    public static final String LOGCAT_TARGET_DEVICE = "logcat_target_device";

    private List<UpdateListener> updateListeners = new ArrayList<>();

    public boolean isOpenFileByClick() {
        return PropertiesComponent.getInstance().getBoolean(OPEN_FILE_BY_CLICK, false);
    }

    public void setOpenFileByClick(boolean openFileByClick) {
        PropertiesComponent.getInstance().setValue(OPEN_FILE_BY_CLICK, openFileByClick);
        onConfigUpdate(OPEN_FILE_BY_CLICK, openFileByClick);
    }

    public String getLogcatTargetDevice() {
        return PropertiesComponent.getInstance().getValue(LOGCAT_TARGET_DEVICE);
    }

    public void setLogcatTargetDevice(String deviceId) {
        PropertiesComponent.getInstance().setValue(LOGCAT_TARGET_DEVICE, deviceId);
        onConfigUpdate(LOGCAT_TARGET_DEVICE, deviceId);
    }

    private void onConfigUpdate(String configName, Object configValue) {
        for (UpdateListener updateListener : updateListeners) {
            updateListener.onConfigUpdate(configName, configValue);
        }
    }

    public void addUpdateListener(UpdateListener updateListener) {
        if (!updateListeners.contains(updateListener)) {
            updateListeners.add(updateListener);
        }
    }

    public void removeUpdateListener(UpdateListener updateListener) {
        updateListeners.add(updateListener);
    }

    private static class LazyHolder {
        private static final Config INSTANCE = new Config();
    }

    public static Config getInstance() {
        return Config.LazyHolder.INSTANCE;
    }

    public interface UpdateListener {
        void onConfigUpdate(String configName, Object configValue);
    }
}
