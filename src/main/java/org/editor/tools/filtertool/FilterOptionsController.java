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

/**
 * Pop-up window for users to set filter input settings.
 */
public class FilterOptionsController implements Initializable {

    private static final Logger FOC_LOGGER = LogManager.getLogger(FilterOptionsController.class.getName());

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

    private EditorController ec;
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
     * Applies filter to the ImageGrid arrays of the original and resized images.
     * Updates the ImageDimensions objects through the EditorController method.
     * Calls on another EditorController method to draw the new images to their respective canvases.
     */
    private void applyFilter() {
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
     * Draws a preview of a filter's effect before altering the actual ImageDimensions objects.
     */
    private void previewFilter() {
        filterOperation = new FilterOperation(resizedImageGrid, inputAttributes);
        int[] pixelArrayNew = filterOperation.startFilter();

        resizedImageGrid.processPixels(pixelArrayNew);
        Image pi = (resizedImageGrid.writeNewPixelArray());
        ec.drawPreviewImage(pi);
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

    /**
     * Gets second user selected filter.
     * Shows a preview of top-right panel.
     *
     * @param actionEvent the action event
     */
    private void getSecondFilter(ActionEvent actionEvent) {
        setCheckerBoard(filterTypeChoiceBox2, 1);
    }

    /**
     * Gets third user selected filter.
     * Shows a preview of bottom-left panel.
     *
     * @param actionEvent the action event
     */
    private void getThirdFilter(ActionEvent actionEvent) {
        setCheckerBoard(filterTypeChoiceBox3, 2);
    }

    /**
     * Gets fourth user selected filter.
     * Shows a preview of bottom-right panel.
     *
     * @param actionEvent the action event
     */
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
        ec.drawPreviousImage();

        stage = (Stage) cancelFilterButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Set filter menu according to selection
     *
     * @param appType the application type
     * @param filterType the filter type
     */
    private void setMenu(FilterApplicationType appType, FilterType filterType) {
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

    /**
     * Makes checkerboard filter menu visible.
     * Loads the available filter options into the choice boxes.
     *
     * */
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
     *
     * @param filterTypeList the filter type list
     * @return the list
     */
    private List<FilterType> maximizeList(List<FilterType> filterTypeList) {
        FilterType filterTypeOne= filterTypeList.get(0);
        filterTypeList.add(filterTypeOne);
        filterTypeList.add(filterTypeOne);
        filterTypeList.add(filterTypeOne);

        return filterTypeList;
    }
}
