package edu.cmu.cs214.hw6Backend.framework.core;

/**
 * Interface for the framework.
 */
public interface ColorFramework {
    /**
     * By default, will give a theme with 10 different colors.
     */
    public static final int DEFAULT_NUM_COLORS = 10;

    /**
     * Gets the ".xxx" file extension of a fileName, if any.
     * @param fileName File name to parse
     * @return File extension, if any
     */
    public static String getFileExtension(String fileName) {
        if ((fileName == null) || (fileName.isEmpty())) {
            return "";
        }

        String extension = "";

        int indexOfLastExtension = fileName.lastIndexOf(".");

        // check last file separator, windows and unix
        int lastSeparatorPosWindows = fileName.lastIndexOf("\\");
        int lastSeparatorPosUnix = fileName.lastIndexOf("/");

        // takes the greater of the two values, which mean last file separator
        int indexOflastSeparator = Math.max(lastSeparatorPosWindows, lastSeparatorPosUnix);

        // make sure the file extension appear after the last file separator
        if (indexOfLastExtension > indexOflastSeparator) {
            extension = fileName.substring(indexOfLastExtension + 1);
        }

        return extension;
    }

    /**
     * Registers a data plugin with the framework, and the framework with the plugin.
     * If plugin already registered, ignore the request.
     * @param plugin DataPlugin to register
     * @return Whether the plugin has been successfully registered
     */
    public boolean registerDataPlugin(DataPlugin plugin);

    /**
     * Get number of colors the framework's output will contain.
     * @return Number of colors in the output
     */
    public int getNumColors();

    /**
     * Get the absolute file path to the uploaded file stored in the framework.
     * @return Absolute file path
     */
    public String getFilePath();
}
