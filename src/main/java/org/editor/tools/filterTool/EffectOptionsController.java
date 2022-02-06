package org.editor.tools.filterTool;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.EditorController;
import org.editor.tools.filterTool.filterControl.Effect;
import org.editor.tools.filterTool.filterControl.effects.EffectType;
import java.net.URL;
import java.util.ResourceBundle;

public class EffectOptionsController implements Initializable {

    private static final Logger EOC_LOGGER = LogManager.getLogger(EffectOptionsController.class);

    @FXML
    private Label effectOptionsLabel;
    @FXML
    private Button applyEffectButton;
    @FXML
    private Button cancelEffectButton;
    @FXML
    private Button closeEffectButton;
    @FXML
    private VBox effectFactorVBox;
    @FXML
    private Slider effectSlider;
    @FXML
    private VBox adjustmentFactorVBox;
    @FXML
    private Slider adjustmentSlider;

    private final EditorController editorController;
    private final Image originalImage;
    private final Image editorImage;
    private Effect effect;

    private double factor;
    private Stage stage;

    public EffectOptionsController(Image originalImage, Image editorImage, EditorController editorController) {
        this.originalImage = originalImage;
        this.editorImage = editorImage;
        this.editorController = editorController;

        EOC_LOGGER.debug("New EffectOptionsController object instantiated: {}.", this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        applyEffectButton.setOnAction(actionEvent -> handleApplyEffect());

        cancelEffectButton.setOnAction(actionEvent -> handleCancelEffect());
        closeEffectButton.setOnAction(actionEvent -> handleCancelEffect());

        adjustmentSlider.setOnMouseReleased(mouseEvent -> handleAdjustmentSlider());
        effectSlider.setOnMouseReleased(mouseEvent -> handleEffectSlider());
    }


    private void applyEffect() {
        Image originalPostEffectImage = effect.applyEffect(originalImage, factor);
        Image editorPostEffectImage = effect.applyEffect(editorImage, factor);

        // Update both filtered images
        editorController.setFilteredImages(originalPostEffectImage, editorPostEffectImage);
        // Draw both to their respective canvas
        editorController.drawFilteredImages();

        EOC_LOGGER.debug("Applying effect {} on {} and {}.", effect, originalImage, editorImage);
    }

    private void previewEffect() {
        Image effectPreviewImage = effect.applyEffect(editorImage, factor);
        editorController.drawPreviewImage(effectPreviewImage);

        EOC_LOGGER.debug("Creating preview for {}.", effect);
    }

    /**
     * User Input
     * */
    @FXML
    private void handleEffectSlider() {
        factor = (effectSlider.getValue());
        previewEffect();

        EOC_LOGGER.debug("Value of {} set to: {}.", effectSlider.getId(), factor);
    }
    @FXML
    private void handleAdjustmentSlider() {
        factor = (adjustmentSlider.getValue());
        previewEffect();

        EOC_LOGGER.debug("Value of {} set to: {}.", adjustmentSlider.getId(), factor);
    }

    @FXML
    private void handleApplyEffect() {
        applyEffect();
        stage = (Stage) applyEffectButton.getScene().getWindow();
        stage.close();

        EOC_LOGGER.debug("Stage closed via {}.", applyEffectButton.getId());
    }

    @FXML
    private void handleCancelEffect() {
        editorController.drawPreviousImage();

        stage = (Stage) cancelEffectButton.getScene().getWindow();
        stage.close();

        EOC_LOGGER.debug("Stage closed via {}.", cancelEffectButton.getId());
    }

    public void setEffectView(EffectType effectType) {
        this.effect = EffectType.TYPE_TO_EFFECT_ENUM_MAP.get(effectType);

        factor = 0;
        adjustmentFactorVBox.setVisible(false);
        effectFactorVBox.setVisible(false);

        switch (effectType) {
            case BLUR:
                effectOptionsLabel.setText("Blur Effect");
                effectFactorVBox.setVisible(true);
                break;
            case SEPIA:
                effectOptionsLabel.setText("Sepia Effect");
                effectFactorVBox.setVisible(true);
                break;
            case DISPLACE:
                effectOptionsLabel.setText("Reflection Effect");
                effectFactorVBox.setVisible(true);
                break;
            case BRIGHTNESS:
                effectOptionsLabel.setText("Brightness Adjustment");
                adjustmentFactorVBox.setVisible(true);
                break;
            case CONTRAST:
                effectOptionsLabel.setText("Contrast Adjustment");
                adjustmentFactorVBox.setVisible(true);
                break;
            case SATURATION:
                effectOptionsLabel.setText("Saturation Adjustment");
                adjustmentFactorVBox.setVisible(true);
                break;
            default:
        }
        EOC_LOGGER.debug("Stage set to {}.", effectType);
    }
}
