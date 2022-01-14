package org.controller.tools.drawingtool.graphiccontrol;

import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

import java.util.ArrayList;

public class Attributes {

    private double rotation = 0;
    private ArrayList<Effect> effects = new ArrayList<>();
    private BlendMode bm = BlendMode.SRC_OVER;
    private double lineWidth = 1;
    private Paint color = Color.BLACK;
    private Font font;
    private TextAlignment txtAlignment = TextAlignment.LEFT;
    private double alpha = 1.0;
    private boolean fill = true;
    private ArcType arcType = ArcType.ROUND;
    private boolean polyClose = true;
    private double startAngle = 0, arcExtent = 180 , arcHeight = 0, arcWidth = 0;
    private String content = "";

    public double getRotation() {
        return rotation;
    }
    public ArrayList<Effect> getEffects() {
        return effects;
    }
    public BlendMode getBm() {
        return bm;
    }
    public double getLineWidth() {
        return lineWidth;
    }
    public Paint getColor() {
        return color;
    }
    public Font getFont() {
        return font;
    }
    public TextAlignment getTxtAlignment() {
        return txtAlignment;
    }
    public double getAlpha() {
        return alpha;
    }
    public boolean isFill() {
        return fill;
    }
    public ArcType getArcType() {
        return arcType;
    }
    public boolean isPolyClose() {
        return polyClose;
    }
    public double getStartAngle() {
        return startAngle;
    }
    public double getArcExtent() {
        return arcExtent;
    }
    public double getArcHeight() {
        return arcHeight;
    }
    public double getArcWidth() {
        return arcWidth;
    }
    public String getContent() {
        return content;
    }


    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
    public void setBm(BlendMode bm) {
        this.bm = bm;
    }
    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }
    public void setColor(Paint color) {
        this.color = color;
    }
    public void setFont(Font font) {
        this.font = font;
    }
    public void setTxtAlignment(TextAlignment txtAlignment) {
        this.txtAlignment = txtAlignment;
    }
    public void setStartAngle(double startAngle) {
        this.startAngle = startAngle;
    }
    public void setArcExtent(double arcExtent) {
        this.arcExtent = arcExtent;
    }
    public void setArcHeight(double arcHeight) {
        this.arcHeight = arcHeight;
    }
    public void setArcWidth(double arcWidth) {
        this.arcWidth = arcWidth;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setFill(boolean fill) {
        this.fill = fill;
    }
    public void addEffect(Effect effect){
        effects.add(effect);
    }
    public void setAlpha(double a){
        this.alpha = a;
    }
    public void setArcType(ArcType t){
        this.arcType = t;
    }
    public void setPolyClose(boolean polyClose) {
        this.polyClose = polyClose;
    }

}
