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


/**
 * Pop-up window for users to set filter input settings.
 */
public class FilterOptionsController implements Initializable {

    private final Logger FOC_LOGGER = LogManager.getLogger(this.getClass());

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

    /**
     * Upon opening a filter menu, the FilterOptionsController instantiates two ImageGrid objects,
     * one for the original image dimensions and another for the resized dimensions.
     *
     * The view of the pop-up menu gets set according to the selected filter.
     *
     * @param original   the original image
     * @param resized    the resized image
     * @param editorController         the object of the EditorController
     */
    public FilterOptionsController(Image original, Image resized, EditorController editorController) {
        inputAttributes = new FilterInputAttributes();
        this.editorController = editorController;
        this.originalImageGrid = new ImageGrid(original);
        this.resizedImageGrid = new ImageGrid(resized);
        originalImageGrid.readPixelsIntoArray();
        resizedImageGrid.readPixelsIntoArray();

        FOC_LOGGER.debug("New FilterOptionsController object instantiated.");
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

        FOC_LOGGER.debug("{} applied on {} and {}", filterType, resizedImageGrid, originalImageGrid);
    }

    /**
     * Draws a preview of a filter's effect before altering the actual ImageDimensions objects.
     */
    private void previewFilter() {
        FilterOperation preview = new FilterOperation(resizedImageGrid, inputAttributes);
        int[] pixelArrayNew = preview.startFilter();
        Image pi = (resizedImageGrid.writeNewPixelArray(pixelArrayNew));
        editorController.drawPreviewImage(pi);

        FOC_LOGGER.debug("Created preview for {} filter.", filterType);
    }

    /**
     * Handles apply filter selection.
     */
    @FXML
    private void handleApplyFilter() {
        applyFilter();
        Stage stage = (Stage) applyFilterButton.getScene().getWindow();
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

        FOC_LOGGER.debug("Toggled {}.", silhouetteToggleButton);
    }

    /**
     * Toggles the glitch filter's complement effect on/off
     */
    @FXML
    private void handleComplement() {
        inputAttributes.setComplementToggle(complementToggleButton.isSelected());
        previewFilter();

        FOC_LOGGER.debug("Toggled {}.", complementToggleButton);
    }

    /**
     * Takes in the mouse's x,y coordinates of the glitch filter's input window and pass them to FilterAttributes object.
     * Shows a preview.
     *
     * @param mouseEvent the mouse event
     */
    @FXML
    private void handleCoordinates(MouseEvent mouseEvent) {
        double offset = 1000.0;

        double xFactor = mouseEvent.getX();
        double yFactor = mouseEvent.getY();
        inputAttributes.setFactorX(xFactor);
        inputAttributes.setFactorY(yFactor * offset);

        previewFilter();

        FOC_LOGGER.debug("Applying {} filter with xFactor = {} and yFactor = {} * {}", filterType, xFactor, yFactor, offset);
    }

    /**
     * Reads the value of the slider and updates the value in the FilterAttributes object.
     * Shows a preview.
     */
    @FXML
    private void changeSliderValue() {
        double sliderOffset = 10000;
        double sliderValue = glitchSlider.getValue();

        inputAttributes.setFactor(sliderValue / sliderOffset);
        previewFilter();

        FOC_LOGGER.debug("Applying {} with new slider value: {} * {}", filterType, sliderValue, sliderOffset);
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

        FOC_LOGGER.debug("Filter {} added to checkerboard index: {}", filterTypeChoice.getValue(), index);
    }

    /**
     * Handle panel increase.
     */
    @FXML
    private void handlePanelIncrease() {
        inputAttributes.increaseRuns();
        previewFilter();

        FOC_LOGGER.debug("Panels increased.");
    }

    /**
     * Handle panel decrease.
     */
    @FXML
    private void handlePanelDecrease() {
        inputAttributes.decreaseRuns();
        previewFilter();

        FOC_LOGGER.debug("Panels decreased.");
    }

    /**
     * Cancel out of the filter menu and revert to the image's previous state.
     */
    @FXML
    private void handleCancelFilter() {
        editorController.drawPreviousImage();

        Stage stage = (Stage) cancelFilterButton.getScene().getWindow();
        stage.close();

        FOC_LOGGER.debug("Filter stage closed via {}", cancelFilterButton.getId());
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

        if (appType == FilterApplicationType.CHECKERBOARD) {
            filterOptionsLabel.setText(appType.toString());
            initCheckerBoard();

            FOC_LOGGER.debug("FilterOptionsController view set to {}.", appType);
        } else {
            filterOptionsLabel.setText(filterType.toString());
            switch (filterType) {
                case GLITCH:
                    initGlitchFilter();

                    break;
                default:
                    initStandardFilter();
            }
            FOC_LOGGER.debug("FilterOptionsController view set to {}", filterType);
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

        FOC_LOGGER.debug("List sized maximized to {}", filterTypeList.size());
    }
}
