package org.editor.tools.drawingTool;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.tools.drawingTool.attributes.*;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import static org.editor.tools.drawingTool.handlers.HandlerFactory.*;


public class DrawOptionsController implements Initializable {
    private static final Logger DO_LOGGER = LogManager.getLogger(DrawOptionsController.class);

    @FXML
    private Slider sliderAlpha, sliderBloom, sliderGlow, sliderBoxBlur, sliderGaussianBlur, sliderMotionBlur;
    @FXML
    private Slider sliderTopOpacity, sliderFraction, sliderBottomOpacity, sliderSpreadDrop, sliderChokeInner, sliderEraserSize;
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
    private TextField txtDropWidth, txtDropHeight, txtDropOffsetX, txtDropOffsetY, txtDropRadius;
    @FXML
    private TextField txtInnerWidth, txtInnerHeight, txtInnerOffsetX, txtInnerOffsetY, txtInnerRadius;
    @FXML
    private TextArea txtContent;
    @FXML
    private ColorPicker cpStrokeFill, cpDrop, cpInner;
    @FXML
    private RadioButton radioStroke, radioFill, eraserCircle;
    @FXML
    private CheckBox checkPolyClosed, applyBloom, applyGlow, applyBoxBlur, applyGaussianBlur, applyMotionBlur;
    @FXML
    private CheckBox applyReflection, applyDropShadow, applyInnerShadow;
    @FXML
    private ChoiceBox<String> cbFontStyle;
    @FXML
    private ComboBox<String> cbFontFamily;
    @FXML
    private TextField txtFontSize;
    @FXML
    private ComboBox<BlurType>  cbDropBlurType, cbInnerBlurType;
    @FXML
    private Button closeDrawOptions;
    @FXML
    private ButtonBar buttons;
    @FXML
    private ToggleGroup strokeFill, eraserShape;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab general, shapes, eraser;
    @FXML
    private Accordion shapeAccordion;
    @FXML
    private TitledPane arc, polygon, rounded_rect, text;

    private Stage stage;
    private Point2D delta;
    private DrawHandlerType tmpDrawHandlerType;

    private void initSelShape(DrawHandlerType drawHandlerType){
        switch (drawHandlerType){
            case ARC:
                selectShapePane(arc);
                break;
            case ROUNDED_RECTANGLE:
                selectShapePane(rounded_rect);
                break;
            case POLYGON:
                selectShapePane(polygon);
                break;
            case TEXT:
                selectShapePane(text);
                break;
            case CIRCLE:
            case LINE:
            case RECTANGLE:
            case ELLIPSES:
                tabPane.getSelectionModel().select(general);
                break;
            case PATH:
                tabPane.getSelectionModel().select(general);
                strokeFill.selectToggle(radioStroke);
                txtRotation.setDisable(true);
                break;
            case ERASER:
                eraser.setDisable(false);
                tabPane.getSelectionModel().select(eraser);
                general.setDisable(true);
                break;
        }
        DO_LOGGER.debug("set selected Shape to: " + drawHandlerType);
    }
    public void setSelShape(DrawHandlerType drawHandlerType){
        this.tmpDrawHandlerType = drawHandlerType;
        initSelShape(drawHandlerType);
    }
    private void selectShapePane(TitledPane shapeOpt){
        shapes.setDisable(false);
        TitledPane[] shapeOpts = new TitledPane[]{arc, polygon, rounded_rect, text};
        Arrays.stream(shapeOpts).forEach(t -> {
            t.setVisible(t.equals(shapeOpt));
        });
        shapeAccordion.setExpandedPane(shapeOpt);
        tabPane.getSelectionModel().select(shapes);
    }
    private void deselectShapePane(){
        shapes.setDisable(true);
        TitledPane[] shapeOpts = new TitledPane[]{arc, polygon, rounded_rect, text};
        Arrays.stream(shapeOpts).forEach(t ->t.setVisible(false));
        tabPane.getSelectionModel().select(general);
    }
    public void resetLayout(){
        switch(tmpDrawHandlerType) {
            case ARC:
            case ROUNDED_RECTANGLE:
            case POLYGON:
            case TEXT:
                deselectShapePane();
                break;
            case CIRCLE:
            case LINE:
            case RECTANGLE:
            case ELLIPSES:
                tabPane.getSelectionModel().select(general);
                break;
            case PATH:
                tabPane.getSelectionModel().select(general);
                strokeFill.selectToggle(radioFill);
                txtRotation.setDisable(false);
                break;
            case ERASER:
                eraser.setDisable(true);
                tabPane.getSelectionModel().select(general);
                general.setDisable(false);
                break;
        }
        DO_LOGGER.debug("reset layout");
    }

    private Bloom getBloom(){
        return new Bloom(sliderBloom.getValue());
    }
    private Glow getGlow(){
        return new Glow(sliderGlow.getValue());
    }
    private BoxBlur getBoxBlur(){
        return new BoxBlur(getTxtNumFields(txtBoxBlurWidth,5),getTxtNumFields(txtBoxBlurHeight,5),
                (int)sliderBoxBlur.getValue());
    }
    private GaussianBlur getGaussianBlur(){
        return new GaussianBlur(sliderGaussianBlur.getValue());
    }
    private MotionBlur getMotionBlur(){
        return new MotionBlur(sliderMotionBlur.getValue(),getTxtNumFields(txtMotionBlurRadius,10));
    }
    private Reflection getReflection(){
        return new Reflection(getTxtNumFields(txtReflectionOffset,0), sliderFraction.getValue(),
                sliderTopOpacity.getValue(), sliderBottomOpacity.getValue());
    }
    private DropShadow getDropShadow(){
        DropShadow d = new DropShadow(cbDropBlurType.getValue(), cpDrop.getValue(), getTxtNumFields(txtDropRadius,10),
                sliderSpreadDrop.getValue(),getTxtNumFields(txtDropOffsetX,0), getTxtNumFields(txtDropOffsetY,0));
        d.setWidth(getTxtNumFields(txtDropWidth,21));
        d.setHeight(getTxtNumFields(txtDropHeight,21));
        return d;
    }
    private InnerShadow getInnerShadow(){
        InnerShadow i = new InnerShadow(cbInnerBlurType.getValue(), cpInner.getValue(), getTxtNumFields(txtInnerRadius,10),
                sliderChokeInner.getValue(),getTxtNumFields(txtInnerOffsetX,0), getTxtNumFields(txtInnerOffsetY,0));
        i.setWidth(getTxtNumFields(txtInnerWidth,21));
        i.setHeight(getTxtNumFields(txtInnerHeight,21));
        return i;
    }

    private boolean isFill(){
        RadioButton sel = (RadioButton)strokeFill.getSelectedToggle();
        if(sel.equals(radioFill)){
            return true;
        }else return !sel.equals(radioStroke);
    }
    private General getGeneral(){
        DO_LOGGER.debug("created new General object");
        return new General(getTxtNumFields(txtRotation,0),
                cbBlendMode.getValue(),
                getTxtNumFields(txtLineWidth,1),
                cpStrokeFill.getValue(),
                sliderAlpha.getValue(),
                isFill());
    }
    private ArcAttributes getArcAttributes(){
        DO_LOGGER.debug("created new ArcAttributes object");
        return new ArcAttributes(getGeneral(),cbArcType.getValue(),
                getTxtNumFields(txtArcStart,0),getTxtNumFields(txtArcExtent,0));
    }
    private PolygonAttributes getPolyAttributes(){
        DO_LOGGER.debug("created new PolyAttributes object");
        return new PolygonAttributes(getGeneral(), checkPolyClosed.isSelected());
    }
    private RoundRectAttributes getRoundRectAttributes(){
        DO_LOGGER.debug("created new RoundRectAttributes object");
        return new RoundRectAttributes(getGeneral(),
                getTxtNumFields(txtArcWidth, 0), getTxtNumFields(txtArcHeight,0));
    }
    private TextAttributes getTextAttributes(){
        DO_LOGGER.debug("created new TextAttributes object");
        Font font;
        String fontfamily = cbFontFamily.getValue();
        switch (cbFontStyle.getValue()){
            case "Bold":
                font = Font.font(fontfamily, FontWeight.BOLD,getTxtNumFields(txtFontSize,20));
                break;
            case "Italic":
                font = Font.font(fontfamily, FontPosture.ITALIC,getTxtNumFields(txtFontSize,20));
                break;
            case "Bold Italic":
                font = Font.font(fontfamily, FontWeight.BOLD, FontPosture.ITALIC,getTxtNumFields(txtFontSize,20));
                break;
            default:
                font = Font.font(fontfamily,getTxtNumFields(txtFontSize,20));
                break;
        }
        return new TextAttributes(getGeneral(),txtContent.getText(),font,cbTxtAlignment.getValue());
    }
    private EraserAttributes getEraserAttributes(){
        DO_LOGGER.debug("created new EraserAttributes object");
        boolean circle = eraserShape.getSelectedToggle() == eraserCircle;
        return new EraserAttributes(getGeneral(),sliderEraserSize.getValue(), circle);
    }

    private AbstractGeneral addSelectedEffects(AbstractGeneral opt){
        CheckBox[] cBoxes = new CheckBox[]{ applyBloom, applyGlow, applyBoxBlur, applyGaussianBlur, applyMotionBlur,
                applyReflection, applyDropShadow, applyInnerShadow};
        Arrays.stream(cBoxes).forEach(c ->{
            if(c.isSelected()){
                Effect e = null;
                TitledPane t = (TitledPane)c.getParent().getParent();
                switch(t.getText()){
                    case "Bloom":
                        e = getBloom(); break;
                    case "Glow":
                        e = getGlow(); break;
                    case "Box Blur":
                        e = getBoxBlur(); break;
                    case "Motion Blur":
                        e = getMotionBlur(); break;
                    case "Gaussian Blur":
                        e = getGaussianBlur(); break;
                    case "Reflection":
                        e = getReflection(); break;
                    case "Drop Shadow":
                        e = getDropShadow(); break;
                    case "Inner Shadow":
                        e = getInnerShadow(); break;
                }
                opt.addEffect(e);
            }

        });
        return opt;
    }
    private AbstractGeneral initAttributes(){
        switch (tmpDrawHandlerType){
            case ARC:
                return getArcAttributes();
            case ROUNDED_RECTANGLE:
                return getRoundRectAttributes();
            case POLYGON:
                return getPolyAttributes();
            case TEXT:
                return getTextAttributes();
            case ERASER:
                return getEraserAttributes();
            default:
                return getGeneral();
        }
    }

    public DrawHandlerType getTmpHandler(){
        return tmpDrawHandlerType;
    }
    public AbstractGeneral getAttributes(){
        return addSelectedEffects(initAttributes());
    }
    @FXML
    private void handleClose(){
        stage = (Stage) closeDrawOptions.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initOnlyNums();
        initComboBoxes();
        initButtons();
        initColorPickers();
    }

    private void initComboBoxes(){
        cbDropBlurType.getItems().addAll(BlurType.values());
        cbDropBlurType.setValue(BlurType.THREE_PASS_BOX);
        cbInnerBlurType.getItems().addAll(BlurType.values());
        cbInnerBlurType.setValue(BlurType.THREE_PASS_BOX);
        cbBlendMode.getItems().addAll(BlendMode.values());
        cbBlendMode.setValue(BlendMode.SRC_OVER);
        cbArcType.getItems().addAll(ArcType.values());
        cbArcType.setValue(ArcType.ROUND);
        cbFontFamily.getItems().addAll(Font.getFamilies());
        cbFontFamily.setValue(Font.getDefault().getFamily());
        cbTxtAlignment.getItems().addAll(TextAlignment.values());
        cbTxtAlignment.setValue(TextAlignment.LEFT);
        cbFontStyle.getItems().addAll("Regular", "Bold", "Italic", "Bold Italic");
        cbFontStyle.setValue("Regular");
    }
    private void initOnlyNums(){
        TextField[] onlyNums = new TextField[]{txtRotation, txtArcStart, txtArcExtent,
                txtArcWidth, txtArcHeight,txtLineWidth , txtBoxBlurWidth, txtBoxBlurHeight, txtMotionBlurRadius,
                txtReflectionOffset, txtDropWidth,
                txtDropHeight, txtDropOffsetX, txtDropOffsetY, txtDropRadius, txtInnerWidth, txtInnerHeight,
                txtInnerOffsetX, txtInnerOffsetY, txtInnerRadius, txtFontSize};
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
        cpDrop.setValue(Color.BLACK);
        cpInner.setValue(Color.BLACK);
    }
    private double getTxtNumFields(TextField txt, double def){
        if(txt.getText() != null && !txt.getText().equals("")){
            return Double.parseDouble(txt.getText());
        }
        return def;
    }


}
