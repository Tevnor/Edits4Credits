package org.editor.tools.drawingtool.attributes;

import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public abstract class AbstractGeneral {

    private final double rotation, lineWidth, alpha;
    private final BlendMode bm;
    private final Paint color;
    private final boolean fill;
    private final ArrayList<Effect> effects;

    protected AbstractGeneral(double rotation, BlendMode bm, double lineWidth, Paint color, double alpha, boolean fill) {
        this.rotation = rotation;
        this.bm = bm;
        this.lineWidth = lineWidth;
        this.color = color;
        this.alpha = alpha;
        this.fill = fill;
        this.effects = new ArrayList<>();
    }
    protected AbstractGeneral(AbstractGeneral abstractGeneral){
        this.rotation = abstractGeneral.getRotation();
        this.bm = abstractGeneral.getBm();
        this.lineWidth = abstractGeneral.getLineWidth();
        this.color = abstractGeneral.getColor();
        this.alpha = abstractGeneral.getAlpha();
        this.fill = abstractGeneral.isFill();
        this.effects = abstractGeneral.getEffects();
    }


    public ArrayList<Effect> getEffects() {
        return new ArrayList<>(effects);
    }
    public void addEffect(Effect e){
        effects.add(e);
    }

    public double getRotation() {
        return rotation;
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
    public double getAlpha() {
        return alpha;
    }
    public boolean isFill() {
        return fill;
    }

}
