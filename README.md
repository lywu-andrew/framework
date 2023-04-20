# Entity Sentiment Analysis Framework
Entity sentiment analysis aims to determine sentiment of different entities identified in a text. Our framework will be using the Google Cloud Natural Language API, which is a web API that uses NLP to process text and provide insight on key factors such as sentiment, salience, etc. To use the framework, you should create implementations of the DataPlugin and VisualizationPlugin interfaces (in the plugin directory) to properly format input texts and create visualizations of analysis results. Remember to edit the files in the resources folder to register your plugins.

## Data Plugin

## Visualization Plugin
The visualizeData method's only parameter is a list of [AnalyzeEntitySentimentResponse](https://cloud.google.com/python/docs/reference/language/latest/google.cloud.language_v1.types.AnalyzeEntitySentimentResponse) objects which are objects from the Google Cloud Natural Language API. The documentation for [Entity](https://cloud.google.com/python/docs/reference/language/latest/google.cloud.language_v1.types.Entity), [EntityMention](https://cloud.google.com/python/docs/reference/language/latest/google.cloud.language_v1.types.EntityMention), and [Sentiment](https://cloud.google.com/python/docs/reference/language/latest/google.cloud.language_v1.types.Sentiment) objects can be found online. Example code for retrieving AnalyzeEntitySentimentResponse information can be found [here](https://cloud.google.com/natural-language/docs/analyzing-entity-sentiment#analyzing_entity_sentiment_2). In the visualizeData method, image files should be created in the plugin/charts folder, and the path to the visualized image should be returned (assuming backend is the root folder).

The list of AnalyzeEntitySentimentResponse objects are ordered based on the order of texts in the directory.

## Before Running the Program

1. Create a project in [Google Cloud](https://console.cloud.google.com/) and enable the Cloud Natural Language API, which requires a billing account.

2. Set up credentials for ADC as described [here](https://cloud.google.com/docs/authentication/application-default-credentials).

## Running the Program

1. Set Up Backend Server

Either run the Java backend by using your IDE or by typing 

```
mvn exec:exec
```
in the back-end folder. This will start the Java server at http://localhost:8080.

2. Set Up Frontend Server

In the front-end folder, run

```
npm install
npm start
```

This will start the front-end server at http://localhost:3000. You can update the front-end code as the server is running in the development mode (i.e., npm start). It will automatically recompile and reload.