package com.liucr.gotocode.toolwindow;

import com.intellij.openapi.project.Project;
import com.liucr.gotocode.ClickEvent;

import javax.swing.*;
import java.awt.*;

public class ClickEventItem {

    private JPanel itemPanel;
    private JLabel itemName;
    private JList<StackTraceElement> childList;

    private ClickEvent clickEvent;
    private DefaultComboBoxModel<StackTraceElement> clickEventData = new DefaultComboBoxModel<>();

    public ClickEventItem(Project project, ClickEvent clickEvent) {
        itemName.setText(clickEvent.getName());
        this.clickEvent = clickEvent;
        initClickEventList();
    }


    private void initClickEventList() {
        for (StackTraceElement element : clickEvent.stackTraceElements) {
            clickEventData.addElement(element);
        }
        childList.setModel(clickEventData);
        childList.setCellRenderer(new ListCellRenderer<StackTraceElement>() {

            @Override
            public Component getListCellRendererComponent(JList<? extends StackTraceElement> list,
                                                          StackTraceElement value,
                                                          int index,
                                                          boolean isSelected,
                                                          boolean cellHasFocus) {
                Label label = new Label();
                label.setText(value.getFileName() + " : " + value.getMethodName());
                return label;
            }
        });
    }

    public JComponent getComponent() {
        return itemPanel;
    }


}
