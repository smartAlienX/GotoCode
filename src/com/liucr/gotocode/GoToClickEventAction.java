package com.liucr.gotocode;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.liucr.gotocode.adb.Adb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GoToClickEventAction extends AnAction {

    public static void main(String[] args) throws IOException {
        //adb devices -l
        new GoToClickEventAction().actionPerformed(null);
    }

    private Project project;
    private Process logcat;
    private List<ClickEvent> clickEvents = new ArrayList<ClickEvent>();

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        project = e.getData(PlatformDataKeys.PROJECT);
        if (logcat == null) {
            logcat = Adb.getInstance().getCommendProcess("logcat", "ClickEvent:D", "*:S");
            if (logcat == null) {
                return;
            }
            try {
                InputStream inputStream = logcat.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while (true) {
                    String line = bufferedReader.readLine();
                    System.out.println(line);
                    if (line == null) {
                        return;
                    }
                    parseLogcat(line);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    private void parseLogcat(String line) {
        ClickEvent clickEvent = ClickEvent.parse(line);
        clickEvents.add(clickEvent);
        System.out.println(clickEvent.toString());
        openFile(clickEvent);
    }

    private void openFile(ClickEvent clickEvent) {
        if (clickEvent.stackTraceElements.size() > 0) {
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
    }

}
