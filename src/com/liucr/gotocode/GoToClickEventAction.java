package com.liucr.gotocode;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.liucr.gotocode.adb.Adb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GoToClickEventAction extends AnAction {

    public static void main(String[] args) throws IOException {
        //adb devices -l
        new GoToClickEventAction().actionPerformed(null);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Process logcat = Adb.getInstance().getCommendProcess("logcat", "ClickEvent:D", "*:S");
        if (logcat == null) {
            return;
        }
        try {
            InputStream inputStream = logcat.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                String line = bufferedReader.readLine();
                System.out.println(line);
                if (line == null) {
                    return;
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
