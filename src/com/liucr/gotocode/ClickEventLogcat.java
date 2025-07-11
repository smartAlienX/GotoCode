package com.liucr.gotocode;

import com.intellij.openapi.application.ApplicationManager;
import com.liucr.gotocode.adb.Adb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ClickEventLogcat implements Config.UpdateListener {

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());
    private Process logcatProcess;
    private BufferedReader bufferedReader;
    private boolean logcatRun = false;

    private List<ClickEventListener> eventListeners = new ArrayList<>();
    private List<ClickEvent> clickEventList = new ArrayList<>();

    private ClickEventLogcat() {
        Config.getInstance().addUpdateListener(this);
    }

    public void start() {

        if (logcatRun) {
            return;
        }

        if (logcatProcess == null || logcatProcess.isAlive()) {
            String targetDeviceId = Config.getInstance().getLogcatTargetDevice();
            if (targetDeviceId == null) {
                logcatProcess = Adb.getInstance().getCommendProcess("logcat", "ClickEvent:D", "*:S");
            } else {
                logcatProcess = Adb.getInstance()
                        .getCommendProcess("-s", targetDeviceId, "logcat", "ClickEvent:D", "*:S");
            }
        }

        if (logcatProcess == null) {
            return;
        }

        InputStream inputStream = logcatProcess.getInputStream();
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        logcatRun = true;
        long time = System.currentTimeMillis();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (logcatRun) {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            return;
                        }
                        System.out.println(this + "  >>  " + line);
                        if (System.currentTimeMillis() - time > 2000) {
                            parseLogcat(line);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        bufferedReader.close();
                        logcatProcess.destroy();
                        logcatProcess = null;
                        bufferedReader = null;
                        logcatRun = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void stop() {
        logcatRun = false;
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bufferedReader = null;
        }
        if (logcatProcess != null) {
            logcatProcess.destroy();
            logcatProcess = null;
        }
    }

    public boolean isStart() {
        return logcatRun;
    }

    public void addClickEventListener(ClickEventListener clickEventListener) {
        if (clickEventListener == null
                || eventListeners.contains(clickEventListener)) {
            return;
        }
        eventListeners.add(clickEventListener);
    }

    public void removeClickEventListener(ClickEventListener clickEventListener) {
        eventListeners.remove(clickEventListener);
    }

    private void parseLogcat(String line) {
        ClickEvent clickEvent = ClickEvent.parse(line);
        if (clickEvent == null) {
            return;
        }
        clickEventList.add(0, clickEvent);
        System.out.println(clickEvent.toString());
        ApplicationManager.getApplication().invokeLater(() -> {
            for (ClickEventListener eventListener : eventListeners) {
                eventListener.onAddClickEvent(clickEvent);
            }
            System.out.println("invokeLater onAddClickEvent");
        });
    }

    @Override
    public void onConfigUpdate(String configName, Object configValue) {
        if (configName.equals(Config.LOGCAT_TARGET_DEVICE)) {

        }
    }

    public interface ClickEventListener {
        void onAddClickEvent(ClickEvent clickEvent);
    }

    private static class LazyHolder {
        private static final ClickEventLogcat INSTANCE = new ClickEventLogcat();
    }

    public static ClickEventLogcat getInstance() {
        return LazyHolder.INSTANCE;
    }
}
