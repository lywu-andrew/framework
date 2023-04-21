package edu.cmu.cs214.hw6.framework.core;

import java.io.IOException;
import java.nio.file.Path;

public interface DataPlugin {

    /**
     * Gets the name of the data plugin.
     */
    String getPluginName();

    /**
     * Convert the file at the given path to a String.
     * @throws IOException
     */
    String convertToString(Path filePath) throws IOException;

}