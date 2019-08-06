package com.liucr.gotocode;

import com.intellij.openapi.application.ApplicationManager;
import com.liucr.gotocode.adb.Adb;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ClickEventLogcat {

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());
    private Process logcatProcess;
    private boolean logcatRun = false;

    private List<ClickEventListener> eventListeners = new ArrayList<>();
    private List<ClickEvent> clickEventList = new ArrayList<>();

    private ClickEventLogcat() {

    }

    public void start() {

        if (logcatRun) {
            return;
        }

        if (logcatProcess == null || logcatProcess.isAlive()) {
            logcatProcess = Adb.getInstance().getCommendProcess("logcat", "ClickEvent:D", "*:S");
        }

        if (logcatProcess == null) {
            return;
        }

        InputStream inputStream = logcatProcess.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        logcatRun = true;
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
                        parseLogcat(line);
                    }
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void stop() {
        logcatRun = false;
    }

    public boolean isStrat() {
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
        clickEventList.add(0, clickEvent);
        System.out.println(clickEvent.toString());

        ApplicationManager.getApplication().invokeLater(() -> {
            for (ClickEventListener eventListener : eventListeners) {
                eventListener.onAddClickEvent(clickEvent);
            }
        });
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
