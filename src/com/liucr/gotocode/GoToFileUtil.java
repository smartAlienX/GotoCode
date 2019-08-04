package com.liucr.gotocode;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;

public class GoToFileUtil {

    public static void openFile(Project project, ClickEvent clickEvent) {
        if (clickEvent.stackTraceElements.size() > 0) {
            openFile(project, clickEvent.stackTraceElements.get(0));
        }
    }

    public static void openFile(Project project, StackTraceElement traceElement) {
        ApplicationManager.getApplication().invokeLater(() ->
                ApplicationManager.getApplication().runReadAction(() -> {
                    PsiClass psiClass = JavaPsiFacade.getInstance(project)
                            .findClass(traceElement.getClassName(), GlobalSearchScope.allScope(project));
                    PsiFile containingFile = null;
                    if (psiClass != null) {
                        containingFile = psiClass.getContainingFile();
                        VirtualFile virtualFile = containingFile.getVirtualFile();
                        OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, virtualFile, traceElement.getLineNumber(), 10);
                        openFileDescriptor.navigate(true);
                    }
                }));
    }

}
