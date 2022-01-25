package org.editor.layout;

import org.editor.EditorController;

public class EditorControllerLayoutManager {


    public double getStackWidth(EditorController editorController) {

        double returnWidth;
        // define the maximum size for the stack pane based on the monitor size
        int maxStackHeight = (int) (editorController.getWindow().getScreenHeight() - editorController.getMenuBarHeight());
        int maxStackWidth = (int) (editorController.getWindow().getScreenWidth() - editorController.getToolBarWidth());

        // when the aspect ratio is greater than 1 calculate based on the width
        if (useWidthOrHeight(editorController)) {
            double stackHeight = Math.round(editorController.getWindow().getScreenHeight() - editorController.getMenuBarHeight());
            double stackWidth = Math.round(stackHeight * editorController.getProject().getProjectAspectRatio());
            returnWidth = stackWidth;

            if (stackWidth > maxStackWidth) {
                stackWidth = maxStackWidth;
                returnWidth = stackWidth;
                stackHeight = Math.round(stackWidth * editorController.getProject().getProjectAspectRatio());
            }

        }
        // when the aspect ratio is smaller than 1 calculate based on height
        else {
            double stackWidth = Math.round(editorController.getWindow().getScreenWidth() - editorController.getToolBarWidth());
            double stackHeight = Math.round(stackWidth * (1 / editorController.getProject().getProjectAspectRatio()));
            returnWidth = stackWidth;

            if (stackHeight > maxStackHeight) {
                stackHeight = maxStackHeight;
                stackWidth = Math.round(stackHeight * editorController.getProject().getProjectAspectRatio());
                returnWidth = stackWidth;
            }
        }
        return returnWidth;
    }
    public double getStackHeight(EditorController editorController) {

        double returnHeight;
        // define the maximum size for the stack pane based on the monitor size
        int maxStackHeight = (int) (editorController.getWindow().getScreenHeight() - editorController.getMenuBarHeight());
        int maxStackWidth = (int) (editorController.getWindow().getScreenWidth() - editorController.getToolBarWidth());

        // when the aspect ratio is greater than 1 calculate based on the width
        if (useWidthOrHeight(editorController)) {
            double stackHeight = Math.round(editorController.getWindow().getScreenHeight() - editorController.getMenuBarHeight());
            double stackWidth = Math.round(stackHeight * editorController.getProject().getProjectAspectRatio());
            returnHeight = stackHeight;

            if (stackWidth > maxStackWidth) {
                stackWidth = maxStackWidth;
                stackHeight = Math.round(stackWidth * editorController.getProject().getProjectAspectRatio());
                returnHeight = stackHeight;

            }
        }
        // when the aspect ratio is smaller than 1 calculate based on height
        else {
            double stackWidth = Math.round(editorController.getWindow().getScreenWidth() - editorController.getToolBarWidth());
            double stackHeight = Math.round(stackWidth * (1 / editorController.getProject().getProjectAspectRatio()));
            returnHeight = stackHeight;

            if (stackHeight > maxStackHeight) {
                stackHeight = maxStackHeight;
                stackWidth = Math.round(stackHeight * editorController.getProject().getProjectAspectRatio());
                returnHeight = stackHeight;
            }
        }
        return returnHeight;
    }
    public boolean useWidthOrHeight(EditorController editorController){
        return !(editorController.getProject().getProjectAspectRatio() > 1);
    }
}
