package main.java.edu.cmu.cs.cs214.esaAnalysis.framework.core;

import main.java.edu.cmu.cs.cs214.esaAnalysis.framework.core.types.AnalysisResult;

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
