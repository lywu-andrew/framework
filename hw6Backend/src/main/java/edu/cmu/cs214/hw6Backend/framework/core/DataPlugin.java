package edu.cmu.cs214.hw6Backend.framework.core;

/**
 * Defines basic template for DataPlugin.
 */
public interface DataPlugin {
    /**
     * Registers plugin with framework.
     * This is your only chance to save a reference to the framework.
     * @param framework Framework to save locally
     */
    void onRegister(ColorFramework framework);

    /**
     * Check if the uploaded file can be processed by this DataPlugin.
     * @param filePath Absolute path to the image file
     * @return Whether image can be processed by the plugin
     */
    boolean isValidFileType(String filePath);

    /**
     * Converts file stored at filePath to a data URI the front end can use.
     * 
     * Note: the data URI must represent an image file!
     * The frontend will attempt to use this data URI to display your image.
     * 
     * @param filePath Absolute path to the image file
     * @return A data URI the front end can use
     */
    String convertFileToDataURI(String filePath);

    /**
     * Converts file stored at filePath to image data the framework can use.
     * @param filePath Absolute path to the image file
     * @return ImageData the framework can process
     */
    ImageData convertFileToImage(String filePath);

    /**
     * Type(s) of files this plugin supports.
     * 
     * Must be in the format: .xxx or .xxxx
     * E.g. ".jpg", ".png"
     * 
     * Must be unique to the plugin, and cannot be changed during execution!
     * This will also show up on the webpage to inform the users what kind of files
     * they can provide to the framework.
     * 
     * @return Supported file type extension
     */
    String getSupportedFileType();

    /**
     * Name of this plugin
     * Must be unique to the plugin, and cannot be changed during execution!
     * @return Plugin name
     */
    String getPluginName();
}
