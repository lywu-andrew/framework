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
