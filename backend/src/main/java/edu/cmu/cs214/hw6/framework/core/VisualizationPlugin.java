package edu.cmu.cs214.hw6.framework.core;

import java.io.IOException;
import java.util.List;

import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;

public interface VisualizationPlugin {

    static final String IMG_DIRECTORY = "src/main/java/edu/cmu/cs214/hw6/plugin/charts/";

    /**
     * Gets the name of the visualization plugin.
     */
    String getPluginName();

    /**
     * Visualizes the given data using the visualization type.
     * @param data The results of entity sentiment analysis
     * @return File path to visualization image file
     * @throws IOException
     */
    String visualizeData(List<AnalyzeEntitySentimentResponse> result) throws IOException;

}
