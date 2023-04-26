package edu.cmu.cs214.hw6Backend.framework.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class ColorFrameworkImpl implements ColorFramework {
    /**
     * Number of colors the output theme will contain.
     */
    private int numColors;

    /**
     * Registered data plugins.
     */
    private List<DataPlugin> dataPlugins;

    /**
     * Selected data plugins.
     */
    private DataPlugin selectedDataPlugin;

    /**
     * Absolute path to uploaded file; null if nothing uploaded.
     */
    private String filePath;

    /**
     * Data URI string representing uploaded file; null if nothing uploaded.
     */
    private String dataURI;

    /**
     * Loaded image, will be processed in the future.
     */
    private ImageData img;

    /**
     * Color theme as a result of analyzing img.
     */
    private ColorOutput output;

    /**
     * Constructor for ColorFramework.
     * Defaults number of colors in the resulting theme to 10.
     */
    public ColorFrameworkImpl() {
        this(DEFAULT_NUM_COLORS);
    }

    /**
     * Constructor for ColorFramework.
     * @param numColors Number of colors desired in the resulting theme.
     */
    public ColorFrameworkImpl(int numColors) {
        this.numColors = numColors;
        dataPlugins = new ArrayList<DataPlugin>();
        img = null;
        output = null;
        filePath = null;
        dataURI = null;
    }

    /**
     * Registers a data plugin with the framework, and the framework with the plugin.
     * If plugin already registered, ignore the request.
     * @param plugin DataPlugin to register
     * @return Whether the plugin has been successfully registered
     */
    public boolean registerDataPlugin(DataPlugin plugin) {
        plugin.onRegister(this);
        for (DataPlugin existingPlugin : dataPlugins) {
            if (plugin.getPluginName().equals(existingPlugin.getPluginName())) {
                // Don't repeatedly add data plugins
                return false;
            }
        }
        dataPlugins.add(plugin);
        return true;
    }

    /**
     * Saves the absoluted path to the uploaded file.
     */
    public void setFilePath(String path) {
        if ((path == null) || (path == "")) {
            // Ignore empty paths
            return;
        }
        filePath = path;
    }

    /**
     * Converts file stored at this.filePath to data URI using DataPlugin.
     * @return Whether conversion was successful
     */
    public boolean loadImageAndGetDataURI() {
        if ((filePath == null) || (filePath == "")) { // No file uploaded yet
            return false;
        }

        boolean canProcess = false;
        for (DataPlugin plugin : dataPlugins) {
            if (plugin.isValidFileType(filePath)) {
                canProcess = true;
                selectedDataPlugin = plugin;
            }
        }

        if (!canProcess) { // No registered plugin can process the image
            return false;
        }

        // Convert file stored at this.filePath
        dataURI = selectedDataPlugin.convertFileToDataURI(filePath);
        return true;
    }

    /**
     * Converts file stored at this.filePath to ImageData using DataPlugin.
     * @return Whether conversion was successful
     */
    public boolean convertFileToImageData() {
        if ((filePath == null) || (filePath == "") || (selectedDataPlugin == null)) {
            // No file uploaded, yet, reject request
            return false;
        }

        // Convert file stored at this.filePath
        img = selectedDataPlugin.convertFileToImage(filePath);
        return true;
    }
    
    /**
     * Converts pixels from the a loaded image (ImageData) to ColorOutput.
     * @return Whether conversion was successful
     */
    public boolean convertImageToColors() {
        if ((img == null) || (img.getWidth() == 0) || (img.getHeight() == 0)) {
            return false;
        }

        // Assuming colors are evenly distributed, compute distances between each
        int difR = img.getMaxByR().getRed() - img.getMinByR().getRed();
        int difG = img.getMaxByG().getGreen() - img.getMinByG().getGreen();
        int difB = img.getMaxByB().getBlue() - img.getMinByB().getBlue();
        int difA = img.getMaxByA().getAlpha() - img.getMinByA().getAlpha();
        int intervalRGB = (difR + difG + difB) / numColors;
        int intervalA = difA / numColors;

        // Gather equal copies of the same pixel in to a Map
        Map<Integer, Integer> colorsWithQuant = new HashMap<Integer, Integer>();
        for (int col = 0; col < img.getWidth(); col++) {
            for (int row = 0; row < img.getHeight(); row++) {
                RgbPixel pixel = img.getColor(row, col);
                int key = pixel.getColorHex();
                if (colorsWithQuant.containsKey(key)) {
                    int quant = colorsWithQuant.get(key);
                    colorsWithQuant.replace(key, quant + 1);
                } else {
                    colorsWithQuant.put(key, 1);
                }
            }
        }

        // Sort unique colors list based on number of occurrence
        List<Map.Entry<Integer, Integer>> uniqueColors = new ArrayList<Map.Entry<Integer, Integer>>(colorsWithQuant.entrySet());
        uniqueColors.sort(new QuantComparator());

        List<RgbPixel> result = new ArrayList<RgbPixel>();
        Queue<RgbPixel> remainder = new ArrayDeque<>();

        // Requesting more colors than the number of unique colors in the image
        // gaurantees repeats, and every color should appear at least once
        if (uniqueColors.size() <= numColors) {
            int i = 0;
            int num = 0;
            while (num < numColors) {
                i = i % uniqueColors.size();
                result.add(RgbPixel.getPixelFromHex(uniqueColors.get(i).getKey()));
                i++;
                num++;
            }
            output = new ColorOutput(result);
            return true;
        } 

        // Pick colors that are as unique as possible
        int num = 1;
        int i = 1;
        RgbPixel curPixel = RgbPixel.getPixelFromHex(uniqueColors.get(0).getKey());
        result.add(curPixel);
        while ((num < numColors) && (i < uniqueColors.size())) {
            curPixel = RgbPixel.getPixelFromHex(uniqueColors.get(i).getKey());
            if (pixelUnique(result, curPixel, intervalRGB, intervalA)) {
                result.add(curPixel);
                num++;
            } else {
                remainder.add(curPixel);
            }
            i++;
        }

        // If not enough colors selected, start from front of unused colors and pick as many as needed
        if (num != numColors) {
            while ((num < numColors) && (!remainder.isEmpty())) {
                RgbPixel pixel = remainder.remove();
                if (pixel.getAlpha() != 0) { // Don't grab transparent colors
                    result.add(pixel);
                    num++;
                }
            }
        }

        // Still not enough colors selected, start from front and pick as many as needed
        if (num != numColors) {
            i = 0;
            while (num < numColors) {
                i = i % uniqueColors.size();
                RgbPixel pixel = RgbPixel.getPixelFromHex(uniqueColors.get(i).getKey());
                if (pixel.getAlpha() != 0) { // Don't grab transparent colors
                    result.add(pixel);
                    num++;
                }
                i++;
            }
        }
        output = new ColorOutput(result);
        return true;
    }

    /**
     * For comparing Map.Entry<Integer, Integer> by value.
     */
    private static class QuantComparator implements Comparator<Map.Entry<Integer, Integer>> {
        @Override
        public int compare(Map.Entry<Integer, Integer> entry1, Map.Entry<Integer, Integer> entry2) {
            return Integer.compare(entry2.getValue(), entry1.getValue());
        }
    }

    /**
     * Checks that there're no repeated pixels or pixels that are "too close" to existing ones.
     * White (0xFFFFFFFF) is not considered "unique."
     * @param pixels Pixels that have been picked
     * @param pixelToCheck Pixel to verify against existing pixels
     * @param intervalRGB Expected RGB distance between pixels
     * @param intervalA Expected alpha distance between pixels
     * @return Whether pixel is "unique" enough
     */
    private boolean pixelUnique(List<RgbPixel> pixels, RgbPixel pixelToCheck,
                                  int intervalRGB, int intervalA) {
        if (((pixelToCheck.getRed() == RgbPixel.COLOR_MAX) &&
            (pixelToCheck.getGreen() == RgbPixel.COLOR_MAX) &&
            (pixelToCheck.getBlue() == RgbPixel.COLOR_MAX) &&
            (pixelToCheck.getAlpha() == RgbPixel.COLOR_MAX)) ||
            (pixelToCheck.getAlpha() == 0)) {
            // Should ignore pure white and transparent colors
            return false;
        }
        for (RgbPixel pixel : pixels) {
            int totalDiff = (Math.abs(pixel.getRed() - pixelToCheck.getRed()) +
                             Math.abs(pixel.getGreen() - pixelToCheck.getGreen()) +
                             Math.abs(pixel.getBlue() - pixelToCheck.getBlue()));
            if ((totalDiff < intervalRGB) ||
                ((intervalA > 0) && (Math.abs(pixel.getAlpha() - pixelToCheck.getAlpha()) < intervalA))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets number of colors included in ColorOutput.
     * @param num Number of colors included in ColorOutput
     */
    public void setNumColors(int num) {
        if (num <= 0) { // Cannot set number of colors to non-positive number
            return;
        }
        numColors = num;
        if (output != null) {
            // Output already exists, need to recompute
            convertImageToColors();
        }
    }

    public int getNumColors() {
        return numColors;
    }

    public ColorOutput getColorOutput() {
        if (output == null) { // Return empty output
            return new ColorOutput();
        }
        return new ColorOutput(output.getAllColors());
    }

    public ImageData getImageData() {
        if (img == null) { // Return empty ImageData
            return new ImageData();
        }
        return new ImageData(img.getWidth(), img.getHeight(), img.getAllColors());
    }

    public String[] getSupportedFileTypes() {
        String[] result = new String[dataPlugins.size()];
        int i = 0;
        for (DataPlugin plugin : dataPlugins) {
            result[i] = plugin.getSupportedFileType();
            i++;
        }
        return result;
    }

    public String getDataURI() {
        if (dataURI == null) {
            return "";
        }
        return dataURI;
    }

    public String getFilePath() {
        if (filePath == null) {
            return "";
        }
        return filePath;
    }

    /**
     * Reinitializes the framework's private fields.
     */
    public void clearResults() {
        img = null;
        output = null;
        filePath = null;
        dataURI = null;
        numColors = DEFAULT_NUM_COLORS;
    }
}
