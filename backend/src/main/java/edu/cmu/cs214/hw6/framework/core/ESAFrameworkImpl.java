package edu.cmu.cs214.hw6.framework.core;

import java.util.List;

import edu.cmu.cs214.hw6.framework.core.types.Text;

public class ESAFrameworkImpl implements ESAFramework {
    private DataPlugin registeredDataPlugin;
    private VisualizationPlugin registeredVisPlugin;
    private List<Text> texts;
    
    public ESAFrameworkImpl() {

    }

    /**
     * Registers a new {@link DataPlugin} with the framework
     */
    public void registerDataPlugin(DataPlugin plugin) {
        plugin.onRegister(this);
        registeredDataPlugin = plugin;
    }

    /**
     * Registers a new {@link VisualizationPlugin} with the framework
     */
    public void registerVisPlugin(VisualizationPlugin plugin) {
        plugin.onRegister(this);
        registeredVisPlugin = plugin;
    }

    /**
     * Source: https://www.tutorialspoint.com/how-to-read-data-from-all-files-in-a-directory-using-java
     */
    @Override
    public void uploadData(String directoryPath) {
        
    }

    @Override
    public String getAnalyzedVisualization() {
        return null;
    }
}
