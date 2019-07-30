package com.liucr.gotocode.adb;

import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.SystemInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Adb {

    private static final String ANDROID_SDK_TYPE_NAME = "Android SDK";
    private static final String ADB_SUBPATH = "/platform-tools/";
    private static final String ADB_WINDOWS = "adb.exe";
    private static final String ADB_UNIX = "adb";

    private String androidSdkPath = "C:\\Android\\SDK";

    public static void main(String[] args) throws IOException {
        //adb devices -l
        Adb.getInstance().commend("logcat");
    }

    private Adb() {

    }

    public void commend(String command) {
        String adbPath = getAdbPath();
        ProcessBuilder processBuilder = new ProcessBuilder(adbPath, command);
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                String line = bufferedReader.readLine();
                System.out.println(line);
                if (line == null) {
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAndroidSdkPath() {
        if (androidSdkPath == null) {
            Sdk[] allJdks = ProjectJdkTable.getInstance().getAllJdks();
            for (Sdk jdk : allJdks) {
                String sdkTypeName = jdk.getSdkType().getName();
                if (ANDROID_SDK_TYPE_NAME.equals(sdkTypeName)) {
                    androidSdkPath = jdk.getHomePath();
                }
            }
        }
        return androidSdkPath;
    }

    public String getAdbPath() {
        String androidSdkPath = getAndroidSdkPath();
        String adb = SystemInfo.isWindows ? ADB_WINDOWS : ADB_UNIX;
        return androidSdkPath + ADB_SUBPATH + adb;
    }

    private boolean checkAndroidSdkPath() {
        if (androidSdkPath == null) {
            return false;
        }
        return true;
    }

    private static class LazyHolder {
        private static final Adb INSTANCE = new Adb();
    }

    public static Adb getInstance() {
        return LazyHolder.INSTANCE;
    }
}
