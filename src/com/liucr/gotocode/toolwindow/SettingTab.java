package com.liucr.gotocode.toolwindow;

import com.intellij.openapi.project.Project;
import com.liucr.gotocode.ClickEventLogcat;
import com.liucr.gotocode.Config;
import com.liucr.gotocode.Log;
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

        openFileByClickSetting();
        deviceListSetting();
    }

    private void openFileByClickSetting() {
        openFileByClickCheckBox.setSelected(Config.getInstance().isOpenFileByClick());
        openFileByClickCheckBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Config.getInstance().setOpenFileByClick(openFileByClickCheckBox.isSelected());
            }
        });
    }

    private void deviceListSetting() {
        refreshButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDeviceListData();
            }
        });

        GridLayout gridLayout = new GridLayout(0, 1);
        deviceListPanel.setLayout(gridLayout);
        setDeviceListData();
    }

    private void setDeviceListData() {
        List<Adb.Device> deviceList = Adb.getInstance().getDeviceList();
        deviceListPanel.removeAll();
        ButtonGroup buttonGroup = new ButtonGroup();
        String curTargetDevice = Config.getInstance().getLogcatTargetDevice();
        for (Adb.Device device : deviceList) {
            JRadioButton jRadioButton = new JRadioButton();
            jRadioButton.setText(device.name + " : " + device.deviceNumber);
            deviceListPanel.add(jRadioButton);
            buttonGroup.add(jRadioButton);

            if (device.deviceNumber.equals(curTargetDevice)) {
                jRadioButton.setSelected(true);
            } else {
                jRadioButton.setSelected(false);
            }

            jRadioButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Config.getInstance().setLogcatTargetDevice(device.deviceNumber);
                }
            });
        }
    }

    public Component getComponent() {
        return settingTab;
    }
}
