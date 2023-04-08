package main.java.edu.cmu.cs.cs214.entityAnalysis.framework.core;

import main.java.edu.cmu.cs.cs214.entityAnalysis.framework.core.types.AnalysisResult;

public interface VisualizationPlugin {

    /**
     * Gets the name of the visualization plugin.
     */
    String getPluginName();

    /**
     * Visualizes the given data using the visualization type.
     * @param data The results of entity sentiment analysis
     * @return An HTML string of the generated visualization
     */
    String visualizeData(AnalysisResult analysisResult);

}
