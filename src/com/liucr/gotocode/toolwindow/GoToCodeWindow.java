package com.liucr.gotocode.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.liucr.gotocode.ClickEvent;
import com.liucr.gotocode.ClickEventLogcat;
import com.liucr.gotocode.GoToFileUtil;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.tree.TreePath;
import java.awt.*;

public class GoToCodeWindow implements ClickEventLogcat.ClickEventListener {

    public final String START = "Start";
    public final String STOP = "Stop";

    private Project project;

    private JPanel myToolWindowContent;
    private JTabbedPane tabbedPane;
    private JPanel clickEventTab;
    private JPanel curActivityTab;
    private JButton clickEventSwitch;
    private JList<ClickEvent> clickEventList;

    private DefaultComboBoxModel<ClickEvent> clickEventData = new DefaultComboBoxModel<>();

    public GoToCodeWindow(Project project, ToolWindow toolWindow) {
        this.project = project;

        initSwitchButton();
        initClickEventList();
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

    private void initClickEventList() {
        clickEventList.setModel(clickEventData);
        clickEventList.setCellRenderer(new ListCellRenderer<ClickEvent>() {

            @Override
            public Component getListCellRendererComponent(JList<? extends ClickEvent> list,
                                                          ClickEvent value,
                                                          int index,
                                                          boolean isSelected,
                                                          boolean cellHasFocus) {

                JButton jButton = new JButton();
                if (value.stackTraceElements.size() > 0) {
                    jButton.setText(value.stackTraceElements.get(0).getMethodName());
                }

                return jButton;
            }
        });
    }

    public JComponent getContent() {
        return myToolWindowContent;
    }

    @Override
    public void onAddClickEvent(ClickEvent clickEvent) {
        openFile(clickEvent);
        clickEventData.insertElementAt(clickEvent, 0);
    }

    private void openFile(final ClickEvent clickEvent) {
        GoToFileUtil.openFile(project, clickEvent);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
