package edu.cmu.cs214.hw6.framework.core;

public interface ESAFramework {

    /**
     * Upload all of the files in the given local directory path.
     * 
     * Note that this directory should contain only files that match the type
     * handled by the registered data plugin.
     * 
     * @param string The path to the local directory containing the data
     */
    void uploadData(String directoryPath);

    /**
     * Conduct the entity-sentiment analysis and get the visualization.
     * 
     * @return An HTML string of the generated visualization
     */
    String getAnalyzedVisualization();

}
