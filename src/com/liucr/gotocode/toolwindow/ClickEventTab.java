package com.liucr.gotocode.toolwindow;

import com.intellij.openapi.project.Project;
import com.liucr.gotocode.ClickEvent;
import com.liucr.gotocode.ClickEventLogcat;
import com.liucr.gotocode.GoToFileUtil;

import javax.swing.*;
import java.awt.*;

public class ClickEventTab implements ClickEventLogcat.ClickEventListener {

    public final String START = "Start";
    public final String STOP = "Stop";

    private Project project;

    private JPanel clickEventTab;
    private JButton clickEventSwitch;
    private JList<ClickEvent> clickEventList;

    private DefaultComboBoxModel<ClickEvent> clickEventData = new DefaultComboBoxModel<>();

    public ClickEventTab(Project project) {
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


                return new ClickEventItem(project, value).getComponent();
            }
        });
    }

    @Override
    public void onAddClickEvent(ClickEvent clickEvent) {
        openFile(clickEvent);
        clickEventData.insertElementAt(clickEvent, 0);
    }

    private void openFile(final ClickEvent clickEvent) {
        GoToFileUtil.openFile(project, clickEvent);
    }

    public Component getComponent() {
        return clickEventTab;
    }
}
