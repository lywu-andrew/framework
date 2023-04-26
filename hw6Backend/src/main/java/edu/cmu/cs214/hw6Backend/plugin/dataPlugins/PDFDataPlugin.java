package edu.cmu.cs214.hw6Backend.plugin.dataPlugins;

import edu.cmu.cs214.hw6Backend.framework.core.ColorFramework;
import edu.cmu.cs214.hw6Backend.framework.core.DataPlugin;
import edu.cmu.cs214.hw6Backend.framework.core.ImageData;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.Graphics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.ImageType;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

public class PDFDataPlugin  implements DataPlugin {
    private static final String SUPPORTED_FILE_TYPE = ".pdf";
    private static final String PLUGIN_NAME = "PDF Data Plugin";

    /**
     * How many pages will be combined into 1 BufferedImage.
     */
    private static final int MAX_PDF_PAGES = 3;

    private BufferedImage img;
    
    /**
     * This plugin doesn't use anything from the framework, so it won't save anything locally.
     * @param framework ColorFramework this plugin is attempting to register with
     */
    public void onRegister(ColorFramework framework) {
        return;
    }

    /**
     * Checks if the file stored at filePath is a .pdf file.
     * @param filePath Absolute path to the file
     * @return If the file is a .pdf file
     */
    public boolean isValidFileType(String filePath) {
        String fileExtension = ColorFramework.getFileExtension(filePath);
        if (fileExtension.toLowerCase().equals(SUPPORTED_FILE_TYPE.substring(1))) {
            return true;
        }
        return false;
    }

    /**
     * Converts file stored at filePath to a data URI the front end can use.
     * @param filePath Absolute path to the image file
     * @return A data URI the front end can use
     */
    public String convertFileToDataURI(String filePath) {
        try {
            readPDFIntoImg(filePath); // Will set the local field "img"
        } catch (IOException e) {
            return "";
        }

        if (img == null) { // Conversion was unsuccessful
            return "";
        }

        // Attempt to convert to data URI
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "jpg", bytes);
            String imageString = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(bytes.toByteArray());
            return imageString;
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Converts file stored at filePath to ImageData for ColorFramework.
     * @param filePath File's absolute path
     * @return File, represented as ImageData
     */
    public ImageData convertFileToImage(String filePath) {
        if (img == null) {
            try {
                readPDFIntoImg(filePath);
            } catch (IOException e){
                return new ImageData();
            }
        }
        return new ImageData(img.getWidth(), img.getHeight(),
                             (DataBufferInt) img.getRaster().getDataBuffer());
    }

    /**
     * Get the supported file extension.
     * @return Supported file extension
     */
    public String getSupportedFileType() {
        return SUPPORTED_FILE_TYPE;
    }

    /**
     * Get the unique plugin name.
     * @return Plugin name
     */
    public String getPluginName() {
        return PLUGIN_NAME;
    }

    /**
     * Combines the first few pages of PDF located at filePath into 1 jpg.
     * @param filePath Absolute path to the PDF file
     * @throws IOException if file doesn't exist
     */
    private void readPDFIntoImg(String filePath) throws IOException {
        File file = new File(filePath);
        PDDocument document = PDDocument.load(file);
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        // Render PDF pages as BufferedImages
        int numPages = document.getNumberOfPages();
        if (numPages <= 0) { // Ignore empty PDF
            return;
        }
        // To ensure that the computation is fast enough, only accept first 3 pages
        numPages = (numPages > MAX_PDF_PAGES) ? MAX_PDF_PAGES : numPages;
        List<BufferedImage> images = new ArrayList<BufferedImage>(numPages);
        for (int page = 0; page < numPages; page++) {
            BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            images.add(image);
        }

        // Combine resulting BufferedImages as one BufferedImage
        img = new BufferedImage(images.get(0).getWidth(), numPages * images.get(0).getHeight(),
                                BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        int y = 0;
        for(BufferedImage image : images){
            g.drawImage(image, 0, y, null);
            y += images.get(0).getHeight();
        }

        document.close();
    }
}
