package org.editor.tools.filterTool.filterControl.effects;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.editor.tools.filterTool.filterControl.Effect;

public class BlurEffect implements Effect {
    @Override
    public Image applyEffect(Image image, double factor) {
        ImageView imageView = new ImageView(image);

        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(factor / 1.5);

        imageView.setEffect(gaussianBlur);

        return imageView.snapshot(null, null);
    }
}
