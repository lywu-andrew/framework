# Entity Sentiment Analysis Framework
Entity sentiment analysis aims to determine sentiment of different entities identified in a text. Our framework will be using the Google Cloud Natural Language API, which is a web API that uses NLP to process text and provide insight on key factors such as sentiment, salience, etc. To use the framework, you should create implementations of the DataPlugin and VisualizationPlugin interfaces (in the plugin directory) to properly format input texts and create visualizations of analysis results. Remember to edit the files in the resources folder to register your plugins.

## Data Plugin

## Visualization Plugin
The visualizeData method's only parameter is a list of [AnalyzeEntitySentimentResponse](https://cloud.google.com/python/docs/reference/language/latest/google.cloud.language_v1.types.AnalyzeEntitySentimentResponse) objects which are objects from the Google Cloud Natural Language API. The documentation for [Entity](https://cloud.google.com/python/docs/reference/language/latest/google.cloud.language_v1.types.Entity), [EntityMention](https://cloud.google.com/python/docs/reference/language/latest/google.cloud.language_v1.types.EntityMention), and [Sentiment](https://cloud.google.com/python/docs/reference/language/latest/google.cloud.language_v1.types.Sentiment) objects can be found online. Example code for retrieving AnalyzeEntitySentimentResponse information can be found [here](https://cloud.google.com/natural-language/docs/analyzing-entity-sentiment#analyzing_entity_sentiment_2). In the visualizeData method, image files should be created in the plugin/charts folder, and the path to the visualized image should be returned (assuming backend is the root folder).

The list of AnalyzeEntitySentimentResponse objects are ordered based on the order of texts in the directory.

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

## Set Up GCloud

This is a critical step necessary in order to make Google Cloud Natural Language API Calls.

1. Install gcloud: follow https://stackoverflow.com/a/52438247 until step 7
2. Get Google Cloud Platform API credentials in JSON: https://www.youtube.com/watch?v=rWcLDax-VmM
3. In your Google Cloud console (https://console.cloud.google.com/), navigate to your project and search for “Cloud Natural Language API”.
4. Click “Enable” and set up a free trial (must provide billing information).
5. Set your environmental variable: use the path to the JSON file you downloaded from step 2 and run the following command in your terminal.

```
$ export GOOGLE_APPLICATION_CREDENTIALS=“<your_path”
```
It will look something like
```
$ export GOOGLE_APPLICATION_CREDENTIALS=“/Users/sunnyliang/Downloads/hw6-esa-framework-project-823d94c3d2c3.json
```
Verify this worked by running the following
```
$ echo $GOOGLE_APPLICATION_CREDENTIALS
```
6. Now you are ready to make API calls