package com.liucr.gotocode;

import com.intellij.lang.jvm.JvmMethod;
import com.intellij.lang.jvm.JvmParameter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;

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

    public static void openFile(Project project, String className) {
        ApplicationManager.getApplication().invokeLater(() ->
                ApplicationManager.getApplication().runReadAction(() -> {
                    PsiClass psiClass = JavaPsiFacade.getInstance(project)
                            .findClass(className, GlobalSearchScope.allScope(project));
                    PsiFile containingFile = null;
                    if (psiClass != null) {
                        containingFile = psiClass.getContainingFile();
                    } else {
                        PsiFile[] filesByName = PsiShortNamesCache.getInstance(project).getFilesByName(className + ".java");
                        if (filesByName.length == 0) {
                            filesByName = PsiShortNamesCache.getInstance(project).getFilesByName(className + ".kt");
                        }
                        if (filesByName.length == 0) {
                            filesByName = PsiShortNamesCache.getInstance(project).getFilesByName(className + ".xml");
                        }
                        if (filesByName.length > 0) {
                            containingFile = filesByName[0];
                        }
                    }
                    if (containingFile != null) {
                        VirtualFile virtualFile = containingFile.getVirtualFile();
                        OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, virtualFile);
                        openFileDescriptor.navigate(true);
                    }
                }));
    }

}
