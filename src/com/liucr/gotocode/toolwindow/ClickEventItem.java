package com.liucr.gotocode.toolwindow;

import com.intellij.openapi.project.Project;
import com.liucr.gotocode.ClickEvent;
import com.liucr.gotocode.GoToFileUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClickEventItem {

    private Project project;

    private JPanel itemPanel;
    private JLabel itemName;
    private JList<StackTraceElement> childList;
    private JButton button1;

    private ClickEvent clickEvent;
    private DefaultComboBoxModel<StackTraceElement> clickEventData = new DefaultComboBoxModel<>();

    public ClickEventItem(Project project, ClickEvent clickEvent) {
        this.project = project;
        itemName.setText(clickEvent.getName());
        this.clickEvent = clickEvent;
        initClickEventList();

        itemName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                GoToFileUtil.openFile(project, clickEvent);
            }
        });

        button1.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GoToFileUtil.openFile(project, clickEvent);
            }
        });
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
                JButton label = new JButton();
                label.setText(value.getFileName() + " : " + value.getMethodName());
                label.requestFocus();
                label.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GoToFileUtil.openFile(project, value);
                    }
                });
                return label;
            }
        });

        childList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int index = childList.locationToIndex(e.getPoint());
                StackTraceElement element = clickEventData.getElementAt(index);
                GoToFileUtil.openFile(project, element);
            }
        });
    }

    public JComponent getComponent() {
        return itemPanel;
    }


}
