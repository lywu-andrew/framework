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
    
    public ESAFrameworkImpl() {
        registeredVisPlugins = new ArrayList<VisualizationPlugin>();
        registeredDataPlugins = new ArrayList<DataPlugin>();
        texts = new ArrayList<String>();
    }

    /**
     * Registers a new {@link DataPlugin} with the framework
     */
    public void registerDataPlugin(DataPlugin plugin) {
        plugin.onRegister(this);
        registeredDataPlugins.add(plugin);
    }

    /**
     * Registers a new {@link VisualizationPlugin} with the framework
     */
    public void registerVisPlugin(VisualizationPlugin plugin) {
        plugin.onRegister(this);
        registeredVisPlugins.add(plugin);
    }

    /**
     * Source: https://www.tutorialspoint.com/how-to-read-data-from-all-files-in-a-directory-using-java
     * @throws IOException
     */
    @Override
    public void uploadData(String directoryPathStr) throws IOException {
        // Creating a File object for directory
        File directoryPath = new File(directoryPathStr);
        // List of all files and directories
        File filesList[] = directoryPath.listFiles();
        for (File file : filesList) {
            texts.add(currDataPlugin.convertToString(file.toPath()));
        }
    }

    @Override
    public String getAnalyzedVisualization() throws Exception {
        List<AnalyzeEntitySentimentResponse> responses = analyzeTexts();
        return currVisPlugin.visualizeData(responses);
    }

    /**
     * For a list of Texts, get the entity sentiment API responses.
     * 
     * @param texts A list of Text objects
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
