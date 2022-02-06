package org.editor.tools.filterTool.filterControl.effects;

import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.editor.tools.filterTool.filterControl.Effect;

public class SepiaEffect implements Effect {
    @Override
    public Image applyEffect(Image image, double factor) {
        SepiaTone sepiaTone = new SepiaTone();
        sepiaTone.setLevel(factor / 100);

        ImageView imageView = new ImageView(image);
        imageView.setEffect(sepiaTone);

        return imageView.snapshot(null, null);
    }
}
