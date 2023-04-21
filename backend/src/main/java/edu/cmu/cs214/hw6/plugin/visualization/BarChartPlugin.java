package edu.cmu.cs214.hw6.plugin.visualization;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;

import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.Entity;

import edu.cmu.cs214.hw6.framework.core.VisualizationPlugin;

public class BarChartPlugin implements VisualizationPlugin {
    private final String PLUGIN_NAME = "barchart";

    /**
     * Gets the name of the visualization plugin.
     */
    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }

    /**
     * Visualizes the salience scores of all entities with a salience greater than 0.01
     * using a bar graph.
     * 
     * @param responses The results of entity sentiment analysis
     * @return File path to visualization image file
     * @throws IOException
     */
    @Override
    public String visualizeData(List<AnalyzeEntitySentimentResponse> responses) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (AnalyzeEntitySentimentResponse response : responses) {
            for (Entity e : response.getEntitiesList()) {
                if (e.getSalience() > 0.01) {
                    dataset.addValue((Number)e.getSalience(), "salience score", e.getName());
                }
            }
        }
        
        JFreeChart barChart = ChartFactory.createBarChart("Entity Salience", "Entities", "Salience Value", dataset);
        CategoryAxis axis = barChart.getCategoryPlot().getDomainAxis();
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        File imgFile = new File(String.join("", IMG_DIRECTORY, PLUGIN_NAME, ".jpeg"));
        ChartUtils.saveChartAsJPEG(imgFile, barChart, 1000, 1000);
        return String.join("", IMG_DIRECTORY, imgFile.getName());
    }

}
