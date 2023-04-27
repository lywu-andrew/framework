# Implementing Plugins for Team yiningd-yihey-jasonkwo-public's Framework

## Data Plugins
Since both plugins are registered, they automatically load in on start up. No extra work is needed, and the user can now input `.jpeg` or `.heic` files.

**JPEG Data Plugin**
- Located `hw6Backend/src/java/edu/cmu/cs214/hw6Backend/plugin/dataPlugins/JPEGDataPlugin`.
- Converts a local `.jpg` file to an `ImageData` that the framework can process and extract colors from. 

**HEIC Data Plugin**
- Located `hw6Backend/src/java/edu/cmu/cs214/hw6Backend/plugin/dataPlugins/HEICDataPlugin`.
- Reads a saved `.heic` file, converts a local `.jpg` file to an `ImageData` that the framework can process and extract colors from. 
- Uses 3rd party library JDeli to read in `.heic` files. Since we're using a trial, any uploaded `.heic` file will have a watermark in the GUI display.

## Visualization Plugin
