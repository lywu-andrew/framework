package edu.cmu.cs214.hw6.framework.core;

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.IOException;

import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Document.Type;

public class ESAFrameworkImpl implements ESAFramework {

    private List<DataPlugin> registeredDataPlugins;
    private DataPlugin currDataPlugin;
    private List<VisualizationPlugin> registeredVisPlugins;
    private VisualizationPlugin currVisPlugin;
    private List<String> texts;
    private String imgPath;
    private String directoryPathStr;
    
    public ESAFrameworkImpl() {
        registeredDataPlugins = new ArrayList<DataPlugin>();
        registeredVisPlugins = new ArrayList<VisualizationPlugin>();
        currDataPlugin = null;
        currVisPlugin = null;
        texts = new ArrayList<String>();
        imgPath = null;
        directoryPathStr = null;
    }

    public DataPlugin getCurrDataPlugin() {
        return currDataPlugin;
    }

    public VisualizationPlugin getCurrVisPlugin() {
        return currVisPlugin;
    }

    public List<DataPlugin> getDataPlugins() {
        return registeredDataPlugins;
    }

    public List<VisualizationPlugin> getVisPlugins() {
        return registeredVisPlugins;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getDirectoryPathStr() {
        return directoryPathStr;
    }

    public void setDirectoryPathStr(String path) {
        directoryPathStr = path;
    }

    /**
     * Registers a new {@link DataPlugin} with the framework
     */
    public void registerDataPlugin(DataPlugin plugin) {
        registeredDataPlugins.add(plugin);
    }

    /**
     * Changes the current {@link DataPlugin} in the framework
     * 
     * @param index The index for the desired dataPlugin in the list
     */
    public void selectDataPlugin(int index) {
        if (index < 0 || index >= registeredDataPlugins.size()) return;
        else currDataPlugin = registeredDataPlugins.get(index);
    }

    /**
     * Registers a new {@link VisualizationPlugin} with the framework
     */
    public void registerVisPlugin(VisualizationPlugin plugin) {
        registeredVisPlugins.add(plugin);
    }

    /**
     * Changes the current {@link VisualizationPlugin} in the framework
     * 
     * @param index The index for the desired visualizationPlugin in the list
     */
    public void selectVisPlugin(int index) {
        if (index < 0 || index >= registeredVisPlugins.size()) return;
        else currVisPlugin = registeredVisPlugins.get(index);
    }

    /**
     * Source: https://www.tutorialspoint.com/how-to-read-data-from-all-files-in-a-directory-using-java
     * @throws IOException
     */
    @Override
    public void uploadData() throws IOException {
        // Creating a File object for directory
        File directoryPath = new File(this.directoryPathStr);
        // List of all files and directories
        File filesList[] = directoryPath.listFiles();
        for (File file : filesList) {
            texts.add(currDataPlugin.convertToString(file.toPath()));
        }
    }

    /**
     * Conduct the entity-sentiment analysis and get the visualization.
     * 
     * @return File path to visualization image file returned by visualization plugin
     * @throws Exception
     */
    @Override
    public String getAnalyzedVisualization() throws Exception {
        List<AnalyzeEntitySentimentResponse> responses = analyzeTexts();
        imgPath = currVisPlugin.visualizeData(responses);
        return imgPath;
    }

    /**
     * For a list of texts, get the entity sentiment API responses.
     * 
     * @return A list of API responses
     * @throws Exception
     */
    private List<AnalyzeEntitySentimentResponse> analyzeTexts() throws Exception {
        List<AnalyzeEntitySentimentResponse> responses = new ArrayList<>();
        for (String text : texts) {
            responses.add(getESA(text));
        }
        return responses;
    }

    /**
     * Detect the entity sentiments of the given text using the Google Cloud
     * Language Beta API.
     * 
     * Sources: 
     * https://cloud.google.com/natural-language/docs/analyzing-entity-sentiment
     * https://github.com/GoogleCloudPlatform/java-docs-samples/blob/HEAD/language/snippets/src/main/java/com/example/language/Analyze.java
     * 
     * @param text A String input text to analyze
     * @throws Exception
     */
    private AnalyzeEntitySentimentResponse getESA(String text) throws Exception {
        // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
            AnalyzeEntitySentimentRequest request =
                AnalyzeEntitySentimentRequest.newBuilder()
                    .setDocument(doc)
                    .setEncodingType(EncodingType.UTF16)
                    .build();
            // Detect entity sentiments in the given string
            AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);
            return response;
        }
    }
}
