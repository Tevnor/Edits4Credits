package org.editor.tools.filtertool;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.editor.EditorController;
import org.editor.tools.filtertool.filtercontrol.Effect;
import org.editor.tools.filtertool.filtercontrol.effects.EffectType;
import java.net.URL;
import java.util.ResourceBundle;

public class EffectOptionsController implements Initializable {

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
    }

    private void previewEffect() {
        Image effectPreviewImage = effect.applyEffect(editorImage, factor);
        editorController.drawPreviewImage(effectPreviewImage);
    }

    /**
     * User Input
     * */
    @FXML
    private void handleEffectSlider() {
        factor = (effectSlider.getValue());
        previewEffect();
    }
    @FXML
    private void handleAdjustmentSlider() {
        factor = (adjustmentSlider.getValue());
        previewEffect();
    }

    @FXML
    private void handleApplyEffect() {
        applyEffect();
        stage = (Stage) applyEffectButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancelEffect() {
        editorController.drawPreviousImage();

        stage = (Stage) cancelEffectButton.getScene().getWindow();
        stage.close();
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
    }
}
