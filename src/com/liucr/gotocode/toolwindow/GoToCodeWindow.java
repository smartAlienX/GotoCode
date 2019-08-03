package com.liucr.gotocode.toolwindow;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.liucr.gotocode.ClickEvent;
import com.liucr.gotocode.ClickEventLogcat;

import javax.swing.*;

public class GoToCodeWindow implements ClickEventLogcat.ClickEventListener {

    public final String START = "Start";
    public final String STOP = "Stop";

    private Project project;

    private JPanel myToolWindowContent;
    private JButton startButton;

    public GoToCodeWindow(Project project, ToolWindow toolWindow) {
        this.project = project;
        ClickEventLogcat.getInstance().addClickEventListener(this);
        initSwitchButton();
    }

    private void initSwitchButton() {
        if (ClickEventLogcat.getInstance().isStrat()) {
            startButton.setText(STOP);
        } else {
            startButton.setText(START);
        }
        startButton.addActionListener(e -> {
            if (START.equals(startButton.getText())) {
                ClickEventLogcat.getInstance().start();
                startButton.setText(STOP);
            } else {
                ClickEventLogcat.getInstance().stop();
                startButton.setText(START);
            }
        });
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }

    @Override
    public void onAddClickEvent(ClickEvent clickEvent) {
        openFile(clickEvent);
    }

    private void openFile(final ClickEvent clickEvent) {
        if (clickEvent.stackTraceElements.size() > 0) {
            ApplicationManager.getApplication().invokeLater(new Runnable() {
                public void run() {
                    ApplicationManager.getApplication().runReadAction(new Runnable() {
                        public void run() {
                            StackTraceElement stackTraceElement = clickEvent.stackTraceElements.get(0);
                            PsiClass psiClass = JavaPsiFacade.getInstance(project)
                                    .findClass(stackTraceElement.getClassName(), GlobalSearchScope.allScope(project));
                            PsiFile containingFile = null;
                            if (psiClass != null) {
                                containingFile = psiClass.getContainingFile();
                                VirtualFile virtualFile = containingFile.getVirtualFile();
                                OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, virtualFile, stackTraceElement.getLineNumber(), 10);
                                openFileDescriptor.navigate(true);
                            }
                        }
                    });
                }
            });
        }
    }
}
