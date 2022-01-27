package org.editor.tools.filtertool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.EditorController;
import org.editor.tools.filtertool.filtercontrol.FilterApplicationType;
import org.editor.tools.filtertool.filtercontrol.FilterInputAttributes;
import org.editor.tools.filtertool.filtercontrol.filter.FilterType;
import org.editor.tools.imagetool.ImageGrid;

import java.net.URL;
import java.util.*;

import static org.editor.tools.filtertool.filtercontrol.FilterApplicationType.CHECKERBOARD;

public class FilterOptionsController implements Initializable {

    private static final Logger FOC_LOGGER = LogManager.getLogger(FilterOptionsController.class.getName());

    @FXML
    private GridPane filterGridPane;

    /**
     * General Nodes
     * */
    @FXML
    private Label filterOptionsLabel;
    @FXML
    private Button applyFilterButton;
    @FXML
    private Button cancelFilterButton;
    @FXML
    private GridPane intensityGridPane;
    private Stage stage;

    /**
     * Checkerboard Nodes
     * */
    @FXML
    private GridPane checkerboardGridPane;
    @FXML
    private ChoiceBox<FilterType> filterTypeChoiceBox1;
    @FXML
    private ChoiceBox<FilterType> filterTypeChoiceBox2;
    @FXML
    private ChoiceBox<FilterType> filterTypeChoiceBox3;
    @FXML
    private ChoiceBox<FilterType> filterTypeChoiceBox4;

    /**
     * Glitch Nodes
     * */
    @FXML
    private GridPane glitchGridPane;
    @FXML
    private Slider glitchSlider;
    @FXML
    private ToggleButton complementToggleButton;
    @FXML
    private ToggleButton silhouetteToggleButton;

    /**
     * Global Variables
     * */
    private EditorController ec;
//    private ImageTool imageTool;

    private ImageGrid originalImageGrid;
    private ImageGrid resizedImageGrid;

    private FilterOperation filterOperation;
    private FilterType filterType;
    private List<FilterType> filterTypeList;
    private FilterInputAttributes inputAttributes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Init
     * */
    public void initFilterOptions(Image original, Image resized, FilterApplicationType appType, FilterType filterType, EditorController ec) {
        inputAttributes = new FilterInputAttributes();
        this.ec = ec;
        this.originalImageGrid = new ImageGrid(original);
        this.resizedImageGrid = new ImageGrid(resized);
        originalImageGrid.setPixelArray();
        resizedImageGrid.setPixelArray();

        filterTypeList = new ArrayList<>();
        this.filterType = filterType;

        setMenu(appType, filterType);
    }

    /**
     * Apply filter on original and resized image
     * */
    public void applyFilter() {
        // On resized
        filterOperation = new FilterOperation(resizedImageGrid, inputAttributes);
        int[] resizedPixelArrayNew = filterOperation.startFilter();
        resizedImageGrid.processPixels(resizedPixelArrayNew);
        Image editorImage = resizedImageGrid.writeNewPixelArray();

        // On original
        filterOperation = new FilterOperation(originalImageGrid, inputAttributes);
        int[] originalPixelArrayNew = filterOperation.startFilter();
        originalImageGrid.processPixels(originalPixelArrayNew);
        Image originalImage = originalImageGrid.writeNewPixelArray();


        // Update both filtered images
        ec.setFilteredImages(originalImage, editorImage);
        // Draw both to their respective canvas
        ec.drawFilteredImages();
    }
    /**
     * Show preview of filter
     * */
    public void previewFilter() {
        filterOperation = new FilterOperation(resizedImageGrid, inputAttributes);
        int[] pixelArrayNew = filterOperation.startFilter();

        resizedImageGrid.processPixels(pixelArrayNew);
        Image pi = (resizedImageGrid.writeNewPixelArray());
        ec.drawPreviewImage(pi);
    }

    public void handleApplyFilter() {
//        inputAttributes.setOriginal(true);
        applyFilter();
        stage = (Stage) applyFilterButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Filter Input Options
     * */
    public void handleSilhouette() {
        inputAttributes.setSilhouetteToggle(silhouetteToggleButton.isSelected());
        previewFilter();
    }
    public void handleComplement() {
        inputAttributes.setComplementToggle(complementToggleButton.isSelected());
        previewFilter();
    }

    // Get x and y coordinates from input pane for glitch factors
    public void handleCoordinates(MouseEvent mouseEvent) {
        inputAttributes.setFactorX(mouseEvent.getX());
        inputAttributes.setFactorY(mouseEvent.getY() * 1000);
        previewFilter();
    }
    public void changeSliderValue() {
        inputAttributes.setFactor(glitchSlider.getValue() / 10000);
        previewFilter();
    }

    /**
     * Checkerboard Input Options
     * */
    public void getFirstFilter(ActionEvent actionEvent) {
        setCheckerBoard(filterTypeChoiceBox1, 0);
    }
    public void getSecondFilter(ActionEvent actionEvent) {
        setCheckerBoard(filterTypeChoiceBox2, 1);
    }
    public void getThirdFilter(ActionEvent actionEvent) {
        setCheckerBoard(filterTypeChoiceBox3, 2);
    }
    public void getFourthFilter(ActionEvent actionEvent) {
        setCheckerBoard(filterTypeChoiceBox4, 3);
    }
    private void setCheckerBoard(ChoiceBox<FilterType> filterTypeChoice, int index) {
        if (null != filterTypeList.get(index)) {
            filterTypeList.remove(index);
        }
        filterTypeList.add(index, filterTypeChoice.getValue());
        inputAttributes.setFilterTypeList(filterTypeList);

        previewFilter();
    }

    public void handlePanelIncrease() {
        inputAttributes.increaseRuns();
        previewFilter();
    }
    public void handlePanelDecrease() {
        inputAttributes.decreaseRuns();
        previewFilter();
    }

    /**
     * Cancel
     * */
    public void handleCancelFilter() {
        // Reset to last image state
        ec.drawPreviousImage();
        // Close pop-up
        stage = (Stage) cancelFilterButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Set filter menu according to selection
     * */
    public void setMenu(FilterApplicationType appType, FilterType filterType) {
        checkerboardGridPane.setVisible(false);
        glitchGridPane.setVisible(false);
        intensityGridPane.setVisible(false);

        if (appType == CHECKERBOARD) {
            filterOptionsLabel.setText(appType.toString());
            initCheckerBoard();
        } else {
            filterOptionsLabel.setText(filterType.toString());
            switch (filterType) {
                case GLITCH:
                    initGlitchFilter();
                    break;
                case NOISE:
                    //TODO
                    initStandardFilter();
                    break;
                default:
                    initStandardFilter();
            }
        }
    }

//    @FXML
    private void initCheckerBoard() {
        // Visual
        checkerboardGridPane.setVisible(true);

        // Functional
        inputAttributes.setFactorX(333);
        inputAttributes.setFactorY(222 * 111);
        inputAttributes.setRuns(1);

        filterTypeList.add(FilterType.ORIGINAL);
        filterTypeList.add(FilterType.ORIGINAL);
        filterTypeList.add(FilterType.ORIGINAL);
        filterTypeList.add(FilterType.ORIGINAL);

        if (filterTypeChoiceBox1.getItems().isEmpty()) {
            filterTypeChoiceBox1.setValue(FilterType.ORIGINAL);
            filterTypeChoiceBox2.setValue(FilterType.ORIGINAL);
            filterTypeChoiceBox3.setValue(FilterType.ORIGINAL);
            filterTypeChoiceBox4.setValue(FilterType.ORIGINAL);

            filterTypeChoiceBox1.getItems().addAll(FilterType.TYPE_TO_FILTER_ENUM_MAP.keySet());
            filterTypeChoiceBox2.getItems().addAll(FilterType.TYPE_TO_FILTER_ENUM_MAP.keySet());
            filterTypeChoiceBox3.getItems().addAll(FilterType.TYPE_TO_FILTER_ENUM_MAP.keySet());
            filterTypeChoiceBox4.getItems().addAll(FilterType.TYPE_TO_FILTER_ENUM_MAP.keySet());

            filterTypeChoiceBox1.setOnAction(this::getFirstFilter);
            filterTypeChoiceBox2.setOnAction(this::getSecondFilter);
            filterTypeChoiceBox3.setOnAction(this::getThirdFilter);
            filterTypeChoiceBox4.setOnAction(this::getFourthFilter);
        }
    }
    private void initGlitchFilter() {
        // Visual
        glitchGridPane.setVisible(true);

        // Functional
        filterTypeList.add(filterType);
        filterTypeList = maximizeList(filterTypeList);
        inputAttributes.setFilterTypeList(filterTypeList);
        inputAttributes.setRuns(2);
    }
    private void initStandardFilter() {
        // Visual
        intensityGridPane.setVisible(true);

        // Functional
        filterTypeList.add(filterType);
        filterTypeList = maximizeList(filterTypeList);
        inputAttributes.setFilterTypeList(filterTypeList);
        inputAttributes.setRuns(2);
    }

    /**
     * Maximize filter list for panel quarters
     * */
    public List<FilterType> maximizeList(List<FilterType> filterTypeList) {
        FilterType filterTypeOne= filterTypeList.get(0);
        filterTypeList.add(filterTypeOne);
        filterTypeList.add(filterTypeOne);
        filterTypeList.add(filterTypeOne);

        return filterTypeList;
    }
}
