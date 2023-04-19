package edu.cmu.cs214.hw6.framework.core;

import java.io.IOException;
import java.util.List;

public interface ESAFramework {

    public DataPlugin getCurrDataPlugin();

    public VisualizationPlugin getCurrVisPlugin();

    public List<DataPlugin> getDataPlugins();

    public List<VisualizationPlugin> getVisPlugins();

    public String getImgPath();

    public String getDirectoryPathStr();

    /**
     * Registers a new {@link DataPlugin} with the framework
     * 
     * @param plugin The DataPlugin to register
     */
    public void registerDataPlugin(DataPlugin plugin);

    /**
     * Registers a new {@link VisualizationPlugin} with the framework
     * 
     * @param plugin The VisualizationPlugin to register
     */
    public void registerVisPlugin(VisualizationPlugin plugin);

    /**
     * Changes the current {@link DataPlugin} in the framework
     * 
     * If the index is out of bounds, there will be no action
     * 
     * @param index The index for the desired dataPlugin in the list
     */
    public void selectDataPlugin(int index);

    /**
     * Changes the current {@link VisualizationPlugin} in the framework
     * 
     * If the index is out of bounds, there will be no action
     * 
     * @param index The index for the desired visualizationPlugin in the list
     */
    public void selectVisPlugin(int index);

    /**
     * Upload all of the files in the given local directory path. This path should
     * be stored in the directoryPathStr attribute.
     * 
     * Note that this directory should contain only files that match the type
     * handled by the registered data plugin.
     * 
     * @throws IOException
     */
    void uploadData() throws IOException;

    /**
     * Conduct the entity-sentiment analysis and get the visualization.
     * 
     * @return File path to visualization image file returned by visualization plugin
     * @throws Exception
     */
    String getAnalyzedVisualization() throws Exception;

}
