package com.liucr.gotocode.toolwindow;

import com.intellij.openapi.project.Project;
import com.liucr.gotocode.ClickEvent;

import javax.swing.*;

public class ClickEventItem {

    private JLabel itemName;
    private JPanel itemPanel;

    public ClickEventItem(Project project, ClickEvent clickEvent) {
        itemName.setText(clickEvent.toString());
    }

    public JComponent getComponent() {
        return itemPanel;
    }

}
