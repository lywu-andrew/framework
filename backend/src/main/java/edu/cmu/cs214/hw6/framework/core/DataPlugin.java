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

    /**
     * Called (only once) when the plug-in is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * before the game has begun (if necessary).
     *
     * @param framework The {@link ESAFramework} instance with which
     *                  the plug-in was registered.
     */
    void onRegister(ESAFramework framework);

}