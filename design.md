# HW6a Project Design

## Domain
The framework domain our team chose was text entity sentiment analysis. Entity sentiment analysis aims to determine sentiment of different entities identified in a text. Our framework will be using the [Google Cloud Natural Language API](https://cloud.google.com/natural-language/docs/analyzing-entity-sentiment#language-entity-sentiment-string-java), which is a web API that uses NLP to process text and provide insight on key factors such as sentiment, salience, etc. In particular, we plan to make API requests to the method for analyzing entity sentiment. 

Similar to the example in Appendix A, the framework will receive text from different sources provided by the data plugins, perform entity sentiment analysis on the selected texts, and display the data results with different visualizations provided by the visualization plugins.

### Data Plugin Examples
Data plugins can provide a list of text fragments with corresponding time stamps. For example, some possibilities include:
* File extension plugins that read textual data from .pdf or .txt files
* Yelp plugin that takes in Yelp review comments for a specific restaurant or business, or all of a user's Yelp reviews using their handle with the Yelp API
* Lyric plugin that takes songs and takes lyric text data using the Musixmatch API

### Visualization Plugin Examples
Visualization plugins can provide the entity sentiment analysis (list of entities, each salience score, content, magnitude) of a list of text fragments with corresponding time stamps. The visualization plugin can specify which aspects of the analysis to visualize. For example, some possibilities include:
* Bar charts comparing salience/sentiment of the different entities in different text articles. More specifically, one aspect could be the salience of voter participation in political articles from different news companies.
* Word cloud with the identified entities. One implementation of the plugin could take in the top 20 entities with the highest salience and display them according to their score. It’s also possible to generate a word cloud according to sentiment scores.

## Generality vs. Specificity
Our framework performs the entity sentiment analysis by itself, which encourages reusability for all kinds of text data from any source. Thus, there is a large variety of uses for this text analysis framework with any generic text input. For users, this grants more flexibility in how the data plugins are implemented, since they only need to provide the text in a format for the framework to properly read. In addition, this allows reuse between different data and visualization plugins as well. Our decision to choose entity sentiment analysis is based on the versatility of text, which makes our framework both generic enough to all types of text and also specific enough to make meaningful visualizations for each type of text.

The general flow of the whole text entity sentiment analysis process is as follows:
* The data plugin cleans up text from a specific source and passes it to the framework.
* The framework receives the text data and performs entity sentiment analysis with Google’s Natural Language API, creating analysis results.
* The visualization plugin uses the analysis results to generate a visualization of selected aspects of the entity sentiment analysis.

To use this framework, the user needs to provide data and visualization plugins to specify what texts they would like analyzed and what visualizations they want to display. After running the program, they will be presented with a GUI which allows them to render specific pairings of data plugins with visualization plugins (can be mixed and matched).

Our design includes abstractions to organize the data input and API response. Text is a data class that contains the string text from the data source and the datetime the text was created. AnalysisResult is a data class containing the entity sentiment analysis results for a Text; it closely mirrors the Google Cloud API response in order to simplify the process of converting from JSON object to Java objects. See the Plugin interfaces section for specifics more on these abstraction types.

These abstractions are specific and meaningful in order to facilitate efficiency and understandability for the users. For example, Text is an abstraction to fulfill standardization of the texts’ format, so they can be uniformly passed into the framework. Since these classes limit the input text type and the user only needs to understand 2 main concepts, this decision supports the design principle of cohesion and minimizing conceptual weight. In addition, the names are very intuitive, which satisfies the design principle of naming. This design is extensible because the simple plugin interfaces and clearly-defined input and output types make it easy for users to make their own plugins with minimal documentation required. This design is also flexible because the simple interfaces allow for very different data sources (from a single txt file to Twitter API calls for tweets across time) and for very different visualizations (from comparing sentiment of a single entity over time to comparing salience of many entities in a single text).

## Project structure
We organized this project based on the structure of recitation 9’s repository. Our project is separated into backend and frontend, with the core functionality of the framework and plugins in the backend. Within the backend src/main folder, we’ve separated the framework, plugins, and tests to be in different packages.

The framework interface and implementation are in the framework.core package, while data objects needed for GUI rendering are in the framework.gui package. In addition, both plugin interfaces will also be in the framework.core package. The data and visualization plugins are separated in the plugin package, and those contain implementations of the plugin interfaces from the user. These plugins use files in the texts package which control most of the external API calls made by users and clean the text data for the plugin to return. The key abstractions, the Text and AnalysisResult classes, used by data and visualization plugins are in the framework.core.types package.

To register plugins to the framework, the folder resources/META-INF/services contains a file which lists all the plugins that the framework will use. After the framework is instantiated, it’ll call registerPlugin on each plugin to load the plugins into the framework.

Below is a picture of the project structure:

![alt text.](/images/organization.png "This is the project structure.")

## Plugin interfaces
```
public interface DataPlugin {

    /**
     * Gets the name of the data plugin.
     */
    String getPluginName();

    /**
     * Gets a list of the texts.
     */
    List<Text> getTexts();

}
```
For the DataPlugin interface, the important method is getTexts() which is called by the framework to get the Text objects containing the specific texts from the plugin. Note that this interface specifically allows for multiple texts in order to allow users to compare the analyses. 

```
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

}
```
For the VisualizationPlugin interface, the important method is visualizeData(AnalysisResult analysisResult) which visualizes the results in a chart and returns an HTML string.

As discussed earlier, we created data classes which abstract the types required to pass between the data plugin, framework, and visualization plugin. 
```
public class Text {
    private String content;
    private LocalDateTime datetime;

    public Text(String content, LocalDateTime datetime) {
        this.content = content;
        this.datetime = datetime;
    }

    /**
     * Gets the content of this text.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Gets the datetime.
     */
    public LocalDateTime getDatetime() {
        return this.datetime;
    }
}
```
Text is used for data plugins.

```
public class AnalysisResult {
    private Entity[] entities;
}

public class Entity {
    private String name;
    private String type;
    private float salience;
    private List<Mention> mentions;
    private Sentiment sentiment;
}

public class Sentiment {
    private float magnitude;
    private float score;
}

public class Mention {
    private int beginOffset;
    private Sentiment sentiment;
}
```
AnalysisResult is the result of the entity sentiment analysis, and it is the input for the visualization plugin. It contains other descriptive classes: Entity, Sentiment, Mention. Each Entity has an overall salience, overall salience, and a list of mentions and their sentiments. The classes do not have constructors, getters, or setters because this may make it easier to convert JSON objects to Java objects.

![alt text.](/images/analysisresult.png "This is the AnalysisResult object.")

This UML diagram visualizes the relationships between AnalysisResult, Entity, Sentiment, and Mention.

![alt text.](/images/backend.png "This is the backend flow.")

The UML diagram shows how the interfaces, plugins, and framework are connected. Each implemented plugin will have a private field referring to the framework interface which will be used to call framework methods.

![alt text.](/images/flowchart.png "This is the flowchart.")

This flowchart shows the general process of the user interacting with the GUI and how the program is connected between backend and frontend through NanoHTTPD.