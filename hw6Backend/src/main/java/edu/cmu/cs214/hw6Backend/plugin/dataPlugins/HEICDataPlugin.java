package edu.cmu.cs214.hw6Backend.plugin.dataPlugins;

import edu.cmu.cs214.hw6Backend.framework.core.ColorFramework;
import edu.cmu.cs214.hw6Backend.framework.core.DataPlugin;
import edu.cmu.cs214.hw6Backend.framework.core.ImageData;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.util.Base64;

import com.idrsolutions.image.JDeli;
import com.idrsolutions.image.encoder.OutputFormat;

public class HEICDataPlugin implements DataPlugin {
    private static final String SUPPORTED_FILE_TYPE = ".heic";
    private static final String PLUGIN_NAME = "HEIC Data Plugin";

    private BufferedImage img;
    
    /**
     * This plugin doesn't use anything from the framework, so it won't save anything locally.
     * @param framework ColorFramework this plugin is attempting to register with
     */
    public void onRegister(ColorFramework framework) {
        return;
    }

    /**
     * Checks if the file stored at filePath is a .jpeg file.
     * @param filePath Absolute path to the file
     * @return If the file is a .jpeg file
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
            img = importImage(filePath);
        } catch (IOException e) {
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        // Attempt to convert to data URI
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            JDeli.write(img, OutputFormat.HEIC, bytes);
            String imageString = "data:image/heic;base64," + Base64.getEncoder().encodeToString(bytes.toByteArray());
            return imageString;
        } catch (IOException e) {
            return "";
        } catch (Exception e) {
            e.printStackTrace();
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
                img = importImage(filePath);
            } catch (IOException e){
                return new ImageData();
            } catch (Exception e) {
                e.printStackTrace();
                return new ImageData();
            }
        }
        // type is TYPE_3BYTE_BGR
        return new ImageData(img.getWidth(), img.getHeight(),
                             (DataBufferByte) img.getRaster().getDataBuffer(),
                             false);
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
     * Imports the file stored at filePath as a BufferedImage.
     * @param filePath File's absolute path
     * @return File, read as a IOException
     * @throws Exception
     */
    private BufferedImage importImage(String filePath) throws Exception {
        File f = new File(filePath);
        return JDeli.read(f);
    }
}
