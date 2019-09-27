package com.liucr.gotocode.tool;

import com.liucr.gotocode.adb.Adb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ActivityInformationTool {

    //adb shell dumpsys activity com.joonmall.app/.module.order.OrderListActivity
    public static ActivityInformation getActivityInformation() {
        ActivityInformation activityInformation = new ActivityInformation();
        try {
            String focusActivity = getFocusActivity();

            //获取当前activity名称
            String[] split = focusActivity.split("/");
            activityInformation.name = split[1];
            activityInformation.simpleName = split[1].substring(split[1].lastIndexOf(".") + 1);

            Process process = getProcess("shell", "dumpsys", "activity", focusActivity);
            InputStream inputStream = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            boolean isLocalFragmentActivityLine = false;
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);

                if (line.contains("Local FragmentActivity")) {
                    isLocalFragmentActivityLine = true;
                    continue;
                }

                if (line.contains("Added Fragments") && isLocalFragmentActivityLine) {
                    break;
                }

                if (isLocalFragmentActivityLine) {
                    while (line.startsWith(" ")) {
                        line = line.replaceFirst(" ", "");
                    }
                    if (line.startsWith("#")) {
                        FragmentInformation fragmentInformation = new FragmentInformation();
                        int start = line.indexOf(" ");
                        int end = line.indexOf("{");
                        fragmentInformation.simpleName = line.substring(start + 1, end);
                        activityInformation.informationList.add(fragmentInformation);
                        continue;
                    }

                    int size = activityInformation.informationList.size();
                    if (size == 0) {
                        continue;
                    }
                    FragmentInformation fragmentInformation = activityInformation.informationList.get(size - 1);
                    if (line.contains("mAdded")) {
                        String[] s = line.split(" ");
                        String[] split1 = s[0].split("=");
                        fragmentInformation.added = "true".equals(split1[1]);
                        continue;
                    }

                    if (line.contains("mHidden")) {
                        String[] s = line.split(" ");
                        String[] split1 = s[0].split("=");
                        fragmentInformation.hidden = "true".equals(split1[1]);
                        if (!fragmentInformation.hidden) {
                            activityInformation.curFragmentInformation = fragmentInformation;
                        }
                        continue;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\n" + "------------------- END -----------------------");
        System.out.println("\n" + "------------------- ActivityInformation -----------------------");
        System.out.println(activityInformation.toString());
        return activityInformation;
    }


    /**
     * 获取当前app的包名
     * adb shell dumpsys window | findstr mCurrentFocus : mCurrentFocus=Window{a3da1c5 u0 com.joonmall.app/com.joonmall.app.module.home.MainActivity}
     *
     * @return PackageName
     */
    public static String getFocusActivity() {
        String text = null;
        try {
            Process process = getProcess("shell", "dumpsys", "window", "w", "|grep", "mCurrentFocus");
            InputStream inputStream = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            text = bufferedReader.readLine();
            System.out.println(text);
            int lastIndexOfSpace = text.lastIndexOf(" ");
            text = text.substring(lastIndexOfSpace + 1, text.length() - 1);
            System.out.println("FocusActivity : " + text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    private static Process getProcess(String... commend) {
//        String targetDeviceId = Config.getInstance().getLogcatTargetDevice();
        Process process;
//        if (targetDeviceId == null) {
        process = Adb.getInstance().getCommendProcess(commend);
//        } else {
//            String[] targetCommend = new String[commend.length + 2];
//            targetCommend[0] = "-s";
//            targetCommend[1] = targetDeviceId;
//            System.arraycopy(targetCommend, 2, commend, 0, commend.length);
//            process = Adb.getInstance().getCommendProcess(targetCommend);
//        }
        return process;
    }

    public static class ActivityInformation {
        private String name;
        private String simpleName;
        private FragmentInformation curFragmentInformation;
        private List<FragmentInformation> informationList = new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSimpleName() {
            return simpleName;
        }

        public void setSimpleName(String simpleName) {
            this.simpleName = simpleName;
        }

        public FragmentInformation getCurFragmentInformation() {
            return curFragmentInformation;
        }

        public void setCurFragmentInformation(FragmentInformation curFragmentInformation) {
            this.curFragmentInformation = curFragmentInformation;
        }

        public List<FragmentInformation> getInformationList() {
            return informationList;
        }

        public void setInformationList(List<FragmentInformation> informationList) {
            this.informationList = informationList;
        }

        @Override
        public String toString() {
            return "ActivityInformation{" +
                    "name='" + name + '\'' +
                    ", simpleName='" + simpleName + '\'' +
                    ", informationList=" + informationList +
                    '}';
        }
    }

    public static class FragmentInformation {
        private String name;
        private String simpleName;
        private boolean added;
        private boolean hidden;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSimpleName() {
            return simpleName;
        }

        public void setSimpleName(String simpleName) {
            this.simpleName = simpleName;
        }

        public boolean isAdded() {
            return added;
        }

        public void setAdded(boolean added) {
            this.added = added;
        }

        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean hidden) {
            this.hidden = hidden;
        }

        @Override
        public String toString() {
            return "FragmentInformation{" +
                    "name='" + name + '\'' +
                    ", simpleName='" + simpleName + '\'' +
                    ", added=" + added +
                    ", hidden=" + hidden +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {
        //adb devices -l
        ActivityInformationTool.getActivityInformation();
    }
}
