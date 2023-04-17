package edu.cmu.cs214.hw6.framework.core;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;

import edu.cmu.cs214.hw6.framework.core.types.Text;

import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Token;

public class ESAFrameworkImpl implements ESAFramework {
    private List<DataPlugin> registeredDataPlugins;
    private DataPlugin currDataPlugin;
    private List<VisualizationPlugin> registeredVisPlugins;
    private VisualizationPlugin currVisPlugin;
    
    public ESAFrameworkImpl() {

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
     */
    @Override
    public void uploadData(String directoryPath) {
        
    }

    @Override
    public String getAnalyzedVisualization() {
        return null;
    }

    /**
     * For a list of Texts, get the entity sentiment API responses.
     * 
     * @param texts A list of Text objects
     * @return A list of API responses
     * @throws Exception
     */
    private List<AnalyzeEntitySentimentResponse> analyzeTexts(List<Text> texts) throws Exception {
        List<AnalyzeEntitySentimentResponse> responses = new ArrayList<>();
        for (Text text : texts) {
            responses.add(getESA(text.getContent()));
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

            // Print the response
            // for (Entity entity : response.getEntitiesList()) {
            //     System.out.printf("Entity: %s\n", entity.getName());
            //     System.out.printf("Salience: %.3f\n", entity.getSalience());
            //     System.out.printf("Sentiment : %s\n", entity.getSentiment());
            //     for (EntityMention mention : entity.getMentionsList()) {
            //         System.out.printf("Begin offset: %d\n", mention.getText().getBeginOffset());
            //         System.out.printf("Content: %s\n", mention.getText().getContent());
            //         System.out.printf("Magnitude: %.3f\n", mention.getSentiment().getMagnitude());
            //         System.out.printf("Sentiment score : %.3f\n", mention.getSentiment().getScore());
            //         System.out.printf("Type: %s\n\n", mention.getType());
            //     }
            // }
        }
    }
}
