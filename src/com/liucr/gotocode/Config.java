package com.liucr.gotocode;

import com.intellij.ide.util.PropertiesComponent;

public class Config {

    private String studioPath;

    public boolean isOpenFileByClick() {
        return PropertiesComponent.getInstance().getBoolean("isOpenFileByClick", false);
    }

    public void setOpenFileByClick(boolean openFileByClick) {
        PropertiesComponent.getInstance().setValue("isOpenFileByClick", openFileByClick);
    }

    private static class LazyHolder {
        private static final Config INSTANCE = new Config();
    }

    public static Config getInstance() {
        return Config.LazyHolder.INSTANCE;
    }
}
