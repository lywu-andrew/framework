package edu.cmu.cs214.hw6.plugin.visualization;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;

import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.Entity;

import edu.cmu.cs214.hw6.framework.core.ESAFramework;
import edu.cmu.cs214.hw6.framework.core.VisualizationPlugin;

public class BarChartPlugin implements VisualizationPlugin {
    private final String PLUGIN_NAME = "barchart";

    private ESAFramework framework;

    /**
     * Gets the name of the visualization plugin.
     */
    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }

    /**
     * Visualizes the salience scores of all entities using a bar graph.
     * @param responses The results of entity sentiment analysis
     * @return File path to visualization image file
     * @throws IOException
     */
    @Override
    public String visualizeData(List<AnalyzeEntitySentimentResponse> responses) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (AnalyzeEntitySentimentResponse response : responses) {
            for (Entity e : response.getEntitiesList()) {
                dataset.addValue((Number)e.getSalience(), "salience score", e.getName());
            }
        }
        JFreeChart barChart = ChartFactory.createBarChart("Entity Salience", "Entities", "Salience value", dataset);
        File imgFile = new File(String.join("", IMG_DIRECTORY, PLUGIN_NAME, ".jpeg"));
        ChartUtils.saveChartAsJPEG(imgFile, barChart, 600, 400);
        return String.join("", IMG_DIRECTORY, imgFile.getName());
    }

    /**
     * Called (only once) when the plug-in is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * before the game has begun (if necessary).
     *
     * @param framework The {@link ESAFramework} instance with which
     *                  the plug-in was registered.
     */
    @Override
    public void onRegister(ESAFramework f) {
        framework = f;
    }
}
