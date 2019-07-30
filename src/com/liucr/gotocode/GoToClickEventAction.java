package com.liucr.gotocode;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.liucr.gotocode.adb.Adb;

public class GoToClickEventAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Adb.getInstance().commend("logcat");
    }
}
