package edu.cmu.cs214.hw6.framework.core;

import java.io.IOException;
import java.util.List;

public interface ESAFramework {

    public DataPlugin getCurrDataPlugin();

    public VisualizationPlugin getCurrVisPlugin();

    public List<DataPlugin> getDataPlugins();

    public List<VisualizationPlugin> getVisPlugins();

    public String getImgPath();

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
     * Upload all of the files in the given local directory path.
     * 
     * Note that this directory should contain only files that match the type
     * handled by the registered data plugin.
     * 
     * @param string The path to the local directory containing the data
     * @throws IOException
     */
    void uploadData(String directoryPath) throws IOException;

    /**
     * Conduct the entity-sentiment analysis and get the visualization.
     * 
     * @return An HTML string of the generated visualization
     * @throws Exception
     */
    String getAnalyzedVisualization() throws Exception;

}
