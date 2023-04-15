package main.java.edu.cmu.cs.cs214.entityAnalysis.framework.core;

public class EntityAnalysisFrameworkImpl implements EntityAnalysisFramework {
    private DataPlugin registeredDataPlugin;
    private VisualizationPlugin registeredVisPlugin;
    
    public EntityAnalysisFrameworkImpl() {

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