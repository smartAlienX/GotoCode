package com.liucr.gotocode.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.liucr.gotocode.ClickEvent;
import com.liucr.gotocode.ClickEventLogcat;
import com.liucr.gotocode.GoToFileUtil;

import javax.swing.*;

public class GoToCodeWindow implements ClickEventLogcat.ClickEventListener {

    public final String START = "Start";
    public final String STOP = "Stop";

    private Project project;

    private JPanel myToolWindowContent;
    private JTabbedPane tabbedPane;
    private JPanel clickEventTab;
    private JPanel curActivityTab;
    private JButton clickEventSwitch;

    public GoToCodeWindow(Project project, ToolWindow toolWindow) {
        this.project = project;
        initSwitchButton();
        ClickEventLogcat.getInstance().addClickEventListener(this);
    }

    private void initSwitchButton() {
        if (ClickEventLogcat.getInstance().isStrat()) {
            clickEventSwitch.setText(STOP);
        } else {
            clickEventSwitch.setText(START);
        }
        clickEventSwitch.addActionListener(e -> {
            if (START.equals(clickEventSwitch.getText())) {
                ClickEventLogcat.getInstance().start();
                clickEventSwitch.setText(STOP);
            } else {
                ClickEventLogcat.getInstance().stop();
                clickEventSwitch.setText(START);
            }
        });
    }

    public JComponent getContent() {
        return myToolWindowContent;
    }

    @Override
    public void onAddClickEvent(ClickEvent clickEvent) {
        openFile(clickEvent);
    }

    private void openFile(final ClickEvent clickEvent) {
        GoToFileUtil.openFile(project, clickEvent);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
