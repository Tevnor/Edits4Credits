package org.editor.tools.filtertool;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.editor.EditorController;
import org.editor.tools.filtertool.filtercontrol.Filter;
import org.editor.tools.filtertool.filtercontrol.FilterInputAttributes;
import org.editor.tools.filtertool.filtercontrol.filter.FilterType;
import org.editor.tools.imagetool.ImageGrid;
import org.editor.tools.imagetool.ImageTool;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FilterOptionsController implements Initializable {

    private static final Logger FOC_LOGGER = LogManager.getLogger(FilterOptionsController.class.getName());

    @FXML
    private VBox filterPickVBox;
    @FXML
    private VBox factorVBox;
    @FXML
    private VBox panelVBox;
    @FXML
    private VBox silhouetteVBox;
    @FXML
    private Label filterOptionsLabel;

    @FXML
    private Slider glitchSlider;
    @FXML
    private ToggleButton complementToggleButton;
    @FXML
    private ToggleButton silhouetteToggleButton;
    @FXML
    private Button applyFilterButton;
    @FXML
    private Button cancelFilterButton;

    private Stage stage;

    private EditorController ec;
    private ImageTool imageTool;

    private ImageGrid originalImageGrid;
    private ImageGrid resizedImageGrid;

    private FilterOperation filterOperation;

    private List<FilterType> filterTypeList;
    private FilterInputAttributes inputAttributes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Init
     * */
    public void initFilterOptions(Image original, Image resized, List<FilterType> filterTypeList, String filterName, EditorController ec) {
        setMenu(filterName);

        inputAttributes = new FilterInputAttributes();
        this.ec = ec;
        this.originalImageGrid = new ImageGrid(original);
        this.resizedImageGrid = new ImageGrid(resized);
        originalImageGrid.setPixelArray();
        resizedImageGrid.setPixelArray();

        this.filterTypeList = maximizeList(filterTypeList);
        inputAttributes.setFilterTypeList(filterTypeList);

        filterOptionsLabel.setText(filterName);
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

    public void handleApplyFilter(ActionEvent actionEvent) {
//        inputAttributes.setOriginal(true);
        applyFilter();
        stage = (Stage) applyFilterButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Filter Input Options
     * */
    public void handleSilhouette(ActionEvent actionEvent) {
        inputAttributes.setSilhouetteToggle(silhouetteToggleButton.isSelected());
        previewFilter();
    }

    public void handleComplement(ActionEvent actionEvent) {
        inputAttributes.setComplementToggle(complementToggleButton.isSelected());
        previewFilter();
    }

    public void changeSliderValue(MouseEvent mouseEvent) {
        inputAttributes.setFactor(glitchSlider.getValue());
        previewFilter();
    }

    /**
     * Cancel
     * */
    public void handleCancelFilter(ActionEvent actionEvent) {
        // Reset to last image state
        ec.drawPreviousImage();

        // Close pop-up
        stage = (Stage) cancelFilterButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Maximize filter list for panel quarters
     * */
    public List<FilterType> maximizeList(List<FilterType> filterTypeList) {
        FilterType filterTypeEnumOne;
        FilterType filterTypeEnumTwo;

        switch (filterTypeList.size()) {
            case 1:
                filterTypeEnumOne = filterTypeList.get(0);
                filterTypeList.add(filterTypeEnumOne);
                filterTypeList.add(filterTypeEnumOne);
                filterTypeList.add(filterTypeEnumOne);
                return filterTypeList;
            case 2:
                filterTypeEnumOne = filterTypeList.get(0);
                filterTypeEnumTwo = filterTypeList.get(1);
                filterTypeList.add(filterTypeEnumTwo);
                filterTypeList.add(filterTypeEnumOne);
                return filterTypeList;
            default:
                return filterTypeList;
        }
    }

    public void setMenu(String name) {
        if (name.equals("Checkerboard")) {
            silhouetteVBox.setVisible(false);
            factorVBox.setVisible(false);
            panelVBox.setVisible(true);
            filterPickVBox.setVisible(true);
        } else {
            silhouetteVBox.setVisible(true);
            factorVBox.setVisible(true);
            panelVBox.setVisible(false);
            filterPickVBox.setVisible(false);
        }
    }
}
