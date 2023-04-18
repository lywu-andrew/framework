package edu.cmu.cs214.hw6.plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import edu.cmu.cs214.hw6.framework.core.DataPlugin;
import edu.cmu.cs214.hw6.framework.core.ESAFramework;

public class TxtPlugin implements DataPlugin {
    private final String PLUGIN_NAME = "txt";
    
    private ESAFramework framework;

    /**
     * Gets the name of the data plugin.
     */
    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }

    /**
     * Convert the text in TXT file at the given path to a String.
     * @throws IOException
     */
    @Override
    public String convertToString(Path filePath) throws IOException {
        String content = Files.readString(filePath);
        return content;
    }

    /**
     * Called (only once) when the plug-in is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * before the game has begun (if necessary).
     *
     * @param framework The {@link ESAFramework} instance with which
     *                  the plug-in was registered.
     */
    @Override
    public void onRegister(ESAFramework f) {
        framework = f;
    }
}
