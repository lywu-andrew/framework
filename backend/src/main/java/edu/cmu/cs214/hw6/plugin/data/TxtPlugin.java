package edu.cmu.cs214.hw6.plugin.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import edu.cmu.cs214.hw6.framework.core.DataPlugin;

public class TxtPlugin implements DataPlugin {
    private final String PLUGIN_NAME = "txt";
    
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

}
