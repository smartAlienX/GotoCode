package com.liucr.gotocode.toolwindow;

import com.intellij.openapi.project.Project;
import com.liucr.gotocode.GoToFileUtil;
import com.liucr.gotocode.tool.ActivityInformationTool;
import com.liucr.gotocode.tool.ActivityInformationTool.ActivityInformation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GoToActivityTab extends BaseTab implements ActivityInformationTool.ActivityInformationCallback {

    private JPanel content;
    private JButton getTheCurrentActivityButton;
    private JLabel currentActivity;
    private JLabel activityLayout;
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
                ActivityInformationTool.getActivityInformation(project, GoToActivityTab.this);
            }
        });

        currentActivity.addMouseListener(new FocusMouseAdapter(currentActivity) {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (activityInformation != null) {
                    String name = activityInformation.getName();
                    GoToFileUtil.openFile(project, name);
                }
            }
        });

        activityLayout.addMouseListener(new FocusMouseAdapter(activityLayout) {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (activityInformation != null) {
                    String name = activityInformation.getLayoutName();
                    int start = name.lastIndexOf(".") + 1;
                    name = name.substring(start);
                    GoToFileUtil.openFile(project, name);
                }
            }
        });

        currentFragment.addMouseListener(new FocusMouseAdapter(currentFragment) {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (activityInformation != null
                        && activityInformation.getCurFragmentInformation() != null) {
                    String name = activityInformation.getCurFragmentInformation().getSimpleName();
                    GoToFileUtil.openFile(project, name);
                }
            }
        });
    }

    public void update() {
        if (activityInformation == null) {
            return;
        }
        currentActivity.setText(activityInformation.getSimpleName());
        activityLayout.setText(activityInformation.getLayoutName());

        if (activityInformation.getCurFragmentInformation() != null) {
            currentFragment.setText(activityInformation.getCurFragmentInformation().getSimpleName());
        } else {
            currentFragment.setText(null);
        }

        if (fragmentListPanel.getComponentCount() != 0) {
            fragmentListPanel.removeAll();
        }
        List<ActivityInformationTool.FragmentInformation> informationList = activityInformation.getInformationList();
        for (ActivityInformationTool.FragmentInformation fragmentInformation : informationList) {
            JLabel jLabel = new JLabel(fragmentInformation.getSimpleName());
            jLabel.addMouseListener(new FocusMouseAdapter(jLabel) {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    GoToFileUtil.openFile(project, fragmentInformation.getSimpleName());
                }
            });
            fragmentListPanel.add(jLabel);
            fragmentListPanel.validate();
        }
    }

    public Component getComponent() {
        return content;
    }

    @Override
    public void onActivityInformation(ActivityInformation activityInformation) {
        this.activityInformation = activityInformation;
        update();
    }

    public class FocusMouseAdapter extends MouseAdapter {

        private JLabel jLabel;
        private Color color;

        public FocusMouseAdapter(JLabel jPanel) {
            this.jLabel = jPanel;
            color = jPanel.getForeground();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            jLabel.setForeground(Color.decode("#5188BA"));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            jLabel.setForeground(color);
        }
    }
}
