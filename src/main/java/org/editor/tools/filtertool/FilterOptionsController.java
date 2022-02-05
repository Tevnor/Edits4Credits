package org.editor.tools.filtertool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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

/**
 * Pop-up window for users to set filter input settings.
 */
public class FilterOptionsController implements Initializable {

    private static final Logger FOC_LOGGER = LogManager.getLogger(FilterOptionsController.class.getName());

    @FXML
    private Button closeFilterButton;
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
    @FXML
    private Button panelIncreaseButton;
    @FXML
    private Button panelDecreaseButton;

    /**
     * Glitch Nodes
     * */
    @FXML
    private GridPane glitchGridPane;
    @FXML
    private Pane coordinatePane;
    @FXML
    private Slider glitchSlider;
    @FXML
    private ToggleButton complementToggleButton;
    @FXML
    private ToggleButton silhouetteToggleButton;

    private final EditorController editorController;
    private final ImageGrid originalImageGrid;
    private final ImageGrid resizedImageGrid;
    private final FilterInputAttributes inputAttributes;
    private FilterType filterType;
    private List<FilterType> filterTypeList;

    public FilterOptionsController(Image original, Image resized, EditorController ec) {
        inputAttributes = new FilterInputAttributes();
        this.editorController = ec;
        this.originalImageGrid = new ImageGrid(original);
        this.resizedImageGrid = new ImageGrid(resized);
        originalImageGrid.readPixelsIntoArray();
        resizedImageGrid.readPixelsIntoArray();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        applyFilterButton.setOnAction(actionEvent -> handleApplyFilter());

        cancelFilterButton.setOnAction(actionEvent -> handleCancelFilter());
        closeFilterButton.setOnAction(actionEvent -> handleCancelFilter());

        coordinatePane.setOnMouseReleased((this::handleCoordinates));
        complementToggleButton.setOnAction(actionEvent -> handleComplement());
        silhouetteToggleButton.setOnAction(actionEvent -> handleSilhouette());
        glitchSlider.setOnMouseReleased(mouseEvent -> changeSliderValue());

        panelIncreaseButton.setOnAction(actionEvent -> handlePanelIncrease());
        panelDecreaseButton.setOnAction(actionEvent -> handlePanelDecrease());
    }

    /**
     * Upon opening a filter menu, the FilterOptionsController instantiates two ImageGrid objects,
     * one for the original image dimensions and another for the resized dimensions.
     *
     * The view of the pop-up menu gets set according to the selected filter.
     *
     * @param original   the original image
     * @param resized    the resized image
     * @param appType    the application type of the selected filter
     * @param filterType the type of the selected filter
     * @param ec         the object of the EditorController
     */
//    public void initFilterOptions(Image original, Image resized, FilterApplicationType appType, FilterType filterType, EditorController ec) {
//        inputAttributes = new FilterInputAttributes();
//        this.ec = ec;
//        this.originalImageGrid = new ImageGrid(original);
//        this.resizedImageGrid = new ImageGrid(resized);
//        originalImageGrid.readPixelsIntoArray();
//        resizedImageGrid.readPixelsIntoArray();
//
//        filterTypeList = new ArrayList<>();
//        this.filterType = filterType;
//
//        setMenu(appType, filterType);
//    }

    /**
     * Applies filter to the ImageGrid arrays of the original and resized images.
     * Updates the ImageDimensions objects through the EditorController method.
     * Calls on another EditorController method to draw the new images to their respective canvases.
     */
    private void applyFilter() {
        // On editor image
        FilterOperation editorOperation = new FilterOperation(resizedImageGrid, inputAttributes);
        int[] editorPixelArrayNew = editorOperation.startFilter();
        Image editorImage = resizedImageGrid.writeNewPixelArray(editorPixelArrayNew);

        // On original image
        FilterOperation originalOperation = new FilterOperation(originalImageGrid, inputAttributes);
        int[] originalPixelArrayNew = originalOperation.startFilter();
        Image originalImage = originalImageGrid.writeNewPixelArray(originalPixelArrayNew);

        // Update both filtered images
        editorController.setFilteredImages(originalImage, editorImage);
        // Draw both to their respective canvas
        editorController.drawFilteredImages();
    }

    /**
     * Draws a preview of a filter's effect before altering the actual ImageDimensions objects.
     */
    private void previewFilter() {
        FilterOperation preview = new FilterOperation(resizedImageGrid, inputAttributes);
        int[] pixelArrayNew = preview.startFilter();
        Image pi = (resizedImageGrid.writeNewPixelArray(pixelArrayNew));
        editorController.drawPreviewImage(pi);
    }

    /**
     * Handles apply filter selection.
     */
    @FXML
    private void handleApplyFilter() {
        applyFilter();
        stage = (Stage) applyFilterButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Toggles the glitch filter's silhouette effect on/off.
     * Shows a preview.
     */
    @FXML
    private void handleSilhouette() {
        inputAttributes.setSilhouetteToggle(silhouetteToggleButton.isSelected());
        previewFilter();
    }

    /**
     * Toggles the glitch filter's complement effect on/off
     */
    @FXML
    private void handleComplement() {
        inputAttributes.setComplementToggle(complementToggleButton.isSelected());
        previewFilter();
    }

    /**
     * Takes in the mouse's x,y coordinates of the glitch filter's input window and pass them to FilterAttributes object.
     * Shows a preview.
     *
     * @param mouseEvent the mouse event
     */
    @FXML
    private void handleCoordinates(MouseEvent mouseEvent) {
        inputAttributes.setFactorX(mouseEvent.getX());
        inputAttributes.setFactorY(mouseEvent.getY() * 1000);
        previewFilter();
    }

    /**
     * Reads the value of the slider and updates the value in the FilterAttributes object.
     * Shows a preview.
     */
    @FXML
    private void changeSliderValue() {
        inputAttributes.setFactor(glitchSlider.getValue() / 10000);
        previewFilter();
    }

    // FIXME: 01.02.2022 disable apply filter with empty selection

    /**
     * Gets first user selected filter.
     * Shows a preview of top-left panel.
     *
     * @param actionEvent the action event
     */
    private void getFirstFilter(ActionEvent actionEvent) {
        setCheckerBoard(filterTypeChoiceBox1, 0);
    }
    private void getSecondFilter(ActionEvent actionEvent) {
        setCheckerBoard(filterTypeChoiceBox2, 1);
    }
    private void getThirdFilter(ActionEvent actionEvent) {
        setCheckerBoard(filterTypeChoiceBox3, 2);
    }
    private void getFourthFilter(ActionEvent actionEvent) {
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

    /**
     * Handle panel increase.
     */
    @FXML
    private void handlePanelIncrease() {
        inputAttributes.increaseRuns();
        previewFilter();
    }

    /**
     * Handle panel decrease.
     */
    @FXML
    private void handlePanelDecrease() {
        inputAttributes.decreaseRuns();
        previewFilter();
    }

    /**
     * Cancel out of the filter menu and revert to the image's previous state.
     */
    @FXML
    private void handleCancelFilter() {
        editorController.drawPreviousImage();

        stage = (Stage) cancelFilterButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Set filter menu according to selection
     *
     * @param appType the application type
     * @param filterType the filter type
     */
    public void setFilterView(FilterApplicationType appType, FilterType filterType) {
        filterTypeList = new ArrayList<>();
        this.filterType = filterType;

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
                default:
                    initStandardFilter();
            }
        }
    }

    /**
     * Makes checkerboard filter menu visible.
     * Loads the available filter options into the choice boxes.
     *
     * */
    private void initCheckerBoard() {
        // Visual
        checkerboardGridPane.setVisible(true);
        glitchSlider.setValue(0);

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
        maximizeList(filterTypeList);
        inputAttributes.setFilterTypeList(filterTypeList);
        inputAttributes.setRuns(2);
    }
    private void initStandardFilter() {
        // Visual
        intensityGridPane.setVisible(true);

        // Functional
        filterTypeList.add(filterType);
        maximizeList(filterTypeList);
        inputAttributes.setFilterTypeList(filterTypeList);
        inputAttributes.setRuns(2);
    }

    /**
     * Maximize filter list for panel quarters
     *
     * @param filterTypeList the filter type list
     */
    private void maximizeList(List<FilterType> filterTypeList) {
        FilterType filterTypeOne= filterTypeList.get(0);
        filterTypeList.add(filterTypeOne);
        filterTypeList.add(filterTypeOne);
        filterTypeList.add(filterTypeOne);
    }
}
