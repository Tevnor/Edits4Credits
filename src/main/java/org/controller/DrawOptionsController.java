package org.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.controller.tools.drawingtool.graphiccontrol.Attributes;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class DrawOptionsController implements Initializable {
    @FXML
    private Slider sliderAlpha, sliderBloom, sliderGlow, sliderBoxBlur, sliderGaussianBlur, sliderMotionBlur;
    @FXML
    private Slider sliderTopOpacity, sliderFraction, sliderBottomOpacity, sliderSpreadDrop, sliderChokeInner;
    @FXML
    private ChoiceBox<BlendMode> cbBlendMode;
    @FXML
    private ChoiceBox<ArcType> cbArcType;
    @FXML
    private ChoiceBox<TextAlignment> cbTxtAlignment;
    @FXML
    private TextField txtRotation, txtArcStart, txtArcExtent, txtArcWidth, txtArcHeight, txtLineWidth;
    @FXML
    private TextField txtBoxBlurWidth, txtBoxBlurHeight, txtMotionBlurRadius, txtReflectionOffset;
    @FXML
    private TextField txtShadowWidth, txtShadowHeight, txtShadowRadius;
    @FXML
    private TextField txtDropWidth, txtDropHeight, txtDropOffsetX, txtDropOffsetY, txtDropRadius;
    @FXML
    private TextField txtInnerWidth, txtInnerHeight, txtInnerOffsetX, txtInnerOffsetY, txtInnerRadius;
    @FXML
    private TextArea txtContent;
    @FXML
    private ColorPicker cpStrokeFill, cpShadow, cpDrop, cpInner;
    @FXML
    private RadioButton radioStroke, radioFill;
    @FXML
    private CheckBox checkPolyClosed, applyBloom, applyGlow, applyBoxBlur, applyGaussianBlur, applyMotionBlur;
    @FXML
    private CheckBox applyReflection, applyShadow, applyDropShadow, applyInnerShadow;
    @FXML
    private ComboBox<String> cbFontFamily;
    @FXML
    private ComboBox<Double> cbFontSize;
    @FXML
    private ComboBox<BlurType> cbShadowBlurType, cbDropBlurType, cbInnerBlurType;
    @FXML
    private Button closeDrawOptions;
    @FXML
    private ButtonBar buttons;
    @FXML
    private ToggleGroup strokeFill;


    private Stage stage;
    private Point2D delta;
    private Attributes attributes;
    private final Bloom b = new Bloom();
    private final Glow g = new Glow();
    private final BoxBlur bB = new BoxBlur();
    private final GaussianBlur gB = new GaussianBlur();
    private final MotionBlur mB = new MotionBlur();
    private final Reflection r = new Reflection();
    private final Shadow s = new Shadow();
    private final DropShadow dS = new DropShadow();
    private final InnerShadow iS = new InnerShadow();

    public Attributes getAttributes(){
        return attributes;
    }
    public void setBloom(){
        b.setThreshold(sliderBloom.getValue());
    }
    public void setGlow(){
        g.setLevel(sliderGlow.getValue());
    }
    public void setBoxBlurIterations(){
        bB.setIterations((int)sliderBoxBlur.getValue());
    }
    public void setGaussianBlur(){
        gB.setRadius(sliderGaussianBlur.getValue());
    }
    public void setMotionBlur(){
        mB.setAngle(sliderMotionBlur.getValue());
    }
    public void setReflectionTop(){
        r.setTopOpacity(sliderTopOpacity.getValue());
    }
    public void setReflectionBottom(){
        r.setBottomOpacity(sliderBottomOpacity.getValue());
    }
    public void setReflectionFraction(){
        r.setFraction(sliderFraction.getValue());
    }
    public void setDropSpread(){
        dS.setSpread(sliderSpreadDrop.getValue());
    }
    public void setInnerChoke(){
        iS.setChoke(sliderChokeInner.getValue());
    }
    public void setAlpha(){
        attributes.setAlpha(sliderAlpha.getValue());
    }
    public void setBlendMode(){
        attributes.setBm(cbBlendMode.getValue());
    }

    public void handleTxtRotation(ActionEvent e){
        if(txtRotation.getText() != null){
            attributes.setRotation(Double.parseDouble(txtRotation.getText()));
        }
    }
    public void handleTxtArcStart(ActionEvent e){
        if(txtArcStart.getText() != null) {
            attributes.setStartAngle(Double.parseDouble(txtArcStart.getText()));
        }
    }
    public void handleTxtArcExtent(ActionEvent e){
        if(txtArcExtent.getText() != null){
                attributes.setArcExtent(Double.parseDouble(txtArcExtent.getText()));
        }
    }
    public void handleTxtArcWidth(ActionEvent e){
        if(txtArcWidth.getText() != null){
                attributes.setArcWidth(Double.parseDouble(txtArcWidth.getText()));
        }
    }
    public void handleTxtLineWidth(ActionEvent e){
        if(txtLineWidth.getText() != null){
            attributes.setLineWidth(Double.parseDouble(txtLineWidth.getText()));
        }
    }
    public void handleTxtArcHeight(ActionEvent e){
        if(txtArcHeight.getText() != null){
            attributes.setArcHeight(Double.parseDouble(txtArcHeight.getText()));
        }
    }
    public void handleTxtBoxBlurWidth(ActionEvent e){
        if(txtBoxBlurWidth.getText() != null){
            bB.setWidth(Double.parseDouble(txtBoxBlurWidth.getText()));
        }

    }
    public void handleTxtBoxBlurHeight(ActionEvent e){
        if(txtBoxBlurHeight.getText() != null){
            bB.setHeight(Double.parseDouble(txtBoxBlurHeight.getText()));
        }

    }
    public void handleTxtMotionBlurRadius(ActionEvent e){
        if(txtMotionBlurRadius.getText() != null){
            mB.setRadius(Double.parseDouble(txtMotionBlurRadius.getText()));
        }
    }
    public void handleTxtReflectionOffset(ActionEvent e){
        if(txtReflectionOffset.getText() != null){
            r.setTopOffset(Double.parseDouble(txtReflectionOffset.getText()));
        }
    }
    public void handleTxtShadowWidth(ActionEvent e){
        if(txtShadowWidth.getText() != null){
            s.setWidth(Double.parseDouble(txtShadowWidth.getText()));
        }

    }
    public void handleTxtShadowHeight(ActionEvent e){
        if(txtShadowHeight.getText() != null){
            s.setHeight(Double.parseDouble(txtShadowHeight.getText()));
        }
    }
    public void handleTxtShadowRadius(ActionEvent e){
        if(txtShadowRadius.getText() != null){
            s.setRadius(Double.parseDouble(txtShadowRadius.getText()));
        }
    }
    public void handleTxtDropWidth(ActionEvent e){
        if(txtDropWidth.getText() != null){
            dS.setWidth(Double.parseDouble(txtDropWidth.getText()));
        }

    }
    public void handleTxtDropHeight(ActionEvent e){
        if(txtDropHeight.getText() != null){
            dS.setHeight(Double.parseDouble(txtDropHeight.getText()));
        }
    }
    public void handleTxtDropRadius(ActionEvent e){
        if(txtDropRadius.getText() != null){
            dS.setRadius(Double.parseDouble(txtDropRadius.getText()));
        }
    }
    public void handleTxtDropOffsetX(ActionEvent e){
        if(txtDropOffsetX.getText() != null){
            dS.setOffsetX(Double.parseDouble(txtDropOffsetX.getText()));
        }
    }
    public void handleTxtDropOffsetY(ActionEvent e){
        if(txtDropOffsetY.getText() != null){
            dS.setOffsetY(Double.parseDouble(txtDropOffsetY.getText()));
        }
    }
    public void handleTxtInnerWidth(ActionEvent e){
        if(txtInnerWidth.getText() != null){
            iS.setWidth(Double.parseDouble(txtInnerWidth.getText()));
        }
    }
    public void handleTxtInnerHeight(ActionEvent e){
        if(txtInnerHeight.getText() != null){
            iS.setHeight(Double.parseDouble(txtInnerHeight.getText()));
        }
    }
    public void handleTxtInnerRadius(ActionEvent e){
        if(txtInnerRadius.getText() != null){
            iS.setRadius(Double.parseDouble(txtInnerRadius.getText()));
        }
    }
    public void handleTxtInnerOffsetX(ActionEvent e){
        if(txtInnerOffsetX.getText() != null){
            iS.setOffsetX(Double.parseDouble(txtInnerOffsetX.getText()));
        }
    }
    public void handleTxtInnerOffsetY(ActionEvent e){
        if(txtInnerOffsetY.getText() != null){
            iS.setOffsetY(Double.parseDouble(txtInnerOffsetY.getText()));
        }
    }

    public void setColorStrokeFill(ActionEvent e){
        attributes.setColor(cpStrokeFill.getValue());
    }
    public void setColorShadow(ActionEvent e){
        s.setColor(cpShadow.getValue());
    }
    public void setColorDrop(ActionEvent e){
        dS.setColor(cpDrop.getValue());
    }
    public void setColorInner(ActionEvent e){
        iS.setColor(cpInner.getValue());
    }
    private void setStrokeFill(){
        RadioButton sel = (RadioButton)strokeFill.getSelectedToggle();
        if(sel.equals(radioFill)){
            attributes.setFill(true);
        }else if(sel.equals(radioStroke)){
            attributes.setFill(false);
        }
        attributes.setColor(cpStrokeFill.getValue());
    }
    private void setSliders(){
        setBloom();
        setGlow();
        setBoxBlurIterations();
        setGaussianBlur();
        setMotionBlur();
        setReflectionBottom();
        setReflectionTop();
        setReflectionFraction();
        setDropSpread();
        setInnerChoke();
    }
    private void setColorShadows(){
        s.setColor(cpShadow.getValue());
        dS.setColor(cpDrop.getValue());
        iS.setColor(cpInner.getValue());
    }
    private void setBlurTypes(){
        s.setBlurType(cbShadowBlurType.getValue());
        dS.setBlurType(cbDropBlurType.getValue());
        iS.setBlurType(cbInnerBlurType.getValue());
    }
    private void setGeneral(){
        setAlpha();
        setBlendMode();
        setStrokeFill();
    }
    private void setShapeAttributes(){
        attributes.setArcType(cbArcType.getValue());
        attributes.setPolyClose(checkPolyClosed.isSelected());
        attributes.setFont(new Font(cbFontFamily.getValue(),cbFontSize.getValue()));
        attributes.setTxtAlignment(cbTxtAlignment.getValue());
        attributes.setContent(txtContent.getText());
    }
    private void setEffects(){
        setSliders();
        setBlurTypes();
        setColorShadows();
        addSelectedEffects();
    }

    public void addSelectedEffects(){
        CheckBox[] cBoxes = new CheckBox[]{ applyBloom, applyGlow, applyBoxBlur, applyGaussianBlur, applyMotionBlur,
                applyReflection, applyShadow, applyDropShadow, applyInnerShadow};
        Arrays.stream(cBoxes).forEach(c ->{
            if(c.isSelected()){
                Effect e = null;
                TitledPane t = (TitledPane)c.getParent().getParent();
                switch(t.getText()){
                    case "Bloom":
                        e = b; break;
                    case "Glow":
                        e = g; break;
                    case "Box Blur":
                        e = bB; break;
                    case "Motion Blur":
                        e = mB; break;
                    case "Gaussian Blur":
                        e = gB; break;
                    case "Reflection":
                        e = r; break;
                    case "Shadow":
                        e = s; break;
                    case "Drop Shadow":
                        e = dS; break;
                    case "Inner Shadow":
                        e = iS; break;
                }
                attributes.addEffect(e);
            }

        });
    }

    public void handleClose(ActionEvent e){
        setGeneral();
        setShapeAttributes();
        setEffects();
        stage = (Stage) closeDrawOptions.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initOnlyNums();
        initComboBoxes();
        initButtons();
        initColorPickers();
        attributes = new Attributes();
    }

    private void initComboBoxes(){
        ComboBox[] blurTypes = new ComboBox[]{cbShadowBlurType, cbDropBlurType, cbInnerBlurType};
        Arrays.stream(blurTypes).forEach(b ->{
            b.getItems().addAll(BlurType.values());
            b.setValue(BlurType.GAUSSIAN);
        });
        cbBlendMode.getItems().addAll(BlendMode.values());
        cbBlendMode.setValue(BlendMode.SRC_OVER);
        cbArcType.getItems().addAll(ArcType.values());
        cbArcType.setValue(ArcType.ROUND);
        cbFontFamily.getItems().addAll(Font.getFamilies());
        cbFontFamily.setValue(Font.getDefault().getFamily());
        cbFontSize.getItems().addAll(8.0,9.0,10.0,11.0,12.0,14.0,16.0,18.0,20.0,22.0,24.0,26.0,28.0,36.0,48.0,72.0,96.0);
        cbFontSize.setValue(Font.getDefault().getSize());
        cbTxtAlignment.getItems().addAll(TextAlignment.values());
        cbTxtAlignment.setValue(TextAlignment.LEFT);
        setSliders();
    }
    private void initOnlyNums(){
        TextField[] onlyNums = new TextField[]{txtRotation, txtArcStart, txtArcExtent,
                txtArcWidth, txtArcHeight,txtLineWidth , txtBoxBlurWidth, txtBoxBlurHeight, txtMotionBlurRadius,
                txtReflectionOffset, txtShadowWidth, txtShadowHeight, txtShadowRadius, txtDropWidth,
                txtDropHeight, txtDropOffsetX, txtDropOffsetY, txtDropRadius, txtInnerWidth, txtInnerHeight,
                txtInnerOffsetX, txtInnerOffsetY, txtInnerRadius};
        Arrays.stream(onlyNums).forEach(t -> t.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                t.setText(newValue.replaceAll("[^\\d]", ""));
            }
        }));
    }
    private void initButtons(){
        buttons.addEventHandler(MouseEvent.ANY, event -> {
            stage = (Stage) closeDrawOptions.getScene().getWindow();
            if(event.getEventType().equals(MouseEvent.MOUSE_PRESSED)){
                delta = new Point2D(stage.getX()-event.getScreenX(),stage.getY()-event.getScreenY());
            }else if(event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)){
                stage.setX(event.getScreenX()+delta.getX());
                stage.setY(event.getScreenY()+delta.getY());
            }
        });
    }
    private void initColorPickers(){
        cpStrokeFill.setValue(Color.BLACK);
        cpShadow.setValue(Color.BLACK);
        cpDrop.setValue(Color.BLACK);
        cpInner.setValue(Color.BLACK);
    }


}
