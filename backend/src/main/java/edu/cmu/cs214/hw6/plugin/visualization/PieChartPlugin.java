package edu.cmu.cs214.hw6.plugin.visualization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Visualizes the average sentiment magnitudes of entities with an average 
     * sentiment magnitude greater than 0 using a pie chart.
     * 
     * @param responses The results of entity sentiment analysis
     * @return File path to visualization image file
     * @throws IOException
     */
    @Override
    public String visualizeData(List<AnalyzeEntitySentimentResponse> responses) throws IOException {
        // Group the entities and their sentiment magnitudes into a dictionary of lists
        Map<String, List<Float>> dict = new HashMap<>();
        for (AnalyzeEntitySentimentResponse response : responses) {
            List<Entity> entityList = response.getEntitiesList();
            for (int i = 0; i < entityList.size(); i++) {
                Entity e = entityList.get(i);
                if (dict.containsKey(e.getName())) {
                    dict.get(e.getName()).add(Float.valueOf((float) e.getSentiment().getMagnitude()));
                } else {
                    dict.put(e.getName(), new ArrayList<Float>());
                    dict.get(e.getName()).add(Float.valueOf((float) e.getSentiment().getMagnitude()));
                }
            }
        }

        // Calculate average sentiment magnitude of each entity and inject them into a dataset
        DefaultPieDataset<String> dataset = new DefaultPieDataset<String>();
        int i = 0;
        for (Map.Entry<String,List<Float>> entry : dict.entrySet()) {
            entry.getKey();
            int sum = 0;
            for (int j = 0; j < entry.getValue().size(); j++) {
                sum += entry.getValue().get(j);
            }
            float avg = sum / entry.getValue().size();
            if (avg != 0) {
                dataset.insertValue(i, entry.getKey(), sum / entry.getValue().size());
                i++;
            }
        }

        // Create the pie chart using the dataset
        JFreeChart pieChart = ChartFactory.createPieChart("Average Sentiment Magnitude of Each Entity", dataset);
        File imgFile = new File(String.join("", IMG_DIRECTORY, PLUGIN_NAME, ".jpeg"));
        ChartUtils.saveChartAsJPEG(imgFile, pieChart, 1000, 1000);
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
