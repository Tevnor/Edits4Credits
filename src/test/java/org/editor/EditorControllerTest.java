package org.editor;


import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EditorControllerTest {
    JFXPanel jfxPanel = new JFXPanel();
    private Canvas testCanvas;
    private GraphicsContext gc;
    private Image testImage = new Image("https://www.chip.de/ii/4/7/2/8/5/5/4/f8c3bf084e08658b.jpg");

    /*@Override
    protected EditorController getEditorController() {
        EditorController tester;
        return tester;

    @Test
    public void createImageFromCanvasTest(){
        tester = new EditorController();
        testCanvas = new Canvas(1000,1000);
        gc = testCanvas.getGraphicsContext2D();
        gc.drawImage(testImage, 0,0,1000,1000);

        assertNotNull("Doesn't return image.", tester.createImageFromCanvas(testCanvas));
    */



}