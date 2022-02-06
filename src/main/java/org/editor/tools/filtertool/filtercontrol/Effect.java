package org.editor.tools.filtertool.filtercontrol;

import javafx.scene.image.Image;

/**
 * The interface for the image effects
 * */
public interface Effect {
    /**
     * Apply the effect on the image according to the interface implementation
     * @param image the image the effect is applied to
     * @param factor the input variable the effect uses to gauge its strength
     * @see Image
     * */

    Image applyEffect(Image image, double factor);
}
