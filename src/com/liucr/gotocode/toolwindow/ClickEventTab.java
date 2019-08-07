package com.liucr.gotocode.toolwindow;

import com.intellij.openapi.project.Project;
import com.liucr.gotocode.ClickEvent;
import com.liucr.gotocode.ClickEventLogcat;
import com.liucr.gotocode.GoToFileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ClickEventTab implements ClickEventLogcat.ClickEventListener, ClickEventItem.OnClickEventItemRemove {

    public final String START = "Start";
    public final String STOP = "Stop";

    private Project project;

    private JPanel clickEventTab;
    private JButton clickEventSwitch;
    private JPanel clickEventListPanel;
    private JButton deleteAllButton;

    private DefaultComboBoxModel<ClickEvent> clickEventData = new DefaultComboBoxModel<>();

    public ClickEventTab(Project project) {
        this.project = project;
        initSwitchButton();
        initClickEventList();
        ClickEventLogcat.getInstance().addClickEventListener(this);

        deleteAllButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickEventListPanel.removeAll();
            }
        });
    }

    private void initSwitchButton() {
        if (ClickEventLogcat.getInstance().isStart()) {
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
        GridLayout gridLayout = new GridLayout(0, 1);
        clickEventListPanel.setLayout(gridLayout);
    }

    @Override
    public void onAddClickEvent(ClickEvent clickEvent) {
        openFile(clickEvent);
        clickEventListPanel.add(new ClickEventItem(project, clickEvent, this).getComponent(), 0);
    }

    @Override
    public void remove(ClickEventItem clickEventItem) {
        clickEventListPanel.remove(clickEventItem.getComponent());
    }

    private void openFile(final ClickEvent clickEvent) {
        GoToFileUtil.openFile(project, clickEvent);
    }

    public Component getComponent() {
        return clickEventTab;
    }


}
