package org.editor.tools.filtertool.filtercontrol.effects;

import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.FloatMap;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.editor.tools.filtertool.filtercontrol.Effect;

public class DisplacementEffect implements Effect {
    @Override
    public Image applyEffect(Image image, double factor) {
        int width = (int) factor * 5;
        int height = (int) factor * 2;

        FloatMap floatMap = new FloatMap();
        floatMap.setWidth(width);
        floatMap.setHeight(height);

        for (int i = 0; i < width; i++) {
            double v = (Math.sin(i / 20.0 * Math.PI) - 0.5) / 40.0;
            for (int j = 0; j < height; j++) {
                floatMap.setSamples(i, j, 0.0f, (float) v);
            }
        }
        DisplacementMap displacementMap = new DisplacementMap();
        displacementMap.setMapData(floatMap);

        ImageView imageView = new ImageView(image);
        imageView.setEffect(displacementMap);

        return imageView.snapshot(null, null);
    }
}
