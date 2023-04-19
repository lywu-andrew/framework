# Entity Sentiment Analysis

## Data Plugin

## Visualization Plugin
Implement methods in the VisualizationPlugin interface. The visualizeData method's only parameter is a list of [AnalyzeEntitySentimentResponse](https://cloud.google.com/python/docs/reference/language/latest/google.cloud.language_v1.types.AnalyzeEntitySentimentResponse) objects which are objects from the Google Cloud Natural Language API. The documentation for [Entity](https://cloud.google.com/python/docs/reference/language/latest/google.cloud.language_v1.types.Entity), [EntityMention](https://cloud.google.com/python/docs/reference/language/latest/google.cloud.language_v1.types.EntityMention), and [Sentiment](https://cloud.google.com/python/docs/reference/language/latest/google.cloud.language_v1.types.Sentiment) objects can be found online. Example code for retrieving AnalyzeEntitySentimentResponse information can be found [here](https://cloud.google.com/natural-language/docs/analyzing-entity-sentiment#analyzing_entity_sentiment_2). In the visualizeData method, image files should be created in the plugin/charts folder, and the path to the visualized image should be returned (assuming backend is the root folder).

The list of AnalyzeEntitySentimentResponse objects are ordered based on the order of texts in the directory.