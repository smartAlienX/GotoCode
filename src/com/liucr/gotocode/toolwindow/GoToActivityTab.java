package com.liucr.gotocode.toolwindow;

import com.intellij.openapi.project.Project;
import com.liucr.gotocode.tool.ActivityInformationTool;
import com.liucr.gotocode.tool.ActivityInformationTool.ActivityInformation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GoToActivityTab extends BaseTab {

    private JPanel content;
    private JButton getTheCurrentActivityButton;
    private JLabel currentActivity;
    private JLabel currentFragment;
    private JPanel fragmentListPanel;

    private ActivityInformation activityInformation;

    public GoToActivityTab(Project project) {
        super(project);
        initActionEvent();
        fragmentListPanel.setLayout(new BoxLayout(fragmentListPanel, BoxLayout.PAGE_AXIS));
    }

    private void initActionEvent() {
        getTheCurrentActivityButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activityInformation = ActivityInformationTool.getActivityInformation();
                update();
            }
        });

        currentActivity.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });

        currentFragment.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
    }

    public void update() {
        if (activityInformation == null) {
            return;
        }
        currentActivity.setText(activityInformation.getSimpleName());

        if (activityInformation.getCurFragmentInformation() != null) {
            currentFragment.setText(activityInformation.getCurFragmentInformation().getSimpleName());
        }else {
            currentFragment.setText(null);
        }

        if(fragmentListPanel.getComponentCount()!=0){
            fragmentListPanel.removeAll();
        }
        List<ActivityInformationTool.FragmentInformation> informationList = activityInformation.getInformationList();
        for (ActivityInformationTool.FragmentInformation fragmentInformation : informationList) {
            JLabel jLabel = new JLabel(fragmentInformation.getSimpleName());
            jLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                }
            });
            fragmentListPanel.add(jLabel);
            fragmentListPanel.validate();
        }
    }

    public Component getComponent() {
        return content;
    }
}
