package org.example;

import javafx.animation.ScaleTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Editor implements Initializable {

    @FXML
    private Slider variableSlider;
    @FXML
    private ToggleButton silhouetteToggle;
    @FXML
    private ToggleButton complementToggle;
    @FXML
    private ToggleButton originalToggle;
    @FXML
    private ImageView editorImageView;

    String imagePath = "/images/";
    String fileName = "4k.jpg";
    private Image sourceImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + fileName)));

    private int width = (int) sourceImage.getWidth();
    private int height = (int) sourceImage.getHeight();

    private int ivHeight = 1280;
    private int ratio = ivHeight/height;

    public void setSourceImage() {
        i = scale(sourceImage, width, ivHeight, true, true);
        resizedImageView = new ImageView(i);
        resizedImage = resizedImageView.getImage();
    }

    Image i = scale(sourceImage, width, ivHeight, true, true);
    ImageView resizedImageView = new ImageView(i);

    Image resizedImage = resizedImageView.getImage();

    private final int resizedWidth = (int) resizedImageView.getImage().getWidth();
    private final int resizedHeight = (int) resizedImageView.getImage().getHeight();

    public Image scale(Image source, int targetWidth, int targetHeight, boolean preserveRatio, boolean smooth) {
        ImageView imageView = new ImageView(source);
        imageView.setPreserveRatio(preserveRatio);
        imageView.setSmooth(smooth);
        imageView.setFitWidth(targetWidth*ratio);
        imageView.setFitHeight(targetHeight);
        System.out.println("th " + targetHeight);
        System.out.println("h " + height);
        System.out.println("tw " + targetWidth*ratio);
        return imageView.snapshot(null, null);
    }


    private WritableImage wImageOriginal = new WritableImage(width, height);
    private WritableImage wImageCurrent = new WritableImage(resizedWidth, resizedHeight);

    private WritableImage wImageLast = new WritableImage(width, height);

    private final PixelReader pixelReader = resizedImage.getPixelReader();
    private final PixelReader pixelReaderOriginal = sourceImage.getPixelReader();

    private final PixelWriter pixelWriterCurrent = wImageCurrent.getPixelWriter();
    private final PixelWriter pixelWriterOriginal = wImageOriginal.getPixelWriter();

    // Boolean to toggle apply method
    private boolean applyOnOriginal;
    // Boolean to toggle ARGB cast to short
    private boolean silhouette;
    // Boolean to toggle bitwise complement
    private boolean complement;

    // Value of variable slider
    private double sliderValue;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editorImageView.setImage(resizedImage);
    }

    // Glitch filter

    public void chooseFilter() {
        if (applyOnOriginal) {
            applyFilter(width, height, pixelReaderOriginal, pixelWriterOriginal);
        }
        applyFilter(resizedWidth, resizedHeight, pixelReader, pixelWriterCurrent);
    }
    public void applyFilter(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter) {
        System.out.println(resizedWidth + ", " + resizedHeight);
        int w, h;

        // Check apply method
            // Reading the color of the image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    //Retrieving the color of the pixel of the loaded image
                    double d = sliderValue / 10000;
                    int argb = (int) (pixelReaderOriginal.getArgb(x, y) / (1 + d));
                    short shortArgb;

                    if (silhouette) {
                        shortArgb = (short) argb;
                        if (complement) {
                            shortArgb = (short) ~argb;
                        }
                        pixelWriterOriginal.setArgb(x, y, shortArgb);
                    } else {
                        // Setting the color to the writable image
                        pixelWriterOriginal.setArgb(x, y, argb);
                    }
                }
            }
        for (int y = 0; y < resizedHeight; y++) {
            for (int x = 0; x < resizedWidth; x++) {
                //Retrieving the color of the pixel of the loaded image
                double d = sliderValue / 10000;
                int argb = (int) (pixelReader.getArgb(x, y) / (1 + d));
                short shortArgb;

                if (silhouette) {
                    shortArgb = (short) argb;
                    if (complement) {
                        shortArgb = (short) ~argb;
                    }
                    pixelWriterCurrent.setArgb(x, y, shortArgb);
                } else {
                    // Setting the color to the writable image
                    pixelWriterCurrent.setArgb(x, y, argb);
                }
            }
        }
        wImageLast = wImageCurrent;
        editorImageView.setImage(wImageCurrent);
    }
    public void changeSliderValue(MouseEvent mouseEvent) {
        sliderValue = variableSlider.getValue();
        chooseFilter();
    }
    public void toggleSilhouette(MouseEvent e) {
        silhouette = silhouetteToggle.isSelected();
        if (silhouette) {
            silhouetteToggle.setTextFill(Color.WHITE);
        }
        chooseFilter();
    }
    public void toggleComplement(MouseEvent e) {
        complement = complementToggle.isSelected();
        chooseFilter();
    }

    // Checkerboard filter
    private int panels = 2;
    public void onCheckerboardButtonClicked(ActionEvent actionEvent) {
        chooseCheckerboard();
    }
    public void chooseCheckerboard() {
        if(applyOnOriginal){
            applyCheckerboard(width, height, pixelReaderOriginal, pixelWriterOriginal);
        }
        applyCheckerboard(resizedWidth, resizedHeight, pixelReader, pixelWriterCurrent);
    }
    public void applyCheckerboard(int width, int height, PixelReader pixelReader, PixelWriter pixelWriter) {
        // Computations counter variable
        int c = 0;

        // Size of the panels
        int blockHeight = height / panels;
        int blockWidth = width / panels;

        width = blockWidth * panels;
        height = blockHeight * panels;

        // x and y pixel iterator
        int yPixel = 0;
        int factor = 7;
        // Set up checkerboard loop
        for (int yPanels = 0; yPanels < (panels / 2); yPanels++) {                                // Iterate through two alternating panel rows at a time
            for (int row = 0; row < blockHeight; row++) {                                       // Iterate through each row of the panel
                int xPixel = 0;
                for (int xPanels = 0; xPanels < (panels / 2); xPanels++) {                        // Iterate through two alternating panel columns at a time
                    for (int column = 0; column < blockWidth; column++) {                       // Iterate through each column of the panel
                        applyGlitch(xPixel, yPixel, factor, pixelReader, pixelWriter);
                        xPixel++;
                        c++;
                    }                                                                           // Alternate X
                    for (int column = 0; column < blockWidth; column++) {                       // Iterate through each column of the panel
                        applyBW(xPixel, yPixel, pixelReader, pixelWriter);
                        xPixel++;
                        c++;
                    }
                }
                yPixel++;
            }                                                                                   // Alternate Y
            for (int row = 0; row < blockHeight; row++) {                                       // Iterate through each row of the panel
                int xPixel = 0;
                for (int xPanels = 0; xPanels < (panels / 2); xPanels++) {
                    // Iterate through two alternating panel columns at a time
                    for (int column = 0; column < blockWidth; column++) {
                        // Apply black and white filter
                        applyOriginal(xPixel, yPixel, pixelReader, pixelWriter);
                        xPixel++;
                        c++;
                    }
                    for (int column = 0; column < blockWidth; column++) {                       // Iterate through each column of the panel
//                        int argb = (int) (pixelReader.getArgb(xPixel, yPixel) / 1.04) * 7;      // Retrieving the color of the pixel of the loaded image and apply filter
//                        writer.setArgb(xPixel, yPixel, argb);                                   // Setting the modified color to the writable image
                        applyInvert(xPixel, yPixel, pixelReader, pixelWriter);

                        xPixel++;
                        c++;
                    }
                    // Alternate X
                }
                yPixel++;
            }
        }
        System.out.println(c);
        wImageLast = wImageCurrent;
        editorImageView.setImage(wImageCurrent);
    }
    public void decrementPanels() {
        if(panels <= 2) {
            panels = 1;
        } else {
            panels -= 2;
        }
        chooseCheckerboard();
    }
    public void incrementPanels() {
        if (panels >= 14) {
            panels = 16;
        } else if (panels == 1) {
            panels = 2;
        } else {
            panels += 2;
        }
        chooseCheckerboard();
    }

    // Standard filters
    public void applyGlitch(int xPixel, int yPixel, int factor, PixelReader pixelReader, PixelWriter writer) {
        //Retrieving the color of the pixel of the loaded image
        int argb = (int) (pixelReader.getArgb(xPixel, yPixel) / 1.00222) * factor;
        //Setting the color to the writable image
        writer.setArgb(xPixel, yPixel, argb);
    }
    public void applyBW(int xPixel, int yPixel, PixelReader pixelReader, PixelWriter writer) {
        //Retrieving the color of the pixel of the loaded image
        Color color = pixelReader.getColor(xPixel, yPixel);
        //Setting the color to the writable image
        writer.setColor(xPixel, yPixel, color.grayscale());
    }
    public void applyOriginal(int xPixel, int yPixel, PixelReader pixelReader, PixelWriter writer) {
        // Retrieving the color of the pixel of the loaded image
        int argb = pixelReader.getArgb(xPixel, yPixel);
        // Setting the original color to the writable image
        writer.setArgb(xPixel, yPixel, argb);
    }
    public void applyInvert(int xPixel, int yPixel, PixelReader pixelReader, PixelWriter writer) {
        // Retrieving the color of the pixel of the loaded image
        Color color = pixelReader.getColor(xPixel, yPixel);
        // Setting the original color to the writable image
        writer.setColor(xPixel, yPixel, color.invert());
    }

    // Drag and drop
    @FXML
    private void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }
    @FXML
    private void handleDragDropped(DragEvent dragEvent) {
        String filename;
        try {
//            List<File> files = dragEvent.getDragboard().getFiles();
            File f = dragEvent.getDragboard().getFiles().get(0);
            Image droppedImage = new Image(new FileInputStream(f));
            setDroppedImage(droppedImage, f.getName());
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void setDroppedImage(Image droppedImage, String name) {
        fileName = name;
        sourceImage = droppedImage;
        setSourceImage();
        editorImageView.setImage(droppedImage);
    }

    // Save Image to disk
    public void saveImage(ActionEvent actionEvent) {
        if (applyOnOriginal) {
            saveToFile(wImageOriginal);
        } else {
            saveToFile(wImageCurrent);
        }
    }
    public static void saveToFile(WritableImage writableImage) {
        try {
            File outputFile = new File("saved.png");
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(bufferedImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Toggle between original and scaled down version
    public void toggleOriginal(MouseEvent mouseEvent) {
        applyOnOriginal = originalToggle.isSelected();
    }
}
