package com.liucr.gotocode.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class GoToCodeWindowFactory implements ToolWindowFactory {


    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        GoToCodeWindow goToCodeWindow = new GoToCodeWindow(project, toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(goToCodeWindow.getComponent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

}
