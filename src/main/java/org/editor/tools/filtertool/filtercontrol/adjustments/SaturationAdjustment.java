package org.editor.tools.filtertool.filtercontrol.adjustments;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.editor.tools.filtertool.filtercontrol.Effect;

public class SaturationAdjustment implements Effect {
    @Override
    public Image applyEffect(Image image, double factor) {

        ColorAdjust brightnessAdjust = new ColorAdjust();
        brightnessAdjust.setSaturation(factor / 100);

        ImageView imageView = new ImageView(image);
        imageView.setEffect(brightnessAdjust);

        return imageView.snapshot(null, null);
    }
}
