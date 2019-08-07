package com.liucr.gotocode.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public class GoToCodeWindow {

    public final String START = "Start";
    public final String STOP = "Stop";

    private Project project;

    private JPanel myToolWindowContent;
    private JTabbedPane tabbedPane;

    public GoToCodeWindow(Project project, ToolWindow toolWindow) {
        this.project = project;
        tabbedPane.add("ClickEvent",new ClickEventTab(project).getComponent());
        tabbedPane.add("Setting",new SettingTab(project).getComponent());
    }

    public JComponent getComponent(){
        return myToolWindowContent;
    }

}
