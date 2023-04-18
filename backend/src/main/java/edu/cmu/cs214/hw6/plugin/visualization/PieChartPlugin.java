package edu.cmu.cs214.hw6.plugin.visualization;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;

import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.Entity;

import edu.cmu.cs214.hw6.framework.core.ESAFramework;
import edu.cmu.cs214.hw6.framework.core.VisualizationPlugin;

public class PieChartPlugin implements VisualizationPlugin {
    private final String PLUGIN_NAME = "piechart";

    private ESAFramework framework;

    /**
     * Gets the name of the visualization plugin.
     */
    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }

    /**
     * Visualizes the given data using the visualization type.
     * @param data The results of entity sentiment analysis
     * @return File path to visualization image file
     * @throws IOException
     */
    @Override
    public String visualizeData(List<AnalyzeEntitySentimentResponse> responses) throws IOException {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<String>();
        for (AnalyzeEntitySentimentResponse response : responses) {
            List<Entity> entityList = response.getEntitiesList();
            for (int i = 0; i < entityList.size(); i++) {
                Entity e = entityList.get(i);
                dataset.insertValue(i, e.getName(), e.getSentiment().getMagnitude());
            }
        }
        JFreeChart pieChart = ChartFactory.createPieChart("Entity Sentiment Magnitude", dataset);
        File imgFile = new File(String.join("", "../charts/", PLUGIN_NAME, ".jpeg"));
        ChartUtils.saveChartAsJPEG(imgFile, pieChart, 600, 400);
        return imgFile.getPath();
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
