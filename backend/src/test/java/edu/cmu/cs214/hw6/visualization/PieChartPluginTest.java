package edu.cmu.cs214.hw6.visualization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.cmu.cs214.hw6.plugin.visualization.PieChartPlugin;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.Sentiment;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for PieChartPlugin
 * 
 * This test file mainly tests PieChartPlugin.visualizeData(responses). We mock API responses
 * using Mockito mock for the API objects and confirm the resulting visualization image file exists.
 */
public class PieChartPluginTest {

    private PieChartPlugin plugin; // This sample plugin visualizes entities with their sentiment magnitudes.
    private List<AnalyzeEntitySentimentResponse> responses;

    @Before
    public void setUp() {
        this.plugin = new PieChartPlugin();
        this.responses = new ArrayList<>();
        AnalyzeEntitySentimentResponse r1 = mock(AnalyzeEntitySentimentResponse.class);
        responses.add(r1);
        AnalyzeEntitySentimentResponse r2 = mock(AnalyzeEntitySentimentResponse.class);
        responses.add(r2);

        List<Entity> eList1 = new ArrayList<>();
        Entity e1 = mock(Entity.class);
        eList1.add(e1);
        Entity e2 = mock(Entity.class);
        eList1.add(e2);
        when(r1.getEntitiesList()).thenReturn(eList1);
        List<Entity> eList2 = new ArrayList<>();
        Entity e3 = mock(Entity.class);
        eList1.add(e3);
        Entity e4 = mock(Entity.class);
        eList1.add(e4);
        when(r2.getEntitiesList()).thenReturn(eList2);

        // random entity name values
        when(e1.getName()).thenReturn("fun");
        when(e2.getName()).thenReturn("games");
        when(e3.getName()).thenReturn("hi");
        when(e4.getName()).thenReturn("bye");

        Sentiment s1 = mock(Sentiment.class);
        when(e1.getSentiment()).thenReturn(s1);
        Sentiment s2 = mock(Sentiment.class);
        when(e2.getSentiment()).thenReturn(s2);
        Sentiment s3 = mock(Sentiment.class);
        when(e3.getSentiment()).thenReturn(s3);
        Sentiment s4 = mock(Sentiment.class);
        when(e4.getSentiment()).thenReturn(s4);

        // random sentiment magnitude values
        when(s1.getMagnitude()).thenReturn((float)0.9);
        when(s2.getMagnitude()).thenReturn((float)0.2);
        when(s3.getMagnitude()).thenReturn((float)0.31);
        when(s4.getMagnitude()).thenReturn((float)0.75);
    }

    @Test
    public void testPieChartVisualization() {
        try {
            String ret = plugin.visualizeData(responses);
            assertEquals(ret, "src/main/java/edu/cmu/cs214/hw6/plugin/charts/piechart.jpeg");
            File f = new File("src/main/java/edu/cmu/cs214/hw6/plugin/charts/piechart.jpeg");
            assertTrue(f.exists() && !f.isDirectory());
            f.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}