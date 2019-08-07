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
    private JButton deleteButton;

    private ClickEvent clickEvent;
    private DefaultComboBoxModel<StackTraceElement> clickEventData = new DefaultComboBoxModel<>();

    public ClickEventItem(Project project, ClickEvent clickEvent, OnClickEventItemRemove itemRemove) {
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

        deleteButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemRemove.remove(ClickEventItem.this);
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
                JLabel label = new JLabel();
                label.setText(value.getFileName() + "(" + value.getLineNumber() + ")" + ":" + value.getMethodName() + "()");
                label.requestFocus();
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

    public interface OnClickEventItemRemove {
        void remove(ClickEventItem clickEventItem);
    }
}
