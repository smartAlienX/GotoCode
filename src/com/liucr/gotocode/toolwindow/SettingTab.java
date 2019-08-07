package com.liucr.gotocode.toolwindow;

import com.intellij.openapi.project.Project;
import com.liucr.gotocode.Config;
import com.liucr.gotocode.adb.Adb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class SettingTab {

    private Project project;

    private JPanel settingTab;
    private JCheckBox openFileByClickCheckBox;
    private JButton refreshButton;
    private JPanel deviceListPanel;

    public SettingTab(Project project) {
        this.project = project;

        openFileByClickCheckBox.setSelected(Config.getInstance().isOpenFileByClick());
        openFileByClickCheckBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Config.getInstance().setOpenFileByClick(openFileByClickCheckBox.isSelected());
            }
        });

        refreshButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getDeviceList();
            }
        });

        GridLayout gridLayout = new GridLayout(0, 1);
        deviceListPanel.setLayout(gridLayout);
        getDeviceList();
    }

    private void getDeviceList() {
        List<Adb.Device> deviceList = Adb.getInstance().getDeviceList();
        deviceListPanel.removeAll();
        ButtonGroup buttonGroup = new ButtonGroup();
        for (Adb.Device device : deviceList) {
            JRadioButton jRadioButton = new JRadioButton();
            jRadioButton.setText(device.name + " : " + device.deviceNumber);
            deviceListPanel.add(jRadioButton);
            buttonGroup.add(jRadioButton);

            jRadioButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(device.toString());
                }
            });
        }
    }

    public Component getComponent() {
        return settingTab;
    }
}
