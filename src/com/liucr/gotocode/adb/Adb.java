package com.liucr.gotocode.adb;

import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.SystemInfo;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Adb {

    private static final String ANDROID_SDK_TYPE_NAME = "Android SDK";
    private static final String ADB_SUBPATH = "/platform-tools/";
    private static final String ADB_WINDOWS = "adb.exe";
    private static final String ADB_UNIX = "adb";

//        private String androidSdkPath ;
    private String androidSdkPath = "C:\\Android\\SDK";
//    private String androidSdkPath = "D:\\andoird\\SDK";

    public static void main(String[] args) throws IOException {
        //adb devices -l
//        Adb.getInstance().runCommand("logcat", "*:I");
        List<Device> deviceList = Adb.getInstance().getDeviceList();
        System.out.println(deviceList.toString());
    }

    private Adb() {

    }

    /**
     * 获取当前连接的设备
     */
    public List<Device> getDeviceList() {

        List<Device> deviceList = new ArrayList<>();

        Process logcatProcess = getCommendProcess("devices", "-l");
        if (logcatProcess == null) {
            return new ArrayList<>();
        }
        InputStream inputStream = logcatProcess.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("product") && line.contains("model")) {
                    Device device = new Device();
                    device.deviceNumber = line.substring(0, line.indexOf(" "));
                    String text = line.substring(line.indexOf("model:"));
                    device.name = text.substring(6, text.indexOf(" device"));
                    deviceList.add(device);
                }
            }
//            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deviceList;
    }

    public void runCommand(String... command) {
        Process commendProcess = getCommendProcess(command);
        if (commendProcess != null) {
            commendProcess.destroy();
        }
    }

    @Nullable
    public Process getCommendProcess(String... command) {
        String adbPath = getAdbPath();
        List<String> commandList = new ArrayList<String>();
        commandList.add(adbPath);
        commandList.addAll(Arrays.asList(command));
        ProcessBuilder processBuilder = new ProcessBuilder(commandList);
        processBuilder.redirectErrorStream(true);

        try {
            return processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getAndroidSdkPath() {
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

    private String getAdbPath() {
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


    public static class Device {

        public String name;

        public String deviceNumber;

        @Override
        public String toString() {
            return name + " : " + deviceNumber;
        }
    }
}
